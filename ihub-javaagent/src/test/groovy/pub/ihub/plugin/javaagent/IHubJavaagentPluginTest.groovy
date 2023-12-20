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
package pub.ihub.plugin.javaagent

import com.ryandens.javaagent.JavaagentBasePlugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskProvider
import org.gradle.testfixtures.ProjectBuilder
import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

@Title('IHubJavaagentPlugin测试套件')
class IHubJavaagentPluginTest extends IHubSpecification {

    def 'Javaagent插件配置测试'() {
        setup: '初始化项目'
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-javaagent'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Javaagent插件应用配置测试'() {
        setup: '初始化项目'
        buildFile << '''
            plugins {
                id 'application'
                id 'pub.ihub.plugin.ihub-javaagent'
            }
        '''
        propertiesFile << 'iHubJavaagent.javaagent=pub.ihub.integration:ihub-agent'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Javaagent插件SpringBoot配置测试'() {
        setup: '初始化项目'
        Project project = ProjectBuilder.builder().build()

        when: '构建项目'
        project.pluginManager.apply 'org.springframework.boot'
        def bootRun = project.tasks.register 'bootRun', JavaExec
        project.pluginManager.apply IHubJavaagentPlugin
        project.plugins.getPlugin(IHubJavaagentPlugin).CONFIGURE_JAVA_EXEC bootRun.get()

        then: '检查结果'

        then: '检查结果'
        project.plugins.hasPlugin JavaagentBasePlugin
    }

}
