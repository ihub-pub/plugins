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
package pub.ihub.plugin.groovy

import static pub.ihub.plugin.groovy.IHubGroovyExtension.ModulesType.ALL
import static pub.ihub.plugin.groovy.IHubGroovyExtension.ModulesType.BASE
import static pub.ihub.plugin.groovy.IHubGroovyExtension.ModulesType.EXTENSION

import org.gradle.api.Project
import pub.ihub.plugin.IHubExtension

/**
 * Groovy插件扩展
 * @author liheng
 */
class IHubGroovyExtension implements IHubExtension {

	final Project project

	static final List<String> BASE_MODULES = [
		'groovy',
		'groovy-astbuilder',
		'groovy-datetime',
		'groovy-dateutil',
		'groovy-groovydoc',
		'groovy-json',
		'groovy-nio',
		'groovy-sql',
		'groovy-templates',
		'groovy-xml',
	]

	static final List<String> EXTENSION_MODULES = BASE_MODULES + [
		'groovy-ant',
		'groovy-console',
		'groovy-cli-picocli',
		'groovy-docgenerator',
		'groovy-groovysh',
		'groovy-jmx',
		'groovy-jsr223',
		'groovy-macro',
		'groovy-servlet',
		'groovy-swing',
		'groovy-test',
		'groovy-test-junit5',
		'groovy-testng',
	]

	ModulesType modulesType = BASE
	List<String> modules = []

	IHubGroovyExtension(Project project) {
		this.project = project
	}

	List<String> getModules() {
		switch (modulesType) {
			case ALL: return ['groovy-all']
			case EXTENSION: return EXTENSION_MODULES + modules
			case BASE: return BASE_MODULES + modules
		}
	}

	enum ModulesType {

		ALL, BASE, EXTENSION

	}

}
