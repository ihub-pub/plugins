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
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal
import org.gradle.api.internal.file.DefaultFileOperations
import org.gradle.api.internal.file.FileCollectionFactory
import org.gradle.api.internal.file.FileLookup
import org.gradle.api.internal.file.FileOperations

import static java.lang.Boolean.valueOf
import static org.codehaus.groovy.runtime.ResourceGroovyMethods.readLines
import static pub.ihub.plugin.IHubPluginMethods.printLineConfigContent
import static pub.ihub.plugin.IHubPluginMethods.printMapConfigContent
import static pub.ihub.plugin.IHubSettingsExtension.findProperty

/**
 * Gradle配置插件
 * @author henry
 */
class IHubSettingsPlugin implements Plugin<Settings> {

    private static final Map<String, String> PLUGIN_VERSIONS = [
        'com.gradle.plugin-publish': '1.2.0',
    ]

    @Override
    void apply(Settings settings) {
        // 配置插件仓库
        configPluginRepositories settings

        // 扩展配置
        IHubSettingsExtension ext = settings.extensions.create 'iHubSettings', IHubSettingsExtension, settings

        // 配置自定义扩展
        settings.gradle.settingsEvaluated {
            // 配置插件版本
            List<String> ids = readLines IHubSettingsPlugin.classLoader.getResource('META-INF/ihub/plugin-ids')
            settings.pluginManagement {
                plugins {
                    (ids.collectEntries {
                        [(it): IHubSettingsPlugin.package.implementationVersion]
                    } + PLUGIN_VERSIONS).findAll { it.value }.each { key, value ->
                        id key version value
                    }
                }
            }
            printMapConfigContent 'Gradle Plugin Plugins Version', 'ID', 'Version', PLUGIN_VERSIONS

            findProperty(settings, 'iHubSettings.includeBom')?.with {
                settings.include it
            }

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
        settings.dependencyResolutionManagement {
            repositories {
                mavenCentral()
            }
            versionCatalogs {
                // 配置iHubLibs版本
                ihubLibs {
                    from "pub.ihub.lib:ihub-libs:${IHubLibsVersions.LIBS_VERSIONS['ihub']}"
                }
                // 自动配置./gradle目录下的.versions.toml文件
                def baseDirectory = settings.rootDir.listFiles().find { it.name == 'gradle' }
                baseDirectory?.eachFile { File file ->
                    // libs.versions.toml为标准配置文件，会被自动加载
                    if (file.name.endsWith('.versions.toml') && file.name != 'libs.versions.toml') {
                        "${file.name - '.versions.toml'}" {
                            from fileOperationsFor(settings, baseDirectory).configurableFiles(file.name)
                        }
                    }
                }
            }
        }

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
