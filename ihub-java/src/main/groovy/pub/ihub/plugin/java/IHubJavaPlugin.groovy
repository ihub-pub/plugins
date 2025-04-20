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
package pub.ihub.plugin.java

import io.freefair.gradle.plugins.lombok.LombokPlugin
import net.bytebuddy.build.gradle.AbstractByteBuddyTask
import net.bytebuddy.build.gradle.AbstractByteBuddyTaskExtension
import net.bytebuddy.build.gradle.ByteBuddyPlugin
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.compile.AbstractCompile
import org.jmolecules.bytebuddy.JMoleculesPlugin
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomExtension

import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.BEFORE

/**
 * Java插件
 * @author henry
 */
@IHubPlugin(value = IHubJavaExtension, beforeApplyPlugins = [IHubJavaBasePlugin, LombokPlugin, ByteBuddyPlugin])
class IHubJavaPlugin extends IHubProjectPluginAware<IHubJavaExtension> {

    private static final Map<String, Closure> J_MOLECULES_ARCHITECTURE_DEPENDENCIES = [
        // CQRS architecture
        cqrs   : { libs -> libs.bundles.jmolecules.cqrs.get() },
        // Layered architecture
        layered: { libs -> libs.bundles.jmolecules.layered.get() },
        // Onion architecture
        onion  : { libs -> libs.bundles.jmolecules.onion.get() },
    ]

    static final Map<String, Closure> DEFAULT_DEPENDENCIES_CONFIG = [
        // 添加jaxb运行时依赖
        jaxb                     : { libs, IHubBomExtension ext ->
            ext.excludeModules {
                group 'com.sun.xml.bind' modules 'jaxb-core'
            }
            ext.dependencies {
                runtimeOnly 'javax.xml.bind:jaxb-api', 'org.glassfish.jaxb:jaxb-runtime'
            }
        },
        // 添加日志依赖配置
        log                      : { libs, IHubBomExtension ext ->
            ext.excludeModules {
                group 'commons-logging' modules 'commons-logging'
                group 'log4j' modules 'log4j'
                group 'org.apache.logging.log4j' modules 'log4j-core'
                group 'org.slf4j' modules 'slf4j-jcl', 'slf4j-log4j12'
            }
            ext.dependencies {
                implementation libs.slf4j.api.get()
                runtimeOnly libs.bundles.slf4j.runtime.get() as Object[]
            }
        },
        // 添加MapStruct依赖
        mapstruct                : { libs, IHubBomExtension ext ->
            ext.dependencies {
                implementation 'io.github.linpeilie:mapstruct-plus-spring-boot-starter'
                annotationProcessor 'io.github.linpeilie:mapstruct-plus-processor'
                annotationProcessor 'org.projectlombok:lombok-mapstruct-binding'
            }
        },
        // 添加Doc注解依赖
        doc                      : { libs, IHubBomExtension ext ->
            ext.dependencies {
                compileOnly 'io.swagger.core.v3:swagger-core-jakarta'
                annotationProcessor 'com.github.therapi:therapi-runtime-javadoc-scribe'
                annotationProcessor 'pub.ihub.integration:ihub-bytebuddy-plugin'
            }
        },
        // 添加jMolecules依赖
        jmolecules               : { libs, IHubBomExtension ext ->
            ext.dependencies {
                implementation J_MOLECULES_ARCHITECTURE_DEPENDENCIES[
                    ext.project.extensions.getByType(IHubJavaExtension).jmoleculesArchitecture.get()
                ].call(libs) as Object[]
            }
        },
        // 添加jMolecules-integrations依赖
        'jmolecules-integrations': { libs, IHubBomExtension ext ->
            ext.dependencies {
                implementation libs.bundles.jmolecules.integrations.get() as Object[]
                testImplementation libs.jmolecules.archunit.get()
            }
        },
    ]

    static final Closure JAVA_CONFIG = { IHubJavaExtension ext, AbstractCompile compile ->
        // 兼容性配置
        ext.sourceCompatibility.orNull?.with { version ->
            compile.sourceCompatibility = version
        }
        ext.targetCompatibility.orNull?.with { version ->
            compile.targetCompatibility = version
        }
        compile.options.encoding = ext.compileEncoding.get()
        compile.options.incremental = ext.gradleCompilationIncremental.get()
        compile.options.compilerArgs += ext.compilerArgs.orNull.with { args ->
            args ? args.tokenize() : []
        }
    }

    @Override
    void apply() {
        withExtension(BEFORE) { ext ->
            withTask AbstractCompile, JAVA_CONFIG.curry(ext)

            withExtension(IHubBomExtension) {
                ext.defaultDependencies.get().split(',').each { dependency ->
                    DEFAULT_DEPENDENCIES_CONFIG[dependency]?.call ext.project.ihub, it
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
            if (it.jvmArgs.present) {
                withTask(JavaExec) { exec ->
                    exec.jvmArgs it.jvmArgs.get().tokenize()
                }
            }
        }

        // 配置lombok.config
        // 由于Lombok插件6.1.0之后不再自动生成lombok.config文件，后续ihub插件维护该功能
        // 详见：https://github.com/freefair/gradle-plugins/issues/379
        afterEvaluate {
            def lombokConfig = project.file 'lombok.config'
            // 不覆盖本地lombok.config配置
            if (lombokConfig.exists()) {
                return
            }
            lombokConfig.createNewFile()
            lombokConfig << '# This file is generated by the \'pub.ihub.plugin.ihub-java\' gradle plugin.\n'
            lombokConfig << [
                'config.stopBubbling'                : true,
                'lombok.addLombokGeneratedAnnotation': true,
            ].collect { k, v -> "$k = $v" }.join('\n')
        }
    }

}
