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
import org.gradle.api.Project
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware

import static pub.ihub.plugin.IHubPluginMethods.tap
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.BOM
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.DEPENDENCY
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.EXCLUDE
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.GROUP
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.MODULES
import static pub.ihub.plugin.bom.IHubVersionProperties.GROUP_DEPENDENCY_VERSION_CONFIG
import static pub.ihub.plugin.bom.IHubVersionProperties.GROUP_MAVEN_BOM_VERSION_CONFIG
import static pub.ihub.plugin.bom.IHubVersionProperties.GROUP_VERSION_CONFIG



/**
 * BOM（Bill of Materials）组件依赖管理
 * @author henry
 */
@IHubPlugin(value = IHubBomExtension, beforeApplyPlugins = [DependencyManagementPlugin])
@SuppressWarnings('NestedBlockDepth')
class IHubBomPlugin extends IHubProjectPluginAware<IHubBomExtension> {

    @Override
    void apply() {
        // 添加默认配置
        withExtension {
            defaultConfig it
        }

        // 配置项目依赖
        configProject project
    }

    private static void defaultConfig(IHubBomExtension ext) {
        // 配置导入bom
        ext.importBoms {
            GROUP_MAVEN_BOM_VERSION_CONFIG.each { library, config ->
                group config.v1 module config.v2 version ext.findVersion(library, config.v3)
            }
        }
        // 配置组件依赖版本
        ext.dependencyVersions {
            GROUP_DEPENDENCY_VERSION_CONFIG.each { library, config ->
                group config.v1 modules config.v2 version ext.findVersion(library, config.v3)
            }
        }
        // 配置组版本策略（建议尽量使用bom）
        ext.groupVersions {
            GROUP_VERSION_CONFIG.each { library, config ->
                group config.v1 version ext.findVersion(library, config.v2)
            }
        }
        // 配置默认排除项
        ext.excludeModules {
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
        ext.dependencies {
            compileOnly 'cn.hutool:hutool-all'
            implementation 'org.slf4j:slf4j-api'
            runtimeOnly 'org.slf4j:jul-to-slf4j', 'org.slf4j:log4j-over-slf4j'
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
                printConfigContent ext
            }
        }
    }

    private static void printConfigContent(IHubBomExtension ext) {
        ext.printConfig BOM, 'Group Maven Bom Version', groupTap(35), moduleTap(), versionTap(15)
        ext.printConfig MODULES, 'Group Maven Module Version', groupTap(35), moduleTap(), versionTap(15)
        ext.printConfig GROUP, 'Group Maven Default Version', groupTap(), versionTap()
        ext.printConfig EXCLUDE, 'Exclude Group Modules', groupTap(40), moduleTap()
        ext.printConfig DEPENDENCY, 'Config Default Dependencies', dependencyTypeTap(), dependenciesTap()
    }

    private static Tuple2<String, Integer> groupTap(Integer width = null) {
        tap 'Group', width
    }

    private static Tuple2<String, Integer> versionTap(Integer width = 30) {
        tap 'Version', width
    }

    private static Tuple2<String, Integer> moduleTap() {
        tap 'Module', null
    }

    private static Tuple2<String, Integer> dependencyTypeTap() {
        tap 'DependencyType', 30
    }

    private static Tuple2<String, Integer> dependenciesTap() {
        tap 'Dependencies'
    }

}
