/*
 * Copyright (c) 2023-2024 the original author or authors.
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
package pub.ihub.plugin.shadow

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * @author henry
 */
@Title('IHubShadowPlugin测试套件')
class IHubShadowPluginTest extends IHubSpecification {

    def 'Shadow插件配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-shadow'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        testProjectDir.newFile('.java-local.properties') << 'spring.profiles.active=dev'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Shadow插件应用配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'application'
                plugin 'pub.ihub.plugin.ihub-shadow'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Shadow插件组件配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'maven-publish'
                plugin 'signing'
                plugin 'pub.ihub.plugin.ihub-shadow'
            }
        '''
        testProjectDir.newFolder 'src', 'main', 'java', 'pub', 'ihub', 'agent'
        def agentClass = testProjectDir.newFile 'src/main/java/pub/ihub/agent/IHubAgent.java'
        agentClass << 'package pub.ihub.agent;\n'
        agentClass << 'public class IHubAgent {\n'
        if (withPremain) {
            agentClass << '    public static void premain(String agentArgs) {\n'
            agentClass << '        System.out.println("premain");\n'
            agentClass << '    }\n'
        }
        if (withAgentmain) {
            agentClass << '    public static void agentmain(String agentArgs) {\n'
            agentClass << '        System.out.println("agentmain");\n'
            agentClass << '    }\n'
        }
        agentClass << '}'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        where:
        withPremain | withAgentmain
        true        | true
        true        | false
        false       | true
    }

}
