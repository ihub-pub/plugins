/*
 * Copyright (c) 2023 the original author or authors.
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
import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.tasks.JavaExec
import pub.ihub.plugin.IHubDependencyAware
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware

import static com.ryandens.javaagent.JavaForkOptionsConfigurer.configureJavaForkOptions
import static com.ryandens.javaagent.JavaagentBasePlugin.CONFIGURATION_NAME
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER

/**
 * javaagent插件
 */
@IHubPlugin(IHubJavaagentExtension)
@CompileStatic
class IHubJavaagentPlugin extends IHubProjectPluginAware<IHubJavaagentExtension> implements IHubDependencyAware {

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
                compile CONFIGURATION_NAME, ext.javaagent.get()
            }
        }
    }

    private void configureJavaExec(String taskName) {
        applyPlugin JavaagentBasePlugin as Class<Plugin<Project>>
        project.tasks.named(taskName, JavaExec).configure CONFIGURE_JAVA_EXEC
    }

    private static final Closure CONFIGURE_JAVA_EXEC = { JavaExec exec ->
        configureJavaForkOptions exec, project.configurations.named(CONFIGURATION_NAME).map { it.files }
    }

}
