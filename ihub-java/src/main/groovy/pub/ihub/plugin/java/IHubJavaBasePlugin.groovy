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

import groovy.transform.CompileStatic
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
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubPluginsPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomPlugin

import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER

/**
 * @author liheng
 * @since 2025/4/20
 */
@IHubPlugin(beforeApplyPlugins = [
    IHubPluginsPlugin, IHubBomPlugin, JavaPlugin, JavaLibraryPlugin, ProjectReportsPlugin, BuildDashboardPlugin
])
class IHubJavaBasePlugin extends IHubProjectPluginAware {

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
    protected void apply() {
        withExtension(AFTER) {
            // 配置Jar属性
            withTask Jar, JAR_CONFIG.curry(project)

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
