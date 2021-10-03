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
package pub.ihub.plugin.bom

import com.github.benmanes.gradle.versions.VersionsPlugin
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.gradle.api.Project
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware

import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER



/**
 * BOM（Bill of Materials）组件依赖管理
 * @author henry
 */
@IHubPlugin(value = IHubBomExtension, beforeApplyPlugins = [DependencyManagementPlugin])
@SuppressWarnings('NestedBlockDepth')
class IHubBomPlugin extends IHubProjectPluginAware<IHubBomExtension> {

    @Override
    void apply() {
        // 配置ihub-bom
        extension.importBoms {
            group 'pub.ihub.lib' module 'ihub-libs' version '1.0.0'
        }
        // 配置默认排除项
        extension.excludeModules {
            group 'c3p0' modules 'c3p0'
            group 'commons-logging' modules 'commons-logging'
            group 'com.zaxxer' modules 'HikariCP'
            group 'log4j' modules 'log4j'
            group 'org.apache.logging.log4j' modules 'log4j-core'
            group 'org.apache.tomcat' modules 'tomcat-jdbc'
            group 'org.slf4j' modules 'slf4j-jcl', 'slf4j-log4j12'
            group 'stax' modules 'stax-api'
        }
        // 配置默认依赖组件
        extension.dependencies {
            compileOnly 'cn.hutool:hutool-all'
            implementation 'org.slf4j:slf4j-api'
            runtimeOnly 'org.slf4j:jul-to-slf4j', 'org.slf4j:log4j-over-slf4j'
        }

        // 配置项目依赖
        configProject project

        // 自定义依赖升级输出
        if (project == project.rootProject) {
            applyPlugin VersionsPlugin
            withTask DependencyUpdatesTask, {
                it.configure {
                    outputFormatter = dependencyUpdatesOutputFormatter
                }
            }
        }
    }

    private void configProject(Project project) {
        withExtension(AFTER) { ext ->
            project.dependencyManagement {
                // 导入bom配置
                imports {
                    ext.bomVersions.each {
                        mavenBom "$it.id:$it.module:$it.version"
                    }
                }

                // 配置组件版本
                dependencies {
                    ext.dependencyVersions.each { config ->
                        dependencySet(group: config.id, version: config.version) {
                            config.modules.each { entry it }
                        }
                    }
                }
            }

            project.configurations {
                all {
                    resolutionStrategy {
                        // 配置组件组版本（用于配置无bom组件）
                        eachDependency {
                            ext.groupVersions.find { s -> s.id == it.requested.group }?.version?.with { v ->
                                it.useVersion v
                            }
                        }
                        // 不缓存动态版本
                        cacheDynamicVersionsFor 0, 'seconds'
                        // 不缓存快照模块
                        cacheChangingModulesFor 0, 'seconds'
                    }
                    // 排除组件依赖
                    ext.excludeModules.each {
                        if (it.modules.contains('all')) {
                            exclude group: it.id
                        } else {
                            it.modules.each { module -> exclude group: it.id, module: module }
                        }
                    }
                }
                // 配置组件依赖
                ext.dependencies.each { spec ->
                    maybeCreate(spec.id).dependencies.addAll spec.modules.collect {
                        // 支持导入项目
                        project.dependencies.create it.startsWith(':') ? project.project(it) : it
                    }
                }
            }

            ext.refreshCommonSpecs()

            project.gradle.taskGraph.whenReady {
                ext.printConfigContent()
            }
        }
    }

    private final dependencyUpdatesOutputFormatter = { result ->
        result.current.dependencies.with {
            if (!empty) {
                String title = 'The following dependencies are using the latest version'
                printConfigContent title, it.collect { dependency ->
                    [dependency.group, dependency.name, dependency.version]
                }, 'Group', 'Module', 'Version'
            }
        }
        result.exceeded.dependencies.with {
            if (!empty) {
                String title = 'The following dependencies exceed the version found at the revision level'
                printConfigContent title, it.collect { dependency ->
                    [dependency.group, dependency.name, dependency.version, dependency.latest]
                }, 'Group', 'Module', 'Current version', 'Latest version'
            }
        }
        result.outdated.dependencies.with {
            if (!empty) {
                String title = 'The following dependencies have later versions'
                printConfigContent title, it.collect { dependency ->
                    [
                        dependency.group,
                        dependency.name,
                        dependency.version,
                        dependency.available.release ?: dependency.available.milestone
                    ]
                }, 'Group', 'Module', 'Current version', 'Latest version'
            }
        }
    }

}
