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



/**
 * @author henry
 */
@Slf4j
@Title('基础插件扩展测试套件')
class IHubBasicPluginTest extends IHubSpecification {

    def '基础构建测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def '项目组件仓库配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'

        when: '构建项目'
        propertiesFile << '''
iHub.mavenLocalEnabled=true
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

    def 'Groovy插件配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << """
            apply {
                plugin 'pub.ihub.plugin.ihub-groovy'
            }
        """

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-xml                                  │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-dateutil                             │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-templates                            │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-nio                                  │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy                                      │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-json                                 │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-groovydoc                            │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-sql                                  │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-datetime                             │'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-datetime                             │'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '修改版本以及依赖组件模块'
        propertiesFile << 'iHub.compileGroovyAllModules=true\n'
        propertiesFile << 'groovy.version=2.5.14\n'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains '│ implementation                 │ org.codehaus.groovy:groovy-all                                  │'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Java插件配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << """
            apply {
                plugin 'pub.ihub.plugin.ihub-java'
            }
        """

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains '│ runtimeOnly                    │ javax.xml.bind:jaxb-api                                         │'
        result.output.contains '│ runtimeOnly                    │ org.glassfish.jaxb:jaxb-runtime                                 │'
        result.output.contains '│ com.sun.xml.bind                         │ jaxb-core                                             │'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '修改版本以及依赖组件模块'
        propertiesFile << 'iHub.javaCompatibility=8\n'
        propertiesFile << 'iHub.javaJaxbRuntime=false\n'
        result = gradleBuilder.build()

        then: '检查结果'
        !result.output.contains('│ runtimeOnly                    │ javax.xml.bind:jaxb-api                                         │')
        !result.output.contains('│ runtimeOnly                    │ org.glassfish.jaxb:jaxb-runtime                                 │')
        !result.output.contains('│ com.sun.xml.bind                         │ jaxb-core                                             │')
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Publish插件配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << """
            apply {
                plugin 'pub.ihub.plugin.ihub-publish'
            }
            github {
                slug = 'freefair/gradle-plugins'
            }
        """

        when: '构建项目'
        propertiesFile << '''
version=1.0.0
'''
        def result = gradleBuilder.withArguments('-DiHub.repoUsername=username', '-DiHub.repoPassword=password').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        propertiesFile << '''
iHubPublish.publishNeedSign=true
iHubPublish.signingKeyId=id
iHubPublish.signingSecretKey=secret
iHubPublish.signingPassword=password
iHubPublish.publishDocs=true
'''
        result = gradleBuilder.withArguments('-DiHub.repoUsername=username', '-DiHub.repoPassword=password').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Groovy Publish配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << """
            apply {
                plugin 'pub.ihub.plugin.ihub-groovy'
                plugin 'pub.ihub.plugin.ihub-publish'
            }
            github {
                slug = 'freefair/gradle-plugins'
            }
        """

        when: '构建项目'
        propertiesFile << '''
version=1.0.0
iHubPublish.publishNeedSign=true
iHubPublish.signingKeyId=id
iHubPublish.signingSecretKey=secret
iHubPublish.signingPassword=password
iHubPublish.publishDocs=true
'''
        def result = gradleBuilder.withArguments('-DiHub.repoUsername=username', '-DiHub.repoPassword=password').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Native插件配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << """
            apply {
                plugin 'pub.ihub.plugin.ihub-native'
            }
        """

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        buildFile << """
            iHubNative {
                bpJvmVersion = '11'
            }
        """
        testProjectDir.newFile('.java-local.properties') << 'spring.profiles.active=dev'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'GitHooks插件配置测试'() {
        setup: '初始化项目'
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-git-hooks'
            }

            iHubGitHooks {
                hooks = ['pre-commit': 'build']
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
