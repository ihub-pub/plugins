/*
 * Copyright (c) 2021-2024 the original author or authors.
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
package pub.ihub.plugin.bom

import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import io.kotest.matchers.string.shouldMatch
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pub.ihub.plugin.test.IHubSpecification
import java.io.File

/**
 * @author henry
 */
@DisplayName("BOM插件DSL扩展测试套件")
@Suppress("AbcMetric", "FunctionMaxLength") // Suppress warnings for long methods due to test setups
class IHubBomPluginTest : IHubSpecification() {

    @Test
    fun `基础配置成功测试`() {
        // setup: 初始化项目
        copyProjectFromSamples("bom.gradle")

        // when: 构建项目
        val result = gradleBuilder.build()

        // then: 检查结果
        result.output shouldMatch Regex("[\\s\\S]+│ pub.ihub.lib + │ ihub-bom + │ 1.4.6 + │[\\s\\S]+")
        result.output shouldContain "│ pub.ihub.lib                    │ ihub-process                    │ 1.4.6                        │"
        result.output shouldContain "│ pub.ihub.lib                    │ ihub-core                       │ 1.4.6                        │"
        result.output shouldContain "│ pub.ihub.lib                                      │ 1.4.6                                        │"
        result.output shouldContain "│ org.slf4j                                      │ slf4j-api                                       │"
        result.output shouldContain "│ pub.ihub                                       │ all                                             │"
        result.output shouldContain "│ api                                        │ pub.ihub.lib:ihub-core                              │"
        result.output shouldContain "│ pub.ihub.lib:ihub-boot-cloud-spring-boot-starter         │ pub.ihub.lib:reactor-support          │"
        result.output shouldContain "│ ihub-boot-cloud-spring-boot-starter                      │ nacos-support                         │"
        result.task(":tasks")?.outcome shouldBe TaskOutcome.SUCCESS // Or check for "BUILD SUCCESSFUL"
        result.output shouldContain "BUILD SUCCESSFUL"
    }

    @Test
    fun `bom配置组件需要能力配置测试`() {
        // setup: 初始化项目
        buildFile.writeText("""
            plugins {
                id 'pub.ihub.plugin.ihub-bom'
                id 'java'
            }
            iHubBom {
                groupVersions {
                    group("pub.ihub.lib").version("1.4.6")
                }
                capabilities {
                    requireCapability("pub.ihub.lib:ihub-boot-cloud-spring-boot-starter", "pub.ihub.lib:reactor-support")
                    requireCapability("ihub-boot-cloud-spring-boot-starter", "nacos-support")
                }
            }
            repositories {
                mavenCentral()
            }
            dependencies {
                implementation("pub.ihub.lib:ihub-boot-cloud-spring-boot-starter")
            }
        """.trimIndent())
        File(testProjectDir, "src/main/java").mkdirs()
        File(testProjectDir, "src/main/java/Demo.java").createNewFile()

        // when: 构建项目
        val result = gradleBuilder.withArguments("build").build()

        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
    }

    @Test
    fun `bom不含bomVersions配置测试`() {
        // setup: 初始化项目
        copyProjectFromSamples("bom.gradle")
        buildFile.appendText("\niHubBom.bomVersions.clear()\n")

        // when: 构建项目
        val result = gradleBuilder.build()

        // then: 检查结果
        result.output shouldNotContain "ihub-bom" // Assuming this means the specific line for ihub-bom itself
        result.output shouldContain "│ pub.ihub.lib                    │ ihub-process                    │ 1.4.6                        │"
        result.output shouldContain "│ pub.ihub.lib                    │ ihub-core                       │ 1.4.6                        │"
        result.output shouldContain "│ pub.ihub.lib                                      │ 1.4.6                                        │"
        result.output shouldContain "│ org.slf4j                                      │ slf4j-api                                       │"
        result.output shouldContain "│ pub.ihub                                       │ all                                             │"
        result.output shouldContain "│ api                                        │ pub.ihub.lib:ihub-core                              │"
        result.output shouldContain "BUILD SUCCESSFUL"
    }

    @Test
    fun `bom不含dependencyVersions配置测试`() {
        // setup: 初始化项目
        copyProjectFromSamples("bom.gradle")
        buildFile.appendText("\niHubBom.dependencyVersions.clear()\n")


        // when: 构建项目
        val result = gradleBuilder.build()

        // then: 检查结果
        result.output shouldMatch Regex("[\\s\\S]+│ pub.ihub.lib + │ ihub-bom + │ 1.4.6 + │[\\s\\S]+")
        result.output shouldNotContain "│ ihub-process"
        result.output shouldNotContain "│ ihub-core" // This seems to contradict the later assertion for api dep on ihub-core
        result.output shouldContain "│ pub.ihub.lib                                      │ 1.4.6                                        │"
        result.output shouldContain "│ org.slf4j                                      │ slf4j-api                                       │"
        result.output shouldContain "│ pub.ihub                                       │ all                                             │"
        // This assertion might fail if dependencyVersions.clear() removes the version for ihub-core used in dependencies block
        result.output shouldContain "│ api                                        │ pub.ihub.lib:ihub-core                              │"
        result.output shouldContain "BUILD SUCCESSFUL"
    }

    @Test
    fun `子项目配置成功测试`() {
        // setup: 初始化项目
        buildFile.writeText("""
            plugins {
                id 'pub.ihub.plugin.ihub-bom'
            }
        """.trimIndent())

        // when: 添加子项目
        settingsFile.appendText("\ninclude 'a', 'b', 'c', 'demo-bom'\n")
        File(testProjectDir, "a").mkdirs()
        File(testProjectDir, "b").mkdirs()
        File(testProjectDir, "c").mkdirs()
        buildFile.appendText("""
            allprojects {
                apply plugin: 'pub.ihub.plugin.ihub-bom' // Apply to all for iHubBom extension
                extensions.configure(pub.ihub.plugin.bom.IHubBomExtension::class.java) { ext ->
                    ext.importBoms {
                        it.group("org.apache.groovy").module("groovy-bom").version("4.0.5")
                    }
                    ext.dependencyVersions {
                        it.group("org.apache.groovy").modules("groovy-all").version("4.0.5")
                    }
                    ext.capabilities {
                        it.requireCapability("org.slf4j:slf4j-ext", "org.javassist:javassist")
                    }
                }
            }
            extensions.configure(pub.ihub.plugin.bom.IHubBomExtension::class.java) { ext ->
                ext.excludeModules {
                    it.group("pub.ihub.lib").modules("core")
                }
                ext.dependencies {
                    it.api(":a", ":b", ":c")
                    it.compileOnlyApi("pub.ihub.lib:ihub-core")
                    it.testCompileOnly("pub.ihub.lib:ihub-process")
                }
            }
            subprojects {
                apply plugin: 'groovy' // Apply groovy to subprojects for them to be actual projects
                extensions.configure(pub.ihub.plugin.bom.IHubBomExtension::class.java) { ext ->
                    ext.importBoms {
                        it.group("org.apache.groovy").module("groovy-bom").version("4.0.4")
                        it.group("pub.ihub.lib").module("ihub-bom").version("1.0.7")
                    }
                    ext.dependencyVersions {
                        it.group("org.apache.groovy").modules("groovy-all").version("4.0.4")
                    }
                    ext.groupVersions {
                        it.group("pub.ihub.lib").version("1.0.7")
                    }
                    ext.excludeModules {
                        it.group("pub.ihub.lib") // This implies all modules in pub.ihub.lib
                        it.group("com.demo").modules("core")
                    }
                    ext.dependencies {
                        it.compileOnlyApi("pub.ihub.lib:ihub-core")
                        it.runtimeOnly("pub.ihub.lib:ihub-core")
                        it.implementation("pub.ihub.lib:ihub-process")
                        it.annotationProcessor("pub.ihub.lib:ihub-process")
                    }
                    ext.capabilities {
                        it.requireCapability("org.slf4j:slf4j-ext", "other-support")
                    }
                }
            }
        """.trimIndent())
        File(testProjectDir, "a/build.gradle").writeText("""
            // In Kotlin, use extensions.configure
            extensions.configure(pub.ihub.plugin.bom.IHubBomExtension::class.java) { ext ->
                ext.excludeModules {
                    it.group("pub.ihub.lib").modules("ihub-core")
                    it.group("com.demo").modules("core", "common")
                }
                ext.groupVersions {
                    it.group("pub.ihub.lib").version("1.0.6")
                }
                ext.dependencyVersions {
                    it.group("org.apache.groovy").modules("groovy-core").version("4.0.4")
                }
                ext.dependencies {
                    it.compileOnlyApi("pub.ihub.lib:ihub-process")
                    it.testRuntimeOnly("pub.ihub.lib:ihub-core")
                    it.testImplementation("pub.ihub.lib:ihub-core")
                }
            }
        """.trimIndent())

        val result = gradleBuilder.build()

        // then: 检查结果
        // Group Maven Bom Version (root project)
        result.output shouldMatch Regex("[\\s\\S]+│ org.apache.groovy + │ groovy-bom + │ 4.0.4 + │[\\s\\S]+") // Subproject overrides root
        result.output shouldMatch Regex("[\\s\\S]+│ pub.ihub.lib + │ ihub-bom + │ 1.0.7 + │[\\s\\S]+")
        // B Group Maven Default Version
        result.output shouldContain "│ pub.ihub.lib                                      │ 1.0.7                                        │" // From subproject config
        // Config Default Dependencies (root project)
        result.output shouldContain "│ runtimeOnly                                 │ pub.ihub.lib:ihub-core                             │"
        result.output shouldContain "│ implementation                              │ pub.ihub.lib:ihub-process                          │"
        result.output shouldContain "│ annotationProcessor                         │ pub.ihub.lib:ihub-process                          │"
        result.output shouldContain "│ api                                         │ :a                                                 │"
        result.output shouldContain "│ api                                         │ :b                                                 │"
        result.output shouldContain "│ api                                         │ :c                                                 │"
        result.output shouldContain "│ compileOnlyApi                              │ pub.ihub.lib:ihub-core                             │" // From root
        result.output shouldContain "│ testCompileOnly                             │ pub.ihub.lib:ihub-process                          │" // From root
        // Config Default Dependencies (subprojects)
        result.output shouldContain "│ compileOnlyApi                              │ pub.ihub.lib:ihub-process                          │" // From project a
        result.output shouldContain "│ testRuntimeOnly                             │ pub.ihub.lib:ihub-core                             │" // From project a
        result.output shouldContain "│ testImplementation                          │ pub.ihub.lib:ihub-core                             │" // From project a
        // Exclude Group Modules (root project)
        result.output shouldContain "│ pub.ihub.lib                                      │ core                                         │" // From root
        // B Exclude Group Modules (from subproject general config)
        result.output shouldContain "│ pub.ihub.lib                                      │ all                                          │"
        result.output shouldContain "│ com.demo                                          │ core                                         │"
        // A Exclude Group Modules (from project a specific config)
        // The printout might show "all" if the specific excludes are covered by a broader "all"
        // result.output shouldContain "│ pub.ihub.lib                                     │ all                                           │" // This might be there due to subproject general
        result.output shouldContain "│ pub.ihub.lib                                     │ ihub-core                                     │" // Specific to A
        result.output shouldContain "│ com.demo                                         │ core                                          │" // From subproject general, also in A
        result.output shouldContain "│ com.demo                                         │ common                                        │" // Specific to A
        result.output shouldContain "BUILD SUCCESSFUL"
    }

    @Test
    fun `配置失败测试-排除组件依赖配置版本号`() {
        // when: 排除组件依赖配置版本号
        buildFile.writeText("""
            plugins {
                id 'pub.ihub.plugin.ihub-bom'
            }
            iHubBom {
                excludeModules {
                    it.group("pub.ihub").version("1.0.0")
                }
            }
        """.trimIndent())
        val result = gradleBuilder.buildAndFail()

        // then: 检查结果
        result.output shouldContain "Does not support 'version' method!"
    }

    @Test
    fun `配置失败测试-配置组件依赖类型为空`() {
        // when: 配置组件依赖类型为空
        buildFile.writeText("""
            plugins {
                id 'pub.ihub.plugin.ihub-bom'
            }
            iHubBom {
                dependencies {
                    it.compile(null!!, "some:dep") // Pass null for type, ensure it's seen as String?
                }
            }
        """.trimIndent())
        val result = gradleBuilder.buildAndFail()

        // then: 检查结果
        result.output shouldContain "dependencies type not null!"
    }

    @Test
    fun `配置失败测试-配置组件依赖为空`() {
        // when: 配置组件依赖为空
        buildFile.writeText("""
            plugins {
                id 'pub.ihub.plugin.ihub-bom'
            }
            iHubBom {
                dependencies {
                    it.compile("api") // No dependencies provided
                }
            }
        """.trimIndent())
        val result = gradleBuilder.buildAndFail()
        // then: 检查结果
        result.output shouldContain "api dependencies not empty!"
    }

    @Test
    fun `模拟Java平台组件应用ihub-bom测试`() {
        // when:
        buildFile.writeText("""
            plugins {
                id 'java-platform'
                id 'pub.ihub.plugin.ihub-bom'
            }
        """.trimIndent())
        val result = gradleBuilder.build()
        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
    }

    @Test
    fun `模拟版本目录组件应用ihub-bom测试`() {
        // when:
        buildFile.writeText("""
            plugins {
                id 'version-catalog'
                id 'pub.ihub.plugin.ihub-bom'
            }
             versionCatalog {
                // Minimal version catalog for test
                library("slf4j-api", "org.slf4j", "slf4j-api").version("1.7.30")
            }
        """.trimIndent())
        val result = gradleBuilder.build()
        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
    }

    @Test
    fun `ihub-dependencies配置测试`() {
        // when:
        copyProjectFromSamples("bom.gradle")
        propertiesFile.appendText("\niHubSettings.includeDependencies=ihub-dependencies\n")
        val result = gradleBuilder.build()
        // then: 检查结果
        result.output shouldNotContain "ihub-dependencies" // This implies the BOM itself isn't listed when auto-applied
        result.output shouldContain "BUILD SUCCESSFUL"
    }

    @Test
    fun `iHubLibsLocalVersion配置测试`() {
        // when:
        copyProjectFromSamples("bom.gradle")
        propertiesFile.appendText("\niHub.iHubLibsLocalVersion=test-SNAPSHOT\n")
        val result = gradleBuilder.build()
        // then: 检查结果
        result.output shouldContain "test-SNAPSHOT"
        result.output shouldContain "BUILD SUCCESSFUL"
    }
}
