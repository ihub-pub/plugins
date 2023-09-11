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
package pub.ihub.plugin.node

import com.github.gradle.node.NodeExtension
import com.github.gradle.node.util.PlatformHelperKt
import org.gradle.api.Project
import org.gradle.process.internal.ExecException
import org.gradle.testfixtures.ProjectBuilder
import pub.ihub.plugin.node.cnpm.task.CnpmTask
import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * @author henry
 */
@Title('Node测试套件')
class IHubNodePluginTest extends IHubSpecification {

    @Override
    def setup() {
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-node'
            }
        '''
    }

    def '基础配置测试'() {
        when: '执行任务'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '任务测试'() {
        when: 'install任务'
        def result = gradleBuilder.withArguments('cnpmInstall').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: 'sync任务'
        result = gradleBuilder.withArguments('cnpmSync').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '自定义任务'
        buildFile << '''
            task cnpm_run_dev(type: pub.ihub.plugin.node.cnpm.task.CnpmTask) {
                args = ['run','dev']
            }
        '''
        testProjectDir.newFile('package.json') << '''
{
  "name": "test-demo",
  "description": "demo project",
  "author": "henry",
  "private": true,
  "scripts": {
    "dev": "clean",
    "build": "clean"
  },
  "engines": {
    "node": ">=6"
  }
}
'''
        result = gradleBuilder.withArguments('cnpm_run_dev').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '执行任务'
        result = gradleBuilder.withArguments('cnpm_run_dev', '-DiHubNode.cnpmVersion=9.2.0').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '静态模拟非window环境测试'() {
        when:
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply IHubNodePlugin
        project.extensions.getByType(NodeExtension)
                .resolvedPlatform.set(PlatformHelperKt.parsePlatform('linux', '') { 'name' })
        project.tasks.withType(CnpmTask) {
            it.exec()
        }

        then:
        def exception = thrown(ExecException)
        exception.message
    }

    def '静态模拟window环境测试'() {
        when:
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply IHubNodePlugin
        project.extensions.getByType(NodeExtension)
            .resolvedPlatform.set(PlatformHelperKt.parsePlatform('windows', '') { 'name' })
        project.tasks.withType(CnpmTask) {
            it.exec()
        }

        then:
        def exception = thrown(ExecException)
        exception.message
    }

}
