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
import pub.ihub.plugin.generate.IHubGeneratePlugin
import pub.ihub.plugin.generate.IHubGithubConfigTask
import spock.lang.IgnoreIf
import spock.lang.Title

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS



/**
 * @author henry
 */
@Slf4j
@Title('生成插件扩展测试套件')
class IHubGeneratePluginTest extends IHubSpecification {

    def 'Github配置任务方法测试'() {
        setup:
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply IHubGeneratePlugin

        expect:
        project.tasks.findByName('generateGithubConfig') instanceof IHubGithubConfigTask
        project.generateGithubConfig.actions.each { it.execute project.generateGithubConfig }
        project.generateGithubConfig.actions.each { it.execute project.generateGithubConfig }
    }

    @IgnoreIf({ System.getProperty('fast.test')?.toBoolean() })
    def 'Github配置任务测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '构建项目'
        def result = gradleBuilder.withArguments('generateGithubConfig').build()

        then: '检查结果'
        result.task(':generateGithubConfig').outcome == SUCCESS
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        result = gradleBuilder.withArguments('generateGithubConfig').build()

        then: '检查结果'
        result.task(':generateGithubConfig').outcome == SUCCESS
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
