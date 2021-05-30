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
import org.gradle.internal.impldep.org.junit.After
import org.gradle.internal.impldep.org.junit.Before
import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.Test
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS



/**
 * @author henry
 */
@Slf4j
@Title('IHubPluginsPlugin测试套件')
@SuppressWarnings('PrivateFieldCouldBeFinal')
class IHubPluginsPluginTest extends Specification {

    @Rule
    @Shared
    private TemporaryFolder testProjectDir = new TemporaryFolder()
    private File buildFile

    @Before
    def setupSpec() {
        testProjectDir.create()
    }

    @Before
    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            plugins {
                id 'pub.ihub.plugin'
            }
        """
    }

    @After
    def cleanup() {
        buildFile.delete()
    }

    @After
    def cleanupSpec() {
        testProjectDir.delete()
    }

    @Test
    def 'test build'() {
        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .build()

        then:
        result.output.contains('BUILD SUCCESSFUL')
    }

    @Test
    def 'test repositories'() {
        when:
        testProjectDir.newFolder 'libs'
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .build()

        then:
        result.output.contains('flatDir')
        result.task(':help').outcome == SUCCESS
    }

}
