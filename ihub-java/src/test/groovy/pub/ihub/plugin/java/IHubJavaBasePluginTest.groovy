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
package pub.ihub.plugin.java

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * 单独验证 ihub-java-base 插件可独立 apply（不携带 ihub-java 的默认依赖、jvmArgs 等业务规则）。
 *
 * @author henry
 */
@Title('IHubJavaBasePlugin 测试套件')
class IHubJavaBasePluginTest extends IHubSpecification {

    def '测试 JavaBase 插件单独应用 - 默认无依赖配置'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-java-base'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '应能成功 apply 且不抛异常'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试 JavaBase 与 ihub-java 协同：iHubJava 扩展由 ihub-java 提供'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-java-base'
                plugin 'pub.ihub.plugin.ihub-java'
            }

            iHubJava {
                jvmArgs = '-Xmx512m'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: 'iHubJavaExtension 应正常注册'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
