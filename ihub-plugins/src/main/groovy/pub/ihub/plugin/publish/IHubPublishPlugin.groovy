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
package pub.ihub.plugin.publish

import static pub.ihub.plugin.IHubPluginAware.EvaluateStage.AFTER
import static pub.ihub.plugin.IHubPluginAware.EvaluateStage.BEFORE
import static pub.ihub.plugin.IHubPluginMethods.findProperty
import static pub.ihub.plugin.groovy.IHubGroovyPlugin.registerGroovydocJar
import static pub.ihub.plugin.java.IHubJavaBasePlugin.registerJavadocsJar
import static pub.ihub.plugin.java.IHubJavaBasePlugin.registerSourcesJar

import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.os.OperatingSystem
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin
import pub.ihub.plugin.IHubPluginAware
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.java.IHubJavaBasePlugin

/**
 * 组件发布插件
 * @author liheng
 */
class IHubPublishPlugin implements IHubPluginAware<IHubPublishExtension> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply IHubJavaBasePlugin

		boolean isRelease = project.version ==~ /(\d+\.)+\d+/

		project.pluginManager.apply MavenPublishPlugin
		getExtension(project, PublishingExtension).identity {
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
					createExtension(project, 'iHubPublish', IHubPublishExtension, EvaluateStage.AFTER) { ext ->
						artifactId = project.jar.archiveBaseName.get()
						ext.configPom it, project.versionDetails()
					}
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
		getExtension(project, SigningExtension).identity {
			required = isRelease && findProperty('publishNeedSign', project, false.toString()).toBoolean()
			// TODO 签名待调试
			if (OperatingSystem.current().windows) {
				useGpgCmd()
			} else {
				useInMemoryPgpKeys findProperty('signingKeyId', project),
					findProperty('signingSecretKey', project), findProperty('signingPassword', project)
			}
			getExtension(project, PublishingExtension, AFTER) {
				if (required) {
					sign it.publications.mavenJava
				}
			}
		}

		// 添加配置元信息
		getExtension(project, IHubBomExtension, BEFORE) {
			if (it.enabledDefaultConfig) {
				it.dependencies {
					annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
				}
			}
		}
		project.compileJava.inputs.files project.processResources
	}

	private static List<TaskProvider> registerJarTasks(Project project) {
		boolean publishDocs = findProperty('publishDocs', false.toString()).toBoolean()
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
