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

import org.gradle.api.Action
import org.gradle.api.Project
import pub.ihub.plugin.IHubExtProperty
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.bom.impl.Capability
import pub.ihub.plugin.bom.impl.CapabilitySpecImpl
import pub.ihub.plugin.bom.impl.Dependency
import pub.ihub.plugin.bom.impl.DependencySpecImpl
import pub.ihub.plugin.bom.impl.Exclude
import pub.ihub.plugin.bom.impl.ExcludeSpecImpl
import pub.ihub.plugin.bom.impl.Group
import pub.ihub.plugin.bom.impl.GroupSpecImpl
import pub.ihub.plugin.bom.impl.Module
import pub.ihub.plugin.bom.impl.ModuleSpecImpl
import pub.ihub.plugin.bom.impl.Modules
import pub.ihub.plugin.bom.impl.ModulesSpecImpl
import pub.ihub.plugin.bom.specs.ActionSpec
import pub.ihub.plugin.bom.specs.ConfigSpec
import pub.ihub.plugin.bom.specs.CapabilitySpec
import pub.ihub.plugin.bom.specs.DependencySpec
import pub.ihub.plugin.bom.specs.GroupSpec
import pub.ihub.plugin.bom.specs.ModuleSpec
import pub.ihub.plugin.bom.specs.ModulesSpec
import pub.ihub.plugin.bom.specs.VersionSpec
import pub.ihub.plugin.IHubPluginMethods.printConfigContent
import java.lang.reflect.Modifier
import java.util.function.Supplier
import javax.inject.Inject

/**
 * BOM插件DSL扩展
 * @author liheng
 */
@IHubExtension("iHubBom")
@Suppress("ConfusingMethodName", "UNCHECKED_CAST")
open class IHubBomExtension @Inject constructor(project: Project) : IHubProjectExtensionAware(project), IHubExtProperty {

    val bomVersions: MutableSet<Module> = mutableSetOf()
    val dependencyVersions: MutableSet<Modules> = mutableSetOf()
    val groupVersions: MutableSet<Group> = mutableSetOf()
    val excludeModules: MutableSet<Exclude> = mutableSetOf()
    val dependencies: MutableSet<Dependency> = mutableSetOf()
    val capabilities: MutableSet<Capability> = mutableSetOf()

    /**
     * 导入mavenBom
     * @param action 配置
     */
    fun importBoms(action: Action<GroupSpec<ModuleSpec>>) {
        actionExecute(action, bomVersions, Supplier { ModuleSpecImpl() })
    }

    /**
     * 配置依赖默认版本
     * @param action 配置
     */
    fun dependencyVersions(action: Action<GroupSpec<ModulesSpec>>) {
        actionExecute(action, dependencyVersions, Supplier { ModulesSpecImpl() })
    }

    /**
     * 配置组版本策略（建议尽量使用bom）
     * @param action 配置
     */
    fun groupVersions(action: Action<GroupSpec<VersionSpec>>) {
        actionExecute(action, groupVersions, Supplier { GroupSpecImpl() })
    }

    /**
     * 排除组件依赖
     * @param action 配置
     */
    fun excludeModules(action: Action<GroupSpec<ModulesSpec>>) {
        actionExecute(action, excludeModules, Supplier { ExcludeSpecImpl() })
    }

    /**
     * 配置组件依赖
     * @param action 配置
     */
    fun dependencies(action: Action<DependencySpec>) {
        actionExecute(action, dependencies, Supplier { DependencySpecImpl(project) })
    }

    /**
     * 要求能力
     * @param action 配置
     */
    fun capabilities(action: Action<CapabilitySpec>) {
        actionExecute(action, capabilities, Supplier { CapabilitySpecImpl() })
    }

    private fun <A : ActionSpec<T>, T : ConfigSpec> actionExecute(
        action: Action<A>,
        specs: MutableSet<T>,
        getter: Supplier<A>
    ) {
        val actionSpec = getter.get()
        action.execute(actionSpec)
        actionSpec.specs.forEach { it.appendTo(specs) }
    }

    fun refreshCommonSpecs() {
        if (!isRoot) {
            IHubBomExtension::class.java.declaredFields
                .filter { Modifier.isFinal(it.modifiers) && MutableSet::class.java.isAssignableFrom(it.type) }
                .forEach { refreshCommonSpecs(it.name) }
        }
    }

    fun refreshCommonSpecs(fieldName: String) {
        val specs = getProperty(fieldName) as? MutableSet<ConfigSpec> ?: return
        val rootProjectSpecs = findExtProperty(rootProject, fieldName, mutableSetOf<ConfigSpec>()) as MutableSet<ConfigSpec>

        val updatedRootSpecs = rootProjectSpecs.filterTo(mutableSetOf()) { rootSpec ->
            specs.any { currentSpec -> compareSpec(rootSpec, currentSpec) }
        }
        setExtProperty(rootProject, fieldName, updatedRootSpecs)
    }


    fun printConfigContent() {
        printConfig("bomVersions", "Group Maven Bom Version", "Group", "Module", "Version")
        printConfig("dependencyVersions", "Group Maven Module Version", "Group", "Module", "Version")
        printConfig("groupVersions", "Group Maven Default Version", "Group", "Version")
        printConfig("excludeModules", "Exclude Group Modules", "Group", "Module")
        printConfig("dependencies", "Config Default Dependencies", "DependencyType", "Dependencies")
        printConfig("capabilities", "Config Default Require Capabilities", "Dependency", "Capabilities")
    }

    val isRoot: Boolean
        get() = project == project.rootProject

    private fun printConfig(fieldName: String, title: String, vararg taps: String) {
        val specs = getProperty(fieldName) as? Set<ConfigSpec> ?: emptySet()
        // 获取各项目通用扩展配置
        val commonSpecs = findExtProperty(rootProject, fieldName, mutableSetOf<ConfigSpec>()) as MutableSet<ConfigSpec>
        val data = mutableListOf<List<*>>()

        if (isRoot) {
            // 公共配置追加主项目配置
            specs.forEach { it.appendTo(commonSpecs) }
            setExtProperty(rootProject, fieldName, commonSpecs)
            commonSpecs.forEach { it.appendToPrintData(data) }
        } else {
            // 子项目过滤掉公共配置
            specs.filter { currentSpec -> commonSpecs.none { common -> compareSpec(currentSpec, common) } }
                .forEach { it.appendToPrintData(commonSpecs, data) }
        }
        printConfigContent("${project.name.uppercase()} $title", data, *taps)
    }

    private fun compareSpec(a: ConfigSpec, b: ConfigSpec): Boolean {
        return a.comparedProperties.all { propName ->
            val propA = a::class.java.methods.find { it.name == "get${propName.replaceFirstChar { it.uppercaseChar() }}" || it.name == propName }?.invoke(a)
            val propB = b::class.java.methods.find { it.name == "get${propName.replaceFirstChar { it.uppercaseChar() }}" || it.name == propName }?.invoke(b)
            propA == propB
        }
    }

    // Helper to get property by name, required for refreshCommonSpecs and printConfig
    private fun getProperty(name: String): Any? {
        return try {
            this::class.java.getDeclaredField(name).let {
                it.isAccessible = true
                it.get(this)
            }
        } catch (e: NoSuchFieldException) {
            null
        }
    }
}
