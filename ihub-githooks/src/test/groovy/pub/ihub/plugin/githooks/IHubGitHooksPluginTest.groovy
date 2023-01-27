/*
 * Copyright (c) 2021 Henry 李恒 (henry.box@outlook.com).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pub.ihub.plugin.githooks

import groovy.util.logging.Slf4j
import pub.ihub.plugin.test.IHubSpecification
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

    def 'GitHooks插件commitCheck任务测试-scope检查'() {
        when: '开启范围检查'
        buildFile << '''
            iHubGitHooks {
                type 'build' scopes 'gradle' requiredScope true
            }
        '''
        commitMsgFile << 'build(other): text'
        def result = gradleBuilder.withArguments('commitCheck').buildAndFail()

        then: '检查结果'
        result.output.contains 'Commit msg header scope not in [gradle]!'

        when: '执行任务'
        commitMsgFile.write 'build(gradle): text', true
        result = gradleBuilder.withArguments('commitCheck').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'GitHooks插件commitCheck任务测试-注脚必填检查'() {
        when: 'Footer必填'
        buildFile << '''
            iHubGitHooks {
                footer 'Footer' required true
            }
        '''
        commitMsgFile << 'feat: text'
        def result = gradleBuilder.withArguments('commitCheck').buildAndFail()

        then: '检查结果'
        result.output.contains 'Commit msg footer missing \'Footer\'!'

        when: '执行任务'
        commitMsgFile.write 'feat: text\n\nFooter: footer', true
        result = gradleBuilder.withArguments('commitCheck').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'GitHooks插件commitCheck任务测试-注脚类型必填检查'() {
        when: 'Footer必填'
        buildFile << '''
            iHubGitHooks {
                footer 'Footer' requiredWithType 'feat'
            }
        '''
        commitMsgFile << 'feat: text'
        def result = gradleBuilder.withArguments('commitCheck').buildAndFail()

        then: '检查结果'
        result.output.contains 'Commit msg footer missing \'Footer\' where type is \'feat\'!'

        when: '执行任务'
        commitMsgFile.write 'feat: text\n\nFooter: footer', true
        result = gradleBuilder.withArguments('commitCheck').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'GitHooks插件commitCheck任务测试-注脚值正则校验'() {
        when: '注解值开启校验但无注解值'
        buildFile << '''
            iHubGitHooks {
                footer 'Closes' valueRegex '\\\\d+'
            }
        '''
        commitMsgFile << 'feat: text'
        def result = gradleBuilder.withArguments('commitCheck').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '注解值开启校验注解值错误'
        commitMsgFile.write 'feat: text\n\nCloses: abc', true
        result = gradleBuilder.withArguments('commitCheck').buildAndFail()

        then: '检查结果'
        result.output.contains 'Commit msg footer \'Closes\' check fail with regex: \'\\d+\'!'

        when: '注解值开启校验且注解值正确'
        commitMsgFile.write 'feat: text\n\nCloses: 123', true
        result = gradleBuilder.withArguments('commitCheck').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'GitHooks插件commitCheck任务测试-生成自定义配置'() {
        when: '非IDEA环境，没有插件目录'
        def result = gradleBuilder.withArguments('-Didea.plugins.path=').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        !new File(testProjectDir.root, '.gradle/pub.ihub.plugin.cache/conventionalCommit.json').exists()
        !new File(testProjectDir.root, '.idea/conventionalCommit.xml').exists()

        when: 'IDEA环境，没有Conventional Commit插件'
        result = gradleBuilder.withArguments('-Didea.plugins.path=' + testProjectDir.root.path).build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        !new File(testProjectDir.root, '.gradle/pub.ihub.plugin.cache/conventionalCommit.json').exists()
        !new File(testProjectDir.root, '.idea/conventionalCommit.xml').exists()

        when: 'IDEA环境且有Conventional Commit插件'
        def pluginsPath = testProjectDir.newFolder('.plugins', 'idea-conventional-commit').parentFile.path
        result = gradleBuilder.withArguments('-Didea.plugins.path=' + pluginsPath).build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        new File(testProjectDir.root, '.gradle/pub.ihub.plugin.cache/conventionalCommit.json').exists()
        new File(testProjectDir.root, '.idea/conventionalCommit.xml').exists()

        when: '模拟插件配置已存在且没有自定义配置'
        new File(testProjectDir.root, '.idea/conventionalCommit.xml').write '''<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="general">
    <option name="other" value="text" />
  </component>
</project>''', true
        result = gradleBuilder.withArguments('-Didea.plugins.path=' + pluginsPath).build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '模拟插件配置已存在且设置了自定义配置'
        new File(testProjectDir.root, '.idea/conventionalCommit.xml').write '''<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="general">
    <option name="customFilePath" value="path" />
  </component>
</project>''', true
        result = gradleBuilder.withArguments('-Didea.plugins.path=' + pluginsPath).build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'GitHooks插件commitCheck任务测试-成功'() {
        setup: '初始化项目'
        buildFile << '''
            iHubGitHooks {
                type 'type' scope 'scope' description 'Scope description'
                footer 'Other' description 'Other description'
            }
        '''
        commitMsgFile << 'feat(some): text\n\nCloses: 123'

        when: '执行任务'
        def result = gradleBuilder.withArguments('commitCheck').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
