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
	void apply() {

		//<editor-fold desc="配置插件仓库以及版本">

		def pluginVersion = PLUGINS_DEPENDENCY_VERSION_MAPPING.collectEntries { id, version ->
			[(id): findProperty(id + '.version', version)]
		} as Map<String, String>
		target.pluginManagement {
			repositories {
				def dirs = "$target.rootProject.projectDir/gradle/plugins"
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
		printConfigContent 'Gradle Plugin Repos', target.pluginManagement.repositories*.displayName
		printConfigContent 'Gradle Plugin Plugins Version', tap('ID'), tap('Version', 30), pluginVersion

		//</editor-fold>

		target.rootProject.name = findProperty 'projectName', target.rootProject.name

		//<editor-fold desc="子项目扩展配置">

		def includeDirs = findProperty 'includeDirs'
		def skippedDirs = findProperty 'skippedDirs'
		if (includeDirs) {
			includeDirs.split(',').each {
				target.include ":$it"
				target.project(":$it").name = target.rootProject.name + '-' + it
			}
		} else if (skippedDirs) {
			target.rootDir.eachDir {
				if (!skippedDirs.split(',').contains(it.name)) {
					target.include ":$it.name"
					target.project(":$it.name").name = target.rootProject.name + '-' + it.name
				}
			}
		}

		target.extensions.add('includeSubprojects') {
			String projectPath, String namePrefix = projectPath + '-', String nameSuffix = '' ->
				new File(target.rootDir, projectPath).identity {
					target.include ":$name"
					eachDir { dir ->
						if (!['build', 'src'].contains(dir.name)) {
							def subprojectName = ":$name:$dir.name"
							target.include subprojectName
							target.project(subprojectName).name = namePrefix + dir.name + nameSuffix
						}
					}
				}
		}

		target.extensions.add('includeSubproject') {
			String projectPath, String namePrefix = target.rootProject.name + '-', String nameSuffix = '' ->
				def subprojectName = ":$projectPath"
				target.include subprojectName
				target.project(subprojectName).name = namePrefix + projectPath + nameSuffix
		}

		//</editor-fold>

	}

}
