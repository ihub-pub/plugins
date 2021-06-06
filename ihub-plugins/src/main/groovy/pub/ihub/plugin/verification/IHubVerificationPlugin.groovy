/*
 * Copyright (c) 2021 Henry 李恒 (henry.box@outlook.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pub.ihub.plugin.verification

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.quality.CodeNarcExtension
import org.gradle.api.plugins.quality.CodeNarcPlugin
import org.gradle.api.plugins.quality.PmdExtension
import org.gradle.api.plugins.quality.PmdPlugin
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import pub.ihub.plugin.IHubProjectPlugin
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.bom.IHubBomPlugin

import static pub.ihub.plugin.IHubProjectPlugin.EvaluateStage.AFTER



/**
 * 代码检查插件
 * @author liheng
 */
class IHubVerificationPlugin extends IHubProjectPlugin<IHubVerificationExtension> {

    Class<? extends Plugin<Project>>[] beforeApplyPlugins = [IHubBomPlugin]
    String extensionName = 'iHubVerification'

    @Override
    void apply() {
        if (project.plugins.hasPlugin(JavaPlugin)) {
            configPmd project
        }
        if (project.plugins.hasPlugin(GroovyPlugin)) {
            configCodenarc project
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
                String ruleset = ext.pmdRulesetFile
                if (project.file(ruleset).exists()) {
                    it.ruleSetFiles = project.files ruleset
                } else {
                    it.ruleSets = IHubVerificationExtension.PMD_DEFAULT_RULESET
                }
                it.consoleOutput = ext.pmdConsoleOutput
                it.ignoreFailures = ext.pmdIgnoreFailures
                it.toolVersion = ext.pmdVersion
            }
        }
    }

    private void configCodenarc(Project project) {
        applyPlugin CodeNarcPlugin
        withExtension(AFTER) { ext ->
            withExtension(CodeNarcExtension) {
                it.configFile = project.rootProject.with {
                    file(ext.codenarcFile).with {
                        String tmpPath = "$projectDir/build/tmp"
                        exists() ? it : file("$tmpPath/codenarc.groovy").tap {
                            mkdir tmpPath
                            createNewFile()
                            write IHubVerificationExtension.CODENARC_DEFAULT_RULESET
                        }
                    }
                }
                it.ignoreFailures = ext.codenarcIgnoreFailures
                it.toolVersion = ext.codenarcVersion
            }
        }
    }

    private void configJacoco(Project project) {
        applyPlugin JacocoPlugin
        withExtension(AFTER) { ext ->
            withExtension(JacocoPluginExtension) {
                it.toolVersion = ext.jacocoVersion
            }

            /**
             * 分支覆盖率达到100%
             * 由于groovy在编译时会生成无效字节码，所以指令覆盖率无法达到100%，等待官方修复，详见
             * https://github.com/jacoco/jacoco/issues/884
             * http://groovy.329449.n5.nabble.com/Groovy-2-5-4-generates-dead-code-td5755188.html
             */
            JacocoCoverageVerification jacocoCoverageVerification = withTask('jacocoTestCoverageVerification') {
                it.violationRules {
                    // rule #1：bundle分支覆盖率
                    rule {
                        enabled = ext.jacocoBundleBranchCoverageRuleEnabled
                        limit {
                            counter = 'BRANCH'
                            value = 'COVEREDRATIO'
                            minimum = ext.jacocoBundleBranchCoveredRatio.toBigDecimal()
                        }
                    }
                    // rule #2：bundle指令覆盖率
                    rule {
                        enabled = ext.jacocoBundleInstructionCoverageRuleEnabled
                        limit {
                            minimum = ext.jacocoBundleInstructionCoveredRatio.toBigDecimal()
                        }
                    }
                    // rule #3：package指令覆盖率
                    rule {
                        enabled = ext.jacocoPackageInstructionCoverageRuleEnabled
                        element = 'PACKAGE'
                        limit {
                            minimum = ext.jacocoPackageInstructionCoveredRatio.toBigDecimal()
                        }
                    }
                }
            }

            // 覆盖率报告排除main class
            JacocoReport jacocoTestReport = withTask('jacocoTestReport') { task ->
                afterEvaluate {
                    task.classDirectories.from = project.files(task.classDirectories.files.collect { dir ->
                        project.fileTree dir: dir, exclude: ext.jacocoReportExclusion
                    })
                }
            }

            // 一些任务依赖和属性设置
            withTask('check').dependsOn jacocoCoverageVerification
            withTask('test').finalizedBy jacocoTestReport, jacocoCoverageVerification
        }
    }

}
