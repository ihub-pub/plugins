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
package pub.ihub.plugin.githooks

import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.XmlUtil
import com.google.gson.GsonBuilder
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * IHub Git Hooks插件扩展
 * @author henry
 */
@IHubExtension("iHubGitHooks")
@Suppress("ConfusingMethodName", "MemberVisibilityCanBePrivate", "TooManyFunctions", "SpreadOperator")
open class IHubGitHooksExtension @Inject constructor(project: Project, objectFactory: ObjectFactory) :
    IHubProjectExtensionAware(project) {

    companion object {
        private val DEFAULT_TYPES: Map<String, List<String>> = mapOf(
            "refactor" to listOf("代码重构", "Changes which neither fix a bug nor add a feature"),
            "fix" to listOf("修复错误", "Changes which patch a bug"),
            "feat" to listOf("引入新功能", "Changes which introduce a new feature"),
            "build" to listOf("架构改动", "Changes which affect the build system or external dependencies"),
            "chore" to listOf("与业务无关的改动", "Changes which aren't user-facing"),
            "style" to listOf("结构改进/代码格式化", "Changes which don't affect code logic, such as white-spaces, formatting, missing semi-colons"),
            "test" to listOf("更新测试", "Changes which add missing tests or correct existing tests"),
            "docs" to listOf("更新文档", "Changes which affect documentation"),
            "perf" to listOf("性能改善", "Changes which improve performance"),
            "ci" to listOf("CI/CD", "Changes which affect CI configuration files and scripts"),
            "revert" to listOf("代码回滚", "Changes which revert a previous commit")
        )

        private val DEFAULT_SCOPES: Map<String, List<String>> = mapOf(
            "build" to listOf("gradle", "maven", "ant"),
            "ci" to listOf("github", "gitlab", "jenkins", "travis")
        )

        private val DEFAULT_FOOTERS: Map<String, String> = mapOf(
            "BREAKING CHANGE" to "The commit introduces breaking API changes",
            "Closes" to "The commit closes issues or pull requests",
            "Implements" to "The commit implements features",
            "Co-authored-by" to "The commit is co-authored by another person.<br/>For multiple people use one line each",
            "Refs" to "The commit references other commits by their hash ID.<br/>For multiple hash IDs use a comma as separator"
        )

        fun assertRule(condition: Boolean, message: String) {
            if (!condition) {
                throw GradleException("$message See https://doc.ihub.pub/plugins/iHubGitHooks")
            }
        }
    }

    /**
     * 自定义hooks路径
     */
    @get:IHubProperty(type = [IHubProperty.Type.PROJECT, IHubProperty.Type.SYSTEM])
    val hooksPath: Property<String> = objectFactory.property(String::class.java)

    /**
     * 自定义hooks
     */
    val hooks: MapProperty<String, String> = objectFactory.mapProperty(String::class.java, String::class.java).convention(emptyMap())

    /**
     * 提交描述正则表达式
     */
    val descriptionRegex: Property<String> = objectFactory.property(String::class.java).convention(".{1,100}")

    /**
     * 提交类型
     */
    val types: MutableSet<TypeSpec> = mutableSetOf()

    /**
     * 注脚信息
     */
    val footers: MutableSet<FooterSpec> = mutableSetOf()

    fun types(vararg types: String) {
        this.types.addAll(types.map { typeName ->
            TypeSpec(typeName).also { spec ->
                spec.description(*(DEFAULT_TYPES[typeName]?.toTypedArray() ?: emptyArray()))
                spec.scopes(*(DEFAULT_SCOPES[typeName]?.toTypedArray() ?: emptyArray()))
            }
        })
    }

    fun type(name: String): TypeSpec {
        return TypeSpec(name).also { types.add(it) }
    }

    fun footers(vararg footers: String) {
        this.footers.addAll(footers.map { footerName ->
            FooterSpec(footerName).also { spec ->
                spec.description(DEFAULT_FOOTERS[footerName] ?: "")
            }
        })
    }

    fun footer(name: String): FooterSpec {
        return FooterSpec(name).also { footers.add(it) }
    }

    fun checkHeader(header: String): List<String> {
        val typePattern = types.joinToString("|") { Regex.escape(it.name) }
        val regex = """^($typePattern)(\((.+)\))?!?: ${descriptionRegex.get()}""".toRegex()
        val matchResult = regex.matchEntire(header)
        assertRule(matchResult != null, "Commit msg header check fail!")
        return listOfNotNull(matchResult!!.groupValues[1], matchResult.groupValues[4].takeIf { it.isNotEmpty() })
    }


    private val hooksDir: File by lazy {
        project.layout.projectDirectory.dir(".gradle").dir("pub.ihub.plugin.hooks").asFile
    }

    fun writeHook(hookName: String, command: String) {
        val scriptPath: Path = hooksDir.toPath().resolve(hookName)
        Files.createDirectories(scriptPath.parent)
        val lines = listOf(
            "#!/bin/bash",
            "# This file is generated by the 'pub.ihub.plugin.ihub-git-hooks' Gradle plugin",
            "echo 'ihub $hookName hook: $command'",
            command
        )
        Files.write(scriptPath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        scriptPath.toFile().setExecutable(true, false)
    }

    fun writeHooks() {
        if (hooksDir.exists() && !hooksDir.deleteRecursively()) {
            project.logger.warn("Could not delete old hooks directory: $hooksDir")
        }
        hooks.get().forEach(this::writeHook)
    }

    private fun String.execute(workingDir: File = project.projectDir, timeout: Long = 60): String? {
        return try {
            val parts = this.split("\\s".toRegex())
            val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(timeout, TimeUnit.SECONDS)
            proc.inputStream.bufferedReader().readText()
        } catch (e: IOException) {
            project.logger.error("Error executing command: $this", e)
            null
        }
    }


    fun execute(hooksPathValue: String?, hooksValue: Map<String, String>?) {
        val logger: Logger = project.logger
        try {
            if (!hooksPathValue.isNullOrEmpty()) {
                "git config core.hooksPath $hooksPathValue".execute()
                logger.lifecycle("Set git hooks path: $hooksPathValue")
            } else if (!hooksValue.isNullOrEmpty()) {
                writeHooks()
                "git config core.hooksPath .gradle/pub.ihub.plugin.hooks".execute()
                logger.lifecycle("Set git hooks path: .gradle/pub.ihub.plugin.hooks")
            } else {
                "git config --unset core.hooksPath".execute()
                logger.lifecycle("Unset git hooks path, learn more see https://doc.ihub.pub/plugins/iHubGitHooks")
            }
        } catch (e: Exception) {
            logger.lifecycle("Git hooks config fail: ${e.message}")
        }
    }

    fun configDefaultGitCommitCheck() {
        types(*DEFAULT_TYPES.keys.toTypedArray())
        footers(*DEFAULT_FOOTERS.keys.toTypedArray())
    }

    fun configConventionalCommit() {
        val rootProject = project.rootProject
        val pluginConfigPath = rootProject.file(".idea/conventionalCommit.xml").path
        val configPath = generateConventionalCommitConfig(rootProject)
        val document: Document
        val component: Element

        if (FileUtil.exist(pluginConfigPath)) {
            document = XmlUtil.readXML(pluginConfigPath)
            val existingComponent = XmlUtil.getNodeByXPath("//component[@name='general']", document.documentElement) as? Element
            if (existingComponent != null) {
                val existingOption = XmlUtil.getNodeByXPath("option[@name='customFilePath']", existingComponent)
                if (existingOption != null) {
                    return // Already configured
                }
                component = existingComponent
            } else {
                component = document.createElement("component")
                component.setAttribute("name", "general")
                document.documentElement.appendChild(component)
            }
        } else {
            document = XmlUtil.createXml()
            val projectElement = document.createElement("project")
            projectElement.setAttribute("version", "4")
            document.appendChild(projectElement)
            component = document.createElement("component")
            component.setAttribute("name", "general")
            projectElement.appendChild(component)
        }

        val optionElement = document.createElement("option")
        optionElement.setAttribute("name", "customFilePath")
        optionElement.setAttribute("value", configPath)
        component.appendChild(optionElement)

        XmlUtil.toFile(document, pluginConfigPath)
    }

    private fun generateConventionalCommitConfig(rootProject: Project): String {
        val tmpPath = File(rootProject.projectDir, ".gradle/pub.ihub.plugin.cache")
        tmpPath.mkdirs()
        val configFile = File(tmpPath, "conventionalCommit.json")

        val configMap = mapOf(
            "types" to types.associate { typeSpec -> typeSpec.name to typeSpec.generateConfig()[typeSpec.name] },
            "footerTypes" to footers.map { it.generateConfig() }
        )
        val gson = GsonBuilder().setPrettyPrinting().create()
        configFile.writeText(gson.toJson(configMap))
        return configFile.path
    }


    open class ConfigSpec<T : ConfigSpec<T>>(val name: String) {
        var description: String = ""

        @Suppress("UNCHECKED_CAST")
        fun description(vararg descriptionParts: String): T {
            this.description = descriptionParts.joinToString("<br/>")
            return this as T
        }

        open fun generateConfig(): Map<String, Any?> {
            return mapOf(name to mapOf("description" to description))
        }

        // For compatibility with Groovy's == on these objects if based on name
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as ConfigSpec<*>
            return name == other.name
        }

        override fun hashCode(): Int = name.hashCode()
    }

    class TypeSpec(name: String) : ConfigSpec<TypeSpec>(name) {
        val scopes: MutableSet<ConfigSpec<*>> = mutableSetOf()
        var requiredScope: Boolean = false

        fun scopes(vararg scopes: String): TypeSpec {
            this.scopes.addAll(scopes.map { ConfigSpec<ConfigSpec<*>>(it) })
            return this
        }

        fun scope(name: String): ConfigSpec<*> {
            return ConfigSpec<ConfigSpec<*>>(name).also { scopes.add(it) }
        }

        fun requiredScope(requiredScope: Boolean): TypeSpec {
            this.requiredScope = requiredScope
            return this
        }

        fun checkScope(scope: String?) {
            assertRule(
                !requiredScope || (scope != null && scopes.any { it.name == scope }),
                "Commit msg header scope not in [${scopes.joinToString(", ") { it.name }}]!"
            )
        }

        override fun generateConfig(): Map<String, Any?> {
            val scopeDesc = if (scopes.isNotEmpty()) "<br/>Example scopes: ${scopes.joinToString(", ") { it.name }}" else ""
            return mapOf(
                name to mapOf(
                    "description" to (description + scopeDesc),
                    "scopes" to scopes.associate { it.name to it.generateConfig()[it.name] }
                )
            )
        }
    }

    class FooterSpec(name: String) : ConfigSpec<FooterSpec>(name) {
        var required: Boolean = false
        var types: Array<String> = emptyArray()
        var valueRegex: String? = null

        fun required(required: Boolean): FooterSpec {
            this.required = required
            return this
        }

        fun valueRegex(valueRegex: String): FooterSpec {
            this.valueRegex = valueRegex
            return this
        }

        fun requiredWithType(vararg types: String): FooterSpec {
            this.types = arrayOf(*types)
            return this
        }

        fun checkRequired(footerValue: String?) {
            assertRule(!required || !footerValue.isNullOrEmpty(), "Commit msg footer missing '$name'!")
        }

        fun checkRequiredWithType(type: String, footerValue: String?) {
            assertRule(
                !this.types.contains(type) || !footerValue.isNullOrEmpty(),
                "Commit msg footer missing '$name' where type is '$type'!"
            )
        }

        fun checkValue(footerValue: String?) {
            assertRule(
                valueRegex.isNullOrEmpty() || footerValue.isNullOrEmpty() || footerValue.matches(valueRegex!!.toRegex()),
                "Commit msg footer '$name' check fail with regex: '$valueRegex'!"
            )
        }

        override fun generateConfig(): Map<String, Any?> {
            // This differs from Groovy, which was a list of maps.
            // Assuming a single map per footer is intended for the JSON.
            return mapOf("name" to name, "description" to description)
        }
    }
}
