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
package pub.ihub.plugin.groovy

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * @author henry
 */
@Title('IHubGroovyPlugin测试套件')
class IHubGroovyPluginTest extends IHubSpecification {

    @Override
    def setup() {
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-groovy'
            }
        '''
    }

    def 'Groovy插件配置测试'() {
        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains '│ implementation                       │ org.apache.groovy:groovy-xml                              │'
        result.output.contains '│ implementation                       │ org.apache.groovy:groovy-dateutil                         │'
        result.output.contains '│ implementation                       │ org.apache.groovy:groovy-templates                        │'
        result.output.contains '│ implementation                       │ org.apache.groovy:groovy-nio                              │'
        result.output.contains '│ implementation                       │ org.apache.groovy:groovy                                  │'
        result.output.contains '│ implementation                       │ org.apache.groovy:groovy-json                             │'
        result.output.contains '│ implementation                       │ org.apache.groovy:groovy-groovydoc                        │'
        result.output.contains '│ implementation                       │ org.apache.groovy:groovy-sql                              │'
        result.output.contains '│ implementation                       │ org.apache.groovy:groovy-datetime                         │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
