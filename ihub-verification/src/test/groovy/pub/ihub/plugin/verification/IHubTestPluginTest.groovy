/*
 * Copyright (c) 2022-2026 the original author or authors.
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

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * 单独验证 ihub-test 插件可独立 apply（不携带 verification 的 codenarc/pmd/jacoco 规则）。
 *
 * @author henry
 */
@Title('IHubTestPlugin 测试套件')
class IHubTestPluginTest extends IHubSpecification {

    def '测试 Test 插件单独应用 - 默认配置'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'java'
                plugin 'pub.ihub.plugin.ihub-test'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '应能成功 apply 且不抛异常'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试 Test 插件可通过扩展配置 classes 过滤'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'java'
                plugin 'pub.ihub.plugin.ihub-test'
            }

            iHubTest {
                classes = '**/SmokeTest'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: 'iHubTestExtension 应正常注册并接受属性'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
