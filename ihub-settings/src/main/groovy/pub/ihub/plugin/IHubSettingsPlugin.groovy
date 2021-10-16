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

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

import static pub.ihub.plugin.IHubPluginMethods.printLineConfigContent
import static pub.ihub.plugin.IHubPluginMethods.printMapConfigContent
import static pub.ihub.plugin.IHubSettingsExtension.findProperty



/**
 * Gradle配置插件
 * @author henry
 */
class IHubSettingsPlugin implements Plugin<Settings> {

    private static final List<String> IHUB_PLUGINS = [
        'pub.ihub.plugin',
        'pub.ihub.plugin.ihub-bom',
        'pub.ihub.plugin.ihub-java',
        'pub.ihub.plugin.ihub-groovy',
        'pub.ihub.plugin.ihub-publish',
        'pub.ihub.plugin.ihub-test',
        'pub.ihub.plugin.ihub-verification',
        'pub.ihub.plugin.ihub-boot',
        'pub.ihub.plugin.ihub-native',
        'pub.ihub.plugin.ihub-git-hooks',
    ]

    private static final Map<String, String> PLUGIN_VERSIONS = [
        'com.gradle.plugin-publish': '0.16.0',
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
            settings.pluginManagement {
                plugins {
                    (IHUB_PLUGINS.collectEntries {
                        [(it): IHubSettingsPlugin.package.implementationVersion]
                    } + PLUGIN_VERSIONS).findAll { it.value }.each { key, value ->
                        id key version value
                    }
                }
            }
            printMapConfigContent 'Gradle Plugin Plugins Version', 'ID', 'Version', PLUGIN_VERSIONS

            // 配置子项目
            Map<String, List<String>> projectSpecs = [:]
            settings.rootDir.eachDir { dir ->
                ext.getProjectSpec(dir.name)?.with { spec ->
                    projectSpecs.putAll spec.includeSubProject(dir)
                }
            }
            printMapConfigContent 'Include Gradle Projects', 'Path', 'Projects', projectSpecs
        }
    }

    private void configPluginRepositories(Settings settings) {
        settings.pluginManagement {
            repositories {
                String dirs = "$settings.rootProject.projectDir/gradle/plugins"
                if ((dirs as File).directory) {
                    flatDir dirs: dirs
                }
                maven {
                    name 'AliYunGradlePlugin'
                    url 'https://maven.aliyun.com/repository/gradle-plugin'
                }
                maven {
                    name 'AliYunSpringPlugin'
                    url 'https://maven.aliyun.com/repository/spring-plugin'
                }
                maven {
                    name 'SpringRelease'
                    url 'https://repo.spring.io/release'
                }
                // 添加私有仓库
                findProperty(settings, 'iHub.releaseRepoUrl')?.with { repoUrl ->
                    maven {
                        name 'ReleaseRepo'
                        url repoUrl
                    }
                }
                findProperty(settings, 'iHub.snapshotRepoUrl')?.with { repoUrl ->
                    maven {
                        name 'SnapshotRepo'
                        url repoUrl
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

}
