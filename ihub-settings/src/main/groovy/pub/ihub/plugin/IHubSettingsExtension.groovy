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

import static pub.ihub.plugin.IHubPluginMethods.idTap
import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.IHubPluginMethods.versionTap

import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.initialization.Settings

/**
 * 子项目配置扩展
 * @author liheng
 */
class IHubSettingsExtension implements IHubExtension {

	private static final List<String> EXCLUDE_DIRS = [
		'build', 'src', 'conf', 'libs', 'logs', 'docs', 'classes', 'target', 'out', 'node_modules', 'db', 'gradle',
	]

	private boolean alreadyUsedInclude = false

	final Settings settings

	IHubSettingsExtension(Settings settings) {
		this.settings = settings
	}

	/**
	 * 主项目名称
	 * @return 主项目名称
	 */
	String getProjectName() {
		findProperty 'projectName', settings.rootProject.name
	}

	/**
	 * 添加项目
	 * @param projectPath 项目路径
	 * @param namePrefix 项目名称前缀
	 * @param nameSuffix 项目名称后缀
	 */
	void includeProject(String projectPath, String namePrefix = settings.rootProject.name + '-', String nameSuffix = '') {
		String gradleProjectPath = ":$projectPath"
		String projectName = projectPath.split(':').last()
		if (settings.findProject(gradleProjectPath)) {
			println 'included project -> ' + projectPath
		} else if (projectPath.startsWith('.') || projectName in EXCLUDE_DIRS) {
			println 'exclude project -> ' + projectPath
		} else {
			println 'include project -> ' + projectPath
			settings.include gradleProjectPath
			settings.project(gradleProjectPath).name = namePrefix + projectName + nameSuffix
		}
		alreadyUsedInclude = true
	}

	/**
	 * 添加多个项目
	 * @param projectPaths 项目路径
	 */
	void includeProjects(String... projectPaths) {
		projectPaths.each { includeProject it }
	}

	/**
	 * 添加子项目
	 * @param projectPath 项目路径
	 * @param namePrefix 项目名称前缀
	 * @param nameSuffix 项目名称后缀
	 */
	void includeSubprojects(String projectPath, String namePrefix = projectPath + '-', String nameSuffix = '') {
		new File(settings.rootDir, projectPath).identity {
			includeProject name, ''
			eachDir { dir ->
				includeProject "$name:$dir.name", namePrefix, nameSuffix
			}
		}
	}

	/**
	 * 通过环境配置添加多个项目
	 * @param includeDirs 项目路径，多路径”,“分割
	 */
	void setIncludeDirs(String includeDirs = findProperty('includeDirs')) {
		includeProjects includeDirs?.split(',')
	}

	/**
	 * 跳过某些目录添加其他项目
	 * skippedDirs与include互斥，使用时须首先使用，且只能使用一次
	 * @param skippedDirs 需要跳过路径，多路径”,“分割
	 */
	void setSkippedDirs(String skippedDirs = findProperty('skippedDirs')) {
		if (skippedDirs) {
			if (alreadyUsedInclude) {
				throw new GradleException('You can no longer use \'skippedDirs\' if you use \'include\'!')
			}
			settings.rootDir.eachDir {
				if (!skippedDirs.split(',').contains(it.name)) {
					includeProject it.name
				}
			}
		}
	}

	/**
	 * 配置插件版本
	 * @param action 配置
	 */
	void pluginVersions(Action<PluginVersionsSpec> action) {
		List<PluginVersionSpec> pluginVersions = new PluginVersionsSpec().tap { action.execute it }.specs
		assert pluginVersions, 'plugin versions config not empty!'
		settings.pluginManagement {
			resolutionStrategy {
				eachPlugin {
					pluginVersions.each { spec ->
						if (spec.id == requested.id.toString()) {
							useVersion spec.version
						}
					}
				}
			}
		}
		printConfigContent 'Gradle Plugin Plugins Version', idTap(), versionTap(),
			pluginVersions.collectEntries { [(it.id): it.version] }
	}

	@Override
	String findProjectProperty(String key) {
		settings.hasProperty(key) ? settings."$key" : null
	}

	private class PluginVersionsSpec {

		private final List<PluginVersionSpec> specs = []

		PluginVersionSpec id(String id) {
			new PluginVersionSpec(id).tap {
				specs << it
			}
		}

	}

	private class PluginVersionSpec {

		private final String id
		private String version

		PluginVersionSpec(String id) {
			this.id = id
		}

		PluginVersionSpec version(String version) {
			this.version = version
			this
		}

	}

}
