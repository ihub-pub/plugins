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
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.ProjectReportsPlugin
import org.gradle.api.reporting.plugins.BuildDashboardPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.AbstractCompile

import static pub.ihub.plugin.IHubPluginMethods.findProperty



/**
 * Java插件
 * @author henry
 */
class IHubJavaPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		if (project.plugins.hasPlugin(IHubJavaPlugin)) {
			return
		}
		project.pluginManager.apply IHubPluginsPlugin
		project.pluginManager.apply JavaLibraryPlugin
		project.pluginManager.apply ProjectReportsPlugin
		project.pluginManager.apply BuildDashboardPlugin

		project.configurations {
			// Java11添加jaxb运行时依赖
			if (JavaVersion.current().java11) {
				maybeCreate('runtimeOnly').dependencies.addAll([
					'javax.xml.bind:jaxb-api',
					'com.sun.xml.bind:jaxb-core',
					'com.sun.xml.bind:jaxb-impl'
				].collect { project.dependencies.create it })
			}
			// 添加lombok依赖
			def lombok = 'org.projectlombok:lombok'
			maybeCreate('compileOnly').dependencies << project.dependencies.create(lombok)
			maybeCreate('annotationProcessor').dependencies << project.dependencies.create(lombok)
		}

		// 兼容性配置
		findProperty('javaCompatibility', project)?.with {
			project.tasks.withType(AbstractCompile) {
				sourceCompatibility = it
				targetCompatibility = it
				options.encoding = 'UTF-8'
				options.incremental = findProperty('gradleCompilationIncremental', project, 'true').toBoolean()
			}
		}

		// 配置Jar属性
		project.tasks.withType(Jar) {
			manifest {
				attributes(
					'Implementation-Title': project.name,
					'Automatic-Module-Name': project.name.replaceAll('-', '.'),
					'Implementation-Version': project.version,
					'Implementation-Vendor': 'IHub',
					'Created-By': 'Java ' + JavaVersion.current().majorVersion
				)
			}
		}

		project.pluginManager.apply IHubVerificationPlugin
	}

}
