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
import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.Title

import static org.gradle.api.Project.GRADLE_PROPERTIES
import static org.gradle.internal.impldep.org.apache.ivy.util.FileUtil.copy



/**
 * @author henry
 */
@Slf4j
@Title('IHubSettingsPlugin测试套件')
@SuppressWarnings('PrivateFieldCouldBeFinal')
class IHubSettingsPluginTest extends Specification {

    @Rule
    private TemporaryFolder testProjectDir = new TemporaryFolder()
    private File settingsFile
    private File propertiesFile
    private GradleRunner gradleBuilder

    /**
     * 初始化项目配置
     */
    void setup() {
        testProjectDir.create()
        settingsFile = testProjectDir.newFile 'settings.gradle'
        propertiesFile = testProjectDir.newFile GRADLE_PROPERTIES
        gradleBuilder = GradleRunner.create().withProjectDir(testProjectDir.root).withPluginClasspath()
        copy getClass().classLoader.getResourceAsStream('testkit-gradle.properties'), propertiesFile, null
        settingsFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-settings'
            }
        '''
    }

    /**
     * 清理测试项目文件
     */
    void cleanup() {
        testProjectDir.delete()
    }

    def '测试插件仓库配置'() {
        when: '基础配置'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'AliYunGradlePlugin'
        result.output.contains 'AliYunSpringPlugin'
        result.output.contains 'SpringRelease'

        when: '本地插件配置'
        testProjectDir.newFolder 'gradle', 'plugins'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'flatDir'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试插件版本配置'() {
        when: '默认配置'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'io.spring.dependency-management'
        result.output.contains 'org.springframework.boot'
        result.output.contains 'org.springframework.experimental.aot'
        result.output.contains 'com.gradle.plugin-publish'
        result.output.contains 'com.github.ben-manes.versions'

        when: '自定义默认配置'
        settingsFile << '''
            iHubSettings {
                pluginVersions {
                    id 'pub.ihub.plugin' version '1.0.0'
                }
            }
        '''
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'pub.ihub.plugin'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试扩展属性配置子项目'() {
        when: '配置项目'
        propertiesFile << 'projectName=demo\n'
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
        result.output.contains 'demo-rest'
        result.output.contains 'demo-service'
        result.output.contains 'demo-other'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试项目属性配置子项目'() {
        when: '配置项目'
        propertiesFile << 'projectName=demo\n'
        propertiesFile << 'includeDirs=rest,service\n'
        testProjectDir.newFolder 'rest'
        testProjectDir.newFolder 'service'
        testProjectDir.newFolder 'other'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'demo-rest'
        result.output.contains 'demo-service'
        !result.output.contains('demo-other')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试跳过目录属性配置子项目'() {
        when: '配置项目'
        propertiesFile << 'projectName=demo\n'
        propertiesFile << 'skippedDirs=other\n'
        testProjectDir.newFolder '.git'
        testProjectDir.newFolder 'src'
        testProjectDir.newFolder 'rest'
        testProjectDir.newFolder 'service'
        testProjectDir.newFolder 'other'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'demo-rest'
        result.output.contains 'demo-service'
        !result.output.contains('demo-other')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '测试配置三级子项目'() {
        when: '配置项目'
        propertiesFile << 'projectName=demo\n'
        testProjectDir.newFolder 'rest'
        testProjectDir.newFolder 'service'
        testProjectDir.newFolder 'other', 'a'
        testProjectDir.newFolder 'other', 'b'
        testProjectDir.newFolder 'other', 'c'
        settingsFile << '''
            iHubSettings {
                includeProjects 'rest', 'service' suffix '-suffix'
                includeProjects 'other' prefix 'prefix-' subproject '-suffix'
            }
        '''
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'demo-rest-suffix'
        result.output.contains 'demo-service-suffix'
        result.output.contains 'prefix-other'
        result.output.contains 'prefix-other-a-suffix'
        result.output.contains 'prefix-other-b-suffix'
        result.output.contains 'prefix-other-c-suffix'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
