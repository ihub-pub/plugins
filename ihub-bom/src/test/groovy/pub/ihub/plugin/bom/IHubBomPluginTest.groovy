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
package pub.ihub.plugin.bom

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

import static org.gradle.api.Project.DEFAULT_BUILD_FILE

/**
 * @author henry
 */
@Title('BOM插件DSL扩展测试套件')
@SuppressWarnings(['AbcMetric', 'MethodSize'])
class IHubBomPluginTest extends IHubSpecification {

    def '基础配置成功测试'() {
        setup: '初始化项目'
        copyProject 'bom.gradle'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.matches '[\\s\\S]+│ pub.ihub.lib + │ ihub-bom + │ 1.4.6 + │[\\s\\S]+'
        result.output.contains '│ pub.ihub.lib                    │ ihub-process                    │ 1.4.6                        │'
        result.output.contains '│ pub.ihub.lib                    │ ihub-core                       │ 1.4.6                        │'
        result.output.contains '│ pub.ihub.lib                                      │ 1.4.6                                        │'
        result.output.contains '│ org.slf4j                                      │ slf4j-api                                       │'
        result.output.contains '│ pub.ihub                                       │ all                                             │'
        result.output.contains '│ api                                        │ pub.ihub.lib:ihub-core                              │'
        result.output.contains '│ pub.ihub.lib:ihub-boot-cloud-spring-boot-starter         │ pub.ihub.lib:reactor-support          │'
        result.output.contains '│ ihub-boot-cloud-spring-boot-starter                      │ nacos-support                         │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'bom配置组件需要能力配置测试'() {
        setup: '初始化项目'
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-bom'
                id 'java'
            }
            iHubBom {
                groupVersions {
                    group 'pub.ihub.lib' version '1.4.6'
                }
                capabilities {
                    requireCapability 'pub.ihub.lib:ihub-boot-cloud-spring-boot-starter', 'pub.ihub.lib:reactor-support'
                    requireCapability 'ihub-boot-cloud-spring-boot-starter', 'nacos-support'
                }
            }
            repositories {
                mavenCentral()
            }
            dependencies {
                implementation 'pub.ihub.lib:ihub-boot-cloud-spring-boot-starter'
            }
        '''
        newFolder 'src', 'main', 'java'
        newFile 'src/main/java/Demo.java'

        when: '构建项目'
        def result = gradleBuilder.withArguments('build').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'bom不含bomVersions配置测试'() {
        setup: '初始化项目'
        copyProject 'bom.gradle'
        buildFile << 'iHubBom.bomVersions.clear()'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        !result.output.contains('ihub-bom')
        result.output.contains '│ pub.ihub.lib                    │ ihub-process                    │ 1.4.6                        │'
        result.output.contains '│ pub.ihub.lib                    │ ihub-core                       │ 1.4.6                        │'
        result.output.contains '│ pub.ihub.lib                                      │ 1.4.6                                        │'
        result.output.contains '│ org.slf4j                                      │ slf4j-api                                       │'
        result.output.contains '│ pub.ihub                                       │ all                                             │'
        result.output.contains '│ api                                        │ pub.ihub.lib:ihub-core                              │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'bom不含dependencyVersions配置测试'() {
        setup: '初始化项目'
        copyProject 'bom.gradle'
        buildFile << 'iHubBom.dependencyVersions.clear()'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.matches '[\\s\\S]+│ pub.ihub.lib + │ ihub-bom + │ 1.4.6 + │[\\s\\S]+'
        !result.output.contains('│ ihub-process')
        !result.output.contains('│ ihub-core')
        result.output.contains '│ pub.ihub.lib                                      │ 1.4.6                                        │'
        result.output.contains '│ org.slf4j                                      │ slf4j-api                                       │'
        result.output.contains '│ pub.ihub                                       │ all                                             │'
        result.output.contains '│ api                                        │ pub.ihub.lib:ihub-core                              │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '子项目配置成功测试'() {
        setup: '初始化项目'
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-bom'
            }
        '''

        when: '添加子项目'
        settingsFile << 'include \'a\', \'b\', \'c\', \'demo-bom\''
        newFolder 'a'
        newFolder 'b'
        newFolder 'c'
        newFolder 'demo-bom'
        buildFile << '''
            allprojects {
                iHubBom {
                    importBoms {
                        group 'org.apache.groovy' module 'groovy-bom' version '4.0.5'
                    }
                    dependencyVersions {
                        group 'org.apache.groovy' modules 'groovy-all' version '4.0.5'
                    }
                    capabilities {
                        requireCapability 'org.slf4j:slf4j-ext', 'org.javassist:javassist'
                    }
                }
            }
            iHubBom {
                excludeModules {
                    group 'pub.ihub.lib' modules 'core'
                }
                dependencies {
                    api ':a', ':b', ':c'
                    compileOnlyApi 'pub.ihub.lib:ihub-core'
                    testCompileOnly 'pub.ihub.lib:ihub-process'
                }
            }
            subprojects {
                apply {
                    plugin 'pub.ihub.plugin.ihub-bom'
                    plugin 'groovy'
                }
                iHubBom {
                    importBoms {
                        group 'org.apache.groovy' module 'groovy-bom' version '4.0.4'
                        group 'pub.ihub.lib' module 'ihub-bom' version '1.0.7'
                    }
                    dependencyVersions {
                        group 'org.apache.groovy' modules 'groovy-all' version '4.0.4'
                    }
                    groupVersions {
                        group 'pub.ihub.lib' version '1.0.7'
                    }
                    excludeModules {
                        group 'pub.ihub.lib'
                        group 'com.demo' modules 'core'
                    }
                    dependencies {
                        compileOnlyApi 'pub.ihub.lib:ihub-core'
                        runtimeOnly 'pub.ihub.lib:ihub-core'
                        implementation 'pub.ihub.lib:ihub-process'
                        annotationProcessor 'pub.ihub.lib:ihub-process'
                    }
                    capabilities {
                        requireCapability 'org.slf4j:slf4j-ext', 'other-support'
                    }
                }
            }
        '''
        newFile('a/' + DEFAULT_BUILD_FILE) << '''
            iHubBom {
                excludeModules {
                    group 'pub.ihub.lib' modules 'ihub-core'
                    group 'com.demo' modules 'core', 'common'
                }
                groupVersions {
                    group 'pub.ihub.lib' version '1.0.6'
                }
                dependencyVersions {
                    group 'org.apache.groovy' modules 'groovy-core' version '4.0.4'
                }
                dependencies {
                    compileOnlyApi 'pub.ihub.lib:ihub-process'
                    testRuntimeOnly 'pub.ihub.lib:ihub-core'
                    testImplementation 'pub.ihub.lib:ihub-core'
                }
            }
        '''
        def result = gradleBuilder.build()

        then: '检查结果'
        // Group Maven Bom Version (root project)
        result.output.matches '[\\s\\S]+│ org.apache.groovy + │ groovy-bom + │ 4.0.4 + │[\\s\\S]+'
        result.output.matches '[\\s\\S]+│ pub.ihub.lib + │ ihub-bom + │ 1.0.7 + │[\\s\\S]+'
        // B Group Maven Default Version
        result.output.contains '│ pub.ihub.lib                                      │ 1.0.7                                        │'
        // Config Default Dependencies (root project)
        result.output.contains '│ runtimeOnly                                 │ pub.ihub.lib:ihub-core                             │'
        result.output.contains '│ implementation                              │ pub.ihub.lib:ihub-process                          │'
        result.output.contains '│ annotationProcessor                         │ pub.ihub.lib:ihub-process                          │'
        result.output.contains '│ api                                         │ :a                                                 │'
        result.output.contains '│ api                                         │ :b                                                 │'
        result.output.contains '│ api                                         │ :c                                                 │'
        result.output.contains '│ compileOnlyApi                              │ pub.ihub.lib:ihub-core                             │'
        result.output.contains '│ testCompileOnly                             │ pub.ihub.lib:ihub-process                          │'
        // Config Default Dependencies (subprojects)
        result.output.contains '│ compileOnlyApi                              │ pub.ihub.lib:ihub-process                          │'
        result.output.contains '│ testRuntimeOnly                             │ pub.ihub.lib:ihub-core                             │'
        result.output.contains '│ testImplementation                          │ pub.ihub.lib:ihub-core                             │'
        // Exclude Group Modules (root project)
        result.output.contains '│ pub.ihub.lib                                      │ core                                         │'
        // B Exclude Group Modules
        result.output.contains '│ pub.ihub.lib                                      │ all                                          │'
        result.output.contains '│ com.demo                                          │ core                                         │'
        // A Exclude Group Modules
        result.output.contains '│ pub.ihub.lib                                     │ all                                           │'
        result.output.contains '│ pub.ihub.lib                                     │ ihub-core                                     │'
        result.output.contains '│ com.demo                                         │ core                                          │'
        result.output.contains '│ com.demo                                         │ common                                        │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '配置失败测试-排除组件依赖配置版本号'() {
        when: '排除组件依赖配置版本号'
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-bom'
            }
            iHubBom {
                excludeModules {
                    group 'pub.ihub' version '1.0.0'
                }
            }
        '''
        def result = gradleBuilder.buildAndFail()

        then: '检查结果'
        result.output.contains 'Does not support \'version\' method!'
    }

    def '配置失败测试-配置组件依赖类型为空'() {
        when: '配置组件依赖类型为空'
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-bom'
            }
            iHubBom {
                dependencies {
                    compile null
                }
            }
        '''
        def result = gradleBuilder.buildAndFail()

        then: '检查结果'
        result.output.contains 'dependencies type not null!'
    }

    def '配置失败测试-配置组件依赖为空'() {
        when: '配置组件依赖为空'
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-bom'
            }
            iHubBom {
                dependencies {
                    compile 'type'
                }
            }
        '''
        def result = gradleBuilder.buildAndFail()
        then: '检查结果'
        result.output.contains 'type dependencies not empty!'
    }

    def '模拟Java平台组件应用ihub-bom测试'() {
        when: '配置组件依赖为空'
        buildFile << '''
            plugins {
                id 'java-platform'
                id 'pub.ihub.plugin.ihub-bom'
            }
        '''
        def result = gradleBuilder.build()
        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '模拟版本目录组件应用ihub-bom测试'() {
        when: '配置组件依赖为空'
        buildFile << '''
            plugins {
                id 'version-catalog'
                id 'pub.ihub.plugin.ihub-bom'
            }
        '''
        def result = gradleBuilder.build()
        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'ihub-dependencies配置测试'() {
        when: '配置组件依赖为空'
        copyProject 'bom.gradle'
        propertiesFile << 'iHubSettings.includeDependencies=ihub-dependencies'
        def result = gradleBuilder.build()
        then: '检查结果'
        !result.output.contains('ihub-dependencies')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'iHubLibsLocalVersion配置测试'() {
        when: '配置组件依赖为空'
        copyProject 'bom.gradle'
        propertiesFile << 'iHub.iHubLibsLocalVersion=test-SNAPSHOT'
        def result = gradleBuilder.build()
        then: '检查结果'
        result.output.contains('test-SNAPSHOT')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '排除模块使用all关键字测试'() {
        setup: '初始化项目'
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-bom'
                id 'java'
            }
            iHubBom {
                excludeModules {
                    group 'com.example' modules 'all'
                }
            }
            repositories {
                mavenCentral()
            }
        '''
        newFolder 'src', 'main', 'java'
        newFile 'src/main/java/Demo.java'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'com.example'
        result.output.contains 'all'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
