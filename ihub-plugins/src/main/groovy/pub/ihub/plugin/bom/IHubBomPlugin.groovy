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

import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubPluginsPlugin
import pub.ihub.plugin.IHubProjectPluginAware

import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER



/**
 * BOM（Bill of Materials）组件依赖管理
 * @author henry
 */
@IHubPlugin(value = IHubBomExtension, beforeApplyPlugins = [IHubPluginsPlugin, DependencyManagementPlugin])
@SuppressWarnings('NestedBlockDepth')
class IHubBomPlugin extends IHubProjectPluginAware<IHubBomExtension> {

    private static final String IHUB_BOM_VERSION = '1.0.3'

    @Override
    void apply() {
        // 配置ihub-bom
        extension.importBoms {
            group 'pub.ihub.lib' module 'ihub-bom' version IHUB_BOM_VERSION
        }
        // 配置默认依赖组件
        extension.dependencies {
            compileOnly 'cn.hutool:hutool-all'
        }

        // 配置项目依赖
        withExtension(AFTER) { ext ->
            configProject ext

            ext.refreshCommonSpecs()

            project.gradle.taskGraph.whenReady {
                ext.printConfigContent()
            }
        }
    }

    private void configProject(IHubBomExtension ext) {
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
    }

}
