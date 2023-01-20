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

import groovy.json.JsonSlurper
import groovy.transform.Memoized
import io.freefair.gradle.plugins.github.GithubPomPlugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlatformExtension
import org.gradle.api.plugins.JavaPlatformPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.TaskProvider
import org.gradle.jvm.tasks.Jar
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubPluginsExtension
import pub.ihub.plugin.IHubPluginsPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.bom.IHubBomPlugin

import static cn.hutool.http.HttpUtil.get
import static io.freefair.gradle.util.GitUtil.githubActions
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER



/**
 * 组件发布插件
 * @author liheng
 */
@IHubPlugin(value = IHubPublishExtension, beforeApplyPlugins = [IHubBomPlugin, IHubPluginsPlugin, MavenPublishPlugin])
class IHubPublishPlugin extends IHubProjectPluginAware<IHubPublishExtension> {

    @Override
    void apply() {
        // 引入GithubPom插件
        if (githubActions) {
            applyPlugin GithubPomPlugin
            afterEvaluate {
                withExtension(PublishingExtension) {
                    it.publications.withType MavenPublication, this::configurePomDevelopers
                }
            }
        }

        IHubPluginsExtension iHubExt = withExtension IHubPluginsExtension

        configPublish project, iHubExt

        configSigning project, extension

        // 添加配置元信息
        if (hasPlugin(JavaPlugin)) {
            withExtension(IHubBomExtension) {
                it.dependencies {
                    annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
                }
                project.compileJava.inputs.files project.processResources
            }
        }

        // 配置项目bom组件
        extension.findProjectProperty('iHubSettings.includeBom')?.with { includeBom ->
            Project bom = project.rootProject.findProject(includeBom.toString())
            if (!bom.plugins.hasPlugin(JavaPlatformPlugin)) {
                bom.with {
                    pluginManager.apply JavaPlatformPlugin
                    pluginManager.apply IHubPublishPlugin
                    extensions.getByType(JavaPlatformExtension).allowDependencies()
                    extensions.getByType(IHubPluginsExtension).autoReplaceLaterVersions = false
                }
                afterEvaluate {
                    configDependencies bom
                }
            }
        }
    }

    private void configPublish(Project project, IHubPluginsExtension iHubExt) {
        boolean publishDocs = extension.publishDocs
        withExtension(PublishingExtension) {
            it.publications {
                create('mavenJava', MavenPublication) {
                    if (hasPlugin(org.gradle.api.plugins.JavaPlatformPlugin)) {
                        from project.components.getByName('javaPlatform')
                        return
                    }
                    from project.components.getByName('java')

                    // release版本时发布sources以及docs包
                    if (release) {
                        List tasks = [
                            this.registerSourcesJar()
                        ]
                        if (publishDocs) {
                            tasks << this.registerJavadocsJar()
                        }
                        if (publishDocs && hasPlugin(GroovyPlugin)) {
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
                }
            }
            it.repositories {
                maven {
                    url release ? iHubExt.releaseRepoUrl : iHubExt.snapshotRepoUrl
                    allowInsecureProtocol iHubExt.repoAllowInsecureProtocol
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

    private void configurePomDevelopers(MavenPublication mavenPublication) {
        mavenPublication.pom.developers {
            contributors(project.github.slug.get()).each { user ->
                developer {
                    id.set user.id
                    name.set user.name
                    email.set user.email
                    url.set user.url
                }
            }
        }
    }

    @Memoized
    private static List<Map<String, String>> contributors(String slug) {
        JsonSlurper jsonSlurper = new JsonSlurper()
        jsonSlurper.parseText(get("https://api.github.com/repos/$slug/contributors")).findAll {
            'User' == it.type
        }.collect {
            jsonSlurper.parseText(get(it.url)).with {
                [
                    id   : login,
                    name : name,
                    email: email,
                    url  : html_url
                ]
            }
        }
    }

    private void configSigning(Project project, IHubPublishExtension extension) {
        project.plugins.apply SigningPlugin
        withExtension(SigningExtension) { ext ->
            ext.required = release && extension.publishNeedSign
            ext.useInMemoryPgpKeys extension.signingKeyId, extension.signingSecretKey, extension.signingPassword
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
            it.from withExtension(JavaPluginExtension).sourceSets.getByName('main').allSource
        }
    }

    private TaskProvider registerJavadocsJar() {
        registerTask('javadocsJar', Jar) {
            it.archiveClassifier.set 'javadoc'
            Task javadocTask = it.project.tasks.getByName('javadoc').tap {
                options.addBooleanOption 'html5', true
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

    private void configDependencies(Project bom) {
        bom.dependencies {
            constraints {
                bom.rootProject.allprojects.each {
                    if (it.plugins.hasPlugin(MavenPublishPlugin) && !it.plugins.hasPlugin(JavaPlatformPlugin)) {
                        api "${bom.rootProject.group}:$it.name:${bom.rootProject.version}"
                    }
                }
            }
        }
    }

}
