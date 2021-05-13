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

import org.gradle.api.Project

/**
 * IHub扩展特征
 * @author liheng
 */
trait IHubExtension {

	Project project

	def <T> T findProperty(String key, T defaultValue = null) {
		findProperty(project, key, String.valueOf(defaultValue)) as T
	}

	def <T> T findEnvProperty(String key, T defaultValue = null) {
		findProperty(key, project, String.valueOf(defaultValue)) as T
	}

}
