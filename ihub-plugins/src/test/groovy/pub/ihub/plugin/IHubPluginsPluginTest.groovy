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
import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.Title

import static java.io.File.separator
import static org.gradle.api.Project.DEFAULT_BUILD_FILE
import static org.gradle.api.Project.GRADLE_PROPERTIES
import static org.gradle.api.initialization.Settings.DEFAULT_SETTINGS_FILE
import static org.gradle.internal.impldep.org.apache.ivy.util.FileUtil.copy
import static org.gradle.internal.impldep.org.codehaus.plexus.util.FileUtils.copyDirectoryStructure
import static org.gradle.internal.impldep.org.codehaus.plexus.util.FileUtils.copyFile
import static org.gradle.internal.impldep.org.codehaus.plexus.util.FileUtils.getFile
import static org.gradle.internal.impldep.org.eclipse.jgit.lib.Constants.OS_USER_DIR
import static org.gradle.testkit.runner.GradleRunner.create
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS



/**
 * @author henry
 */
@Slf4j
@Title('IHubPluginsPlugin测试套件')
@SuppressWarnings('PrivateFieldCouldBeFinal')
@IgnoreIf({ System.getProperty('fast.test')?.toBoolean() })
class IHubPluginsPluginTest extends Specification {

    @Rule
    private TemporaryFolder testProjectDir = new TemporaryFolder()
    private GradleRunner gradleBuilder
    private File propertiesFile

    def setup() {
        testProjectDir.create()
        gradleBuilder = create().withProjectDir(testProjectDir.root).withPluginClasspath()
        propertiesFile = testProjectDir.newFile GRADLE_PROPERTIES
        copy getClass().classLoader.getResourceAsStream('testkit-gradle.properties'), propertiesFile, null
    }

    def cleanup() {
        testProjectDir.delete()
    }

    def '代码检查插件测试'() {
        setup: '初始化项目'
        copyProject 'sample-groovy', 'src', 'conf'
        testProjectDir.newFile(DEFAULT_SETTINGS_FILE) << 'rootProject.name = \'sample-groovy\''

        when: '构建项目'
        def result = gradleBuilder.withArguments('build').build()

        then: '检查结果'
        result.output.contains '┌──────────────────────────────────────────────────────────────────────────────────────────────────┐'
        result.output.contains '│                               SAMPLE-GROOVY Jacoco Report Coverage                               │'
        result.output.contains '├──────────────────────┬────────────────┬─────────────────┬──────────────────┬─────────────────────┤'
        result.output.contains '│ Type                 │ Total          │ Missed          │ Covered          │ Coverage            │'
        result.output.contains '├──────────────────────┼────────────────┼─────────────────┼──────────────────┼─────────────────────┤'
        result.output.contains '│ INSTRUCTION          │ 13             │ 0               │ 13               │ 100.00%             │'
        result.output.contains '│ BRANCH               │ 0              │ 0               │ 0                │ n/a                 │'
        result.output.contains '│ LINE                 │ 1              │ 0               │ 1                │ 100.00%             │'
        result.output.contains '│ COMPLEXITY           │ 1              │ 0               │ 1                │ 100.00%             │'
        result.output.contains '│ METHOD               │ 1              │ 0               │ 1                │ 100.00%             │'
        result.output.contains '│ CLASS                │ 1              │ 0               │ 1                │ 100.00%             │'
        result.output.contains '└──────────────────────┴────────────────┴─────────────────┴──────────────────┴─────────────────────┘'
        result.output.contains '┌──────────────────────────────────────────────────────────────────────────────────────────────────┐'
        result.output.contains '│                                      Jacoco Report Coverage                                      │'
        result.output.contains '├──────────────────┬─────────────┬───────────┬────────────┬────────────┬────────────┬──────────────┤'
        result.output.contains '│ Project          │ Instruct    │ Branch    │ Line       │ Cxty       │ Method     │ Class        │'
        result.output.contains '├──────────────────┼─────────────┼───────────┼────────────┼────────────┼────────────┼──────────────┤'
        result.output.contains '│ sample-groovy    │ 100.00%     │ n/a       │ 100.00%    │ 100.00%    │ 100.00%    │ 100.00%      │'
        result.output.contains '│ total            │ 100.00%     │ n/a       │ 100.00%    │ 100.00%    │ 100.00%    │ 100.00%      │'
        result.output.contains '└──────────────────┴─────────────┴───────────┴────────────┴────────────┴────────────┴──────────────┘'
        result.task(':codenarcMain').outcome == SUCCESS
        result.task(':codenarcTest').outcome == SUCCESS
        result.task(':test').outcome == SUCCESS
        result.task(':jacocoTestReport').outcome == SUCCESS
        result.task(':jacocoTestCoverageVerification').outcome == SUCCESS
        result.output.contains 'BUILD SUCCESSFUL'
    }

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
        def result = gradleBuilder.withArguments('build', '-DiHubJava.gradleCompilationIncremental=false').build()

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

    def '自定义依赖升级打印测试'() {
        setup: '初始化项目'
        copyProject 'sample-groovy', 'src', 'conf'

        when: '检查组件版本'
        def result = gradleBuilder.withArguments('dependencyUpdates').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    private void copyProject(String name, String... dirs) {
        "${getFile(System.getProperty(OS_USER_DIR)).parentFile.path + separator}samples$separator$name".with {
            copyFile getFile(it + separator + DEFAULT_BUILD_FILE), testProjectDir.newFile(DEFAULT_BUILD_FILE)
            dirs.each { dir ->
                copyDirectoryStructure getFile(it + separator + dir), testProjectDir.newFolder(dir)
            }
        }
    }

}
