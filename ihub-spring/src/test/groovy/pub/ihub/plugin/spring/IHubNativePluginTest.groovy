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
package pub.ihub.plugin.spring

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * 单独验证 ihub-native 插件可在不依赖 ihub-boot 配置块的情况下应用，
 * 与 IHubBootPluginTest 的 "Native插件配置测试" 互补。
 *
 * @author henry
 */
@Title('IHubNativePlugin 测试套件')
class IHubNativePluginTest extends IHubSpecification {

    def '测试 Native 插件单独应用 - 默认配置'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-native'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '应能成功 apply 且不抛配置异常'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试 Native 插件应用后可读 iHubBoot 扩展'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-native'
            }

            iHubBoot {
                bootJarRequiresUnpack = 'org.example:lib'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: 'Native 插件依赖的 IHubBootExtension 必须可用'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
