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
package pub.ihub.plugin.bom

import groovy.transform.CompileStatic
import org.gradle.api.Action
import pub.ihub.plugin.IHubExtProperty
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.bom.impl.Dependency
import pub.ihub.plugin.bom.impl.DependencySpecImpl
import pub.ihub.plugin.bom.impl.Exclude
import pub.ihub.plugin.bom.impl.ExcludeSpecImpl
import pub.ihub.plugin.bom.impl.Capability
import pub.ihub.plugin.bom.impl.CapabilitySpecImpl
import pub.ihub.plugin.bom.impl.Group
import pub.ihub.plugin.bom.impl.GroupSpecImpl
import pub.ihub.plugin.bom.impl.Module
import pub.ihub.plugin.bom.impl.ModuleSpecImpl
import pub.ihub.plugin.bom.impl.Modules
import pub.ihub.plugin.bom.impl.ModulesSpecImpl
import pub.ihub.plugin.bom.specs.ActionSpec
import pub.ihub.plugin.bom.specs.ConfigSpec
import pub.ihub.plugin.bom.specs.DependencySpec
import pub.ihub.plugin.bom.specs.CapabilitySpec
import pub.ihub.plugin.bom.specs.GroupSpec
import pub.ihub.plugin.bom.specs.ModuleSpec
import pub.ihub.plugin.bom.specs.ModulesSpec
import pub.ihub.plugin.bom.specs.VersionSpec

import java.lang.reflect.Modifier
import java.util.function.Supplier

import static groovy.transform.TypeCheckingMode.SKIP
import static pub.ihub.plugin.IHubPluginMethods.printConfigContent

/**
 * BOM插件DSL扩展
 * @author liheng
 */
@IHubExtension('iHubBom')
@CompileStatic
@SuppressWarnings('ConfusingMethodName')
class IHubBomExtension extends IHubProjectExtensionAware implements IHubExtProperty {

    final Set<Module> bomVersions = []
    final Set<Modules> dependencyVersions = []
    final Set<Group> groupVersions = []
    final Set<Exclude> excludeModules = []
    final Set<Dependency> dependencies = []
    final Set<Capability> capabilities = []

    /**
     * 导入mavenBom
     * @param action 配置
     */
    @CompileStatic(SKIP)
    void importBoms(Action<GroupSpec<ModuleSpec>> action) {
        actionExecute action, bomVersions, ModuleSpecImpl::new
    }

    /**
     * 配置依赖默认版本
     * @param action 配置
     */
    @CompileStatic(SKIP)
    void dependencyVersions(Action<GroupSpec<ModulesSpec>> action) {
        actionExecute action, dependencyVersions, ModulesSpecImpl::new
    }

    /**
     * 配置组版本策略（建议尽量使用bom）
     * @param action 配置
     */
    @CompileStatic(SKIP)
    void groupVersions(Action<GroupSpec<VersionSpec>> action) {
        actionExecute action, groupVersions, GroupSpecImpl::new
    }

    /**
     * 排除组件依赖
     * @param action 配置
     */
    @CompileStatic(SKIP)
    void excludeModules(Action<GroupSpec<ModulesSpec>> action) {
        actionExecute action, excludeModules, ExcludeSpecImpl::new
    }

    /**
     * 配置组件依赖
     * @param action 配置
     */
    @CompileStatic(SKIP)
    @Deprecated(since = '1.6.0', forRemoval = true)
    void dependencies(Action<DependencySpec> action) {
        actionExecute action, dependencies, DependencySpecImpl::new
    }

    /**
     * 要求能力
     * @param action 配置
     */
    @CompileStatic(SKIP)
    void capabilities(Action<CapabilitySpec> action) {
        actionExecute action, capabilities, CapabilitySpecImpl::new
    }

    @CompileStatic(SKIP)
    private static <A extends ActionSpec<T>, T extends ConfigSpec> void actionExecute(Action<A> action, Set<T> specs,
                                                                                      Supplier<A> getter) {
        A actionSpec = getter.get()
        action.execute actionSpec
        actionSpec.specs*.appendTo specs
    }

    @CompileStatic(SKIP)
    void refreshCommonSpecs() {
        if (!root) {
            IHubBomExtension.declaredFields.findAll { Modifier.isFinal it.modifiers }.name.each {
                refreshCommonSpecs it
            }
        }
    }

    void refreshCommonSpecs(String fieldName) {
        Set<ConfigSpec> specs = getProperty(fieldName) as Set<ConfigSpec>
        setExtProperty rootProject, fieldName, findExtProperty(rootProject, fieldName, specs).with {
            it.findAll { specs.any { s -> compareSpec it, s } }
        }
    }

    void printConfigContent() {
        printConfig 'bomVersions', 'Group Maven Bom Version', 'Group', 'Module', 'Version'
        printConfig 'dependencyVersions', 'Group Maven Module Version', 'Group', 'Module', 'Version'
        printConfig 'groupVersions', 'Group Maven Default Version', 'Group', 'Version'
        printConfig 'excludeModules', 'Exclude Group Modules', 'Group', 'Module'
        printConfig 'dependencies', 'Config Default Dependencies', 'DependencyType', 'Dependencies'
        printConfig 'capabilities', 'Config Default Require Capabilities', 'Dependency', 'Capabilities'
    }

    boolean isRoot() {
        project == project.rootProject
    }

    @CompileStatic(SKIP)
    void printConfig(String fieldName, String title, String... taps) {
        Set<ConfigSpec> specs = this."$fieldName" as Set<ConfigSpec>
        // 获取各项目通用扩展配置
        Set<ConfigSpec> commonSpecs = findExtProperty rootProject, fieldName, []
        List<List<?>> data = []
        if (root) {
            // 公共配置追加主项目配置
            specs*.appendTo commonSpecs
            setExtProperty rootProject, fieldName, commonSpecs
            commonSpecs*.appendToPrintData data
        } else {
            // 子项目过滤掉公共配置
            specs.findAll { commonSpecs.every { s -> !compareSpec(it, s) } }
                *.appendToPrintData commonSpecs, data
        }
        printConfigContent "${project.name.toUpperCase()} $title", data, taps
    }

    @CompileStatic(SKIP)
    static boolean compareSpec(a, b) {
        a.comparedProperties.every { a."$it" == b."$it" }
    }

}
