/*
 * Copyright (c) 2025 the original author or authors.
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

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.ProjectReportsPlugin
import org.gradle.api.reporting.plugins.BuildDashboardPlugin
import org.gradle.api.tasks.bundling.Jar
import pub.ihub.plugin.IHubExtensionAware
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubPluginsPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomPlugin
import java.io.File

/**
 * @author liheng
 * @since 2025/4/20
 */
@IHubPlugin(
    beforeApplyPlugins = [
        IHubPluginsPlugin::class, IHubBomPlugin::class, JavaPlugin::class,
        JavaLibraryPlugin::class, ProjectReportsPlugin::class, BuildDashboardPlugin::class
    ]
)
class IHubJavaBasePlugin : IHubProjectPluginAware<IHubExtensionAware>() { // Using IHubExtensionAware as no specific extension is defined

    companion object {
        val JAR_CONFIG: (Project, Jar) -> Unit = { project, jar ->
            jar.manifest { manifest ->
                manifest.attributes(
                    mapOf(
                        "Implementation-Title" to project.name,
                        "Automatic-Module-Name" to project.name.replace('-', '.'),
                        "Implementation-Version" to project.version,
                        "Implementation-Vendor-Id" to project.group,
                        "Built-By" to "ihub-pub",
                        "Created-By" to "Gradle ${project.gradle.gradleVersion}",
                        "Build-Jdk" to JavaVersion.current().toString()
                    )
                )
            }
        }
    }

    override fun apply() {
        withExtension(EvaluateStage.AFTER) { // No specific extension type, so this action might be redundant or need a target
            // Configure Jar attributes
            withTask(Jar::class.java) { jar -> JAR_CONFIG(project, jar) }

            if (hasPlugin(ApplicationPlugin::class.java)) {
                getExtension(JavaApplication::class.java)?.let { javaApplication ->
                    if (javaApplication.mainClass.isPresent) {
                        return@let
                    }
                    findMainClass()?.let { mainClassName ->
                        javaApplication.mainClass.set(mainClassName)
                        logger.lifecycle("Application set main class: $mainClassName")
                    }
                }
            }
        }
    }

    private fun findMainClass(): String? {
        val javaPluginExtension = getExtension(JavaPluginExtension::class.java) ?: return null
        return javaPluginExtension.sourceSets.findByName("main")?.allJava?.files?.firstNotNullOfOrNull { file ->
            var mainClass: String? = null
            var packageName = ""
            file.readLines().forEach { line ->
                if (line.startsWith("package ")) {
                    packageName = line.substring(8, line.lastIndexOf(';'))
                }
                if (line.matches(Regex(""".*static\s+void\s+main\s*\(\s*String\[]\s+args\s*\)\s*\{.*"""))) {
                    val fileName = file.nameWithoutExtension
                    mainClass = if (packageName.isNotEmpty()) "$packageName.$fileName" else fileName
                    return@forEach // Found main in this file, exit loop for this file
                }
            }
            mainClass // Return mainClass if found in this file, null otherwise to continue search
        }
    }
}
