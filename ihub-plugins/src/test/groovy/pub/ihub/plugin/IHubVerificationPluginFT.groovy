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

import static org.gradle.api.Project.DEFAULT_BUILD_FILE
import static org.gradle.api.Project.GRADLE_PROPERTIES
import static org.gradle.internal.impldep.org.apache.ivy.util.FileUtil.copy



/**
 * @author liheng
 */
@Slf4j
@Title('IHubVerificationPlugin功能测试')
@SuppressWarnings('JUnitPublicNonTestMethod')
class IHubVerificationPluginFT extends Specification {

    @Rule
    private TemporaryFolder testProjectDir = new TemporaryFolder()
    private File buildFile
    private File propertiesFile
    private GradleRunner gradleBuilder

    void setup() {
        testProjectDir.create()
        buildFile = testProjectDir.newFile DEFAULT_BUILD_FILE
        propertiesFile = testProjectDir.newFile GRADLE_PROPERTIES
        gradleBuilder = GradleRunner.create().withProjectDir(testProjectDir.root).withPluginClasspath()
        copy getClass().classLoader.getResourceAsStream('testkit-gradle.properties'), propertiesFile, null
        buildFile << """
            plugins {
                id 'pub.ihub.plugin'
            }
            apply {
                plugin 'pub.ihub.plugin.ihub-groovy'
                plugin 'pub.ihub.plugin.ihub-verification'
            }
        """
    }

    def cleanup() {
        testProjectDir.delete()
    }

    def '代码检查基础构建测试'() {
        when: '基础配置'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'AliYunGradlePlugin'
        result.output.contains 'AliYunSpringPlugin'
        result.output.contains 'SpringRelease'

        when: '本地插件配置'
        result = gradleBuilder.build()

//        then: '检查结果'
//        result.output.contains 'flatDir'

        then:
        result.output.contains 'BUILD SUCCESSFUL'
    }

}
