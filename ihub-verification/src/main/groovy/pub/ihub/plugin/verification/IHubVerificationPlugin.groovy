/*
 * Copyright (c) 2021-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pub.ihub.plugin.verification

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import groovy.xml.XmlParser
import org.gradle.api.Project
import org.gradle.api.attributes.TestSuiteType
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.quality.CodeNarcExtension
import org.gradle.api.plugins.quality.CodeNarcPlugin
import org.gradle.api.plugins.quality.PmdExtension
import org.gradle.api.plugins.quality.PmdPlugin
import org.gradle.api.reporting.ReportingExtension
import org.gradle.testing.jacoco.plugins.JacocoCoverageReport
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoReportAggregationPlugin
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.bom.IHubBomPlugin

import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER


/**
 * 代码检查插件
 * @author liheng
 */
@IHubPlugin(value = IHubVerificationExtension, beforeApplyPlugins = [IHubBomPlugin])
class IHubVerificationPlugin extends IHubProjectPluginAware<IHubVerificationExtension> {

    private static final String[] RULE_TYPE = ['INSTRUCTION', 'BRANCH', 'LINE', 'COMPLEXITY', 'METHOD', 'CLASS']

    static final Closure JACOCO_COVERAGE_CONFIG = { IHubVerificationExtension ext, JacocoCoverageVerification it ->
        it.violationRules {
            // rule #1：bundle分支覆盖率
            rule {
                enabled = ext.jacocoBranchCoverageRuleEnabled.get()
                limit {
                    counter = 'BRANCH'
                    minimum = ext.jacocoBranchCoveredRatio.get().toBigDecimal()
                }
            }
            // rule #2：bundle指令覆盖率
            rule {
                enabled = ext.jacocoInstructionCoverageRuleEnabled.get()
                excludes = ext.jacocoInstructionExclusion.get().tokenize ','
                limit {
                    minimum = ext.jacocoInstructionCoveredRatio.get().toBigDecimal()
                }
            }
            // rule #3：package指令覆盖率
            rule {
                enabled = ext.jacocoPackageCoverageRuleEnabled.get()
                element = 'PACKAGE'
                excludes = ext.jacocoPackageExclusion.get().tokenize ','
                limit {
                    minimum = ext.jacocoPackageCoveredRatio.get().toBigDecimal()
                }
            }
        }
    }

    @Override
    void apply() {
        if (hasPlugin(GroovyPlugin)) {
            configCodenarc project
        }
        if (hasPlugin(JavaPlugin)) {
            configPmd project
        }
        configJacoco project
    }

    private void configPmd(Project project) {
        applyPlugin PmdPlugin
        withExtension(IHubBomExtension).dependencies {
            compile 'pmd', 'com.alibaba.p3c:p3c-pmd'
        }
        withExtension(AFTER) { ext ->
            withExtension(PmdExtension) {
                String ruleset = "$project.rootProject.projectDir/conf/pmd/ruleset.xml"
                if (project.file(ruleset).exists()) {
                    it.ruleSetFiles = project.files ruleset
                } else {
                    it.ruleSets = [
                        'rulesets/java/ali-comment.xml',
                        'rulesets/java/ali-concurrent.xml',
                        'rulesets/java/ali-constant.xml',
                        'rulesets/java/ali-exception.xml',
                        'rulesets/java/ali-flowcontrol.xml',
                        'rulesets/java/ali-naming.xml',
                        'rulesets/java/ali-oop.xml',
                        'rulesets/java/ali-orm.xml',
                        'rulesets/java/ali-other.xml',
                        'rulesets/java/ali-set.xml',
                        'rulesets/vm/ali-other.xml',
                    ]
                }
                it.consoleOutput = ext.pmdConsoleOutput.get()
                it.ignoreFailures = ext.pmdIgnoreFailures.get()
                it.toolVersion = ext.pmdVersion.get()
            }
        }
    }

    private void configCodenarc(Project project) {
        applyPlugin CodeNarcPlugin
        withExtension(AFTER) { ext ->
            withExtension(CodeNarcExtension) {
                it.configFile = project.rootProject.with {
                    file("$rootDir/conf/codenarc/codenarc.groovy").with {
                        String tmpPath = "$projectDir/.gradle/pub.ihub.plugin.cache"
                        exists() ? it : file("$tmpPath/codenarc.groovy").tap {
                            mkdir tmpPath
                            createNewFile()
                            write getClass().getResourceAsStream('/META-INF/codenarc.groovy').readLines().join('\n')
                        }
                    }
                }
                it.ignoreFailures = ext.codenarcIgnoreFailures.get()
                it.toolVersion = ext.codenarcVersion.get()
            }
        }
    }

    private void configJacoco(Project project) {
        applyPlugin JacocoPlugin
        if (project != extension.rootProject) {
            configJacocoAggregation project
        }
        withExtension(AFTER) { ext ->
            withExtension(JacocoPluginExtension).toolVersion = ext.jacocoVersion.get()

            JacocoCoverageVerification jacocoCoverageVerification = withTask 'jacocoTestCoverageVerification',
                JACOCO_COVERAGE_CONFIG.curry(ext)

            // 覆盖率报告排除main class
            JacocoReport jacocoTestReport = withTask('jacocoTestReport') { task ->
                task.reports {
                    xml.required = true
                    html.required = true
                }
                project.afterEvaluate {
                    task.classDirectories.from = project.files(task.classDirectories.files.collect { dir ->
                        project.fileTree dir: dir, exclude: ext.jacocoReportExclusion.get().tokenize(',')
                    })
                }

                task.doLast {
                    File xml = reports.xml.outputLocation.asFile.get()
                    printJacocoReportCoverage xml
                }
            }

            // 一些任务依赖和属性设置
            withTask('check').finalizedBy jacocoCoverageVerification
            withTask('test').finalizedBy jacocoTestReport
        }
    }

    private void configJacocoAggregation(Project project) {
        extension.rootProject.with {
            pluginManager.apply JacocoReportAggregationPlugin
            configurations {
                maybeCreate('jacocoAggregation').dependencies.add dependencies.create(project)
            }
            extensions.getByType(ReportingExtension).reports {
                testCodeCoverageReport(JacocoCoverageReport) {
                    testType = TestSuiteType.UNIT_TEST
                }
            }
        }
    }

    private void printJacocoReportCoverage(File xml) {
        def counters = new XmlParser().tap {
            setFeature 'http://apache.org/xml/features/nonvalidating/load-external-dtd', false
            setFeature 'http://apache.org/xml/features/disallow-doctype-decl', false
        }.parse(xml).counter
        Map<String, ReportData> reportData = RULE_TYPE.collectEntries { type ->
            [(type): counters.find { counter -> counter.'@type' == type }.with {
                it ? new ReportData(it.'@missed' as int, it.'@covered' as int) : new ReportData(0, 0)
            }]
        }

        extension.setExtProperty 'jacocoReportData', reportData

        String title = project.name.toUpperCase() + ' Jacoco Report Coverage'
        printConfigContent title, reportData.collect { type, data ->
            [type, data.total(), data.missed, data.covered, data.coverage]
        }, 'Type', 'Total', 'Missed', 'Covered', 'Coverage'

        if (!extension.findExtProperty(extension.rootProject, 'printJacocoReportCoverage', false)) {
            // 此处弃用方法待观察 https://github.com/gradle/gradle/issues/20151
            project.gradle.buildFinished {
                printFinishedJacocoReportCoverage()
            }
            extension.setExtProperty extension.rootProject, 'printJacocoReportCoverage', true
        }
    }

    private void printFinishedJacocoReportCoverage() {
        Map<String, ReportData> total = RULE_TYPE.collectEntries { [(it): new ReportData(0, 0)] }
        List report = extension.rootProject.allprojects.collect { p ->
            Map<String, ReportData> jacocoReportData = extension.findExtProperty p, 'jacocoReportData'
            jacocoReportData ? [p.name] + jacocoReportData.collect { type, data ->
                total.get(type).tap {
                    covered += data.covered
                    missed += data.missed
                }
                data.coverage
            } : null
        } - null
        report << (['total'] + total.values().coverage)
        printConfigContent 'Jacoco Report Coverage', report,
            'Project', 'Instruct', 'Branch', 'Line', 'Cxty', 'Method', 'Class'
    }

    @CompileStatic
    @TupleConstructor
    private final class ReportData {

        int missed
        int covered

        int total() {
            missed + covered
        }

        String getCoverage() {
            int total = total()
            total ? (covered / total * 100).round(2) + '%' : 'n/a'
        }

    }

}
