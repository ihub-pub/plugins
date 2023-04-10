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
package pub.ihub.plugin.version

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * IHub版本插件测试
 * @author henry
 */
@Title('IHubVersionPlugin测试套件')
class IHubVersionPluginTest extends IHubSpecification {

    def '自定义依赖升级打印方法测试'() {
        setup: '初始化项目'
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply IHubVersionPlugin

        when: '测试配置方法'
        project.plugins.withType(IHubVersionPlugin) {
            it.rejectVersionFilter currentVersion: '5.7.12', candidate: [version: '5.7.13']
            it.rejectVersionFilter currentVersion: '5.7.12.ga', candidate: [version: '5.7.13.m']
            it.rejectVersionFilter currentVersion: '5.7.12.m', candidate: [version: '5.7.13.ga']
        }
        project.plugins.withType(IHubVersionPlugin) {
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
        project.iHubVersion.autoReplaceLaterVersions = true
        project.plugins.withType(IHubVersionPlugin) {
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
        project.plugins.withType(IHubVersionPlugin) {
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
        project.pluginManager.apply IHubVersionPlugin
        project.version = version

        when: '测试配置方法'
        project.extensions.getByType(IHubVersionExtension).useInferringVersion.set inferring
        project.plugins.withType(IHubVersionPlugin) {
            it.configProjectWithGit()
        }

        then: '检查结果'
        project.version.toString() ==~ expected

        where: '3号用例本地执行不通过'
        inferring | version       | expected
        true      | 'unspecified' | /^\d+.\d+.\d+-SNAPSHOT$/
        true      | '1.0.0'       | '1.0.0'
        false     | 'unspecified' | 'unspecified'
        false     | '1.0.0'       | '1.0.0'
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

    def '自定义依赖升级打印测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '检查组件版本'
        def result = gradleBuilder.withArguments('dependencyUpdates').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
