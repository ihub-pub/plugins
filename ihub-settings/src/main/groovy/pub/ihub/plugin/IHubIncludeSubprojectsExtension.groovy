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
 * @author liheng
 */
class IHubIncludeSubprojectsExtension {

	final Settings settings
	String includeDirs
	String skippedDirs

	IHubIncludeSubprojectsExtension(Settings settings) {
		this.settings = settings
		includeDirs = settings.hasProperty('includeDirs') ? settings.includeDirs : null
		skippedDirs = settings.hasProperty('skippedDirs') ? settings.skippedDirs : null
	}

	void include(String projectPath, String namePrefix = projectPath + '-', String nameSuffix = '') {
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

	void include1(String projectPath, String namePrefix = settings.rootProject.name + '-', String nameSuffix = '') {
		def subprojectName = ":$projectPath"
		settings.include subprojectName
		settings.project(subprojectName).name = namePrefix + projectPath + nameSuffix
	}

}
