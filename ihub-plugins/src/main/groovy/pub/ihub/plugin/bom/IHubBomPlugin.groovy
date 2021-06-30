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
import org.gradle.api.Plugin
import org.gradle.api.Project
import pub.ihub.plugin.IHubProjectPlugin

import static pub.ihub.plugin.IHubProjectPlugin.EvaluateStage.AFTER



/**
 * BOM（Bill of Materials）组件依赖管理
 * @author henry
 */
@SuppressWarnings('NestedBlockDepth')
class IHubBomPlugin extends IHubProjectPlugin<IHubBomExtension> {

    Class<? extends Plugin<Project>>[] beforeApplyPlugins = [DependencyManagementPlugin]
    String extensionName = 'iHubBom'

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
            // TODO 由于GitHub仓库token只能个人使用，组件发布到中央仓库方可使用
//					group 'pub.ihub.lib' module 'ihub-libs' version '1.0.0-SNAPSHOT'
            group 'org.springframework.boot' module 'spring-boot-dependencies' version '2.5.1'
            group 'org.springframework.cloud' module 'spring-cloud-dependencies' version '2020.0.3'
            group 'com.alibaba.cloud' module 'spring-cloud-alibaba-dependencies' version '2021.1'
            group 'com.github.xiaoymin' module 'knife4j-dependencies' version '3.0.3'
            group 'com.sun.xml.bind' module 'jaxb-bom-ext' version '3.0.1'
            group 'de.codecentric' module 'spring-boot-admin-dependencies' version '2.4.2'
        }
        // 配置组件依赖版本
        ext.dependencyVersions {
            group 'com.alibaba' modules 'fastjson' version '1.2.76'
            group 'com.alibaba' modules 'druid', 'druid-spring-boot-starter' version '1.2.6'
            group 'com.alibaba.p3c' modules 'p3c-pmd' version '2.1.1'
            group 'com.baomidou' modules 'mybatis-plus', 'mybatis-plus-boot-starter',
                'mybatis-plus-generator' version '3.4.3.1'
            group 'com.github.xiaoymin' modules 'knife4j-aggregation-spring-boot-starter' version '2.0.9'
        }
        // 配置组版本策略（建议尽量使用bom）
        ext.groupVersions {
            group 'cn.hutool' version '5.7.2'
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
                ext.printConfigContent()
            }
        }
    }

}
