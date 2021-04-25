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

import static pub.ihub.plugin.Constants.PLUGINS_DEPENDENCY_VERSION_MAPPING
import static pub.ihub.plugin.IHubPluginMethods.findProperty
import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.IHubPluginMethods.tap

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

/**
 * Gradle配置插件
 * @author henry
 */
class IHubSettingsPlugin implements Plugin<Settings> {

	@Override
	void apply(Settings settings) {
		// 配置插件仓库以及解析策略
		Map pluginVersion = PLUGINS_DEPENDENCY_VERSION_MAPPING.collectEntries { id, version ->
			[(id): findProperty(settings, id + '.version', version)]
		} as Map<String, String>
		settings.pluginManagement {
			repositories {
				String dirs = "$settings.rootProject.projectDir/gradle/plugins"
				if ((dirs as File).directory) {
					flatDir dirs: dirs
				}
				maven {
					name 'AliYunGradlePlugin'
					url 'https://maven.aliyun.com/repository/gradle-plugin'
				}
				maven {
					name 'AliYunSpringPlugin'
					url 'https://maven.aliyun.com/repository/spring-plugin'
				}
				maven {
					name 'SpringRelease'
					url 'https://repo.spring.io/release'
				}
				if (!findByName('Gradle Central Plugin Repository')) {
					gradlePluginPortal()
				}
			}
			printConfigContent 'Gradle Plugin Repos', settings.pluginManagement.repositories*.displayName

			resolutionStrategy {
				eachPlugin {
					pluginVersion.each { id, version ->
						if (id == requested.id.toString()) {
							useVersion version
						}
					}
				}
			}
			printConfigContent 'Gradle Plugin Plugins Version', tap('ID'), tap('Version', 30), pluginVersion

			plugins {
				id IHubSettingsPlugin.package.name version IHubSettingsPlugin.package.implementationVersion
			}
		}

		// 配置主项目名称
		settings.rootProject.name = findProperty settings, 'projectName', settings.rootProject.name

		// 配置子项目
		settings.extensions.create 'iHubInclude', IHubIncludeSubprojectsExtension, settings
	}

}
