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

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.quality.CodeNarcExtension
import org.gradle.api.plugins.quality.CodeNarcPlugin
import org.gradle.api.plugins.quality.PmdExtension
import org.gradle.api.plugins.quality.PmdPlugin
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import pub.ihub.plugin.IHubPluginAware
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.bom.IHubBomPlugin

import static pub.ihub.plugin.IHubPluginAware.EvaluateStage.AFTER
import static pub.ihub.plugin.verification.IHubVerificationExtension.CODENARC_DEFAULT_RULESET
import static pub.ihub.plugin.verification.IHubVerificationExtension.PMD_DEFAULT_RULESET



/**
 * 代码检查插件
 * @author liheng
 */
class IHubVerificationPlugin implements IHubPluginAware<IHubVerificationExtension> {

    @Override
    void apply(Project project) {
        project.pluginManager.apply IHubBomPlugin

        createExtension project, 'iHubVerification', IHubVerificationExtension
        if (project.plugins.hasPlugin(JavaPlugin)) {
            configPmd project
        }
        if (project.plugins.hasPlugin(GroovyPlugin)) {
            configCodenarc project
        }
        configJacoco project
    }

    private void configPmd(Project project) {
        project.pluginManager.apply PmdPlugin
        getExtension(project, IHubBomExtension).dependencies {
            compile 'pmd', 'com.alibaba.p3c:p3c-pmd'
        }
        getExtension(project, IHubVerificationExtension, AFTER) { ext ->
            getExtension(project, PmdExtension).identity {
                String ruleset = ext.pmdRulesetFile
                if (project.file(ruleset).exists()) {
                    ruleSetFiles = project.files ruleset
                } else {
                    ruleSets = PMD_DEFAULT_RULESET
                }
                consoleOutput = ext.pmdConsoleOutput
                ignoreFailures = ext.pmdIgnoreFailures
                toolVersion = ext.pmdVersion
            }
        }
    }

    private void configCodenarc(Project project) {
        project.pluginManager.apply CodeNarcPlugin
        getExtension(project, IHubVerificationExtension, AFTER) { ext ->
            getExtension(project, CodeNarcExtension).identity {
                configFile = project.rootProject.with {
                    file(ext.codenarcFile).with {
                        String tmpPath = "$projectDir/build/tmp"
                        exists() ? it : file("$tmpPath/codenarc.groovy").tap {
                            mkdir tmpPath
                            createNewFile()
                            write CODENARC_DEFAULT_RULESET
                        }
                    }
                }
                ignoreFailures = ext.codenarcIgnoreFailures
                toolVersion = ext.codenarcVersion
            }
        }
    }

    private void configJacoco(Project project) {
        project.pluginManager.apply JacocoPlugin
        getExtension(project, IHubVerificationExtension, AFTER) { ext ->
            getExtension(project, JacocoPluginExtension).identity {
                toolVersion = ext.jacocoVersion
            }

            /**
             * 分支覆盖率达到100%
             * 由于groovy在编译时会生成无效字节码，所以指令覆盖率无法达到100%，等待官方修复，详见
             * https://github.com/jacoco/jacoco/issues/884
             * http://groovy.329449.n5.nabble.com/Groovy-2-5-4-generates-dead-code-td5755188.html
             */
            Task jacocoTestCoverageVerification = project.tasks.getByName('jacocoTestCoverageVerification').tap {
                violationRules {
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
            Task jacocoTestReport = project.tasks.getByName('jacocoTestReport').tap {
                project.afterEvaluate {
                    classDirectories.from = project.files(classDirectories.files.collect { dir ->
                        project.fileTree dir: dir, exclude: ext.jacocoReportExclusion
                    })
                }
            }

            // 一些任务依赖和属性设置
            project.check.dependsOn jacocoTestCoverageVerification
            project.test.finalizedBy jacocoTestReport, jacocoTestCoverageVerification
        }
    }

}
