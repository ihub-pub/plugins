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
@SuppressWarnings('JUnitPublicNonTestMethod')
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
mavenLocalEnabled=true
release.repo.url=http://ihub.pub/nexus/content/repositories/releases
snapshot_repo_url=http://ihub.pub/nexus/content/repositories/snapshots
customize-repo-url=http://ihub.pub/nexus/content/repositories
repoAllowInsecureProtocol=true
repoIncludeGroupRegex=pub\\.ihub\\..*
'''
        testProjectDir.newFolder 'libs'
        def result = gradleBuilder.withArguments('-DrepoUsername=username', '-DrepoPassword=password').build()

        then: '检查结果'
        result.output.contains('flatDir')
        result.output.contains('MavenLocal')
        result.output.contains('ReleaseRepo')
        result.output.contains('SnapshotRepo')
        result.output.contains('CustomizeRepo')
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        propertiesFile << 'repoIncludeGroup=pub.ihub.demo'
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
        propertiesFile << 'groovyAllModules=true\n'
        propertiesFile << 'org.codehaus.groovy.version=2.5.14\n'
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
        result.output.contains '│ compileOnly                    │ org.projectlombok:lombok                                        │'
        result.output.contains '│ annotationProcessor            │ org.projectlombok:lombok                                        │'
        result.output.contains '│ runtimeOnly                    │ javax.xml.bind:jaxb-api                                         │'
        result.output.contains '│ runtimeOnly                    │ org.glassfish.jaxb:jaxb-runtime                                 │'
        result.output.contains '│ com.sun.xml.bind                         │ jaxb-core                                             │'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '修改版本以及依赖组件模块'
        propertiesFile << 'javaCompatibility=8\n'
        propertiesFile << 'javaJaxbRuntime=false\n'
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
        """

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        buildFile << """
            iHubPublish {
                pomName = 'demo'
            }
        """
        propertiesFile << '''
version=1.0.0
'''
        result = gradleBuilder.withArguments('-DrepoUsername=username', '-DrepoPassword=password').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        propertiesFile << '''
publishNeedSign=true
signingKeyId=id
signingSecretKey=secret
signingPassword=password
publishDocs=true
'''
        result = gradleBuilder.withArguments('-DrepoUsername=username', '-DrepoPassword=password').build()

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
        """

        when: '构建项目'
        propertiesFile << '''
version=1.0.0
publishNeedSign=true
signingKeyId=id
signingSecretKey=secret
signingPassword=password
publishDocs=true
'''
        def result = gradleBuilder.withArguments('-DrepoUsername=username', '-DrepoPassword=password').build()

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
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
