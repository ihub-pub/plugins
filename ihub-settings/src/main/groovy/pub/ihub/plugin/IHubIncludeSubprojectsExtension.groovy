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

import static pub.ihub.plugin.IHubPluginMethods.findProperty



/**
 * 子项目配置扩展
 * @author liheng
 */
class IHubIncludeSubprojectsExtension {

	final Settings settings

	IHubIncludeSubprojectsExtension(Settings settings) {
		this.settings = settings
		includeDirs = findProperty settings, 'includeDirs'
		skippedDirs = findProperty settings, 'skippedDirs'
	}

	/**
	 * 添加项目
	 * @param projectPath 项目路径
	 * @param namePrefix 项目名称前缀
	 * @param nameSuffix 项目名称后缀
	 */
	void include(String projectPath, String namePrefix = settings.rootProject.name + '-', String nameSuffix = '') {
		println 'include project -> ' + projectPath
		projectPath = projectPath.with { startsWith(':') ? it : ":$it" }
		settings.include projectPath
		settings.project(projectPath).name = namePrefix + projectPath.split(':').last() + nameSuffix
	}

	/**
	 * 添加多个项目
	 * @param projectPaths 项目路径
	 */
	void includes(String... projectPaths) {
		projectPaths.each { include it }
	}

	/**
	 * 添加子项目
	 * @param projectPath 项目路径
	 * @param namePrefix 项目名称前缀
	 * @param nameSuffix 项目名称后缀
	 */
	void includeSubprojects(String projectPath, String namePrefix = projectPath + '-', String nameSuffix = '') {
		new File(settings.rootDir, projectPath).identity {
			include name, ''
			eachDir { dir ->
				if (!['build', 'src'].contains(dir.name)) {
					include "$name:$dir.name", namePrefix, nameSuffix
				}
			}
		}
	}

	/**
	 * 通过环境配置添加多个项目
	 * @param includeDirs 项目路径，多路径”,“分割
	 */
	void setIncludeDirs(String includeDirs) {
		includes includeDirs?.split(',')
	}

	/**
	 * 跳过某些目录添加其他项目
	 * @param skippedDirs 需要跳过路径，多路径”,“分割
	 */
	void setSkippedDirs(String skippedDirs) {
		if (skippedDirs) {
			settings.rootDir.eachDir {
				if (!skippedDirs.split(',').contains(it.name)) {
					include it.name
				}
			}
		}
	}

}
