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
import spock.lang.Title

import static org.gradle.api.Project.DEFAULT_BUILD_FILE
import static org.gradle.api.initialization.Settings.DEFAULT_SETTINGS_FILE



/**
 * @author henry
 */
@Slf4j
@Title('BOM插件DSL扩展测试套件')
@SuppressWarnings(['AbcMetric', 'MethodSize'])
class IHubBomPluginTest extends IHubSpecification {

    def '基础配置成功测试'() {
        setup: '初始化项目'
        copyProject 'bom.gradle'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains '│ pub.ihub.lib                     │ ihub-bom                      │ 1.0.8                         │'
        result.output.contains '│ pub.ihub.lib                    │ ihub-process                    │ 1.0.8                        │'
        result.output.contains '│ pub.ihub.lib                    │ ihub-core                       │ 1.0.8                        │'
        result.output.contains '│ pub.ihub.lib                                      │ 1.0.8                                        │'
        result.output.contains '│ org.slf4j                                      │ slf4j-api                                       │'
        result.output.contains '│ pub.ihub                                       │ all                                             │'
        result.output.contains '│ api                                        │ pub.ihub.lib:ihub-core                              │'
        result.output.contains '│ org.slf4j:slf4j-ext                                        │ org.javassist:javassist             │'
        result.output.contains '│ org.springframework.cloud:spring-cloud-starter-openfeign   │ spring-cloud-starter-loadbalancer   │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'bom配置组件需要能力配置测试'() {
        setup: '初始化项目'
        copyProject 'bom.gradle'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-java'
            }
            dependencies {
                api 'org.slf4j:slf4j-ext'
                api 'org.springframework.cloud:spring-cloud-starter-openfeign'
            }
        '''
        testProjectDir.newFolder 'src', 'main', 'java'
        testProjectDir.newFile 'src/main/java/Demo.java'

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
        result.output.contains '│ pub.ihub.lib                    │ ihub-process                    │ 1.0.8                        │'
        result.output.contains '│ pub.ihub.lib                    │ ihub-core                       │ 1.0.8                        │'
        result.output.contains '│ pub.ihub.lib                                      │ 1.0.8                                        │'
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
        result.output.contains '│ pub.ihub.lib                     │ ihub-bom                      │ 1.0.8                         │'
        !result.output.contains('│ ihub-process')
        !result.output.contains('│ ihub-core')
        result.output.contains '│ pub.ihub.lib                                      │ 1.0.8                                        │'
        result.output.contains '│ org.slf4j                                      │ slf4j-api                                       │'
        result.output.contains '│ pub.ihub                                       │ all                                             │'
        result.output.contains '│ api                                        │ pub.ihub.lib:ihub-core                              │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '子项目配置成功测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '添加子项目'
        testProjectDir.newFile(DEFAULT_SETTINGS_FILE) << 'include \'a\', \'b\', \'c\''
        testProjectDir.newFolder 'a'
        testProjectDir.newFolder 'b'
        testProjectDir.newFolder 'c'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-bom'
            }
            allprojects {
                iHubBom {
                    importBoms {
                        group 'org.codehaus.groovy' module 'groovy-bom' version '3.0.8'
                    }
                    dependencyVersions {
                        group 'org.codehaus.groovy' modules 'groovy-all' version '3.0.8'
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
                    plugin 'pub.ihub.plugin.ihub-groovy'
                    plugin 'pub.ihub.plugin.ihub-verification'
                    plugin 'pub.ihub.plugin.ihub-publish'
                }
                iHubBom {
                    importBoms {
                        group 'org.codehaus.groovy' module 'groovy-bom' version '2.5.14'
                        group 'pub.ihub.lib' module 'ihub-bom' version '1.0.7'
                    }
                    dependencyVersions {
                        group 'org.codehaus.groovy' modules 'groovy-all' version '2.5.14'
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
                    }
                }
            }
        '''
        testProjectDir.newFile('a/' + DEFAULT_BUILD_FILE) << '''
            iHubBom {
                excludeModules {
                    group 'pub.ihub.lib' modules 'ihub-core'
                    group 'com.demo' modules 'core', 'common'
                }
                groupVersions {
                    group 'pub.ihub.lib' version '1.0.6'
                }
                dependencyVersions {
                    group 'org.codehaus.groovy' modules 'groovy-core' version '2.5.14'
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
        result.output.contains '│ org.codehaus.groovy                   │ groovy-bom                   │ 2.5.14                    │'
        result.output.contains '│ pub.ihub.lib                          │ ihub-bom                     │ 1.0.7                     │'
        result.output.contains '│ pub.ihub.lib                                      │ 1.0.7                                        │'
        result.output.contains '│ api                        │ :a                                                                  │'
        result.output.contains '│ api                        │ :b                                                                  │'
        result.output.contains '│ api                        │ :c                                                                  │'
        result.output.contains '│ implementation             │ org.codehaus.groovy:groovy-xml                                      │'
        result.output.contains '│ implementation             │ org.codehaus.groovy:groovy-dateutil                                 │'
        result.output.contains '│ implementation             │ org.codehaus.groovy:groovy-templates                                │'
        result.output.contains '│ implementation             │ org.codehaus.groovy:groovy-nio                                      │'
        result.output.contains '│ implementation             │ org.codehaus.groovy:groovy                                          │'
        result.output.contains '│ implementation             │ org.codehaus.groovy:groovy-json                                     │'
        result.output.contains '│ implementation             │ org.codehaus.groovy:groovy-groovydoc                                │'
        result.output.contains '│ implementation             │ org.codehaus.groovy:groovy-sql                                      │'
        result.output.contains '│ implementation             │ org.codehaus.groovy:groovy-datetime                                 │'
        result.output.contains '│ annotationProcessor        │ org.springframework.boot:spring-boot-configuration-processor        │'
        result.output.contains '│ compileOnlyApi             │ pub.ihub.lib:ihub-core                                              │'
        result.output.contains '│ testCompileOnly            │ pub.ihub.lib:ihub-process                                           │'
        result.output.contains '│ compileOnlyApi                              │ pub.ihub.lib:ihub-process                          │'
        result.output.contains '│ testRuntimeOnly                             │ pub.ihub.lib:ihub-core                             │'
        result.output.contains '│ testImplementation                          │ pub.ihub.lib:ihub-core                             │'
        result.output.contains '│ pub.ihub.lib                                        │ core                                       │'
        result.output.contains '│ pub.ihub.lib                                     │ all                                           │'
        result.output.contains '│ pub.ihub.lib                                     │ ihub-core                                     │'
        result.output.contains '│ pub.ihub.lib                                      │ all                                          │'
        result.output.contains '│ com.demo                                         │ core                                          │'
        result.output.contains '│ com.demo                                         │ common                                        │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '配置失败测试-排除组件依赖配置版本号'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '排除组件依赖配置版本号'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-bom'
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
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '配置组件依赖类型为空'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-bom'
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
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '配置组件依赖为空'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-bom'
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

}
