/*
 * Copyright (c) 2021-2023 the original author or authors.
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
package pub.ihub.plugin

import io.freefair.gradle.plugins.settings.PluginVersionsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.resolve.DependencyResolutionManagement
import org.gradle.api.internal.GradleInternal
import org.gradle.api.internal.file.DefaultFileOperations
import org.gradle.api.internal.file.FileCollectionFactory
import org.gradle.api.internal.file.FileLookup
import org.gradle.api.internal.file.FileOperations
import org.gradle.api.plugins.JavaPlatformExtension
import org.gradle.api.plugins.JavaPlatformPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.catalog.VersionCatalogPlugin
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.tomlj.Toml

import java.nio.file.FileSystems
import java.nio.file.Path

import static java.lang.Boolean.valueOf
import static org.codehaus.groovy.runtime.ResourceGroovyMethods.readLines
import static org.gradle.api.JavaVersion.current
import static pub.ihub.plugin.IHubLibsVersions.getLibsVersion
import static pub.ihub.plugin.IHubPluginMethods.printLineConfigContent
import static pub.ihub.plugin.IHubPluginMethods.printMapConfigContent
import static pub.ihub.plugin.IHubSettingsExtension.findProperty

/**
 * Gradle配置插件
 * @author henry
 */
class IHubSettingsPlugin implements Plugin<Settings> {

    private static final List<String> IHUB_PLUGIN_IDS = readLines IHubSettingsPlugin
        .classLoader.getResource('META-INF/ihub/plugin-ids')
    private static final String IHUB_PLUGIN_VERSION = getLibsVersion 'ihub-plugins'

    private static final Map<String, String> PLUGIN_ALIAS_IDS = [
        'plugin-publish': 'com.gradle.plugin-publish',
    ]
    private static final Map<String, String> PLUGIN_VERSIONS = PLUGIN_ALIAS_IDS.collectEntries { alias, id ->
        [(id): getLibsVersion(alias)]
    }

    @Override
    void apply(Settings settings) {
        // 配置插件仓库
        configPluginRepositories settings

        // 扩展配置
        IHubSettingsExtension ext = settings.extensions.create 'iHubSettings', IHubSettingsExtension, settings

        // 配置自定义扩展
        settings.gradle.settingsEvaluated {
            // 配置插件版本
            settings.pluginManagement {
                plugins {
                    (IHUB_PLUGIN_IDS.collectEntries {
                        [(it): IHUB_PLUGIN_VERSION]
                    } + PLUGIN_VERSIONS).findAll { it.value }.each { key, value ->
                        id key version value
                    }
                }
            }
            printMapConfigContent 'Gradle Plugin Plugins Version', 'ID', 'Version', PLUGIN_VERSIONS

            // 发布兼容版本组件跳过后续配置
            if (findCompatibilityLibsPath(settings).toFile().exists()) {
                return
            }

            // 配置组件BOM
            ext.includeBom?.with { includeBom settings, it }

            // 配置子项目
            Map<String, List<String>> projectSpecs = [:]
            settings.rootDir.eachDir { dir ->
                ext.getProjectSpec(dir.name)?.with { spec ->
                    projectSpecs.putAll spec.includeSubProject(dir)
                }
            }
            printMapConfigContent 'Include Gradle Projects', 'Path', 'Projects', projectSpecs
        }

        // 配置catalog
        configVersionCatalogs settings, ext

        settings.pluginManager.apply PluginVersionsPlugin
    }

    private void configPluginRepositories(Settings settings) {
        settings.pluginManagement {
            repositories {
                String dirs = "$settings.rootProject.projectDir/gradle/plugins"
                if ((dirs as File).directory) {
                    flatDir dirs: dirs
                }
                if (valueOf findProperty(settings, 'iHub.mavenLocalEnabled')) {
                    mavenLocal()
                }
                if (valueOf findProperty(settings, 'iHub.mavenAliYunEnabled')) {
                    maven {
                        name 'AliYunGradle'
                        url 'https://maven.aliyun.com/repository/gradle-plugin'
                        artifactUrls 'https://plugins.gradle.org/m2'
                    }
                }
                gradlePluginPortal()
                mavenCentral()
                // 添加私有仓库
                if (valueOf findProperty(settings, 'iHub.mavenPrivateEnabled', 'true')) {
                    findProperty(settings, 'iHub.releaseRepoUrl')?.with { repoUrl ->
                        maven mavenRepo(settings, 'ReleaseRepo', repoUrl, true)
                    }
                    findProperty(settings, 'iHub.snapshotRepoUrl')?.with { repoUrl ->
                        maven mavenRepo(settings, 'SnapshotRepo', repoUrl, false)
                    }
                }
                // 添加自定义仓库
                findProperty(settings, 'iHub.customizeRepoUrl')?.with { repoUrl ->
                    maven {
                        name 'CustomizeRepo'
                        url repoUrl
                    }
                }
            }
            printLineConfigContent 'Gradle Plugin Repos', settings.pluginManagement.repositories*.displayName
        }
    }

    private Closure mavenRepo(Settings settings, String repoName, String repoUrl, boolean releases) {
        return {
            name repoName
            url repoUrl
            allowInsecureProtocol valueOf(findProperty(settings, 'iHub.repoAllowInsecureProtocol'))
            mavenContent {
                releases ? releasesOnly() : snapshotsOnly()
            }
        }
    }

    private static void includeBom(Settings settings, String bomName) {
        includeJavaPlatform settings, bomName
        settings.gradle.afterProject { Project project ->
            if (project.name == bomName) {
                project.dependencies {
                    constraints {
                        project.rootProject.allprojects.findAll {
                            it.plugins.hasPlugin(MavenPublishPlugin) && it.plugins.hasPlugin(JavaPlugin)
                        }.each {
                            api "${project.rootProject.group}:$it.name:${project.rootProject.version}"
                        }
                    }
                }
            }
        }
    }

    private static void includeDependencies(Settings settings, String dependName) {
        includeJavaPlatform settings, dependName
        settings.gradle.afterProject { Project project ->
            if (dependName.contains(project.name)) {
                project.dependencies {
                    // 配置平台依赖
                    project.rootProject.allprojects.findAll {
                        it.plugins.hasPlugin(JavaPlatformPlugin) && it.name != project.name
                    }.each {
                        api platform(it)
                    }
                    project.ihubCatalogs.bundles.platform.get().forEach { api platform(it) }
                    // 配置组件版本
                    constraints {
                        api project.rootProject
                        project.ihubCatalogs.bundles.constraints.get().forEach { api it }
                    }
                }

                // 子项目添加平台依赖
                project.rootProject.subprojects {
                    if (it.plugins.hasPlugin(JavaPlugin)) {
                        dependencies {
                            implementation platform(project)
                            pmd platform(project)
                            annotationProcessor platform(project)
                            testAnnotationProcessor platform(project)
                        }
                    }
                }
            }
        }
    }

    private static void includeJavaPlatform(Settings settings, String projectName) {
        settings.include projectName
        settings.gradle.rootProject {
            project(projectName).pluginManager.apply JavaPlatformPlugin
            project(projectName).extensions.getByType(JavaPlatformExtension).allowDependencies()
        }
        settings.gradle.beforeProject { Project project ->
            if (projectName.contains(project.name)) {
                try {
                    project.pluginManager.apply 'pub.ihub.plugin.ihub-publish'
                } catch (e) {
                    project.logger.trace project.name + ': ' + e.message +
                        ' Please add plugin: pub.ihub.plugin.ihub-publish in root project \'build.gradle\'.'
                }
            }
        }
    }

    private static void configVersionCatalogs(Settings settings, IHubSettingsExtension ext) {
        settings.dependencyResolutionManagement {
            repositories {
                mavenCentral()
            }

            // 配置发布Catalog组件
            if ('true' == ext.includeLibs) {
                configIHubPublishCatalogs it, settings, ext
            }

            // 发布兼容版本组件跳过后续配置
            if (findCompatibilityLibsPath(settings).toFile().exists()) {
                return
            }

            // 配置iHubLibs版本
            configIHubCatalogs it

            // 自动配置./gradle/libs目录下的.versions.toml文件
            autoConfigCatalogsFile it, settings

            // 配置profile版本
            configProfileCatalogs it, settings
        }
    }

    private static void configIHubPublishCatalogs(DependencyResolutionManagement management, Settings settings,
                                                  IHubSettingsExtension ext) {
        def baseDirectory = getFilePath(settings, 'gradle').toFile()
        def path = findCompatibilityLibsPath settings
        boolean pathExists = path.toFile().exists()
        management.versionCatalogs {
            settings.gradle.rootProject {
                pluginManager.apply VersionCatalogPlugin
                it.ext.setProperty 'isCompatibilityPublish', pathExists
            }
            ihubCatalogs {
                from fileOperationsFor(settings, baseDirectory).configurableFiles('libs.versions.toml')
                if (pathExists) {
                    def versionsTable = Toml.parse(path).getTable 'versions'
                    versionsTable.keySet().each { key -> version key, versionsTable.get(key) }
                }
            }
            ext.includeDependencies?.with { includeDependencies settings, it }
        }
    }

    private static void configIHubCatalogs(DependencyResolutionManagement management) {
        management.versionCatalogs {
            ihub {
                from "pub.ihub.lib:ihub-libs:${IHubLibsVersions.getCompatibleLibsVersion('ihub-libs')}"
                IHubSettingsPlugin.IHUB_PLUGIN_IDS.each { pluginId ->
                    plugin(pluginId.contains('ihub-') ? pluginId.split('ihub-').last() : 'root', pluginId)
                        .version IHubSettingsPlugin.IHUB_PLUGIN_VERSION
                }
                IHubSettingsPlugin.PLUGIN_ALIAS_IDS.each { aliasId, id ->
                    plugin(aliasId, id).version getLibsVersion(aliasId)
                }
            }
        }
    }

    private static void configProfileCatalogs(DependencyResolutionManagement management, Settings settings) {
        def profile = findProperty settings, 'iHub.profile'
        if (!profile) {
            return
        }
        profile.split(',').each {
            def path = getFilePath settings, 'gradle', 'libs', 'profiles', "${it}.versions.toml"
            if (path.toFile().exists()) {
                management.versionCatalogs {
                    libs {
                        def versionsTable = Toml.parse(path).getTable 'versions'
                        versionsTable.keySet().each { key -> version key, versionsTable.get(key) }
                    }
                }
            }
        }
    }

    private static void autoConfigCatalogsFile(DependencyResolutionManagement management, Settings settings) {
        def libsPath = getFilePath settings, 'gradle', 'libs'
        def baseDirectory = libsPath.toFile()
        if (!baseDirectory.exists()) {
            return
        }
        management.versionCatalogs {
            baseDirectory.eachFile { File file ->
                // libs.versions.toml为标准配置文件，会被自动加载
                if (file.name.endsWith('.versions.toml')) {
                    "${file.name - '.versions.toml'}" {
                        from fileOperationsFor(settings, baseDirectory).configurableFiles(file.name)
                    }
                }
            }
        }
    }

    private static Path findCompatibilityLibsPath(Settings settings) {
        // 查找兼容组件版本
        getFilePath settings, 'gradle', 'libs', 'compatibility', "java${current()}.versions.toml"
    }

    private static Path getFilePath(Settings settings, String... paths) {
        FileSystems.default.getPath settings.rootDir.absolutePath, paths
    }

    private static FileOperations fileOperationsFor(Settings settings, File baseDirectory) {
        def services = (settings.gradle as GradleInternal).services
        def fileLookup = services.get FileLookup
        def fileResolver = fileLookup.getFileResolver baseDirectory
        def fileCollectionFactory = services.get(FileCollectionFactory).withResolver fileResolver
        DefaultFileOperations.createSimple(
            fileResolver,
            fileCollectionFactory,
            services
        )
    }

}
