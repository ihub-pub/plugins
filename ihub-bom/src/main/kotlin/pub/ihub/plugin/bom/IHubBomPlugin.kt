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

import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.Action
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.plugins.JavaPlatformPlugin
import org.gradle.api.plugins.catalog.VersionCatalogPlugin
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.IHubLibsVersions.IHUB_LIBS_LOCAL_VERSION
import pub.ihub.plugin.IHubLibsVersions.getIHubLibsVersion

/**
 * BOM（Bill of Materials）组件依赖管理
 * @author henry
 */
@IHubPlugin(IHubBomExtension::class)
@Suppress("NestedBlockDepth")
class IHubBomPlugin : IHubProjectPluginAware<IHubBomExtension>() {

    override fun apply() {
        // 如果项目为Java平台组件项目时，不执行插件
        if (hasPlugin(JavaPlatformPlugin::class.java)) {
            logger.trace("Java platform project, skip apply ihub-bom plugin")
            return
        }

        project.gradle.taskGraph.whenReady {
            extension.printConfigContent()
        }

        // 如果项目为版本目录组件项目时，不继续执行插件
        if (hasPlugin(VersionCatalogPlugin::class.java)) {
            logger.trace("Version catalog project, skip apply ihub-bom plugin")
            return
        }

        // 项目不包含dependencies组件时，自动配置ihub-bom
        if (!project.hasProperty("iHubSettings.includeDependencies")) {
            val iHubLibsVersion = project.findProperty(IHUB_LIBS_LOCAL_VERSION)?.toString() ?: getIHubLibsVersion()
            extension.importBoms {
                it.group("pub.ihub.lib").module("ihub-dependencies").version(iHubLibsVersion)
            }
        }

        // 配置项目依赖
        withExtension(EvaluateStage.AFTER) { ext ->
            configVersions(ext)
            configProject(ext)
            ext.refreshCommonSpecs()
        }
    }

    private fun configVersions(ext: IHubBomExtension) {
        // 导入bom配置
        if (ext.bomVersions.isNotEmpty()) {
            dependencyManagement {
                it.imports { extensionImportsHandler ->
                    ext.bomVersions.forEach { bom ->
                        extensionImportsHandler.mavenBom("${bom.id}:${bom.module}:${bom.version}")
                    }
                }
            }
        }

        // 配置组件版本
        if (ext.dependencyVersions.isNotEmpty()) {
            dependencyManagement {
                it.dependencies { extensionDependenciesHandler ->
                    ext.dependencyVersions.forEach { config ->
                        extensionDependenciesHandler.dependencySet(mapOf("group" to config.id, "version" to config.version)) { dependencySetHandler ->
                            config.modules.forEach { moduleName -> dependencySetHandler.entry(moduleName) }
                        }
                    }
                }
            }
        }
    }

    private fun configProject(ext: IHubBomExtension) {
        project.configurations.all { configuration: Configuration ->
            configuration.resolutionStrategy.eachDependency { details ->
                ext.groupVersions.find { it.id == details.requested.group }?.version?.let { version ->
                    details.useVersion(version)
                }
            }
            // 不缓存动态版本
            configuration.resolutionStrategy.cacheDynamicVersionsFor(0, "seconds")
            // 不缓存快照模块
            configuration.resolutionStrategy.cacheChangingModulesFor(0, "seconds")

            // 排除组件依赖
            ext.excludeModules.forEach { excludeRule ->
                if (excludeRule.modules.contains("all")) {
                    configuration.exclude(mapOf("group" to excludeRule.group))
                } else {
                    excludeRule.modules.forEach { moduleName ->
                        configuration.exclude(mapOf("group" to excludeRule.group, "module" to moduleName))
                    }
                }
            }
        }

        // 配置组件需要能力
        ext.capabilities.forEach { spec ->
            project.configurations.getByName("compileClasspath") { configuration ->
                configuration.incoming.beforeResolve {
                    configuration.dependencies.filterIsInstance<ExternalModuleDependency>().find { dep ->
                        val depIdentifier = "${dep.group}:${dep.name}"
                        val simpleName = dep.name
                        spec.dependency.matches(Regex("$depIdentifier|$simpleName|${dep.group}")) // Approximate matching
                    }?.let { matchingDependency ->
                        spec.capabilities.forEach { capabilityModule ->
                            project.dependencies.add(
                                "implementation",
                                "${matchingDependency.group}:${matchingDependency.name}"
                            ) { dependency ->
                                if (dependency is ExternalModuleDependency) {
                                    dependency.capabilities { capabilityHandler ->
                                        val capabilityIdentifier = if (capabilityModule.contains(':')) capabilityModule else "${matchingDependency.group}:$capabilityModule"
                                        capabilityHandler.requireCapability(capabilityIdentifier)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun dependencyManagement(action: Action<DependencyManagementExtension>) {
        project.pluginManager.apply(DependencyManagementPlugin::class.java)
        getExtension(DependencyManagementExtension::class.java)?.let { action.execute(it) }
    }
}
