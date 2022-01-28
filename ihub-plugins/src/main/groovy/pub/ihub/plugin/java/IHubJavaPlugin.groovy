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
package pub.ihub.plugin.java

import com.github.jengelman.gradle.plugins.processes.ProcessesPlugin
import io.freefair.gradle.plugins.lombok.LombokPlugin
import net.bytebuddy.build.gradle.AbstractByteBuddyTask
import net.bytebuddy.build.gradle.AbstractByteBuddyTaskExtension
import net.bytebuddy.build.gradle.ByteBuddyPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.ProjectReportsPlugin
import org.gradle.api.reporting.plugins.BuildDashboardPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.AbstractCompile
import org.jmolecules.bytebuddy.JMoleculesPlugin
import org.springdoc.openapi.gradle.plugin.OpenApiGradlePlugin
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.bom.IHubBomPlugin

import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.BEFORE



/**
 * Java插件
 * @author henry
 */
@IHubPlugin(value = IHubJavaExtension, beforeApplyPlugins = [
    IHubBomPlugin, JavaPlugin, JavaLibraryPlugin, LombokPlugin, ProjectReportsPlugin, BuildDashboardPlugin,
    ByteBuddyPlugin
])
class IHubJavaPlugin extends IHubProjectPluginAware<IHubJavaExtension> {

    private static final Map<String, String> J_MOLECULES_ARCHITECTURE_DEPENDENCIES = [
        // CQRS architecture
        cqrs   : 'org.jmolecules:jmolecules-cqrs-architecture',
        // Layered architecture
        layered: 'org.jmolecules:jmolecules-layered-architecture',
        // Onion architecture
        onion  : 'org.jmolecules:jmolecules-onion-architecture',
    ]

    static final Map<String, Closure> DEFAULT_DEPENDENCIES_CONFIG = [
        // 添加jaxb运行时依赖
        jaxb                     : { IHubBomExtension ext ->
            ext.excludeModules {
                group 'com.sun.xml.bind' modules 'jaxb-core'
            }
            ext.dependencies {
                runtimeOnly 'javax.xml.bind:jaxb-api', 'org.glassfish.jaxb:jaxb-runtime'
            }
        },
        // 添加日志依赖配置
        log                      : { IHubBomExtension ext ->
            ext.excludeModules {
                group 'commons-logging' modules 'commons-logging'
                group 'log4j' modules 'log4j'
                group 'org.apache.logging.log4j' modules 'log4j-core'
                group 'org.slf4j' modules 'slf4j-jcl', 'slf4j-log4j12'
            }
            ext.dependencies {
                implementation 'org.slf4j:slf4j-api'
                runtimeOnly 'org.slf4j:jul-to-slf4j', 'org.slf4j:log4j-over-slf4j'
            }
        },
        // 添加MapStruct依赖
        mapstruct                : { IHubBomExtension ext ->
            ext.dependencies {
                implementation 'org.mapstruct:mapstruct'
                annotationProcessor 'org.mapstruct:mapstruct-processor'
            }
        },
        // 添加Doc依赖
        doc                      : { IHubBomExtension ext ->
            ext.dependencies {
                compileOnly 'io.swagger.core.v3:swagger-annotations'
                annotationProcessor 'pub.ihub.lib:ihub-process-doc'
            }
        },
        // 添加jMolecules依赖
        jmolecules               : { IHubBomExtension ext ->
            ext.dependencies {
                implementation 'org.jmolecules:jmolecules-ddd', 'org.jmolecules:jmolecules-events'
                implementation J_MOLECULES_ARCHITECTURE_DEPENDENCIES[
                    ext.project.extensions.getByType(IHubJavaExtension).jmoleculesArchitecture
                ]
            }
        },
        // 添加jMolecules-integrations依赖
        'jmolecules-integrations': { IHubBomExtension ext ->
            ext.dependencies {
                implementation 'org.jmolecules.integrations:jmolecules-spring',
                    'org.jmolecules.integrations:jmolecules-jpa',
                    'org.jmolecules.integrations:jmolecules-jackson'
                testImplementation 'org.jmolecules.integrations:jmolecules-archunit'
            }
        },
    ]

    @Override
    void apply() {
        withExtension(BEFORE) { ext ->
            // 兼容性配置
            ext.compatibility?.with { version ->
                withTask(AbstractCompile) {
                    it.sourceCompatibility = version
                    it.targetCompatibility = version
                    it.options.encoding = 'UTF-8'
                    it.options.incremental = ext.gradleCompilationIncremental
                }
            }

            withExtension(IHubBomExtension) {
                ext.defaultDependencies.split(',').each { dependency ->
                    DEFAULT_DEPENDENCIES_CONFIG[dependency]?.call it
                    if ('jmolecules' == dependency) {
                        // 注：启用byteBuddy插件时，org.gradle.parallel设置false
                        withExtension(AbstractByteBuddyTaskExtension).transformation {
                            it.plugin = JMoleculesPlugin
                        }
                        withTask(AbstractByteBuddyTask) {
                            withTask('classes').dependsOn it
                        }
                    }
                }
            }
        }

        withExtension(AFTER) {
            if (it.applyOpenapiPlugin) {
                applyPlugin ProcessesPlugin, OpenApiGradlePlugin
            }
        }

        // 配置Jar属性
        withTask(Jar) {
            it.manifest {
                attributes(
                    'Implementation-Title': project.name,
                    'Automatic-Module-Name': project.name.replaceAll('-', '.'),
                    'Implementation-Version': project.version,
                    'Implementation-Vendor-Id': project.group,
                    'Created-By': 'Java ' + JavaVersion.current().majorVersion
                )
            }
        }

        // 配置lombok.config
        // 由于Lombok插件6.1.0之后不再自动生成lombok.config文件，后续ihub插件维护该功能
        // 详见：https://github.com/freefair/gradle-plugins/issues/379
        def lombokConfig = project.file 'lombok.config'
        // 不覆盖本地lombok.config配置
        if (!lombokConfig.exists()) {
            lombokConfig.createNewFile()
            lombokConfig << '# This file is generated by the \'pub.ihub.plugin.ihub-java\' gradle plugin.\n'
            lombokConfig << [
                'config.stopBubbling'                : true,
                'lombok.addLombokGeneratedAnnotation': true,
            ].collect { k, v -> "$k = $v" }.join('\n')
        }
    }

}
