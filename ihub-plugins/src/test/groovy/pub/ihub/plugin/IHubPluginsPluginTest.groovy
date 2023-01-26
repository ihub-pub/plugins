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
import pub.ihub.plugin.test.IHubSpecification
import spock.lang.IgnoreIf
import spock.lang.Title

import static org.gradle.api.initialization.Settings.DEFAULT_SETTINGS_FILE



/**
 * @author henry
 */
@Slf4j
@Title('IHubPluginsPlugin测试套件')
@SuppressWarnings('PrivateFieldCouldBeFinal')
@IgnoreIf({ System.getProperty('fast.test')?.toBoolean() })
class IHubPluginsPluginTest extends IHubSpecification {

    def '基础构建测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'Group Maven Bom Version'
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

    def '项目组件仓库配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '构建项目'
        propertiesFile << '''
iHub.mavenLocalEnabled=true
iHub.mavenAliYunEnabled=false
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
        !result.output.contains('AliYunPublic')
        !result.output.contains('AliYunGoogle')
        !result.output.contains('AliYunSpring')
        result.output.contains('ReleaseRepo')
        result.output.contains('SnapshotRepo')
        result.output.contains('CustomizeRepo')
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        propertiesFile << 'iHub.repoIncludeGroup=pub.ihub.demo'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '扩展属性测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
        gradle.taskGraph.whenReady {
            println 'repoUsername:' + iHub.repoUsername
        }
        '''

        when: '读取默认属性'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'repoUsername:null'

        when: '读取扩展属性'
        buildFile << '''
        iHub {
            repoUsername = 'type\\next'
        }
        '''
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'repoUsername:type\next'

        when: '读取项目属性'
        propertiesFile << 'iHub.repoUsername=type\\nprj'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'repoUsername:type\nprj'

        when: '读取环境属性'
        result = gradleBuilder.withEnvironment(REPO_USERNAME: 'type\nenv').build()

        then: '检查结果'
        result.output.contains 'repoUsername:type\nenv'

        when: '读取系统属性'
        result = gradleBuilder.withEnvironment(REPO_USERNAME: 'type\nenv').withArguments('-DiHub.repoUsername=type\nsys').build()

        then: '检查结果'
        result.output.contains 'repoUsername:type\nsys'
    }

    def '自定义依赖升级打印方法测试'() {
        setup: '初始化项目'
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply IHubPluginsPlugin

        when: '测试配置方法'
        project.plugins.withType(IHubPluginsPlugin) {
            it.rejectVersionFilter currentVersion: '5.7.12', candidate: [version: '5.7.13']
            it.rejectVersionFilter currentVersion: '5.7.12.ga', candidate: [version: '5.7.13.m']
            it.rejectVersionFilter currentVersion: '5.7.12.m', candidate: [version: '5.7.13.ga']
        }
        project.plugins.withType(IHubPluginsPlugin) {
            it.dependencyUpdatesOutputFormatter current: [
                dependencies: [[group: 'cn.hutool', name: 'hutool-all', version: '5.7.13']]
            ], exceeded: [
                dependencies: [[group: 'cn.hutool', name: 'hutool-all', version: '5.7.13', latest: '5.7.12']]
            ], outdated: [
                dependencies: [[group: 'cn.hutool', name: 'hutool-all', version: '5.7.12', available: [release: '5.7.13']]]
            ], gradle: [enabled: true, running: [version: '7.2'], current: [version: '7.3']]
            it.dependencyUpdatesOutputFormatter current: [dependencies: []], exceeded: [dependencies: []],
                outdated: [dependencies: []], gradle: [enabled: false]
            it.dependencyUpdatesOutputFormatter current: [dependencies: []], exceeded: [dependencies: []], outdated: [
                dependencies: [[group: 'cn.hutool', name: 'hutool-all', version: '5.7.12', available: [milestone: '5.7.13']]]
            ], gradle: [enabled: false]
        }
        project.iHub.autoReplaceLaterVersions = true
        project.plugins.withType(IHubPluginsPlugin) {
            it.dependencyUpdatesOutputFormatter current: [dependencies: []], exceeded: [dependencies: []], outdated: [
                dependencies: [[group: 'cn.hutool', name: 'hutool-all', version: '5.7.12', available: [release: '5.7.13']]]
            ], gradle: [enabled: false]
        }
        project.buildFile.createNewFile()
        project.buildFile << '''
            dependencies {
                api 'cn.hutool:hutool-all:5.7.12',
                    'cn.hutool:hutool-core:5.7.12'
            }
        '''
        project.plugins.withType(IHubPluginsPlugin) {
            it.dependencyUpdatesOutputFormatter current: [dependencies: []], exceeded: [dependencies: []], outdated: [
                dependencies: [
                    [group: 'cn.hutool', name: 'hutool-all', version: '5.7.12', available: [release: '5.7.13']],
                    [group: 'cn.hutool', name: 'hutool-core', version: '5.7.12', available: [milestone: '5.7.13']]
                ]
            ], gradle: [enabled: false]
        }

        then: '检查结果'
        project.tasks.getByName 'dependencyUpdates'
    }

    def '推断版本号单元测试'() {
        setup: '初始化项目'
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply IHubPluginsPlugin
        project.version = 'unspecified'

        when: '测试配置方法'
        project.extensions.getByType(IHubPluginsExtension).useInferringVersion = true
        project.plugins.withType(IHubPluginsPlugin) {
            it.configProjectWithGit()
        }

        then: '检查结果'
        project.version.toString() ==~ /^\d+.\d+.\d+-SNAPSHOT$/
    }

    def '推断版本号配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '模拟开启推测版本且无git环境'
        def result = gradleBuilder
            .withEnvironment(USE_INFERRING_VERSION: 'true')
            .build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Failed to get current git tag'

        when: '模拟开启推测版本且指定了具体版本号'
        result = gradleBuilder
            .withEnvironment(USE_INFERRING_VERSION: 'true')
            .withArguments('-Pversion=1.0.0')
            .build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Using explicit version 1.0.0'
        !result.output.contains('Inferring version use git tag: ')
        !result.output.contains('Failed to get current git tag')
    }

    def '多项目构建测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        testProjectDir.newFile(DEFAULT_SETTINGS_FILE) << 'include \'a\', \'b\', \'c\''
        testProjectDir.newFolder 'a'
        testProjectDir.newFolder 'b'
        testProjectDir.newFolder 'c'

        when: '构建项目'
        def result = gradleBuilder.withArguments('build').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '自定义依赖升级打印测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '检查组件版本'
        def result = gradleBuilder.withArguments('dependencyUpdates').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
