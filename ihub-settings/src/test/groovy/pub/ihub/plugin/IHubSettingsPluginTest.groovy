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

import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

import static org.gradle.api.Project.DEFAULT_BUILD_FILE
import static org.gradle.api.Project.GRADLE_PROPERTIES
import static org.gradle.api.initialization.Settings.DEFAULT_SETTINGS_FILE
import static org.gradle.internal.impldep.org.apache.ivy.util.FileUtil.copy

/**
 * @author henry
 */
@Title('IHubSettingsPlugin测试套件')
@SuppressWarnings('PrivateFieldCouldBeFinal')
class IHubSettingsPluginTest extends IHubSpecification {

    @Rule
    private TemporaryFolder testProjectDir = new TemporaryFolder()
    private File settingsFile
    private File propertiesFile
    private GradleRunner gradleBuilder

    /**
     * 初始化项目配置
     */
    def setup() {
        testProjectDir.create()
        settingsFile = testProjectDir.newFile DEFAULT_SETTINGS_FILE
        propertiesFile = testProjectDir.newFile GRADLE_PROPERTIES
        gradleBuilder = GradleRunner.create().withProjectDir(testProjectDir.root).withPluginClasspath()
        copy getClass().classLoader.getResourceAsStream('testkit-gradle.properties'), propertiesFile, null
        settingsFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-settings'
            }
        '''
    }

    def '测试插件仓库配置'() {
        when: '基础配置'
        def result = gradleBuilder.build()

        then: '检查结果'
        !result.output.contains('MavenLocal')
        !result.output.contains('AliYunGradle')
        result.output.contains 'Gradle Central Plugin Repository'
        result.output.contains 'MavenRepo'
        !result.output.contains('ReleaseRepo')
        !result.output.contains('SnapshotRepo')
        !result.output.contains('CustomizeRepo')

        when: '私有仓库配置'
        propertiesFile << '''
iHub.mavenLocalEnabled=true
iHub.mavenAliYunEnabled=true
iHub.releaseRepoUrl=https://ihub.pub/nexus/content/repositories/releases
iHub.snapshotRepoUrl=https://ihub.pub/nexus/content/repositories/snapshots
iHub.customizeRepoUrl=https://ihub.pub/nexus/content/repositories
'''
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'MavenLocal'
        result.output.contains 'AliYunGradle'
        result.output.contains 'Gradle Central Plugin Repository'
        result.output.contains 'ReleaseRepo'
        result.output.contains 'SnapshotRepo'
        result.output.contains 'CustomizeRepo'

        when: '本地插件配置'
        testProjectDir.newFolder 'gradle', 'plugins'
        propertiesFile << 'iHub.mavenPrivateEnabled=false'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'flatDir'
        !result.output.contains('ReleaseRepo')
        !result.output.contains('SnapshotRepo')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试插件版本配置'() {
        when: '默认配置'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'com.gradle.plugin-publish'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试catalog配置'() {
        when: '默认配置'
        testProjectDir.newFolder 'gradle', 'libs'
        testProjectDir.newFile('gradle/libs.versions.toml') << '''
[versions]
james-bond = '0.0.7'
'''
        testProjectDir.newFile('gradle/libs/myLibs.versions.toml') << '''
[versions]
henry = '0.0.8'
'''
        testProjectDir.newFile('gradle/libs/other.toml')
        testProjectDir.newFile(DEFAULT_BUILD_FILE) << '''
println "I'm " + libs.versions.james.bond.get()
println "I'm " + myLibs.versions.henry.get()
'''
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'I\'m 0.0.7'
        result.output.contains 'I\'m 0.0.8'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试profile-catalog配置'() {
        when: '默认配置'
        testProjectDir.newFolder 'gradle', 'libs', 'profiles'
        testProjectDir.newFile('gradle/libs.versions.toml') << '''
[versions]
henry = '0.0.7'
'''
        testProjectDir.newFile('gradle/libs/profiles/dev.versions.toml') << '''
[versions]
henry = '0.0.8'
'''
        testProjectDir.newFile(DEFAULT_BUILD_FILE) << '''
println "I'm " + libs.versions.henry.get()
'''
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'I\'m 0.0.7'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '设置profile'
        propertiesFile << 'iHub.profile=dev,other\n'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'I\'m 0.0.8'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试扩展属性配置子项目'() {
        when: '配置项目'
        propertiesFile << 'name=demo\n'
        testProjectDir.newFolder 'rest'
        testProjectDir.newFolder 'service'
        testProjectDir.newFolder 'other'
        settingsFile << '''
            iHubSettings {
                includeProjects 'rest', 'service', 'other'
            }
        '''
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'rest'
        result.output.contains 'service'
        result.output.contains 'other'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试项目属性配置子项目'() {
        when: '配置项目'
        propertiesFile << 'name=demo\n'
        propertiesFile << 'iHubSettings.includeDirs=rest,service\n'
        testProjectDir.newFolder 'rest'
        testProjectDir.newFolder 'service'
        testProjectDir.newFolder 'other'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'rest'
        result.output.contains 'service'
        !result.output.contains('other')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试跳过目录属性配置子项目'() {
        when: '配置项目'
        propertiesFile << 'name=demo\n'
        propertiesFile << 'iHubSettings.skippedDirs=other\n'
        testProjectDir.newFolder '.git'
        testProjectDir.newFolder 'src'
        testProjectDir.newFolder 'rest'
        testProjectDir.newFolder 'service'
        testProjectDir.newFolder 'other'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'rest'
        result.output.contains 'service'
        !result.output.contains('other')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试配置三级子项目'() {
        when: '配置项目'
        propertiesFile << 'name=demo\n'
        testProjectDir.with {
            newFolder 'rest'
            newFolder 'service'
            newFolder 'test'
            newFolder 'other', 'a'
            newFolder 'other', 'b'
            newFolder 'other', 'c'
        }
        settingsFile << '''
            // 模拟重复配置
            include 'test'
            iHubSettings {
                includeProjects 'rest', 'service' suffix '-suffix'
                includeProjects 'other' prefix 'prefix-' subproject
                includeProjects 'test' noPrefix
            }
        '''
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains '│ other                                       │ prefix-other                                       │'
        result.output.contains '│ other                                       │ prefix-other-a                                     │'
        result.output.contains '│ other                                       │ prefix-other-b                                     │'
        result.output.contains '│ other                                       │ prefix-other-c                                     │'
        result.output.contains '│ rest                                        │ rest-suffix                                        │'
        result.output.contains '│ service                                     │ service-suffix                                     │'
        !result.output.contains('│ test')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试配置三级子项目（仅含子项目）'() {
        when: '配置项目'
        testProjectDir.with {
            newFolder 'other', 'a'
            newFolder 'other', 'b'
            newFolder 'other', 'c'
        }
        settingsFile << '''
            iHubSettings {
                includeProjects 'other' prefix 'prefix-' suffix '-suffix' skippedDirs 'c' onlySubproject
            }
        '''
        def result = gradleBuilder.build()

        then: '检查结果'
        !result.output.contains('│ other                                     │ prefix-other                                         │')
        result.output.contains '│ other                                     │ prefix-a-suffix                                      │'
        result.output.contains '│ other                                     │ prefix-b-suffix                                      │'
        !result.output.contains('│ other                                     │ prefix-c-suffix                                      │')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试版本组件配置'() {
        when: '配置项目'
        testProjectDir.newFolder 'gradle'
        copy getClass().classLoader.getResourceAsStream('libs.versions.toml'), testProjectDir.newFile('gradle/libs.versions.toml'), null
        propertiesFile << 'iHubSettings.includeBom=demo-bom\n'
        propertiesFile << 'iHubSettings.includeDependencies=demo-dependencies\n'
        propertiesFile << 'iHubSettings.includeLibs=true\n'
        new File('build/resources/main/META-INF/ihub/plugin-ids') << '\npub.ihub.plugin'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试版本组件兼容模式配置'() {
        when: '配置项目'
        testProjectDir.newFolder 'gradle', 'libs', 'compatibility'
        copy getClass().classLoader.getResourceAsStream('libs.versions.toml'),
            testProjectDir.newFile('gradle/libs.versions.toml'), null
        copy getClass().classLoader.getResourceAsStream('libs.versions.toml'),
            testProjectDir.newFile('gradle/libs/compatibility/java11.versions.toml'), null
        propertiesFile << 'iHubSettings.includeBom=demo-bom\n'
        propertiesFile << 'iHubSettings.includeDependencies=demo-dependehncies\n'
        propertiesFile << 'iHubSettings.includeLibs=true\n'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试Bom组件版本配置'() {
        when: '配置项目'
        testProjectDir.newFolder 'gradle'
        testProjectDir.newFolder 'rest'
        testProjectDir.newFolder 'service'
        testProjectDir.newFolder 'other'
        copy getClass().classLoader.getResourceAsStream('libs.versions.toml'), testProjectDir.newFile('gradle/libs.versions.toml'), null
        propertiesFile << 'iHubSettings.includeBom=demo-bom\n'
        propertiesFile << 'iHubSettings.skippedDirs=samples=gradle\n'
        testProjectDir.newFile(DEFAULT_BUILD_FILE) << '''
        project(':service') {
            apply {
                plugin 'java'
                plugin 'maven-publish'
            }
        }
        project(':other') {
            apply {
                plugin 'maven-publish'
            }
        }
        project(':rest') {
            apply {
                plugin 'java'
            }
        }
        '''
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试Dependencies组件版本配置'() {
        when: '配置项目'
        testProjectDir.newFolder 'gradle'
        testProjectDir.newFolder 'rest'
        testProjectDir.newFolder 'service'
        testProjectDir.newFolder 'other'
        copy getClass().classLoader.getResourceAsStream('libs.versions.toml'), testProjectDir.newFile('gradle/libs.versions.toml'), null
        propertiesFile << 'iHubSettings.includeBom=demo-bom\n'
        propertiesFile << 'iHubSettings.includeDependencies=demo-dependencies\n'
        propertiesFile << 'iHubSettings.includeLibs=true\n'
        propertiesFile << 'iHubSettings.skippedDirs=samples=gradle\n'
        testProjectDir.newFile(DEFAULT_BUILD_FILE) << '''
        subprojects {
            if (project.pluginManager.hasPlugin("java-platform")) {
                return
            }
            apply {
                plugin 'java'
            }
        }
        '''
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
