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
package pub.ihub.plugin


import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Title

/**
 * @author henry
 */
@Title('IHubBase测试套件')
class IHubPluginBaseTest extends Specification {

    private Project project

    void setup() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply IHubPrintPlugin
        project.pluginManager.apply IHubDemoPlugin
    }

    def '测试扩展特征'() {
        setup:
        project.iHubDemo.setExtProperty 'ext1', 'ext1'
        ProjectEvaluationListener nextBatch = project.projectEvaluationBroadcaster
        System.setProperty('iHubDemo.system', 'systemValue')
        do {
            nextBatch = project.stepEvaluationListener(nextBatch) {
                beforeEvaluate project
                afterEvaluate project, null
            }
        } while (nextBatch != null)

        expect:
        project.extensions.findByName('iHubDemo') instanceof IHubDemoExtension
        project.plugins.withType(IHubDemoPlugin).any { it.hasPlugin IHubSimplePlugin }
        false == project.iHubDemo.flag.get()
        'text' == project.iHubDemo.str.get()
        'systemValue' == project.iHubDemo.system.get()
        'env' == project.iHubDemo.env.get()
        'ext1' == project.iHubDemo.findExtProperty('ext1')
        'ext2' == project.iHubDemo.findExtProperty(project.rootProject, 'ext2', 'ext2')
        true == project.iHubDemo.trueFlag.get()
        false == project.iHubDemo.falseFlag.get()
        'true' == project.iHubDemo.trueStrFlag.get()
        'false' == project.iHubDemo.falseStrFlag.get()
        'str' == project.iHubDemo.customizationProperty.get()
        project.iHubDemo.javaHome.get()
    }

    def '测试插件打印方法'() {
        setup:
        System.setProperty 'file.encoding', encoding

        expect:
        project.extensions.findByName('iHubPrint') instanceof IHubPrintExtension

        where:
        encoding << ['GBK', 'UTF-8']
    }

}
