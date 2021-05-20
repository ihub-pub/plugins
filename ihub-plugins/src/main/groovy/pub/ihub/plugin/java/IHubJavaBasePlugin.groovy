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

import static pub.ihub.plugin.IHubPluginAware.EvaluateStage.AFTER

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.ProjectReportsPlugin
import org.gradle.api.reporting.plugins.BuildDashboardPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.AbstractCompile
import pub.ihub.plugin.IHubPluginAware
import pub.ihub.plugin.IHubPluginsExtension
import pub.ihub.plugin.IHubProjectExtension
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.bom.IHubBomPlugin
import pub.ihub.plugin.groovy.IHubGroovyPlugin

/**
 * Java基础插件
 * @author henry
 */
class IHubJavaBasePlugin implements IHubPluginAware<IHubProjectExtension> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply IHubBomPlugin
		project.pluginManager.apply JavaPlugin
		project.pluginManager.apply JavaLibraryPlugin
		project.pluginManager.apply ProjectReportsPlugin
		project.pluginManager.apply BuildDashboardPlugin

		getExtension(project, IHubPluginsExtension, AFTER) { iHubExt ->
			if (!project.plugins.hasPlugin(IHubJavaPlugin) && !project.plugins.hasPlugin(IHubGroovyPlugin)) {
				return
			}
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
