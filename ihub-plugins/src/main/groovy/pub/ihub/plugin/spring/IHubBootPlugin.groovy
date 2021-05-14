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

import static pub.ihub.plugin.IHubPluginAware.EvaluateStage.AFTER
import static pub.ihub.plugin.IHubPluginMethods.findProperty

import org.gradle.api.Project
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun
import pub.ihub.plugin.IHubPluginAware
import pub.ihub.plugin.java.IHubJavaPlugin

/**
 * IHub Spring Boot Plugin
 * @author henry
 */
class IHubBootPlugin implements IHubPluginAware<IHubBootExtension> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply IHubJavaPlugin
		project.pluginManager.apply SpringBootPlugin

		createExtension(project, 'iHubBoot', IHubBootExtension, AFTER) { ext ->
			project.tasks.getByName('bootRun') { BootRun it ->
				it.systemProperties ext.bootRunProperties
				if (ext.bootRunIncludePropNames) {
					ext.bootRunIncludePropNames.split(',').each { propName ->
						it.systemProperty propName, System.getProperty(propName)
					}
				}
				ext.localProperties.each { k, v ->
					it.systemProperties.putIfAbsent k, v
				}
			}

			project.tasks.getByName('bootJar') { BootJar it ->
				it.requiresUnpack ext.bootJarRequiresUnpack
			}
		}

		project.bootBuildImage {
			builder = 'paketobuildpacks/builder:tiny'
		}
	}

}
