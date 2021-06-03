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

import static org.gradle.internal.impldep.org.apache.ivy.util.FileUtil.copy
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS



/**
 * @author henry
 */
@Slf4j
@Title('IHubPluginsPlugin测试套件')
@SuppressWarnings(['PrivateFieldCouldBeFinal', 'JUnitPublicNonTestMethod'])
class IHubPluginsPluginTest extends Specification {

    @Rule
    private TemporaryFolder testProjectDir = new TemporaryFolder()
    private File buildFile
    private File propertiesFile

    def setup() {
        testProjectDir.create()
        propertiesFile = testProjectDir.newFile('gradle.properties')
        copy getClass().classLoader.getResourceAsStream('testkit-gradle.properties'), propertiesFile, null
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            plugins {
                id 'pub.ihub.plugin'
            }
        """
    }

    def cleanup() {
        testProjectDir.delete()
    }

    def 'basic build test'() {
        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .build()

        then:
        result.output.contains('BUILD SUCCESSFUL')
    }

    def 'complete build test'() {
        when:
        testProjectDir.newFile('settings.gradle') << 'include \'a\', \'b\', \'c\''
        testProjectDir.newFolder 'a'
        testProjectDir.newFolder 'b'
        testProjectDir.newFolder 'c'
        testProjectDir.newFile('a/gradle.properties') << '''
version=1.0.0
javaCompatibility=8
publishNeedSign=true
signingKeyId=id
signingSecretKey=secret
signingPassword=password
publishDocs=true
'''
        propertiesFile << '''
mavenLocalEnabled=true
release.repo.url=http://ihub.pub/nexus/content/repositories/releases
snapshot_repo_url=http://ihub.pub/nexus/content/repositories/snapshots
customize-repo-url=http://ihub.pub/nexus/content/repositories
repoAllowInsecureProtocol=true
repoIncludeGroupRegex=pub\\.ihub\\..*
'''
        buildFile << """
            subprojects {
                apply {
                    plugin 'pub.ihub.plugin.ihub-groovy'
                    plugin 'pub.ihub.plugin.ihub-publish'
                    plugin 'pub.ihub.plugin.ihub-test'
                    plugin 'pub.ihub.plugin.ihub-verification'
                    plugin 'pub.ihub.plugin.ihub-native'
                }
            }

            iHubBom {
                importBoms {
                    group 'cn.hutool' module 'hutool-bom' version '5.6.4'
                }
                dependencyVersions {
                    group 'cn.hutool' modules 'core', 'aop' version '5.6.4'
                }
                groupVersions {
                    group 'cn.hutool' version '5.6.4'
                }
                excludeModules {
                    group 'cn.hutool' modules 'core'
                    group 'pub.ihub'
                }
                dependencies {
                    api ':a', ':b', ':c'
                }
            }
        """
        testProjectDir.newFolder 'libs'
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments('-DrepoUsername=username', '-DrepoPassword=password')
            .build()

        then:
        result.output.contains('flatDir')
        result.output.contains('MavenLocal')
        result.output.contains('ReleaseRepo')
        result.output.contains('SnapshotRepo')
        result.output.contains('CustomizeRepo')
        result.task(':help').outcome == SUCCESS
    }

}
