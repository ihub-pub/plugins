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
package pub.ihub.plugin.java

import com.github.jengelman.gradle.plugins.processes.ProcessesPlugin
import groovy.transform.CompileStatic
import io.freefair.gradle.plugins.lombok.LombokPlugin
import net.bytebuddy.build.gradle.AbstractByteBuddyTask
import net.bytebuddy.build.gradle.AbstractByteBuddyTaskExtension
import net.bytebuddy.build.gradle.ByteBuddyPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.ProjectReportsPlugin
import org.gradle.api.reporting.plugins.BuildDashboardPlugin
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.AbstractCompile
import org.jmolecules.bytebuddy.JMoleculesPlugin
import org.springdoc.openapi.gradle.plugin.OpenApiGradlePlugin
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubPluginsPlugin
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
    IHubPluginsPlugin, IHubBomPlugin, JavaPlugin, JavaLibraryPlugin, ProjectReportsPlugin,
    BuildDashboardPlugin, ByteBuddyPlugin
])
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
                implementation 'org.mapstruct:mapstruct'
                annotationProcessor 'org.mapstruct:mapstruct-processor'
            }
        },
        // 添加Doc注解依赖
        doc                      : { libs, IHubBomExtension ext ->
            ext.dependencies {
                compileOnly libs.swagger.annotations.get()
                annotationProcessor 'pub.ihub.lib:ihub-process-doc'
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
        ext.compatibility.orNull?.with { version ->
            compile.sourceCompatibility = version
            compile.targetCompatibility = version
        }
        compile.options.encoding = ext.compileEncoding.get()
        compile.options.incremental = ext.gradleCompilationIncremental.get()
        compile.options.compilerArgs += ext.compilerArgs.orNull.with { args ->
            args ? args.tokenize() : []
        }
    }

    static final Closure JAR_CONFIG = { Project project, Jar jar ->
        jar.manifest {
            attributes(
                'Implementation-Title': project.name,
                'Automatic-Module-Name': project.name.replaceAll('-', '.'),
                'Implementation-Version': project.version,
                'Implementation-Vendor-Id': project.group,
                'Built-By': 'ihub-pub',
                'Created-By': 'Gradle ' + project.gradle.gradleVersion,
                'Build-Jdk': JavaVersion.current().toString()
            )
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

                // Groovy增量编译与Java注释处理器不能同时使用
                if (hasPlugin(GroovyPlugin) && ext.gradleCompilationIncremental.get()) {
                    it.dependencies.removeIf {
                        it.type == 'annotationProcessor'
                    }
                }
            }
        }

        withExtension(AFTER) {
            if (it.applyOpenapiPlugin.get()) {
                applyPlugin ProcessesPlugin, OpenApiGradlePlugin
            }
            if (!hasPlugin(GroovyPlugin)) {
                applyPlugin LombokPlugin
            }

            // 配置Jar属性
            withTask Jar, JAR_CONFIG.curry(project)

            if (it.jvmArgs.present) {
                withTask(JavaExec) { exec ->
                    exec.jvmArgs it.jvmArgs.get().tokenize()
                }
            }

            if (hasPlugin(ApplicationPlugin)) {
                withExtension(JavaApplication) { exec ->
                    if (exec.mainClass.present) {
                        return
                    }
                    findMainClass()?.with {
                        exec.mainClass.set it
                        logger.lifecycle "Application set main class: $it"
                    }
                }
            }
        }

        // 配置lombok.config
        // 由于Lombok插件6.1.0之后不再自动生成lombok.config文件，后续ihub插件维护该功能
        // 详见：https://github.com/freefair/gradle-plugins/issues/379
        afterEvaluate {
            if (!hasPlugin(LombokPlugin)) {
                return
            }
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

    @CompileStatic
    private String findMainClass() {
        withExtension(JavaPluginExtension).sourceSets.findByName('main').java.files.findResult { file ->
            String mainClass = null
            String packageName = ''
            file.readLines().each { line ->
                if (line.startsWith('package ')) {
                    packageName = line.substring(8, line.lastIndexOf(';'))
                }
                if (line ==~ /.*static\s+void\s+main\s*\(\s*String\[]\s+args\s*\)\s*\{.*/) {
                    String filePath = file.path
                    mainClass = packageName + '.' + filePath
                        .substring(filePath.lastIndexOf(File.separator) + 1, filePath.lastIndexOf('.'))
                }
            }
            mainClass
        }
    }

}
