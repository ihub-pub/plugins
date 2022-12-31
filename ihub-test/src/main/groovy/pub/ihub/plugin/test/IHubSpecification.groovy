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
package pub.ihub.plugin.test

import groovy.util.logging.Slf4j
import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import static java.io.File.separator
import static org.gradle.api.Project.DEFAULT_BUILD_FILE
import static org.gradle.api.Project.GRADLE_PROPERTIES
import static org.gradle.internal.impldep.org.apache.ivy.util.FileUtil.copy
import static org.gradle.internal.impldep.org.codehaus.plexus.util.FileUtils.copyDirectoryStructure
import static org.gradle.internal.impldep.org.codehaus.plexus.util.FileUtils.copyFile
import static org.gradle.internal.impldep.org.codehaus.plexus.util.FileUtils.getFile
import static org.gradle.internal.impldep.org.eclipse.jgit.lib.Constants.OS_USER_DIR
import static org.gradle.testkit.runner.GradleRunner.create



/**
 * @author henry
 */
@Slf4j
class IHubSpecification extends Specification {

    @Rule
    protected TemporaryFolder testProjectDir = new TemporaryFolder()
    protected GradleRunner gradleBuilder
    protected File buildFile
    protected File propertiesFile

    def setup() {
        testProjectDir.create()
        gradleBuilder = create().withProjectDir(testProjectDir.root).withPluginClasspath()
        buildFile = testProjectDir.newFile DEFAULT_BUILD_FILE
        propertiesFile = testProjectDir.newFile GRADLE_PROPERTIES
        copy getClass().classLoader.getResourceAsStream('testkit-gradle.properties'), propertiesFile, null
    }

    def cleanup() {
        testProjectDir.delete()
    }

    protected void copyProject(String buildFileName) {
        "${getFile(System.getProperty(OS_USER_DIR)).parentFile.path + separator}samples${separator}sample-extensions"
            .with { copyFile getFile(it + separator + buildFileName), buildFile }
    }

    protected void copyProject(String name, String... dirs) {
        "${getFile(System.getProperty(OS_USER_DIR)).parentFile.path + separator}samples$separator$name".with {
            copyFile getFile(it + separator + DEFAULT_BUILD_FILE), buildFile
            dirs.each { dir ->
                copyDirectoryStructure getFile(it + separator + dir), testProjectDir.newFolder(dir)
            }
        }
    }

}
