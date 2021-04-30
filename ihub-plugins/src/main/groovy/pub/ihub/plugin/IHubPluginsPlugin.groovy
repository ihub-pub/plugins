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

import static pub.ihub.plugin.IHubPluginMethods.findProperty
import static pub.ihub.plugin.IHubPluginMethods.printConfigContent

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle基础插件
 * 配置项目组件仓库
 * @author liheng
 */
class IHubPluginsPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply 'com.palantir.git-version'
		String version = findProperty 'version', project, project.version.toString()
		project.version = 'unspecified' != version ? version : project.gitVersion()

		project.repositories {
			String dirs = "$project.rootProject.projectDir/libs"
			if ((dirs as File).directory) {
				flatDir dirs: dirs
			}
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
			String releaseRepoUrl = findProperty project, 'releaseRepoUrl'
			boolean repoAllowInsecureProtocol = findProperty('repoAllowInsecureProtocol', project, 'false').toBoolean()
			String repoIncludeGroup = findProperty 'repoIncludeGroup', project
			String repoIncludeGroupRegex = findProperty 'repoIncludeGroupRegex', project, '.*'
			String repoUsername = findProperty 'repoUsername', project
			String repoPassword = findProperty 'repoPassword', project
			if (releaseRepoUrl) {
				maven {
					name 'ReleaseRepo'
					url releaseRepoUrl
					allowInsecureProtocol repoAllowInsecureProtocol
					mavenContent {
						releasesOnly()
					}
					content {
						if (repoIncludeGroup) {
							includeGroup repoIncludeGroup
						} else {
							includeGroupByRegex repoIncludeGroupRegex
						}
					}
					if (repoUsername && repoPassword) {
						credentials {
							username repoUsername
							password repoPassword
						}
					}
				}
			}
			String snapshotRepoUrl = findProperty project, 'snapshotRepoUrl'
			if (snapshotRepoUrl) {
				maven {
					name 'SnapshotRepo'
					url snapshotRepoUrl
					allowInsecureProtocol repoAllowInsecureProtocol
					mavenContent {
						snapshotsOnly()
					}
					content {
						if (repoIncludeGroup) {
							includeGroup repoIncludeGroup
						} else {
							includeGroupByRegex repoIncludeGroupRegex
						}
					}
					if (repoUsername && repoPassword) {
						credentials {
							username repoUsername
							password repoPassword
						}
					}
				}
			}
			// TODO 临时使用，相关组件会发布到中央仓库
			maven {
				name 'IHubRepo'
				url 'https://maven.pkg.github.com/ihub-pub/*'
				content {
					includeGroupByRegex 'pub\\.ihub\\..*'
				}
				credentials {
					username 'henry.git@outlook.com'
					password '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'
				}
			}
			// 添加自定义仓库
			String customizeRepoUrl = findProperty project, 'customizeRepoUrl'
			if (customizeRepoUrl) {
				maven {
					name 'CustomizeRepo'
					url customizeRepoUrl
				}
			}
			if (!findByName('MavenRepo')) {
				mavenCentral()
			}
		}

		if (project.name == project.rootProject.name) {
			printConfigContent 'Gradle Project Repos', project.repositories*.displayName
		}

		if (!findProperty(project, 'bomDisabled', 'false').toBoolean()) {
			project.pluginManager.apply IHubBomPlugin
		}

		project.subprojects {
			pluginManager.apply IHubPluginsPlugin
		}
	}
}
