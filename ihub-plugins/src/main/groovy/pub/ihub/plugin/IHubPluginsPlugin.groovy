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

import org.gradle.api.Plugin
import org.gradle.api.Project

import static pub.ihub.plugin.Constants.GROUP_DEFAULT_DEPENDENCIES_MAPPING
import static pub.ihub.plugin.Constants.GROUP_DEPENDENCY_EXCLUDE_MAPPING
import static pub.ihub.plugin.Constants.GROUP_DEPENDENCY_VERSION_CONFIG
import static pub.ihub.plugin.Constants.GROUP_MAVEN_BOM_VERSION_CONFIG
import static pub.ihub.plugin.Constants.GROUP_MAVEN_VERSION_CONFIG
import static pub.ihub.plugin.IHubPluginMethods.findProperty
import static pub.ihub.plugin.IHubPluginMethods.of
import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.IHubPluginMethods.tap



/**
 * Gradle基础插件
 * @author liheng
 */
class IHubPluginsPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.version = findProperty 'version', project, project.version.toString()
		boolean isRoot = project.name == project.rootProject.name
		configRepositories project
		if (isRoot) {
			printConfigContent 'Gradle Project Repos', project.repositories*.displayName
		}
		configDependencyManagement project, isRoot
		project.subprojects {
			pluginManager.apply IHubPluginsPlugin
		}
	}

	/**
	 * 配置项目组件仓库
	 * @param project 项目
	 */
	private void configRepositories(Project project) {
		project.repositories {
			def dirs = "$project.rootProject.projectDir/libs"
			if ((dirs as File).directory) flatDir dirs: dirs
			if (findProperty('mavenLocalEnabled', project, 'false').toBoolean()) {
				mavenLocal()
			}
			maven {
				name 'AliYunPublic'
				url 'https://maven.aliyun.com/repository/public'
			}
			maven {
				name 'AliYunGoogle'
				url 'https://maven.aliyun.com/repository/google'
				artifactUrls 'https://maven.google.com'
			}
			maven {
				name 'AliYunSpring'
				url 'https://maven.aliyun.com/repository/spring'
				artifactUrls 'https://repo.spring.io/release'
			}
			maven {
				name 'SpringRelease'
				url 'https://repo.spring.io/release'
			}
			// 添加私有仓库
			def releaseRepoUrl = findProperty project, 'releaseRepoUrl'
			if (releaseRepoUrl) {
				maven {
					name 'ReleaseRepo'
					url releaseRepoUrl
					credentials {
						username findProperty('repoUsername', project)
						password findProperty('repoPassword', project)
					}
				}
			}
			def snapshotRepoUrl = findProperty project, 'snapshotRepoUrl'
			if (snapshotRepoUrl) {
				maven {
					name 'SnapshotRepo'
					url snapshotRepoUrl
					credentials {
						username findProperty('repoUsername', project)
						password findProperty('repoPassword', project)
					}
				}
			}
			// 添加自定义仓库
			maven {
				name 'CustomizeRepo'
				url findProperty(project, 'customizeRepoUrl', 'https://maven.pkg.github.com/ihub-pub/*')
			}
			if (!findByName("MavenRepo")) mavenCentral()
		}
	}

	/**
	 * 配置组件依赖管理
	 * @param project 项目
	 * @param isRoot 是否主项目
	 */
	private void configDependencyManagement(Project project, boolean isRoot) {
		project.pluginManager.apply 'io.spring.dependency-management'

		project.dependencyManagement {
			// 导入bom配置
			def bomVersion = []
			imports {
				GROUP_MAVEN_BOM_VERSION_CONFIG.each {
					def version = findVersion project, it.v1, it.v3
					if (isRoot || version != it.v3) bomVersion << of(it.v1, it.v2, version)
					mavenBom "$it.v1:$it.v2:$version"
				}
			}
			if (bomVersion) printConfigContent "${project.name.toUpperCase()} Group Maven Bom Version", bomVersion,
				tap('Group', 35), tap('Module'), tap('Version', 15)

			// 配置组件版本
			def dependenciesVersion = []
			dependencies {
				GROUP_DEPENDENCY_VERSION_CONFIG.each { t3 ->
					def version = findVersion project, t3.v1, t3.v2
					if (isRoot || version != t3.v2) dependenciesVersion << of(t3.v1, version, t3.v3)
					dependencySet(group: t3.v1, version: version) {
						t3.v3.each { entry it }
					}
				}
			}
			if (dependenciesVersion) printConfigContent "${project.name.toUpperCase()} Group Maven Module Version",
				dependenciesVersion.inject([]) { list, config ->
					def (group, version, modules) = config
					list + modules.collect { [group, it, version] }
				}, tap('Group', 35), tap('Module'), tap('Version', 15)
		}

		project.configurations {
			all {
				resolutionStrategy {
					// 配置组件组版本（用于配置无bom组件）
					eachDependency {
						String group = it.requested.group
						def defaultVersion = GROUP_MAVEN_VERSION_CONFIG[group]
						if (group != project.group && defaultVersion) {
							def version = findVersion project, group, defaultVersion
							if (version != defaultVersion) println "$project.name group $group use version $version"
							it.useVersion version
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
			if (isRoot) printConfigContent "${project.name.toUpperCase()} Group Maven Default Version",
				tap('Group'), tap('Version', 30), GROUP_MAVEN_VERSION_CONFIG
			if (isRoot) printConfigContent "${project.name.toUpperCase()} Exclude Group Modules",
				tap('Group', 40), tap('Modules'), GROUP_DEPENDENCY_EXCLUDE_MAPPING

			// 配置默认依赖
			GROUP_DEFAULT_DEPENDENCIES_MAPPING.each { type, dependencies ->
				maybeCreate(type).dependencies.addAll dependencies.collect { project.dependencies.create it }
			}
			if (isRoot) printConfigContent "${project.name.toUpperCase()} Config Default Dependencies",
				tap('DependencyType', 30), tap('Dependencies'), GROUP_DEFAULT_DEPENDENCIES_MAPPING
		}
	}

	private static String findVersion(Project project, String group, String defaultVersion) {
		findProperty project, group + '.version', defaultVersion
	}

}
