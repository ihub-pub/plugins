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
import spock.lang.Title



/**
 * @author henry
 */
@Slf4j
@Title('GitHooks测试套件')
class IHubGitHooksPluginTest extends IHubSpecification {

    private File commitMsgFile

    @Override
    def setup() {
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-git-hooks'
            }
        '''
        testProjectDir.newFolder '.git'
        commitMsgFile = testProjectDir.newFile('.git/COMMIT_EDITMSG')
    }

    def 'GitHooks插件配置测试'() {
        when: '使用默认目录'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Unset git hooks path'

        when: '插件扩展配置'
        buildFile << '''
            iHubGitHooks {
                hooks = ['pre-commit': 'build']
            }
        '''
        result = gradleBuilder.build()

        then: '检查结果'
        new File(testProjectDir.root, '.gradle/pub.ihub.plugin.hooks/pre-commit').exists()
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Set git hooks path: .gradle/pub.ihub.plugin.hooks'

        when: '自定义目录'
        propertiesFile << 'iHubGitHooks.hooksPath=.hooks'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Set git hooks path: .hooks'
    }

    def 'GitHooks插件commitCheck任务测试-Not found commit msg file'() {
        setup: '初始化项目'
        commitMsgFile.delete()

        when: '执行任务'
        def result = gradleBuilder.withArguments('commitCheck').buildAndFail()

        then: '检查结果'
        result.output.contains 'Not found file:'
        result.output.contains '.git/COMMIT_EDITMSG'
    }

    def 'GitHooks插件commitCheck任务测试-Commit msg is empty!'() {
        when: '执行任务'
        def result = gradleBuilder.withArguments('commitCheck').buildAndFail()

        then: '检查结果'
        result.output.contains 'Commit msg is empty!'
    }

    def 'GitHooks插件commitCheck任务测试-type检查失败'() {
        setup: '初始化项目'
        commitMsgFile << 'other(other): text'

        when: '执行任务'
        def result = gradleBuilder.withArguments('commitCheck').buildAndFail()

        then: '检查结果'
        result.output.contains 'Commit msg header check fail!'
    }

    def 'GitHooks插件commitCheck任务测试-description检查失败'() {
        setup: '初始化项目'
        commitMsgFile << 'feat: '

        when: 'description长度为0'
        def result = gradleBuilder.withArguments('commitCheck').buildAndFail()

        then: '检查结果'
        result.output.contains 'Commit msg header check fail!'

        when: 'description长度为超过100'
        commitMsgFile << '-' * 101
        result = gradleBuilder.withArguments('commitCheck').buildAndFail()

        then: '检查结果'
        result.output.contains 'Commit msg header check fail!'
    }

    def 'GitHooks插件commitCheck任务测试-成功'() {
        setup: '初始化项目'
        commitMsgFile << 'feat(some): text'

        when: '执行任务'
        def result = gradleBuilder.withArguments('commitCheck').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
