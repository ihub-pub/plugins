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

import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.IHubPluginMethods.tap



/**
 * @author henry
 */
@Slf4j
@Title('IHubAware测试套件')
@SuppressWarnings('JUnitPublicNonTestMethod')
class IHubPluginAwareTest extends Specification {

    private Project project

    void setup() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply IHubDemoPlugin
    }

    def '测试扩展特征'() {
        expect:
        project.extensions.findByName('iHubDemo') instanceof IHubDemoExtension
    }

    def '测试插件通用方法'() {
        expect:
        printConfigContent 'test', tap('t1', 30), tap('t2'), [d1: [1, 2, 3], d2: [4, 5, 6]]
        printConfigContent 'test', []
        printConfigContent 'test', tap('t1')
    }

}
