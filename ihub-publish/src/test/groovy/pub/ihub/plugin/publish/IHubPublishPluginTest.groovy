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
package pub.ihub.plugin.publish

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

import static org.gradle.api.initialization.Settings.DEFAULT_SETTINGS_FILE

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
iHubPublish.signingKeyId=id
iHubPublish.signingSecretKey=secret
iHubPublish.signingPassword=password
iHubPublish.publishDocs=true
'''
        def result = gradleBuilder.withArguments('-DiHub.repoUsername=username', '-DiHub.repoPassword=password')
            .withEnvironment('GITHUB_ACTIONS': 'true', 'GITHUB_REPOSITORY': 'ihub-pub/plugins').build()

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

    def '组件bom配置构建测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        testProjectDir.newFile(DEFAULT_SETTINGS_FILE) << 'include \'a\', \'b\', \'c\',\'demo-bom\''
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
        propertiesFile << 'iHubSettings.includeBom=demo-bom\n'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
