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

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.TaskProvider
import org.gradle.jvm.tasks.Jar
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin
import pub.ihub.plugin.IHubPluginsExtension
import pub.ihub.plugin.IHubProjectPlugin
import pub.ihub.plugin.java.IHubJavaBasePlugin

import static pub.ihub.plugin.IHubProjectPlugin.EvaluateStage.AFTER



/**
 * 组件发布插件
 * @author liheng
 */
class IHubPublishPlugin extends IHubProjectPlugin<IHubPublishExtension> {

    Class<? extends Plugin<Project>>[] beforeApplyPlugins = [IHubJavaBasePlugin]
    String extensionName = 'iHubPublish'

    @Override
    void apply() {
        IHubPluginsExtension iHubExt = withExtension IHubPluginsExtension

        configPublish project, iHubExt

        configSigning project, iHubExt
    }

    private void configPublish(Project project, IHubPluginsExtension iHubExt) {
        applyPlugin MavenPublishPlugin
        withExtension(PublishingExtension) {
            it.publications {
                create('mavenJava', MavenPublication) {
                    from project.components.getByName('java')

                    // release版本时发布sources以及docs包
                    if (release) {
                        boolean publishDocs = iHubExt.publishDocs
                        List tasks = [
                            this.registerSourcesJar()
                        ]
                        if (publishDocs) {
                            tasks << this.registerJavadocsJar()
                        }
                        if (publishDocs && project.plugins.hasPlugin(GroovyPlugin)) {
                            tasks << this.registerGroovydocJar()
                        }
                        tasks.each {
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
                    this.withExtension(EvaluateStage.AFTER) { ext ->
                        artifactId = project.jar.archiveBaseName.get()
                        ext.configPom it
                    }
                }
            }
            it.repositories {
                maven {
                    url release ? iHubExt.releaseRepoUrl : iHubExt.snapshotRepoUrl
                    iHubExt.repoUsername?.with { repoUsername ->
                        credentials {
                            username repoUsername
                            password iHubExt.repoPassword
                        }
                    }
                }
            }
        }
    }

    private void configSigning(Project project, IHubPluginsExtension iHubExt) {
        project.plugins.apply SigningPlugin
        withExtension(SigningExtension) { ext ->
            ext.required = release && iHubExt.publishNeedSign
            ext.useInMemoryPgpKeys iHubExt.signingKeyId, iHubExt.signingSecretKey, iHubExt.signingPassword
            withExtension(PublishingExtension, AFTER) {
                if (ext.required) {
                    ext.sign it.publications.mavenJava
                }
            }
        }
    }

    private boolean isRelease() {
        project.version ==~ /(\d+\.)+\d+/
    }

    private TaskProvider registerSourcesJar() {
        registerTask('sourcesJar', Jar) {
            it.archiveClassifier.set 'sources'
            it.from it.project.convention.getPlugin(JavaPluginConvention).sourceSets.getByName('main').allSource
        }
    }

    private TaskProvider registerJavadocsJar() {
        registerTask('javadocsJar', Jar) {
            it.archiveClassifier.set 'javadoc'
            Task javadocTask = it.project.tasks.getByName('javadoc').tap {
                if (JavaVersion.current().java9Compatible) {
                    options.addBooleanOption 'html5', true
                }
                options.encoding = 'UTF-8'
            }
            it.dependsOn javadocTask
            it.from javadocTask
        }
    }

    private TaskProvider registerGroovydocJar() {
        registerTask('groovydocJar', Jar) {
            it.archiveClassifier.set 'groovydoc'
            Task groovydocTask = it.project.tasks.getByName 'groovydoc'
            it.dependsOn groovydocTask
            it.from groovydocTask.destinationDir
        }
    }

}
