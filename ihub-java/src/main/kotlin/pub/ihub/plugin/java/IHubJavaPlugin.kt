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
import net.bytebuddy.build.gradle.ByteBuddyPlugin
import net.bytebuddy.build.gradle.ByteBuddyTaskExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.compile.AbstractCompile
import org.jmolecules.bytebuddy.JMoleculesPlugin
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomExtension

typealias DependencyConfigAction = (libs: Any, bomExt: IHubBomExtension) -> Unit

/**
 * Java插件
 * @author henry
 */
@IHubPlugin(
    value = IHubJavaExtension::class,
    beforeApplyPlugins = [IHubJavaBasePlugin::class, LombokPlugin::class, ByteBuddyPlugin::class]
)
@Suppress("TooManyFunctions")
class IHubJavaPlugin : IHubProjectPluginAware<IHubJavaExtension>() {

    companion object {
        // Using a more Kotlin-idiomatic way to represent these configurations
        private val J_MOLECULES_ARCHITECTURE_DEPENDENCIES: Map<String, (Any) -> Any?> = mapOf(
            "cqrs" to { libs -> (libs as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("bundles")?.let { it1 -> (it1 as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("jmolecules")?.let { it2 -> (it2 as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("cqrs")?.let { it3 -> (it3 as Provider<*>).orNull } } } },
            "layered" to { libs -> (libs as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("bundles")?.let { it1 -> (it1 as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("jmolecules")?.let { it2 -> (it2 as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("layered")?.let { it3 -> (it3 as Provider<*>).orNull } } } },
            "onion" to { libs -> (libs as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("bundles")?.let { it1 -> (it1 as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("jmolecules")?.let { it2 -> (it2 as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("onion")?.let { it3 -> (it3 as Provider<*>).orNull } } } }
        )


        val DEFAULT_DEPENDENCIES_CONFIG: Map<String, DependencyConfigAction> = mapOf(
            "jaxb" to { _, bomExt ->
                bomExt.excludeModules { it.group("com.sun.xml.bind").modules("jaxb-core") }
                bomExt.dependencies { it.runtimeOnly("javax.xml.bind:jaxb-api", "org.glassfish.jaxb:jaxb-runtime") }
            },
            "log" to { libs, bomExt ->
                bomExt.excludeModules {
                    it.group("commons-logging").modules("commons-logging")
                    it.group("log4j").modules("log4j")
                    it.group("org.apache.logging.log4j").modules("log4j-core")
                    it.group("org.slf4j").modules("slf4j-jcl", "slf4j-log4j12")
                }
                val slf4jApi = (libs as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("slf4j")?.let { (it as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("api")?.let { it1 -> (it1 as Provider<*>).orNull } }
                val slf4jRuntimeBundle = (libs as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("bundles")?.let { (it as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("slf4j")?.let { it1 -> (it1 as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("runtime")?.let { it2 -> (it2 as Provider<*>).orNull } } }

                bomExt.dependencies { depSpec ->
                    slf4jApi?.let { depSpec.implementation(it) }
                    slf4jRuntimeBundle?.let { bundle ->
                        if (bundle is Iterable<*>) depSpec.runtimeOnly(*bundle.filterNotNull().map { it.toString() }.toTypedArray()) else depSpec.runtimeOnly(bundle.toString())
                    }
                }
            },
            "mapstruct" to { _, bomExt ->
                bomExt.dependencies {
                    it.implementation("io.github.linpeilie:mapstruct-plus-spring-boot-starter")
                    it.annotationProcessor("io.github.linpeilie:mapstruct-plus-processor")
                    it.annotationProcessor("org.projectlombok:lombok-mapstruct-binding")
                }
            },
            "doc" to { _, bomExt ->
                bomExt.dependencies {
                    it.compileOnly("io.swagger.core.v3:swagger-core-jakarta")
                    it.annotationProcessor("com.github.therapi:therapi-runtime-javadoc-scribe")
                    it.annotationProcessor("pub.ihub.integration:ihub-bytebuddy-plugin")
                }
            },
            "jmolecules" to { libs, bomExt ->
                val javaExt = bomExt.project.extensions.getByType(IHubJavaExtension::class.java)
                val architecture = javaExt.jmoleculesArchitecture.get()
                val dependencyProvider = J_MOLECULES_ARCHITECTURE_DEPENDENCIES[architecture]
                if (dependencyProvider != null) {
                    val deps = dependencyProvider(libs)
                    bomExt.dependencies { depSpec ->
                        if (deps is Iterable<*>) depSpec.implementation(*deps.filterNotNull().map { it.toString() }.toTypedArray()) else deps?.let { depSpec.implementation(it.toString()) }
                    }
                }
            },
            "jmolecules-integrations" to { libs, bomExt ->
                 val jmoleculesIntegrationsBundle = (libs as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("bundles")?.let { (it as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("jmolecules")?.let { it1 -> (it1 as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("integrations")?.let { it2 -> (it2 as Provider<*>).orNull } } }
                 val jmoleculesArchunit = (libs as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("jmolecules")?.let { (it as ExtensionAware).extensions.getByType(org.gradle.api.plugins.ExtensionsSchema::class.java).findByName("archunit")?.let { it1 -> (it1 as Provider<*>).orNull } }
                bomExt.dependencies { depSpec ->
                    jmoleculesIntegrationsBundle?.let { bundle ->
                         if (bundle is Iterable<*>) depSpec.implementation(*bundle.filterNotNull().map { it.toString() }.toTypedArray()) else depSpec.implementation(bundle.toString())
                    }
                    jmoleculesArchunit?.let { depSpec.testImplementation(it) }
                }
            }
        )

        val JAVA_CONFIG: (IHubJavaExtension, AbstractCompile) -> Unit = { ext, compile ->
            ext.sourceCompatibility.orNull?.let { compile.sourceCompatibility = it }
            ext.targetCompatibility.orNull?.let { compile.targetCompatibility = it }
            compile.options.encoding = ext.compileEncoding.get()
            compile.options.isIncremental = ext.gradleCompilationIncremental.get()
            ext.compilerArgs.orNull?.let { args ->
                compile.options.compilerArgs.addAll(args.split(' ').filter { it.isNotBlank() })
            }
        }
    }

    override fun apply() {
        withExtension(EvaluateStage.BEFORE) { javaExt ->
            withTask(AbstractCompile::class.java) { compileTask -> JAVA_CONFIG(javaExt, compileTask) }

            getExtension(IHubBomExtension::class.java)?.let { bomExt ->
                val ihubLibs = project.extensions.findByName("ihub") // This is how 'ihub.bundles...' was accessed in Groovy
                javaExt.defaultDependencies.get().split(',').map { it.trim() }.filter { it.isNotEmpty() }.forEach { dependencyName ->
                    DEFAULT_DEPENDENCIES_CONFIG[dependencyName]?.invoke(ihubLibs ?: project, bomExt)
                    if ("jmolecules" == dependencyName) {
                        project.extensions.findByType(ByteBuddyTaskExtension::class.java)?.transformation {
                            it.plugin = JMoleculesPlugin::class.java
                        }
                        project.tasks.withType(AbstractByteBuddyTask::class.java).configureEach { byteBuddyTask ->
                            project.tasks.findByName("classes")?.dependsOn(byteBuddyTask)
                        }
                    }
                }
            }
        }

        withExtension(EvaluateStage.AFTER) { javaExt ->
            if (javaExt.jvmArgs.isPresent) {
                withTask(JavaExec::class.java) { exec ->
                    exec.jvmArgs(javaExt.jvmArgs.get().split(' ').filter { it.isNotBlank() })
                }
            }
        }

        project.afterEvaluate {
            val lombokConfigFile = project.file("lombok.config")
            if (!lombokConfigFile.exists()) {
                lombokConfigFile.createNewFile()
                lombokConfigFile.writeText(
                    """
                    # This file is generated by the 'pub.ihub.plugin.ihub-java' gradle plugin.
                    config.stopBubbling = true
                    lombok.addLombokGeneratedAnnotation = true
                    """.trimIndent()
                )
            }
        }
    }
}
