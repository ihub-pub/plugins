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

import groovy.transform.TupleConstructor
import org.gradle.api.Action
import org.gradle.api.initialization.Settings

/**
 * 子项目配置扩展
 * @author liheng
 */
class IHubSettingsExtension implements IHubExtension {

	private static final List<String> EXCLUDE_DIRS = [
		'build', 'src', 'conf', 'libs', 'logs', 'docs', 'classes', 'target', 'out', 'node_modules', 'db', 'gradle',
	]

	final Settings settings
	final Map<String, String> pluginVersions = [:]
	final Map<String, ProjectSpec> projectSpecs = [:]
	final String[] skippedDirs = findProperty('skippedDirs')?.split(',')

	IHubSettingsExtension(Settings settings) {
		this.settings = settings
		// 通过项目属性配置子项目
		includeProjects findProperty('includeDirs')?.split(',')
	}

	/**
	 * 主项目名称
	 * @return 主项目名称
	 */
	String getProjectName() {
		findProperty 'projectName', settings.rootProject.name
	}

	/**
	 * 配置插件版本
	 * @param action 配置
	 */
	void pluginVersions(Action<PluginVersionsSpec> action) {
		List<PluginVersionSpec> versions = new PluginVersionsSpec().tap { action.execute it }.specs
		assert versions, 'plugin versions config not empty!'
		versions.each {
			pluginVersions.put it.id, it.version
		}
	}

	/**
	 * 添加多个项目
	 * @param projectPaths 项目路径
	 */
	ProjectSpec includeProjects(String... projectPaths) {
		new ProjectSpec().tap {
			projectPaths.each { projectPath ->
				projectSpecs.put projectPath, it
			}
		}
	}

	@Override
	String findProjectProperty(String key) {
		settings.hasProperty(key) ? settings."$key" : null
	}

	class ProjectSpec {

		String namePrefix = settings.rootProject.name + '-'
		String nameSuffix = ''
		boolean include = true
		ProjectSpec subprojectSpec

		ProjectSpec namePrefix(String namePrefix) {
			this.namePrefix = namePrefix
			this
		}

		ProjectSpec nameSuffix(String nameSuffix) {
			this.nameSuffix = nameSuffix
			this
		}

		ProjectSpec include(boolean include) {
			this.include = include
			this
		}

		ProjectSpec subprojectSpec(boolean include) {
			new ProjectSpec().tap {
				this.subprojectSpec = it
				it.namePrefix = this.namePrefix
				it.nameSuffix = this.nameSuffix
				it.include = include
			}
		}

		String includeProject(String projectPath, File dir) {
			String gradleProjectPath = ":$projectPath"
			String projectName = projectPath.split(':').last()
			if (projectPath.startsWith('.') || projectName in EXCLUDE_DIRS || projectName in skippedDirs ||
				settings.findProject(gradleProjectPath)) {
				return null
			}
			settings.include gradleProjectPath
			settings.project(gradleProjectPath).tap {
				name = namePrefix + projectPath.replaceAll(':', '-') + nameSuffix
				projectDir = dir
			}.name
		}

	}

	class PluginVersionsSpec {

		private final List<PluginVersionSpec> specs = []

		PluginVersionSpec id(String id) {
			new PluginVersionSpec(id).tap {
				specs << it
			}
		}

	}

	@TupleConstructor(includes = 'id')
	class PluginVersionSpec {

		final String id
		String version

		void version(String version) {
			this.version = findVersion id, version
		}

	}

}
