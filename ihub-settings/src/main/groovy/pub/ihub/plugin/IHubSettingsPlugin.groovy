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


import org.gradle.api.initialization.Settings



/**
 * IHub Gradle Plugins Settings Plugin
 * @author henry
 */
class IHubSettingsPlugin implements IHubPluginAware<Settings> {

	static final Map<String, String> PLUGINS_DEPENDENCY_VERSION_MAPPING = [
		'com.github.ben-manes.versions': '0.38.0',
		'com.palantir.git-version'     : '0.12.3'
	]

	@Override
	void apply(Settings settings) {

		//<editor-fold desc="配置插件仓库以及版本">

		def pluginVersion = PLUGINS_DEPENDENCY_VERSION_MAPPING.collectEntries { id, version ->
			[(id): findProperty(settings, id + '.version', version)]
		} as Map<String, String>
		settings.pluginManagement {
			repositories {
				def dirs = "$settings.rootProject.projectDir/gradle/plugins"
				if ((dirs as File).directory) flatDir dirs: dirs
				[
					'https://maven.aliyun.com/repository/gradle-plugin',
					'https://maven.aliyun.com/repository/spring-plugin',
					'https://repo.spring.io/release'
				].each { repo -> if (!it*.displayName.any { it.contains repo }) maven { url repo } }
				if (!findByName("Gradle Central Plugin Repository")) gradlePluginPortal()
			}

			resolutionStrategy {
				eachPlugin {
					if (requested.id.toString().startsWith(IHubSettingsPlugin.package.name)) {
						useVersion IHubSettingsPlugin.package.implementationVersion
					} else {
						pluginVersion.each { id, version ->
							if (id == requested.id.toString()) useVersion version
						}
					}
				}
			}
		}
		printConfigContent 'Gradle Plugin Repos', settings.pluginManagement.repositories*.displayName
		printConfigContent 'Gradle Plugin Plugins Version', tap('ID'), tap('Version', 30), pluginVersion

		//</editor-fold>

		settings.rootProject.name = findProperty settings, 'projectName', settings.rootProject.name

		//<editor-fold desc="子项目扩展配置">

		def includeDirs = findProperty settings, 'includeDirs'
		def skippedDirs = findProperty settings, 'skippedDirs'
		if (includeDirs) {
			includeDirs.split(',').each {
				settings.include ":$it"
				settings.project(":$it").name = settings.rootProject.name + '-' + it
			}
		} else if (skippedDirs) {
			settings.rootDir.eachDir {
				if (!skippedDirs.split(',').contains(it.name)) {
					settings.include ":$it.name"
					settings.project(":$it.name").name = settings.rootProject.name + '-' + it.name
				}
			}
		}

		settings.extensions.create 'includeSubprojects', IHubIncludeSubprojectsExtension, settings

		//</editor-fold>

	}

}
