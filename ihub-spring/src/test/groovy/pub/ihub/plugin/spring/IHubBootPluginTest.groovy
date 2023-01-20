/*
 * Copyright (c) 2022 Henry 李恒 (henry.box@outlook.com).
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
package pub.ihub.plugin.spring

import groovy.util.logging.Slf4j
import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Ignore
import spock.lang.Title

import static org.gradle.api.initialization.Settings.DEFAULT_SETTINGS_FILE
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS



/**
 * @author henry
 */
@Slf4j
@Title('IHubBootPlugin测试套件')
class IHubBootPluginTest extends IHubSpecification {

    def 'Boot插件配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-boot'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        testProjectDir.newFile('.java-local.properties') << 'spring.profiles.active=dev'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Native插件配置测试'() {
        setup: '初始化项目'
        copyProject 'basic.gradle'
        buildFile << '''
            apply {
                plugin 'pub.ihub.plugin.ihub-native'
            }
        '''

        when: '构建项目'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '构建项目'
        buildFile << '''
            iHubBoot {
                bpJvmVersion = '11'
            }
            iHubNative {
                bpNativeImage = true
            }
        '''
        testProjectDir.newFile('.java-local.properties') << 'spring.profiles.active=dev'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    /**
     * TODO 用例拆分
     */
    @Ignore
    def '多项目构建测试'() {
        setup: '初始化项目'
        copyProject 'sample-multi', 'rest', 'service', 'sdk'
        testProjectDir.newFile(DEFAULT_SETTINGS_FILE) << '''
            rootProject.name = 'sample-multi'
            include 'rest', 'service', 'sdk'
            project(':rest').name = 'sample-multi-rest'
            project(':service').name = 'sample-multi-service'
            project(':sdk').name = 'sample-multi-sdk'
        '''

        when: '构建项目'
        testProjectDir.newFile('.java-local.properties') << 'spring.profiles.active=dev'
        def result = gradleBuilder.withArguments('build').build()

        then: '检查结果'
        result.task(':sample-multi-rest:pmdMain').outcome == SUCCESS
        result.task(':sample-multi-rest:pmdTest').outcome == SUCCESS
        result.task(':sample-multi-rest:test').outcome == SUCCESS
        result.task(':sample-multi-rest:jacocoTestReport').outcome == SUCCESS
        result.task(':sample-multi-rest:jacocoTestCoverageVerification').outcome == SUCCESS
        result.output.contains 'The following 1 profile is active: "dev"'
        result.output.contains 'BUILD SUCCESSFUL'

        when: '添加test本地属性'
        testProjectDir.newFile('.test-java-local.properties') << 'spring.profiles.active=test'
        result = gradleBuilder.withArguments('build').build()

        then: 'test本地属性优先'
        result.output.contains 'The following 1 profile is active: "test"'
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
