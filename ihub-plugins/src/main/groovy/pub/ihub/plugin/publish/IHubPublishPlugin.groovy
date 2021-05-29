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
import pub.ihub.plugin.IHubPluginAware
import pub.ihub.plugin.IHubPluginsExtension
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.java.IHubJavaBasePlugin

import static pub.ihub.plugin.IHubPluginAware.EvaluateStage.AFTER
import static pub.ihub.plugin.IHubPluginAware.EvaluateStage.BEFORE



/**
 * 组件发布插件
 * @author liheng
 */
class IHubPublishPlugin implements IHubPluginAware<IHubPublishExtension> {

    private static TaskProvider registerSourcesJar(Project project) {
        project.tasks.register('sourcesJar', Jar) {
            archiveClassifier.set 'sources'
            from project.convention.getPlugin(JavaPluginConvention).sourceSets.getByName('main').allSource
        }
    }

    private static TaskProvider registerJavadocsJar(Project project) {
        project.tasks.register('javadocsJar', Jar) {
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

    private static TaskProvider registerGroovydocJar(Project project) {
        project.tasks.register('groovydocJar', Jar) {
            archiveClassifier.set 'groovydoc'
            Task groovydocTask = project.tasks.getByName('groovydoc').tap {
                if (JavaVersion.current().java9Compatible) {
                    options.addBooleanOption 'html5', true
                }
                options.encoding = 'UTF-8'
            }
            dependsOn groovydocTask
            from groovydocTask.destinationDir
        }
    }

    @Override
    void apply(Project project) {
        project.pluginManager.apply IHubJavaBasePlugin

        IHubPluginsExtension iHubExt = getExtension project, IHubPluginsExtension

        configPublish project, iHubExt

        configSigning project, iHubExt

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

    private void configPublish(Project project, IHubPluginsExtension iHubExt) {
        project.pluginManager.apply MavenPublishPlugin
        getExtension(project, PublishingExtension).identity {
            publications {
                create('mavenJava', MavenPublication) {
                    from project.components.getByName('java')

                    // release版本时发布sources以及docs包
                    if (iHubExt.release) {
                        boolean publishDocs = iHubExt.publishDocs
                        List tasks = [
                            registerSourcesJar(project)
                        ]
                        if (publishDocs) {
                            tasks << registerJavadocsJar(project)
                        }
                        if (publishDocs && project.plugins.hasPlugin(GroovyPlugin)) {
                            tasks << registerGroovydocJar(project)
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
                    createExtension(project, 'iHubPublish', IHubPublishExtension, EvaluateStage.AFTER) { ext ->
                        artifactId = project.jar.archiveBaseName.get()
                        ext.configPom it, project.versionDetails()
                    }
                }
            }
            String repoUsername = iHubExt.repoUsername
            String repoPassword = iHubExt.repoPassword
            repositories {
                maven {
                    url iHubExt.release ? iHubExt.releaseRepoUrl : iHubExt.snapshotRepoUrl
                    if (repoUsername && repoPassword) {
                        credentials {
                            username repoUsername
                            password repoPassword
                        }
                    }
                }
            }
        }
    }

    private void configSigning(Project project, IHubPluginsExtension iHubExt) {
        project.plugins.apply SigningPlugin
        getExtension(project, SigningExtension).identity {
            required = iHubExt.release && iHubExt.publishNeedSign
            useInMemoryPgpKeys iHubExt.signingKeyId, iHubExt.signingSecretKey, iHubExt.signingPassword
            getExtension(project, PublishingExtension, AFTER) {
                if (required) {
                    sign it.publications.mavenJava
                }
            }
        }
    }

}
