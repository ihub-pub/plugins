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
package pub.ihub.plugin.groovy

import org.gradle.api.plugins.GroovyPlugin
import pub.ihub.plugin.IHubExtensionAware
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.java.IHubJavaBasePlugin

/**
 * Groovy插件
 * @author liheng
 */
@IHubPlugin(beforeApplyPlugins = [IHubJavaBasePlugin::class, GroovyPlugin::class])
class IHubGroovyPlugin : IHubProjectPluginAware<IHubExtensionAware>() { // Using IHubExtensionAware as no specific extension is defined for this plugin itself

    override fun apply() {
        withExtension(IHubBomExtension::class.java) { bomExtension ->
            // Assuming ihub.bundles.groovy.get() returns something convertible to varargs for dependencies
            // In Groovy, `ihub.bundles.groovy.get() as Object[]` casts the result to Object[].
            // In Kotlin, we need to ensure the type is correct for the `dependencies` block.
            // If `ihub.bundles.groovy.get()` returns a list or a single dependency string,
            // it needs to be passed correctly.
            // The `dependencies` block in `IHubBomExtension`'s `DependencySpec` takes `vararg dependencies: Any`.
            val groovyBundle = project.extensions.findByName("ihub")?.let { ihubExtension ->
                (ihubExtension as? org.gradle.api.plugins.ExtensionAware)?.extensions?.findByName("bundles")?.let { bundlesExtension ->
                    (bundlesExtension as? org.gradle.api.plugins.ExtensionAware)?.extensions?.findByName("groovy")?.let { groovyVersionCatalog ->
                        (groovyVersionCatalog as? org.gradle.api.provider.Provider<*>)?.orNull
                    }
                }
            }

            if (groovyBundle != null) {
                bomExtension.dependencies { dependencySpec ->
                    // If groovyBundle is a single string or a Provider<String>
                    // dependencySpec.implementation(groovyBundle)
                    // If groovyBundle is a list/iterable of strings:
                    // dependencySpec.implementation(*(groovyBundle as List<String>).toTypedArray())
                    // Given `as Object[]` in Groovy, it's likely a collection.
                    // For safety, let's assume it could be a single item or a collection.
                    when (groovyBundle) {
                        is Iterable<*> -> dependencySpec.implementation(*groovyBundle.filterNotNull().map { it.toString() }.toTypedArray())
                        else -> dependencySpec.implementation(groovyBundle.toString())
                    }
                }
            } else {
                logger.warn("Could not find 'ihub.bundles.groovy' version catalog accessor.")
            }
        }
    }
}
