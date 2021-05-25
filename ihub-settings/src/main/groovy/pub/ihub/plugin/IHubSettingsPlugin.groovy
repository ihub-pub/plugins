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
import static pub.ihub.plugin.IHubPluginMethods.tap
import static pub.ihub.plugin.IHubPluginMethods.versionTap

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

/**
 * Gradle配置插件
 * @author henry
 */
class IHubSettingsPlugin implements Plugin<Settings> {

	@Override
	void apply(Settings settings) {
		// 配置插件仓库
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

			plugins {
				id IHubSettingsPlugin.package.name version IHubSettingsPlugin.package.implementationVersion
			}
		}

		// 扩展配置
		IHubSettingsExtension ext = settings.extensions.create 'iHubSettings', IHubSettingsExtension, settings

		// 配置常用插件版本
		ext.pluginVersions {
			id 'com.palantir.git-version' version '0.12.3'
			id 'io.spring.dependency-management' version '1.0.11.RELEASE'
			id 'org.springframework.boot' version '2.4.5'
			id 'org.springframework.experimental.aot' version '0.9.2'
			id 'com.gradle.plugin-publish' version '0.14.0'
			id 'com.github.ben-manes.versions' version '0.38.0'
		}

		// 配置主项目名称
		settings.rootProject.name = ext.projectName

		settings.gradle.settingsEvaluated {
			settings.pluginManagement {
				resolutionStrategy {
					eachPlugin {
						ext.pluginVersions.each { id, version ->
							if (id == requested.id.toString()) {
								useVersion version
							}
						}
					}
				}
			}
			printConfigContent 'Gradle Plugin Plugins Version', idTap(), versionTap(), ext.pluginVersions

			Map<String, List<String>> projectSpecs = [:]
			settings.rootDir.eachDir { dir ->
				String path = dir.name
				ext.getProjectSpec(path)?.with { spec ->
					List<String> names = [spec.includeProject(path)]
					spec.subprojectSpec?.with { subSpec ->
						dir.eachDir { subDir ->
							names << subSpec.includeProject("$path:$subDir.name")
						}
					}
					projectSpecs.put path, names - null
				}
			}
			printConfigContent 'Include Gradle Projects', tap('Path', 35), tap('Projects'), projectSpecs
		}
	}

}
