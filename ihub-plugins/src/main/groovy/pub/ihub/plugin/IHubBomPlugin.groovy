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

import static pub.ihub.plugin.Constants.GROUP_DEFAULT_DEPENDENCIES_MAPPING
import static pub.ihub.plugin.Constants.GROUP_DEPENDENCY_EXCLUDE_MAPPING
import static pub.ihub.plugin.Constants.GROUP_DEPENDENCY_VERSION_CONFIG
import static pub.ihub.plugin.Constants.GROUP_MAVEN_BOM_VERSION_CONFIG
import static pub.ihub.plugin.Constants.GROUP_MAVEN_VERSION_CONFIG
import static pub.ihub.plugin.IHubPluginMethods.dependenciesTap
import static pub.ihub.plugin.IHubPluginMethods.dependencyTypeTap
import static pub.ihub.plugin.IHubPluginMethods.findProperty
import static pub.ihub.plugin.IHubPluginMethods.groupTap
import static pub.ihub.plugin.IHubPluginMethods.moduleTap
import static pub.ihub.plugin.IHubPluginMethods.of
import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.IHubPluginMethods.versionTap

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * BOM（Bill of Materials）组件依赖管理
 * @author henry
 */
class IHubBomPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply IHubPluginsPlugin

		boolean isRoot = project.name == project.rootProject.name
		project.pluginManager.apply 'io.spring.dependency-management'

		project.dependencyManagement {
			// 导入bom配置
			List bomVersion = []
			imports {
				GROUP_MAVEN_BOM_VERSION_CONFIG.each {
					if (it.v1 != project.group) {
						String version = findVersion project, it.v1, it.v3
						if (isRoot || version != it.v3) {
							bomVersion << of(it.v1, it.v2, version)
						}
						mavenBom "$it.v1:$it.v2:$version"
					}
				}
			}
			if (bomVersion) {
				printConfigContent "${project.name.toUpperCase()} Group Maven Bom Version", bomVersion,
					groupTap(30), moduleTap(), versionTap(20)
			}

			// 配置组件版本
			List<Tuple3<String, String, List<String>>> dependenciesVersion = []
			dependencies {
				GROUP_DEPENDENCY_VERSION_CONFIG.each { t3 ->
					String version = findVersion project, t3.v1, t3.v2
					if (isRoot || version != t3.v2) {
						dependenciesVersion << of(t3.v1, version, t3.v3)
					}
					dependencySet(group: t3.v1, version: version) {
						t3.v3.each { entry it }
					}
				}
			}
			if (dependenciesVersion) {
				printConfigContent "${project.name.toUpperCase()} Group Maven Module Version",
					dependenciesVersion.inject([]) { list, config ->
						list + config.v3.collect { [config.v1, it, config.v2] }
					}, groupTap(35), moduleTap(), versionTap(15)
			}
		}

		project.configurations {
			all {
				resolutionStrategy {
					// 配置组件组版本（用于配置无bom组件）
					eachDependency {
						String group = it.requested.group
						String defaultVersion = GROUP_MAVEN_VERSION_CONFIG[group]
						String version = findVersion project, group, defaultVersion
						if (version) {
							it.useVersion version
						}
						if (defaultVersion && defaultVersion != version) {
							println "$project.name group $group use version $version"
						}
					}
					// 不缓存动态版本
					cacheDynamicVersionsFor 0, 'seconds'
					// 不缓存快照模块
					cacheChangingModulesFor 0, 'seconds'
				}
				// 排除组件依赖
				GROUP_DEPENDENCY_EXCLUDE_MAPPING.each { group, modules ->
					modules.each { module -> exclude group: group, module: module }
				}
			}
			if (isRoot) {
				printConfigContent "${project.name.toUpperCase()} Group Maven Default Version",
					groupTap(), versionTap(), GROUP_MAVEN_VERSION_CONFIG
			}
			if (isRoot) {
				printConfigContent "${project.name.toUpperCase()} Exclude Group Modules",
					groupTap(40), moduleTap(), GROUP_DEPENDENCY_EXCLUDE_MAPPING
			}

			// 配置默认依赖
			GROUP_DEFAULT_DEPENDENCIES_MAPPING.each { type, dependencies ->
				maybeCreate(type).dependencies.addAll dependencies.collect { project.dependencies.create it }
			}
			if (isRoot) {
				printConfigContent "${project.name.toUpperCase()} Config Default Dependencies",
					dependencyTypeTap(), dependenciesTap(), GROUP_DEFAULT_DEPENDENCIES_MAPPING
			}
		}

		project.extensions.create 'iHubBom', IHubBomExtension, project
	}

	private static String findVersion(Project project, String group, String defaultVersion) {
		findProperty project, group + '.version', defaultVersion
	}

}
