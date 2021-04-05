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
import org.gradle.api.initialization.Settings

import static pub.ihub.plugin.Constants.INCLUDE_DIRS
import static pub.ihub.plugin.Constants.PLUGINS_DEPENDENCY_VERSION_MAPPING
import static pub.ihub.plugin.Constants.PROJECT_NAME
import static pub.ihub.plugin.Constants.SKIPPED_DIRS
import static pub.ihub.plugin.PluginUtils.findProperty
import static pub.ihub.plugin.PluginUtils.printConfigContent
import static pub.ihub.plugin.PluginUtils.tap



/**
 * IHub Gradle Plugins Settings Plugin
 * @author henry
 */
class IHubSettingsPlugin implements Plugin<Settings> {

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
					if (requested.id.toString().startsWith('pub.ihub.plugin')) {
						useVersion IHubSettingsPlugin.class.getPackage().getImplementationVersion()
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

		settings.rootProject.name = findProperty settings, PROJECT_NAME, settings.rootProject.name

		//<editor-fold desc="子项目扩展配置">

		def includeDirs = findProperty settings, INCLUDE_DIRS
		def skippedDirs = findProperty settings, SKIPPED_DIRS
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

		settings.extensions.add('includeSubprojects') {
			String projectPath, String namePrefix = projectPath + '-', String nameSuffix = '' ->
				new File(settings.rootDir, projectPath).identity {
					settings.include ":$name"
					eachDir { dir ->
						if (!['build', 'src'].contains(dir.name)) {
							def subprojectName = ":$name:$dir.name"
							settings.include subprojectName
							settings.project(subprojectName).name = namePrefix + dir.name + nameSuffix
						}
					}
				}
		}

		settings.extensions.add('includeSubproject') {
			String projectPath, String namePrefix = settings.rootProject.name + '-', String nameSuffix = '' ->
				def subprojectName = ":$projectPath"
				settings.include subprojectName
				settings.project(subprojectName).name = namePrefix + projectPath + nameSuffix
		}

		//</editor-fold>

	}

}
