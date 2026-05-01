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
package pub.ihub.plugin.skills

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * IHub AI Skills 插件测试套件
 * @author henry
 */
@Title('IHub Skills 插件测试套件')
class IHubSkillsPluginTest extends IHubSpecification {

    @Override
    def setup() {
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-skills'
            }
        '''
    }

    def '默认配置测试：全部技能文件均被安装'() {
        when: '执行默认构建'
        def result = gradleBuilder.build()

        then: '三个技能文件均已生成'
        result.output.contains 'BUILD SUCCESSFUL'
        new File(testProjectDir, '.claude/commands/ihub-diagnose.md').exists()
        new File(testProjectDir, '.claude/commands/ihub-configure.md').exists()
        new File(testProjectDir, '.github/copilot-instructions.md').exists()
        new File(testProjectDir, 'AGENTS.md').exists()
    }

    def '日志测试：包含技能安装路径'() {
        when: '执行构建'
        def result = gradleBuilder.build()

        then: '日志包含安装信息'
        result.output.contains 'IHub Skills: installed Claude skill /ihub-diagnose'
        result.output.contains 'IHub Skills: installed Claude skill /ihub-configure'
        result.output.contains 'IHub Skills: installed Copilot instructions'
        result.output.contains 'IHub Skills: installed OpenCode AGENTS.md'
    }

    def '全局禁用测试：不安装任何技能文件'() {
        given: '配置全局禁用'
        buildFile << '''
            iHubSkills {
                enabled = false
            }
        '''

        when: '执行构建'
        def result = gradleBuilder.build()

        then: '无技能文件生成'
        result.output.contains 'BUILD SUCCESSFUL'
        !new File(testProjectDir, '.claude/commands/ihub-diagnose.md').exists()
        !new File(testProjectDir, '.github/copilot-instructions.md').exists()
        !new File(testProjectDir, 'AGENTS.md').exists()
    }

    def '单独禁用Claude技能测试'() {
        given: '配置 Claude 技能禁用'
        buildFile << '''
            iHubSkills {
                claudeEnabled = false
            }
        '''

        when: '执行构建'
        def result = gradleBuilder.build()

        then: 'Claude 技能不安装，其他技能正常安装'
        result.output.contains 'BUILD SUCCESSFUL'
        !new File(testProjectDir, '.claude/commands/ihub-diagnose.md').exists()
        new File(testProjectDir, '.github/copilot-instructions.md').exists()
        new File(testProjectDir, 'AGENTS.md').exists()
    }

    def '单独禁用Copilot技能测试'() {
        given: '配置 Copilot 技能禁用'
        buildFile << '''
            iHubSkills {
                copilotEnabled = false
            }
        '''

        when: '执行构建'
        def result = gradleBuilder.build()

        then: 'Copilot 技能不安装，其他技能正常安装'
        result.output.contains 'BUILD SUCCESSFUL'
        new File(testProjectDir, '.claude/commands/ihub-diagnose.md').exists()
        !new File(testProjectDir, '.github/copilot-instructions.md').exists()
        new File(testProjectDir, 'AGENTS.md').exists()
    }

    def '单独禁用OpenCode技能测试'() {
        given: '配置 OpenCode 技能禁用'
        buildFile << '''
            iHubSkills {
                openCodeEnabled = false
            }
        '''

        when: '执行构建'
        def result = gradleBuilder.build()

        then: 'OpenCode 技能不安装，其他技能正常安装'
        result.output.contains 'BUILD SUCCESSFUL'
        new File(testProjectDir, '.claude/commands/ihub-diagnose.md').exists()
        new File(testProjectDir, '.github/copilot-instructions.md').exists()
        !new File(testProjectDir, 'AGENTS.md').exists()
    }

    def '自定义claudeCommands目录测试'() {
        given: '配置自定义目录'
        buildFile << '''
            iHubSkills {
                claudeCommandsDir = layout.projectDirectory.dir('custom-ai/commands')
            }
        '''

        when: '执行构建'
        def result = gradleBuilder.build()

        then: '技能文件写入自定义目录'
        result.output.contains 'BUILD SUCCESSFUL'
        new File(testProjectDir, 'custom-ai/commands/ihub-diagnose.md').exists()
        new File(testProjectDir, 'custom-ai/commands/ihub-configure.md').exists()
    }

    def 'claude技能文件内容测试：包含关键指令'() {
        when: '执行构建'
        gradleBuilder.build()

        then: 'ihub-diagnose.md 包含诊断关键词'
        def diagnoseFile = new File(testProjectDir, '.claude/commands/ihub-diagnose.md')
        diagnoseFile.exists()
        diagnoseFile.text.contains('IHub Plugins')
        diagnoseFile.text.contains('tasks.create')
        diagnoseFile.text.contains('afterEvaluate')

        and: 'ihub-configure.md 包含配置向导关键词'
        def configureFile = new File(testProjectDir, '.claude/commands/ihub-configure.md')
        configureFile.exists()
        configureFile.text.contains('ihub-settings')
        configureFile.text.contains('pub.ihub.plugin')
    }

}
