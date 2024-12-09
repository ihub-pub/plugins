/*
 * Copyright (c) 2021-2024 the original author or authors.
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

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

/**
 * @author henry
 */
@Title('IHubVerificationPlugin测试套件')
class IHubVerificationPluginTest extends IHubSpecification {

    private Project project

    def '代码检查插件测试'() {
        setup: '初始化项目'
        copyProject 'sample-groovy', 'src', 'conf'
        // groovy样例为综合样例，检测插件为独立插件，插件内不能使用pub.ihub.plugin.ihub-groovy
        buildFile.write ''
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin'
                id 'groovy'
                id 'pub.ihub.plugin.ihub-test'
                id 'pub.ihub.plugin.ihub-verification'
            }
            dependencies {
                implementation 'org.apache.groovy:groovy'
            }
        '''
        settingsFile << 'rootProject.name = \'sample-groovy\''

        when: '构建项目'
        def result = gradleBuilder.withArguments('build').build()

        then: '检查结果'
        result.output.contains '┌──────────────────────────────────────────────────────────────────────────────────────────────────┐'
        result.output.contains '│                               SAMPLE-GROOVY Jacoco Report Coverage                               │'
        result.output.contains '├──────────────────────┬────────────────┬─────────────────┬──────────────────┬─────────────────────┤'
        result.output.contains '│ Type                 │ Total          │ Missed          │ Covered          │ Coverage            │'
        result.output.contains '├──────────────────────┼────────────────┼─────────────────┼──────────────────┼─────────────────────┤'
        result.output.contains '│ INSTRUCTION          │ 6              │ 0               │ 6                │ 100.00%             │'
        result.output.contains '│ BRANCH               │ 0              │ 0               │ 0                │ n/a                 │'
        result.output.contains '│ LINE                 │ 1              │ 0               │ 1                │ 100.00%             │'
        result.output.contains '│ COMPLEXITY           │ 1              │ 0               │ 1                │ 100.00%             │'
        result.output.contains '│ METHOD               │ 1              │ 0               │ 1                │ 100.00%             │'
        result.output.contains '│ CLASS                │ 1              │ 0               │ 1                │ 100.00%             │'
        result.output.contains '└──────────────────────┴────────────────┴─────────────────┴──────────────────┴─────────────────────┘'
        result.output.contains '┌──────────────────────────────────────────────────────────────────────────────────────────────────┐'
        result.output.contains '│                                      Jacoco Report Coverage                                      │'
        result.output.contains '├──────────────────┬─────────────┬───────────┬────────────┬────────────┬────────────┬──────────────┤'
        result.output.contains '│ Project          │ Instruct    │ Branch    │ Line       │ Cxty       │ Method     │ Class        │'
        result.output.contains '├──────────────────┼─────────────┼───────────┼────────────┼────────────┼────────────┼──────────────┤'
        result.output.contains '│ sample-groovy    │ 100.00%     │ n/a       │ 100.00%    │ 100.00%    │ 100.00%    │ 100.00%      │'
        result.output.contains '│ total            │ 100.00%     │ n/a       │ 100.00%    │ 100.00%    │ 100.00%    │ 100.00%      │'
        result.output.contains '└──────────────────┴─────────────┴───────────┴────────────┴────────────┴────────────┴──────────────┘'
        result.task(':codenarcMain').outcome == SUCCESS
        result.task(':codenarcTest').outcome == SUCCESS
        result.task(':test').outcome == SUCCESS
        result.task(':jacocoTestReport').outcome == SUCCESS
        result.task(':jacocoTestCoverageVerification').outcome == SUCCESS
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Java检查插件配置测试'() {
        setup: '初始化项目'
        buildFile << '''
            plugins {
                id 'java'
                id 'pub.ihub.plugin.ihub-test'
                id 'pub.ihub.plugin.ihub-verification'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        testProjectDir.newFolder 'conf', 'pmd'
        testProjectDir.newFile 'conf/pmd/ruleset.xml'
        testProjectDir.newFile('.java-local.properties') << 'local.test=property'
        result = gradleBuilder.withArguments('-DiHubTest.runSkippedPropNames=java.endorsed.dirs').build()

        then: '检查结果'
        !result.output.contains('org.spockframework:spock-core')
        !result.output.contains('com.athaydes:spock-reports')
        result.output.contains '│ pmd                                      │ net.sourceforge.pmd:pmd-ant                           │'
        result.output.contains '│ pmd                                      │ net.sourceforge.pmd:pmd-java                          │'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        propertiesFile << 'iHubTest.testFramework=SPOCK'
        result = gradleBuilder.withArguments('-DiHubTest.runIncludePropNames=java.endorsed.dirs').build()

        then: '检查结果'
        result.output.contains '│ testImplementation                       │ org.spockframework:spock-core                         │'
        result.output.contains '│ testRuntimeOnly                          │ com.athaydes:spock-reports                            │'
        result.output.contains '│ pmd                                      │ net.sourceforge.pmd:pmd-groovy                        │'
        result.output.contains '│ pmd                                      │ net.sourceforge.pmd:pmd-ant                           │'
        result.output.contains '│ pmd                                      │ net.sourceforge.pmd:pmd-java                          │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Groovy检查插件配置测试'() {
        setup: '初始化项目'
        buildFile << '''
            plugins {
                id 'groovy'
                id 'pub.ihub.plugin.ihub-test'
                id 'pub.ihub.plugin.ihub-verification'
            }
            iHubTest {
                classes = '**/*Test*'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains '│ testImplementation                       │ org.spockframework:spock-core                         │'
        result.output.contains '│ testRuntimeOnly                          │ com.athaydes:spock-reports                            │'
        result.output.contains '│ pmd                                      │ net.sourceforge.pmd:pmd-groovy                        │'
        result.output.contains '│ pmd                                      │ net.sourceforge.pmd:pmd-ant                           │'
        result.output.contains '│ pmd                                      │ net.sourceforge.pmd:pmd-java                          │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '静态检查结果打印测试'() {
        setup: '初始化项目'
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply IHubVerificationPlugin
        project.pluginManager.apply IHubTestPlugin
        File jacocoTestReportFile = testProjectDir.newFile().tap {
            it << '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?><!DOCTYPE report PUBLIC "-//JACOCO//DTD Report 1.1//EN" "report.dtd"><report name="sample-groovy"><sessioninfo id="henry-PC-e97f6720" start="1623941945572" dump="1623941951115"/><package name="pub/ihub/sample/groovy"><class name="pub/ihub/sample/groovy/HelloWorld" sourcefilename="HelloWorld.groovy"><method name="getHello" desc="()Ljava/lang/String;" line="27"><counter type="INSTRUCTION" missed="0" covered="13"/><counter type="LINE" missed="0" covered="1"/><counter type="COMPLEXITY" missed="0" covered="1"/><counter type="METHOD" missed="0" covered="1"/></method><counter type="INSTRUCTION" missed="0" covered="13"/><counter type="LINE" missed="0" covered="1"/><counter type="COMPLEXITY" missed="0" covered="1"/><counter type="METHOD" missed="0" covered="1"/><counter type="CLASS" missed="0" covered="1"/></class><sourcefile name="HelloWorld.groovy"><line nr="27" mi="0" ci="10" mb="0" cb="0"/><counter type="INSTRUCTION" missed="0" covered="13"/><counter type="LINE" missed="0" covered="1"/><counter type="COMPLEXITY" missed="0" covered="1"/><counter type="METHOD" missed="0" covered="1"/><counter type="CLASS" missed="0" covered="1"/></sourcefile><counter type="INSTRUCTION" missed="0" covered="13"/><counter type="LINE" missed="0" covered="1"/><counter type="COMPLEXITY" missed="0" covered="1"/><counter type="METHOD" missed="0" covered="1"/><counter type="CLASS" missed="0" covered="1"/></package><counter type="INSTRUCTION" missed="0" covered="13"/><counter type="LINE" missed="0" covered="1"/><counter type="COMPLEXITY" missed="0" covered="1"/><counter type="METHOD" missed="0" covered="1"/><counter type="CLASS" missed="0" covered="1"/></report>'''
        }

        when: '应用插件'
        project.plugins.withType(IHubVerificationPlugin) {
            printFinishedJacocoReportCoverage()
            printJacocoReportCoverage jacocoTestReportFile
            printJacocoReportCoverage jacocoTestReportFile
            printFinishedJacocoReportCoverage()
        }

        then: '检查结果'
        project.extensions.findByName('iHubVerification') instanceof IHubVerificationExtension
        project.extensions.findByName('iHubTest') instanceof IHubTestExtension
    }

    def 'Jacoco聚合报告配置测试'() {
        setup: '初始化项目'
        buildFile << '''
            plugins {
                id 'java'
                id 'pub.ihub.plugin.ihub-test'
                id 'pub.ihub.plugin.ihub-verification'
            }
            subprojects {
                apply {
                    plugin 'java'
                    plugin 'pub.ihub.plugin.ihub-test'
                    plugin 'pub.ihub.plugin.ihub-verification'
                }
            }
        '''
        settingsFile << 'include \'a\', \'b\', \'c\''
        testProjectDir.newFolder 'a'
        testProjectDir.newFolder 'b'
        testProjectDir.newFolder 'c'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
