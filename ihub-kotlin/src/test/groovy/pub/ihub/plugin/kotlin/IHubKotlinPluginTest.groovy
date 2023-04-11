/*
 * Copyright (c) 2023 the original author or authors.
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
package pub.ihub.plugin.kotlin

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * @author henry
 */
@Title('IHubKotlinPlugin测试套件')
class IHubKotlinPluginTest extends IHubSpecification {

    @Override
    def setup() {
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-kotlin'
            }
        '''
    }

    def 'Kotlin插件配置测试'() {
        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
