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
package pub.ihub.plugin.meta

import groovy.json.JsonSlurper
import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * IHub AI Metadata 插件测试套件
 * @author henry
 */
@Title('IHub Meta 插件测试套件')
class IHubMetaPluginTest extends IHubSpecification {

    @Override
    def setup() {
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-meta'
            }
        '''
    }

    def '默认配置测试：生成 project-meta.json 并包含基本字段'() {
        when: '执行 iHubMeta 任务'
        def result = gradleBuilder.withArguments('iHubMeta').build()

        then: '构建成功，JSON 文件已生成'
        result.output.contains 'BUILD SUCCESSFUL'
        def jsonFile = new File(testProjectDir, 'build/ihub/project-meta.json')
        jsonFile.exists()

        and: 'JSON 包含项目元数据'
        def json = new JsonSlurper().parse(jsonFile)
        json.project
        json.project.name
        json.gradle
        json.gradle.version
        json.gradle.plugins.find { it.id == 'pub.ihub.plugin.ihub-meta' }
    }

    def '禁用测试：enabled=false 时不生成文件'() {
        given: '配置禁用元数据生成'
        buildFile << '''
            iHubMeta {
                enabled = false
            }
        '''

        when: '执行 iHubMeta 任务'
        def result = gradleBuilder.withArguments('iHubMeta').build()

        then: '构建成功，但不生成 JSON 文件'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'metadata generation is disabled'
        !new File(testProjectDir, 'build/ihub/project-meta.json').exists()
    }

    def '排除依赖测试：includeDependencies=false 时不包含 dependencies 字段'() {
        given: '配置不包含依赖信息'
        buildFile << '''
            iHubMeta {
                includeDependencies = false
            }
        '''

        when: '执行 iHubMeta 任务'
        gradleBuilder.withArguments('iHubMeta').build()

        then: 'JSON 不包含 dependencies 字段'
        def jsonFile = new File(testProjectDir, 'build/ihub/project-meta.json')
        def json = new JsonSlurper().parse(jsonFile)
        !json.containsKey('dependencies')
    }

    def '排除 SourceSet 测试：includeSourceSets=false 时不包含 sourceSets'() {
        given: '配置不包含 sourceSets'
        buildFile << '''
            iHubMeta {
                includeSourceSets = false
            }
        '''

        when: '执行 iHubMeta 任务'
        gradleBuilder.withArguments('iHubMeta').build()

        then: 'JSON 不包含 sourceSets 字段'
        def jsonFile = new File(testProjectDir, 'build/ihub/project-meta.json')
        def json = new JsonSlurper().parse(jsonFile)
        !json.containsKey('sourceSets')
    }

    def '自定义输出路径测试'() {
        given: '自定义输出文件路径'
        buildFile << '''
            iHubMeta {
                outputFile = layout.buildDirectory.file('custom/ai-meta.json')
            }
        '''

        when: '执行 iHubMeta 任务'
        gradleBuilder.withArguments('iHubMeta').build()

        then: 'JSON 文件写入自定义路径'
        def jsonFile = new File(testProjectDir, 'build/custom/ai-meta.json')
        jsonFile.exists()
        def json = new JsonSlurper().parse(jsonFile)
        json.project
    }

    def '插件自包含测试：iHubMeta 出现在 applied plugins 列表中'() {
        when: '执行 iHubMeta 任务后检查 JSON'
        gradleBuilder.withArguments('iHubMeta').build()
        def jsonFile = new File(testProjectDir, 'build/ihub/project-meta.json')
        def json = new JsonSlurper().parse(jsonFile)

        then: 'iHubMeta 插件出现在 applied plugins 列表中'
        json.gradle.plugins.find { it.id == 'pub.ihub.plugin.ihub-meta' }
    }

    def '包含 Java SourceSet 信息测试'() {
        given: '应用 Java 插件并创建源码目录'
        buildFile << '''
            plugins {
                id 'java'
            }
        '''
        newFolder('src/main/java')
        newFolder('src/test/java')

        when: '执行 iHubMeta 任务'
        gradleBuilder.withArguments('iHubMeta').build()

        then: 'JSON 包含 sourceSets 信息'
        def jsonFile = new File(testProjectDir, 'build/ihub/project-meta.json')
        def json = new JsonSlurper().parse(jsonFile)
        json.sourceSets
        json.sourceSets.main
    }

    def '包含依赖信息测试'() {
        given: '应用 Java 插件并添加项目依赖'
        buildFile << '''
            plugins {
                id 'java'
            }
            repositories {
                mavenCentral()
            }
            dependencies {
                implementation 'com.google.guava:guava:33.0.0-jre'
            }
        '''

        when: '执行 iHubMeta 任务'
        gradleBuilder.withArguments('iHubMeta').build()

        then: 'JSON 包含依赖信息'
        def jsonFile = new File(testProjectDir, 'build/ihub/project-meta.json')
        def json = new JsonSlurper().parse(jsonFile)
        json.dependencies
    }

    def '多项目测试：modules 字段包含子项目信息'() {
        given: '创建多项目结构'
        settingsFile << '''
            include 'sub1'
            include 'sub2'
        '''
        newFolder('sub1')
        newFolder('sub2')
        newFile('sub1/build.gradle')

        when: '执行 iHubMeta 任务'
        gradleBuilder.withArguments('iHubMeta').build()

        then: 'JSON 包含子项目信息'
        def jsonFile = new File(testProjectDir, 'build/ihub/project-meta.json')
        def json = new JsonSlurper().parse(jsonFile)
        json.modules.size() == 2
        json.modules.find { it.name == 'sub1' && it.path == ':sub1' }
        json.modules.find { it.name == 'sub2' && it.path == ':sub2' }
    }

}
