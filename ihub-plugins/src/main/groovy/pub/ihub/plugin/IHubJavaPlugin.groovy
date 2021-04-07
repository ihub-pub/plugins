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

import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.ProjectReportsPlugin
import org.gradle.api.reporting.plugins.BuildDashboardPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.AbstractCompile

import static pub.ihub.plugin.Constants.GRADLE_COMPILATION_INCREMENTAL
import static pub.ihub.plugin.Constants.JAVA_COMPATIBILITY



/**
 * @author henry
 */
class IHubJavaPlugin implements IHubPluginAware<Project> {

	@Override
	void apply() {
		target.pluginManager.apply JavaLibraryPlugin
		target.pluginManager.apply IHubBomPlugin
		target.pluginManager.apply ProjectReportsPlugin
		target.pluginManager.apply BuildDashboardPlugin

		target.configurations {
			if (System.getProperty('java.version').startsWith('11')) {
				maybeCreate('runtimeOnly').getDependencies().addAll([
					'javax.xml.bind:jaxb-api',
					'com.sun.xml.bind:jaxb-core',
					'com.sun.xml.bind:jaxb-impl'
				].collect { target.getDependencies().create(it) })
			}
			def lombok = 'org.projectlombok:lombok'
			maybeCreate('compileOnly').getDependencies().add target.getDependencies().create(lombok)
			maybeCreate('annotationProcessor').getDependencies().add target.getDependencies().create(lombok)
		}

		def javaCompatibility = findProperty JAVA_COMPATIBILITY
		if (javaCompatibility) {
			def gradleCompilationIncremental = findProperty(GRADLE_COMPILATION_INCREMENTAL, 'true').toBoolean()
			target.tasks.withType(AbstractCompile) {
				sourceCompatibility = javaCompatibility
				targetCompatibility = javaCompatibility
				options.encoding = 'UTF-8'
				options.incremental = gradleCompilationIncremental
			}
		}

		target.tasks.withType(Jar) {
			manifest {
				attributes(
					'Implementation-Title': target.name,
					'Automatic-Module-Name': target.name.replaceAll('-', '.'),
					'Implementation-Version': target.version,
					'Implementation-Vendor': 'IHub',
					'Created-By': 'Java ' + System.getProperty('java.version')
				)
			}
		}

		target.pluginManager.apply IHubVerificationPlugin
	}

}
