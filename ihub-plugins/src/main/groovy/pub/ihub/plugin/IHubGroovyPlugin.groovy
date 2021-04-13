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

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.tasks.TaskProvider
import org.gradle.jvm.tasks.Jar

import static pub.ihub.plugin.Constants.GROOVY_VERSION
import static pub.ihub.plugin.IHubPluginMethods.findProperty



/**
 * Groovy插件
 * @author liheng
 */
class IHubGroovyPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply IHubJavaBasePlugin
		project.pluginManager.apply GroovyPlugin

		project.configurations {
			maybeCreate('compileOnly').dependencies << project.dependencies
				.create("org.codehaus.groovy:groovy-all:${findProperty(project, 'org.codehaus.groovy.version', GROOVY_VERSION)}")
			if (project.name == project.rootProject.name || !project.rootProject.plugins.hasPlugin(GroovyPlugin)) {
				println project.name + ' dependency groovy-all (only compile)'
			}
		}

		def ext = project.extensions.create 'iHubGroovy', IHubGroovyExtension
		project.afterEvaluate {
			project.configurations {
				maybeCreate('implementation').dependencies.addAll(ext.modules.unique()
					.collect { project.dependencies.create "org.codehaus.groovy:$it" })
			}
		}

		project.pluginManager.apply IHubVerificationPlugin
	}

	static TaskProvider registerGroovydocJar(Project project) {
		project.tasks.register('groovydocJar', Jar) {
			archiveClassifier.set 'groovydoc'
			def groovydocTask = project.tasks.getByName('groovydoc').tap {
				if (JavaVersion.current().java9Compatible) {
					options.addBooleanOption 'html5', true
				}
				options.encoding = 'UTF-8'
			}
			dependsOn groovydocTask
			from groovydocTask.destinationDir
		}
	}

}
