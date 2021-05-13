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
package pub.ihub.plugin.spring

import org.gradle.api.Project
import pub.ihub.plugin.IHubExtension

/**
 * IHub Spring Boot Plugin Extension
 * @author henry
 */
class IHubBootExtension implements IHubExtension {

	//<editor-fold desc="BootRun Configuration">

	/**
	 * bootRun属性
	 */
	Map<String, String> bootRunProperties = [:]
	/**
	 * 项目本地Spring Boot属性
	 */
	String bootRunLocalPropertiesFile = '.spring-boot-local.properties'
	/**
	 * 包含属性名称（“,”分割）
	 */
	String bootRunIncludePropNames = ''
	/**
	 * 排除属性名称（“,”分割）
	 */
	String bootRunSkippedPropNames = ''

	//</editor-fold>

	//<editor-fold desc="BootJar Configuration">

	String bootJarRequiresUnpack = ''

	//</editor-fold>

	IHubBootExtension(Project project) {
		pub_ihub_plugin_IHubExtension__project = project
	}

	String getBootRunLocalPropertiesFile() {
		findProperty 'springbootLocalPropertiesFile', bootRunLocalPropertiesFile
	}

	String getBootRunIncludePropNames() {
		findProperty 'springbootIncludePropNames', bootRunIncludePropNames
	}

	String getBootRunSkippedPropNames() {
		findProperty 'springbootSkippedPropNames', bootRunSkippedPropNames
	}

	String getBootJarRequiresUnpack() {
		findProperty 'springbootRequiresUnpack', bootJarRequiresUnpack
	}

}
