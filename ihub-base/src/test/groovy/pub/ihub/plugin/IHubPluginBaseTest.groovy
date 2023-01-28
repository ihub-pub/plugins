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
import spock.lang.Specification
import spock.lang.Title



/**
 * @author henry
 */
@Slf4j
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

        expect:
        project.extensions.findByName('iHubDemo') instanceof IHubDemoExtension
        project.plugins.withType(IHubDemoPlugin).any { it.hasPlugin IHubSimplePlugin }
        'ext1' == project.iHubDemo.findProjectProperty('ext1')
        false == project.iHubDemo.flag
        'text' == project.iHubDemo.str
        'system' == project.iHubDemo.system
        'env' == project.iHubDemo.env
        'method' == project.iHubDemo.getMethod
        'ext1' == project.iHubDemo.findExtProperty('ext1')
        'ext2' == project.iHubDemo.findExtProperty(project.rootProject, 'ext2', 'ext2')
    }

    def '测试插件打印方法'() {
        expect:
        project.extensions.findByName('iHubPrint') instanceof IHubPrintExtension
    }

}
