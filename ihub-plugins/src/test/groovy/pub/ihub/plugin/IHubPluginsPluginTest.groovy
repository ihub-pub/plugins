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
package pub.ihub.plugin

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * @author henry
 */
@Title('IHubPluginsPlugin测试套件')
@SuppressWarnings('PrivateFieldCouldBeFinal')
class IHubPluginsPluginTest extends IHubSpecification {

    def '基础构建测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Java平台构建测试'() {
        setup: '初始化项目'
        buildFile << '''
plugins {
    id 'java-platform'
    id 'pub.ihub.plugin'
}
'''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        !result.output.contains('Group Maven Bom Version')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '版本目录组件构建测试'() {
        setup: '初始化项目'
        buildFile << '''
plugins {
    id 'version-catalog'
    id 'pub.ihub.plugin'
}
'''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        !result.output.contains('Group Maven Bom Version')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '项目组件仓库配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '构建项目'
        propertiesFile << '''
iHub.mavenLocalEnabled=true
iHub.mavenAliYunEnabled=true
iHub.releaseRepoUrl=https://ihub.pub/nexus/content/repositories/releases
iHub.snapshotRepoUrl=https://ihub.pub/nexus/content/repositories/snapshots
iHub.customizeRepoUrl=https://ihub.pub/nexus/content/repositories
iHub.repoAllowInsecureProtocol=true
iHub.repoIncludeGroupRegex=pub\\.ihub\\..*
'''
        testProjectDir.newFolder 'libs'
        def result = gradleBuilder.withArguments('-DiHub.repoUsername=username', '-DiHub.repoPassword=password').build()

        then: '检查结果'
        result.output.contains('flatDir')
        result.output.contains('MavenLocal')
        result.output.contains('AliYunPublic')
        result.output.contains('ReleaseRepo')
        result.output.contains('SnapshotRepo')
        result.output.contains('CustomizeRepo')
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        propertiesFile << 'iHub.repoIncludeGroup=pub.ihub.demo\n'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains('flatDir')
        result.output.contains('MavenLocal')
        result.output.contains('AliYunPublic')
        result.output.contains('ReleaseRepo')
        result.output.contains('SnapshotRepo')
        result.output.contains('CustomizeRepo')
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        propertiesFile << 'iHub.mavenPrivateEnabled=false'
        result = gradleBuilder.build()

        then: '检查结果'
        !result.output.contains('ReleaseRepo')
        !result.output.contains('SnapshotRepo')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '扩展属性测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
        gradle.taskGraph.whenReady {
            println 'repoUsername:' + iHub.repoUsername.orNull
        }
        '''

        when: '读取默认属性'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'repoUsername:null'

        when: '读取项目属性'
        propertiesFile << 'iHub.repoUsername=type\\nprj'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'repoUsername:type\nprj'

        when: '读取扩展属性'
        buildFile << '''
        iHub {
            repoUsername = 'type\\next'
        }
        '''
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'repoUsername:type\next'

        when: '读取环境属性'
        result = gradleBuilder.withEnvironment(REPO_USERNAME: 'type\nenv').build()

        then: '检查结果'
        result.output.contains 'repoUsername:type\nenv'

        when: '读取系统属性'
        result = gradleBuilder.withEnvironment(REPO_USERNAME: 'type\nenv').withArguments('-DiHub.repoUsername=type\nsys').build()

        then: '检查结果'
        result.output.contains 'repoUsername:type\nsys'
    }

    def '多项目构建测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        settingsFile << 'include \'a\', \'b\', \'c\''
        testProjectDir.newFolder 'a'
        testProjectDir.newFolder 'b'
        testProjectDir.newFolder 'c'

        when: '构建项目'
        def result = gradleBuilder.withArguments('build').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '自动同意Scan插件条款测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '构建项目'
        if (scan) {
            gradleBuilder.withArguments('--scan')
        }
        if (isActions) {
            gradleBuilder.withEnvironment('GITHUB_ACTIONS': 'true')
        }
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        where:
        scan  | isActions
        true  | true
        true  | false
        false | true
        false | false
    }

}
