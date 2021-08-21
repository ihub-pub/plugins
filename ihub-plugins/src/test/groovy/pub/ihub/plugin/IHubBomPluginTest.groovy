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



/**
 * @author henry
 */
@Slf4j
@Title('BOM插件DSL扩展测试套件')
class IHubBomPluginTest extends IHubSpecification {

    def '基础配置成功测试'() {
        setup: '初始化项目'
        copyProject 'bom.gradle'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains '│ cn.hutool                           │ hutool-bom                               │ 5.6.5           │'
        result.output.contains '│ cn.hutool                           │ core                                     │ 5.6.5           │'
        result.output.contains '│ cn.hutool                           │ aop                                      │ 5.6.5           │'
        result.output.contains '│ cn.hutool                                                       │ 5.6.5                          │'
        result.output.contains '│ org.slf4j                                │ slf4j-api                                             │'
        result.output.contains '│ pub.ihub                                 │ all                                                   │'
        result.output.contains '│ api                            │ cn.hutool:hutool-aop                                            │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '子项目配置成功测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '添加子项目'
        testProjectDir.newFile('settings.gradle') << 'include \'a\', \'b\', \'c\''
        testProjectDir.newFolder 'a'
        testProjectDir.newFolder 'b'
        testProjectDir.newFolder 'c'
        buildFile << '''
            iHubBom {
                excludeModules {
                    group 'cn.hutool' modules 'core'
                }
                dependencies {
                    api ':a', ':b', ':c'
                    compileOnlyApi 'cn.hutool:hutool-core'
                    testCompileOnly 'cn.hutool:hutool-log'
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
                        group 'cn.hutool' module 'hutool-bom' version '5.6.6'
                    }
                }
                github {
                    slug = 'freefair/gradle-plugins'
                }
            }
        '''
        testProjectDir.newFile('a/' + DEFAULT_BUILD_FILE) << '''
            iHubBom {
                excludeModules {
                    group 'cn.hutool' modules 'log'
                }
            }
        '''
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains '│ org.codehaus.groovy                 │ groovy-bom                               │ 2.5.14          │'
        result.output.contains '│ cn.hutool                           │ hutool-bom                               │ 5.6.6           │'
        result.output.contains '│ cn.hutool                                │ core                                                  │'
        result.output.contains '│ cn.hutool                                │ log                                                   │'
        result.output.contains '│ api                            │ :a                                                              │'
        result.output.contains '│ api                            │ :b                                                              │'
        result.output.contains '│ api                            │ :c                                                              │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-xml                                  │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-dateutil                             │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-templates                            │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-nio                                  │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy                                      │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-json                                 │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-groovydoc                            │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-sql                                  │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-datetime                             │'
        result.output.contains '│ annotationProcessor            │ org.springframework.boot:spring-boot-configuration-processor    │'
        result.output.contains '│ compileOnlyApi                 │ cn.hutool:hutool-core                                           │'
        result.output.contains '│ testCompileOnly                │ cn.hutool:hutool-log                                            │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '配置失败测试-排除组件依赖配置版本号'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '排除组件依赖配置版本号'
        buildFile << '''
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
