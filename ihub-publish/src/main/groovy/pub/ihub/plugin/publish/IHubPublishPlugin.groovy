/*
 * Copyright (c) 2021-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlatformPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.catalog.CatalogPluginExtension
import org.gradle.api.plugins.catalog.VersionCatalogPlugin
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
import tech.yanand.gradle.mavenpublish.MavenCentralExtension
import tech.yanand.gradle.mavenpublish.MavenCentralPublishPlugin

import static cn.hutool.http.HttpUtil.get
import static io.freefair.gradle.util.GitUtil.isGithubActions

/**
 * 组件发布插件
 * @author liheng
 */
@IHubPlugin(value = IHubPublishExtension, beforeApplyPlugins = [IHubBomPlugin, IHubPluginsPlugin, MavenPublishPlugin])
class IHubPublishPlugin extends IHubProjectPluginAware<IHubPublishExtension> {

    @Override
    void apply() {
        afterEvaluate {
            // 引入GithubPom插件
            if (extension.applyGithubPom.get() && isGithubActions(project.providers)) {
                applyPlugin GithubPomPlugin
                withExtension(PublishingExtension) {
                    it.publications.withType MavenPublication, this::configurePomDevelopers
                }
            }

            IHubPluginsExtension iHubExt = withExtension IHubPluginsExtension

            configPublish project, iHubExt

            configSigning project, extension

            if (extension.publishMavenCentral.get()) {
                applyPlugin MavenCentralPublishPlugin
                withExtension(MavenCentralExtension) {
                    it.authToken.set Base64.encoder.encodeToString((iHubExt.repoUsername.orNull + ':' + iHubExt.repoPassword.orNull).bytes)
                }
            }

            // 添加配置元信息
            if (hasPlugin(JavaPlugin) && extension.addConfigurationMetaInformation) {
                withExtension(IHubBomExtension) {
                    it.dependencies {
                        annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
                    }
                    project.compileJava.inputs.files project.processResources
                }
            }
        }
    }

    private void configPublish(Project project, IHubPluginsExtension iHubExt) {
        boolean publishSources = extension.publishSources.get()
        boolean publishDocs = extension.publishDocs.get()
        withExtension(PublishingExtension) {
            it.publications {
                create('mavenJava', MavenPublication) {
                    it.groupId = project.group
                    it.version = getCompatibilityVersion project

                    if (hasPlugin(JavaPlatformPlugin)) {
                        from project.components.getByName('javaPlatform')
                        return
                    }
                    if (hasPlugin(VersionCatalogPlugin)) {
                        from project.components.getByName('versionCatalog')
                        this.configVersionCatalog()
                        return
                    }
                    from project.components.getByName('java')

                    // release版本时发布sources以及docs包
                    if (release) {
                        List tasks = []
                        if (publishSources) {
                            tasks << this.registerSourcesJar()
                        }
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
                }
            }
            configMavenRepo it, iHubExt
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
        boolean required = release && extension.publishNeedSign.get()
        if (!required) {
            return
        }
        project.plugins.apply SigningPlugin
        withExtension(SigningExtension) { ext ->
            ext.required = required
            if (extension.signingKeyId.present) {
                ext.useInMemoryPgpKeys extension.signingKeyId.get(),
                    extension.signingSecretKey.orNull, extension.signingPassword.orNull
            } else if (extension.signingSecretKey.present) {
                ext.useInMemoryPgpKeys extension.signingSecretKey.get(), extension.signingPassword.orNull
            } else {
                ext.useGpgCmd()
            }
            withExtension(PublishingExtension) {
                ext.sign it.publications.mavenJava
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

    private static getCompatibilityVersion(Project project) {
        // 判断是否需要添加兼容版本号
        if (!project.rootProject.findProperty('isCompatibilityPublish')) {
            return project.version
        }
        String suffix = '-java' + JavaVersion.current()
        project.version.toString().with {
            it.endsWith('-SNAPSHOT') ? it.replace('-SNAPSHOT', suffix + '-SNAPSHOT') : it + suffix
        }
    }


    private void configVersionCatalog() {
        withExtension(CatalogPluginExtension).versionCatalog {
            from project.files('gradle/libs.versions.toml')
            // 匹配兼容的java版本配置
            def versionAliases = withExtension(VersionCatalogsExtension).named 'ihubCatalogs'
            versionAliases.versionAliases.each { alias ->
                version alias, versionAliases.findVersion(alias).get().requiredVersion
            }
        }
    }

    private void configMavenRepo(PublishingExtension ext, IHubPluginsExtension iHubExt) {
        String mavenUrl = release ? iHubExt.releaseRepoUrl.orNull : iHubExt.snapshotRepoUrl.orNull
        if (!extension.publishMavenCentral.get() && mavenUrl) {
            ext.repositories.maven {
                url mavenUrl
                allowInsecureProtocol iHubExt.repoAllowInsecureProtocol.get()
                iHubExt.repoUsername.orNull?.with { repoUsername ->
                    credentials {
                        username repoUsername
                        password iHubExt.repoPassword.orNull
                    }
                }
            }
        }
    }

}
