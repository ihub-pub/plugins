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
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.javadoc.Groovydoc
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar
import org.gradle.plugins.signing.SigningPlugin

import static pub.ihub.plugin.Constants.RELEASE_DOCS_ENABLED
import static pub.ihub.plugin.Constants.RELEASE_SIGNING_ENABLED
import static pub.ihub.plugin.Constants.RELEASE_SOURCES_ENABLED



/**
 * @author liheng
 */
class IHubPublishPlugin implements IHubPluginAware<Project> {

	@Override
	void apply() {
		if (!target.plugins.hasPlugin(IHubJavaPlugin)) {
			target.pluginManager.apply IHubJavaPlugin
		}
		def jarTasks = getJarTasks target

		target.pluginManager.apply MavenPublishPlugin
		target.extensions.getByType(PublishingExtension).identity {
			publications {
				it.create('mavenJava', MavenPublication) { publication ->
					def pom = new IHubPublishPom(target)

					publication.groupId = pom.groupId ?: target.group
					publication.artifactId = pom.pomArtifactId ?: target.name
					assert publication.artifactId, 'artifactId不能为空！'
					publication.version = target.version as String
					assert 'unspecified' != publication.version, 'version不能为空！'

					publication.from target.components.getByName('java')

					jarTasks.each {
						publication.artifact it
					}

					pom.configPom publication
				}
			}
			repositories {
				// TODO 配置远端仓库
			}
		}

		def releaseSigningEnabled = findProperty(RELEASE_SIGNING_ENABLED, 'false').toBoolean()
		target.plugins.apply SigningPlugin
		target.signing.setRequired releaseSigningEnabled
		target.afterEvaluate {
			if (releaseSigningEnabled) {
				target.signing.sign target.publishing.publications
			}
		}
	}

	private static List<TaskProvider> getJarTasks(Project project) {
		assert project.plugins.hasPlugin(JavaPlugin) || project.plugins.hasPlugin(JavaLibraryPlugin)
		def releaseDocsEnabled = findSystemProperty(RELEASE_DOCS_ENABLED, 'false').toBoolean()
		def releaseSourcesEnabled = findSystemProperty(RELEASE_SOURCES_ENABLED, 'true').toBoolean()
		def tasks = []
		if (releaseSourcesEnabled) {
			tasks << project.tasks.register('sourcesJar', Jar) {
				archiveClassifier.set 'sources'
				from project.convention.getPlugin(JavaPluginConvention).sourceSets.getByName('main').allSource
			}
		}
		if (releaseDocsEnabled) {
			tasks << project.tasks.register('javadocsJar', Jar) {
				archiveClassifier.set 'javadoc'
				def javadocTask = project.tasks.getByName('javadoc') as Javadoc
				dependsOn javadocTask
				from javadocTask
			}
		}
		if (releaseDocsEnabled && project.plugins.hasPlugin(GroovyPlugin)) {
			tasks << project.tasks.register('groovydocJar', Jar) {
				archiveClassifier.set 'groovydoc'
				def groovydocTask = project.tasks.getByName('groovydoc') as Groovydoc
				dependsOn groovydocTask
				from groovydocTask.destinationDir
			}
		}
		tasks
	}

}
