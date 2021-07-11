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
package pub.ihub.plugin

import groovy.util.logging.Slf4j
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import pub.ihub.plugin.verification.IHubTestExtension
import pub.ihub.plugin.verification.IHubTestPlugin
import pub.ihub.plugin.verification.IHubVerificationExtension
import pub.ihub.plugin.verification.IHubVerificationPlugin
import spock.lang.Title



/**
 * @author henry
 */
@Slf4j
@Title('IHubVerificationPlugin测试套件')
class IHubVerificationPluginTest extends IHubSpecification {

    private Project project

    def 'Java检查插件配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << """
            apply {
                plugin 'pub.ihub.plugin.ihub-java'
                plugin 'pub.ihub.plugin.ihub-test'
                plugin 'pub.ihub.plugin.ihub-verification'
            }
        """

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        result = gradleBuilder.withArguments('-DiHubTest.runSkippedPropNames=java.endorsed.dirs').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Groovy检查插件配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << """
            apply {
                plugin 'pub.ihub.plugin.ihub-groovy'
                plugin 'pub.ihub.plugin.ihub-test'
                plugin 'pub.ihub.plugin.ihub-verification'
            }
            iHubTest {
                classes = '**/*Test*'
            }
        """

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
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

}
