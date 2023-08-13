/*
 * Copyright (c) 2021-2023 the original author or authors.
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
package pub.ihub.plugin.publish

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * IHubPublishPlugin测试套件
 * @author henry
 */
@Title('IHubPublishPlugin测试套件')
class IHubPublishPluginTest extends IHubSpecification {

    def 'Publish插件配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'java'
                plugin 'pub.ihub.plugin.ihub-publish'
            }
        '''

        when: '构建项目'
        propertiesFile << '''
version=1.0.0
'''
        def result = gradleBuilder.withArguments('-DiHub.repoUsername=username', '-DiHub.repoPassword=password').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        propertiesFile << '''
iHubPublish.publishNeedSign=true
iHubPublish.signingKeyId=id
iHubPublish.signingSecretKey=secret
iHubPublish.signingPassword=password
iHubPublish.publishSources=false
iHubPublish.publishDocs=true
'''
        result = gradleBuilder.withArguments('-DiHub.repoUsername=username', '-DiHub.repoPassword=password')
            .withEnvironment('GITHUB_ACTIONS': 'false').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        result = gradleBuilder.withArguments('-Pversion=main').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Groovy Publish配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'groovy'
                plugin 'pub.ihub.plugin.ihub-publish'
            }
        '''

        when: '构建项目'
        propertiesFile << '''
version=1.0.0
iHubPublish.publishNeedSign=true
'''
        def result = gradleBuilder.withArguments('-DiHub.repoUsername=username', '-DiHub.repoPassword=password').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        propertiesFile << '''
iHubPublish.signingSecretKey=secret
iHubPublish.signingPassword=password
iHubPublish.publishDocs=true
'''
        result = gradleBuilder
            .withArguments('-DiHub.repoUsername=username', '-DiHub.repoPassword=password', '-DiHubPublish.applyGithubPom=true')
            .withEnvironment('GITHUB_ACTIONS': 'true', 'GITHUB_REPOSITORY': 'ihub-pub/plugins').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '模拟GitHub环境applyGithubPom为false'
        result = gradleBuilder
            .withArguments('-DiHubPublish.applyGithubPom=true').withEnvironment('GITHUB_ACTIONS': 'false').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Java平台Publish配置测试'() {
        setup: '初始化项目'
        buildFile << '''
            plugins {
                id 'java-platform'
                id 'pub.ihub.plugin'
            }
            apply {
                plugin 'pub.ihub.plugin.ihub-publish'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '版本目录组件Publish配置测试'() {
        setup: '初始化项目'
        buildFile << '''
            plugins {
                id 'version-catalog'
                id 'pub.ihub.plugin'
            }
            apply {
                plugin 'pub.ihub.plugin.ihub-publish'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '模拟匹配兼容的java版本配置'
        testProjectDir.newFolder 'gradle'
        testProjectDir.newFile 'gradle/libsJava11.versions.toml'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '模拟-SNAPSHOT版本'
        result = gradleBuilder.withArguments('-Pversion=test-SNAPSHOT').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '组件bom配置构建测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        settingsFile << 'include \'a\', \'b\', \'c\',\'demo-bom\''
        testProjectDir.newFolder 'a'
        testProjectDir.newFolder 'b'
        testProjectDir.newFolder 'c'
        buildFile << '''
project(':a') {
    apply {
        plugin 'java-platform'
        plugin 'pub.ihub.plugin.ihub-publish'
    }
}
project(':b') {
    apply {
        plugin 'java'
        plugin 'pub.ihub.plugin.ihub-publish'
    }
}
project(':c') {
    apply {
        plugin 'java'
    }
}
'''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
