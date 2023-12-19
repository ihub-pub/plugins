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
package pub.ihub.plugin.shadow

import com.github.jengelman.gradle.plugins.shadow.ShadowApplicationPlugin
import com.github.jengelman.gradle.plugins.shadow.ShadowExtension
import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.internal.JavaJarExec
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.transform.CompileStatic
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.java.IHubJavaPlugin

import static com.github.jengelman.gradle.plugins.shadow.ShadowApplicationPlugin.SHADOW_RUN_TASK_NAME
import static com.github.jengelman.gradle.plugins.shadow.ShadowBasePlugin.CONFIGURATION_NAME
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER

/**
 * IHub Shadow Plugin
 */
@IHubPlugin(value = IHubShadowExtension, beforeApplyPlugins = [IHubJavaPlugin, ShadowPlugin])
class IHubShadowPlugin extends IHubProjectPluginAware<IHubShadowExtension> {

    @Override
    protected void apply() {
        withExtension(AFTER) { ext ->
            if (hasPlugin(ShadowApplicationPlugin)) {
                withTask(SHADOW_RUN_TASK_NAME) { JavaJarExec it ->
                    ext.systemProperties it, '.shadow-java-local.properties'
                }
                withTask('jar') {
                    it.enabled = false
                }
            }

            withTask(ShadowJar) {
                findAgentClass()?.with { premainClass, agentmainClass ->
                    if (premainClass) {
                        it.manifest.attributes 'Premain-Class': premainClass
                    }
                    if (agentmainClass) {
                        it.manifest.attributes 'Agent-Class': agentmainClass
                    }
                    it.manifest.attributes 'Can-Redefine-Classes': true, 'Can-Retransform-Classes': true
                }
            }

            if (hasPlugin(MavenPublishPlugin)) {
                withExtension(ShadowExtension).component withExtension(PublishingExtension)
                    .publications.maybeCreate(CONFIGURATION_NAME, MavenPublication)
            }
        }
    }

    @CompileStatic
    private Tuple2<String, String> findAgentClass() {
        String premainClass = null, agentmainClass = null
        withExtension(JavaPluginExtension).sourceSets.findByName('main').java.files.find { file ->
            String packageName = ''
            file.readLines().each { line ->
                if (line.startsWith('package ')) {
                    packageName = line.substring(8, line.lastIndexOf(';'))
                }
                if (line ==~ /.*static\s+void\s+premain\s*\(.+\)\s*\{.*/) {
                    String filePath = file.path
                    premainClass = packageName + '.' + filePath
                        .substring(filePath.lastIndexOf(File.separator) + 1, filePath.lastIndexOf('.'))
                }
                if (line ==~ /.*static\s+void\s+agentmain\s*\(.+\)\s*\{.*/) {
                    String filePath = file.path
                    agentmainClass = packageName + '.' + filePath
                        .substring(filePath.lastIndexOf(File.separator) + 1, filePath.lastIndexOf('.'))
                }
            }
            premainClass && agentmainClass
        }
        premainClass || agentmainClass ? Tuple.tuple(premainClass, agentmainClass) : null
    }

}
