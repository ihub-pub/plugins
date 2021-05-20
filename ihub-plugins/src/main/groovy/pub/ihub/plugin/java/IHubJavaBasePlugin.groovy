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
package pub.ihub.plugin.java

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.plugins.ProjectReportsPlugin
import org.gradle.api.reporting.plugins.BuildDashboardPlugin
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.AbstractCompile
import pub.ihub.plugin.IHubPluginsExtension
import pub.ihub.plugin.IHubProjectExtension
import pub.ihub.plugin.IHubPluginsPlugin
import pub.ihub.plugin.IHubPluginAware
import pub.ihub.plugin.bom.IHubBomExtension

/**
 * Java基础插件
 * @author henry
 */
class IHubJavaBasePlugin implements IHubPluginAware<IHubProjectExtension> {

	static TaskProvider registerSourcesJar(Project project) {
		project.tasks.register('sourcesJar', org.gradle.jvm.tasks.Jar) {
			archiveClassifier.set 'sources'
			from project.convention.getPlugin(JavaPluginConvention).sourceSets.getByName('main').allSource
		}
	}

	static TaskProvider registerJavadocsJar(Project project) {
		project.tasks.register('javadocsJar', org.gradle.jvm.tasks.Jar) {
			archiveClassifier.set 'javadoc'
			Task javadocTask = project.tasks.getByName('javadoc').tap {
				if (JavaVersion.current().java9Compatible) {
					options.addBooleanOption 'html5', true
				}
				options.encoding = 'UTF-8'
			}
			dependsOn javadocTask
			from javadocTask
		}
	}

	@Override
	void apply(Project project) {
		project.pluginManager.apply IHubPluginsPlugin
		project.pluginManager.apply JavaPlugin
		project.pluginManager.apply JavaLibraryPlugin
		project.pluginManager.apply ProjectReportsPlugin
		project.pluginManager.apply BuildDashboardPlugin

		IHubPluginsExtension iHubExt = getExtension project, IHubPluginsExtension

		// 兼容性配置
		iHubExt.javaCompatibility?.with { version ->
			project.tasks.withType(AbstractCompile) {
				sourceCompatibility = version
				targetCompatibility = version
				options.encoding = 'UTF-8'
				options.incremental = iHubExt.gradleCompilationIncremental
			}
		}

		// Java11添加jaxb运行时依赖
		if (JavaVersion.current().java11) {
			getExtension(project, IHubBomExtension) {
				it.excludeModules {
					group 'com.sun.xml.bind' modules 'jaxb-core'
				}
				it.dependencies {
					runtimeOnly 'javax.xml.bind:jaxb-api', 'org.glassfish.jaxb:jaxb-runtime'
				}
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
	}

}
