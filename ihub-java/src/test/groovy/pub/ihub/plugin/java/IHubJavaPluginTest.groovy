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
package pub.ihub.plugin.java

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

import static org.gradle.api.initialization.Settings.DEFAULT_SETTINGS_FILE
import static pub.ihub.plugin.java.IHubJavaPlugin.DEFAULT_DEPENDENCIES_CONFIG

/**
 * @author henry
 */
@Title('IHubJavaPlugin测试套件')
class IHubJavaPluginTest extends IHubSpecification {

    def 'Java插件默认配置测试'() {
        setup: '初始化项目（此处借用子项目测试，主项目不触发beforeEvaluate）'
        copyProject 'basic.gradle'
        testProjectDir.newFile(DEFAULT_SETTINGS_FILE) << 'include \'a\', \'b\', \'c\''
        testProjectDir.newFolder 'a'
        testProjectDir.newFolder 'b'
        testProjectDir.newFolder 'c'
        buildFile << '''
            allprojects {
                apply {
                    plugin 'pub.ihub.plugin.ihub-java'
                }
            }
        '''
        propertiesFile << "iHubJava.defaultDependencies=${DEFAULT_DEPENDENCIES_CONFIG.keySet().join(',')}\n"

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains '│ com.sun.xml.bind                                    │ jaxb-core                                  │'
        result.output.contains '│ commons-logging                                     │ commons-logging                            │'
        result.output.contains '│ log4j                                               │ log4j                                      │'
        result.output.contains '│ org.apache.logging.log4j                            │ log4j-core                                 │'
        result.output.contains '│ org.slf4j                                           │ slf4j-log4j12                              │'
        result.output.contains '│ org.slf4j                                           │ slf4j-jcl                                  │'
        result.output.contains '│ compileOnly                      │ io.swagger.core.v3:swagger-annotations                        │'
        result.output.contains '│ runtimeOnly                      │ org.slf4j:jul-to-slf4j                                        │'
        result.output.contains '│ runtimeOnly                      │ javax.xml.bind:jaxb-api                                       │'
        result.output.contains '│ runtimeOnly                      │ org.slf4j:log4j-over-slf4j                                    │'
        result.output.contains '│ runtimeOnly                      │ org.slf4j:jcl-over-slf4j                                      │'
        result.output.contains '│ runtimeOnly                      │ org.glassfish.jaxb:jaxb-runtime                               │'
        result.output.contains '│ implementation                   │ org.slf4j:slf4j-api                                           │'
        result.output.contains '│ implementation                   │ org.mapstruct:mapstruct                                       │'
        result.output.contains '│ annotationProcessor              │ org.mapstruct:mapstruct-processor                             │'
        result.output.contains '│ implementation                   │ org.jmolecules:jmolecules-ddd                                 │'
        result.output.contains '│ implementation                   │ org.jmolecules:jmolecules-events                              │'
        result.output.contains '│ implementation                   │ org.jmolecules:jmolecules-onion-architecture                  │'
        result.output.contains '│ implementation                   │ org.jmolecules.integrations:jmolecules-spring                 │'
        result.output.contains '│ implementation                   │ org.jmolecules.integrations:jmolecules-jpa                    │'
        result.output.contains '│ implementation                   │ org.jmolecules.integrations:jmolecules-jackson                │'
        result.output.contains '│ annotationProcessor              │ pub.ihub.lib:ihub-process-doc                                 │'
        result.output.contains '│ testImplementation               │ org.jmolecules.integrations:jmolecules-archunit               │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Java插件配置测试'() {
        setup: '初始化项目（此处借用子项目测试，主项目不触发beforeEvaluate）'
        copyProject 'basic.gradle'
        testProjectDir.newFile(DEFAULT_SETTINGS_FILE) << 'include \'a\', \'b\', \'c\''
        testProjectDir.newFolder 'a'
        testProjectDir.newFolder 'b'
        testProjectDir.newFolder 'c'
        buildFile << '''
            allprojects {
                apply {
                    plugin 'pub.ihub.plugin.ihub-java'
                }
            }
        '''
        propertiesFile << 'iHubJava.compatibility=8\n'
        propertiesFile << 'iHubJava.defaultDependencies=false\n'
        propertiesFile << 'iHubJava.applyOpenapiPlugin=true\n'
        propertiesFile << 'iHubJava.compilerArgs=-proc:none -nowarn\n'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        !result.output.contains('jaxb-core')
        !result.output.contains('commons-logging')
        !result.output.contains('log4j')
        !result.output.contains('log4j-core')
        !result.output.contains('slf4j-log4j12')
        !result.output.contains('slf4j-jcl')
        !result.output.contains('org.slf4j:jul-to-slf4j')
        !result.output.contains('javax.xml.bind:jaxb-api')
        !result.output.contains('org.slf4j:log4j-over-slf4j')
        !result.output.contains('org.slf4j:jcl-over-slf4j')
        !result.output.contains('org.glassfish.jaxb:jaxb-runtime')
        !result.output.contains('org.slf4j:slf4j-api')
        !result.output.contains('org.mapstruct:mapstruct')
        !result.output.contains('org.mapstruct:mapstruct-processor')
        !result.output.contains('org.jmolecules:jmolecules-ddd')
        !result.output.contains('org.jmolecules:jmolecules-events')
        !result.output.contains('org.jmolecules:jmolecules-onion-architecture')
        !result.output.contains('org.jmolecules.integrations:jmolecules-spring')
        !result.output.contains('org.jmolecules.integrations:jmolecules-jpa')
        !result.output.contains('org.jmolecules.integrations:jmolecules-jackson')
        !result.output.contains('org.jmolecules.integrations:jmolecules-archunit')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Java插件可选功能配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-java'
            }
            iHubJava {
                registerFeature 'servlet', 'cloud-support', 'servlet-support'
                registerFeature 'reactor', 'cloud-support', 'reactor-support'
            }
            dependencies {
                servletApi 'org.springframework.boot:spring-boot-starter-web'
                reactorApi 'org.springframework.boot:spring-boot-starter-webflux'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '项目包含Groovy插件时移除annotationProcessor依赖测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        testProjectDir.newFile(DEFAULT_SETTINGS_FILE) << 'include \'a\', \'b\', \'c\''
        testProjectDir.newFolder 'a'
        testProjectDir.newFolder 'b'
        testProjectDir.newFolder 'c'
        buildFile << '''
            allprojects {
                apply {
                    plugin 'groovy'
                    plugin 'pub.ihub.plugin.ihub-java'
                }
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '禁用增量编译'
        result = gradleBuilder.withArguments('-DiHubJava.gradleCompilationIncremental=false').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '模拟lombok配置文件已存在测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-java'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '再次构建模拟已存在'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
