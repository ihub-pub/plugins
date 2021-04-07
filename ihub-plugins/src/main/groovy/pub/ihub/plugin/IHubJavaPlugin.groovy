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
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.ProjectReportsPlugin
import org.gradle.api.reporting.plugins.BuildDashboardPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.AbstractCompile

import static pub.ihub.plugin.Constants.GRADLE_COMPILATION_INCREMENTAL
import static pub.ihub.plugin.Constants.JAVA_COMPATIBILITY
import static pub.ihub.plugin.PluginUtils.findProperty



/**
 * @author henry
 */
class IHubJavaPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply JavaLibraryPlugin
		project.pluginManager.apply IHubBomPlugin
		project.pluginManager.apply ProjectReportsPlugin
		project.pluginManager.apply BuildDashboardPlugin

		project.configurations {
			if (System.getProperty('java.version').startsWith('11')) {
				maybeCreate('runtimeOnly').getDependencies().addAll([
					'javax.xml.bind:jaxb-api',
					'com.sun.xml.bind:jaxb-core',
					'com.sun.xml.bind:jaxb-impl'
				].collect { project.getDependencies().create(it) })
			}
			def lombok = 'org.projectlombok:lombok'
			maybeCreate('compileOnly').getDependencies().add project.getDependencies().create(lombok)
			maybeCreate('annotationProcessor').getDependencies().add project.getDependencies().create(lombok)
		}

		def javaCompatibility = findProperty project, JAVA_COMPATIBILITY
		if (javaCompatibility) {
			def gradleCompilationIncremental = findProperty(project, GRADLE_COMPILATION_INCREMENTAL, 'true').toBoolean()
			project.tasks.withType(AbstractCompile) {
				sourceCompatibility = javaCompatibility
				targetCompatibility = javaCompatibility
				options.encoding = 'UTF-8'
				options.incremental = gradleCompilationIncremental
			}
		}

		project.tasks.withType(Jar) {
			manifest {
				attributes(
					'Implementation-Title': project.name,
					'Automatic-Module-Name': project.name.replaceAll('-', '.'),
					'Implementation-Version': project.version,
					'Implementation-Vendor': 'IHub',
					'Created-By': 'Java ' + System.getProperty('java.version')
				)
			}
		}

		project.pluginManager.apply IHubVerificationPlugin
	}

}
