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
package pub.ihub.plugin.test

import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * @author henry
 */
abstract class IHubSpecification {

    @TempDir
    lateinit var testProjectDir: File

    protected lateinit var gradleBuilder: GradleRunner
    protected lateinit var settingsFile: File
    protected lateinit var buildFile: File
    protected lateinit var propertiesFile: File

    @BeforeEach
    open fun setup() {
        gradleBuilder = GradleRunner.create().withProjectDir(testProjectDir).withPluginClasspath()

        // Copy libs.versions.toml
        javaClass.classLoader.getResourceAsStream("libs.versions.toml")?.use { inputStream ->
            Files.copy(inputStream, File(testProjectDir, "libs.versions.toml").toPath(), StandardCopyOption.REPLACE_EXISTING)
        }

        settingsFile = File(testProjectDir, Settings.DEFAULT_SETTINGS_FILE)
        settingsFile.writeText("""
            dependencyResolutionManagement {
                versionCatalogs {
                    ihub {
                        from(files("libs.versions.toml"))
                    }
                }
            }
        """.trimIndent())

        buildFile = File(testProjectDir, Project.DEFAULT_BUILD_FILE)
        // Ensure buildFile exists, even if empty initially
        buildFile.createNewFile()

        propertiesFile = File(testProjectDir, Project.GRADLE_PROPERTIES)
        // Copy testkit-gradle.properties
        javaClass.classLoader.getResourceAsStream("testkit-gradle.properties")?.use { inputStream ->
            Files.copy(inputStream, propertiesFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
    }

    protected fun copyProjectFromSamples(buildFileName: String) {
        val samplesDir = File(System.getProperty("user.dir")).parentFile.resolve("samples").resolve("sample-extensions")
        val sourceBuildFile = File(samplesDir, buildFileName)
        if (sourceBuildFile.exists()) {
            Files.copy(sourceBuildFile.toPath(), buildFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        } else {
            throw IllegalArgumentException("Sample build file not found: $sourceBuildFile")
        }
    }

    protected fun copyProjectFromSamples(name: String, vararg dirs: String) {
        val samplesDir = File(System.getProperty("user.dir")).parentFile.resolve("samples").resolve(name)
        val sourceBuildFile = File(samplesDir, Project.DEFAULT_BUILD_FILE)

        if (sourceBuildFile.exists()) {
            Files.copy(sourceBuildFile.toPath(), buildFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        } else {
            throw IllegalArgumentException("Sample build file not found: $sourceBuildFile")
        }

        dirs.forEach { dir ->
            val sourceDir = File(samplesDir, dir)
            if (sourceDir.exists() && sourceDir.isDirectory) {
                val destDir = File(testProjectDir, dir)
                destDir.mkdirs()
                sourceDir.copyRecursively(destDir, overwrite = true)
            } else {
                // Optionally, handle cases where a directory might not exist or is not a directory
                // For now, we'll assume valid directory structures in samples
            }
        }
    }
}
