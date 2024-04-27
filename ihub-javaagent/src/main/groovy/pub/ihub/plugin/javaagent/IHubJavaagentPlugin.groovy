/*
 * Copyright (c) 2023-2024 the original author or authors.
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
package pub.ihub.plugin.javaagent

import com.ryandens.javaagent.JavaagentApplicationPlugin
import com.ryandens.javaagent.JavaagentBasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.tasks.JavaExec
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomPlugin

import static com.ryandens.javaagent.JavaForkOptionsConfigurer.configureJavaForkOptions
import static com.ryandens.javaagent.JavaagentBasePlugin.CONFIGURATION_NAME
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER

/**
 * javaagent插件
 */
@IHubPlugin(value = IHubJavaagentExtension, beforeApplyPlugins = IHubBomPlugin)
class IHubJavaagentPlugin extends IHubProjectPluginAware<IHubJavaagentExtension> {

    @Override
    protected void apply() {
        if (hasPlugin(ApplicationPlugin)) {
            applyPlugin JavaagentApplicationPlugin as Class<Plugin<Project>>
        }
        if (project.plugins.hasPlugin('org.springframework.boot')) {
            configureJavaExec 'bootRun'
        }
        withExtension(AFTER) { ext ->
            if (ext.javaagent.present) {
                Dependency dependency = project.dependencies.create ext.javaagent.get()
                project.dependencies {
                    javaagent group: dependency.group, name: dependency.name, version: dependency.version,
                        classifier: ext.classifier.get()
                }
            }
        }
    }

    private void configureJavaExec(String taskName) {
        applyPlugin JavaagentBasePlugin as Class<Plugin<Project>>
        project.tasks.named(taskName, JavaExec).configure javaExecClosure
    }

    private final Closure javaExecClosure = { JavaExec exec ->
        configureJavaForkOptions exec, project.configurations.named(CONFIGURATION_NAME).map { it.files }
    }

}
