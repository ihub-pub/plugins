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

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubPluginsPlugin
import pub.ihub.plugin.IHubProjectPluginAware

import static org.gradle.api.plugins.JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.API_CONFIGURATION_NAME
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER



/**
 * BOM（Bill of Materials）组件依赖管理
 * @author henry
 */
@IHubPlugin(value = IHubBomExtension, beforeApplyPlugins = IHubPluginsPlugin)
@SuppressWarnings('NestedBlockDepth')
class IHubBomPlugin extends IHubProjectPluginAware<IHubBomExtension> {

    @Override
    void apply() {
        // 配置ihub-bom
        extension.importBoms {
            group 'pub.ihub.lib' module 'ihub-bom' version '1.0.2'
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

    private DependencyHandler getDependencies() {
        project.dependencies
    }

    private void configProject(IHubBomExtension ext) {
        project.configurations {
            // 导入bom配置
            List<Dependency> bomDependencies = ext.bomVersions.collect { spec ->
                "$spec.id:$spec.module:$spec.version"?.with {
                    spec.enforced ? dependencies.enforcedPlatform(it) : dependencies.platform(it)
                }
            }
            [API_CONFIGURATION_NAME, 'pmd', ANNOTATION_PROCESSOR_CONFIGURATION_NAME].each { name ->
                maybeCreate(name).dependencies.addAll bomDependencies
            }
            // 配置组件版本
            dependencies.constraints {
                ext.dependencyVersions.each { spec ->
                    spec.modules.each {
                        add API_CONFIGURATION_NAME, "$spec.id:$it:$spec.version"
                    }
                }
            }

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
                    dependencies.create it.startsWith(':') ? project.project(it) : it
                }
            }
        }
    }

}
