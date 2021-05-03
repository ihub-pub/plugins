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

import static pub.ihub.plugin.Constants.VALUE_FALSE
import static pub.ihub.plugin.IHubGroovyPlugin.registerGroovydocJar
import static pub.ihub.plugin.IHubJavaBasePlugin.registerJavadocsJar
import static pub.ihub.plugin.IHubJavaBasePlugin.registerSourcesJar
import static pub.ihub.plugin.IHubPluginMethods.findProperty

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.os.OperatingSystem
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin

/**
 * 组件发布插件
 * @author liheng
 */
class IHubPublishPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply IHubJavaBasePlugin

		boolean isRelease = project.version ==~ /(\d+\.)+\d+/

		project.pluginManager.apply MavenPublishPlugin
		project.extensions.getByType(PublishingExtension).identity {
			publications {
				create('mavenJava', MavenPublication) {
					from project.components.getByName('java')

					// release版本时发布sources以及docs包
					if (isRelease) {
						registerJarTasks(project).each {
							artifact it
						}
					}

					versionMapping {
						usage('java-api') {
							fromResolutionOf('runtimeClasspath')
						}
						usage('java-runtime') {
							fromResolutionResult()
						}
					}

					it.groupId = project.group
					it.version = project.version
					project.afterEvaluate({ IHubPublishExtension ext ->
						artifactId = project.jar.archiveBaseName.get()
						ext.configPom it, project.versionDetails()
					}.curry(project.extensions.create('iHubPublish', IHubPublishExtension)))
				}
			}
			repositories {
				maven {
					url findProperty(project, isRelease ? 'releaseRepoUrl' : 'snapshotRepoUrl')
					credentials {
						username findProperty('repoUsername', project)
						password findProperty('repoPassword', project)
					}
				}
			}
		}

		project.plugins.apply SigningPlugin
		project.extensions.getByType(SigningExtension).identity {
			required = isRelease && findProperty('publishNeedSign', project, VALUE_FALSE).toBoolean()
			// TODO 签名待调试
			if (OperatingSystem.current().windows) {
				useGpgCmd()
			} else {
				useInMemoryPgpKeys findProperty('signingKeyId', project),
					findProperty('signingSecretKey', project), findProperty('signingPassword', project)
			}
			project.afterEvaluate {
				if (required) {
					sign project.extensions.getByType(PublishingExtension).publications.mavenJava
				}
			}
		}
	}

	private static List<TaskProvider> registerJarTasks(Project project) {
		boolean publishDocs = findProperty('publishDocs', VALUE_FALSE).toBoolean()
		List tasks = [
			registerSourcesJar(project)
		]
		if (publishDocs) {
			tasks << registerJavadocsJar(project)
		}
		if (publishDocs && project.plugins.hasPlugin(GroovyPlugin)) {
			tasks << registerGroovydocJar(project)
		}
		tasks
	}

}
