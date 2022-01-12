/*
 * Copyright (c) 2022 Henry 李恒 (henry.box@outlook.com).
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
package pub.ihub.plugin.doc

import com.github.jengelman.gradle.plugins.processes.ProcessesPlugin
import org.springdoc.openapi.gradle.plugin.OpenApiGradlePlugin
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.java.IHubJavaExtension
import pub.ihub.plugin.java.IHubJavaPlugin

import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER



/**
 * 文档插件
 * @author henry
 */
@IHubPlugin(value = IHubDocExtension, beforeApplyPlugins = IHubJavaPlugin)
class IHubDocPlugin extends IHubProjectPluginAware<IHubDocExtension> {

    @Override
    void apply() {
        withExtension(IHubJavaExtension) {
            it.byteBuddyPlugins.put SwaggerByteBuddyPlugin, [sourcePath: project.projectDir]
        }
        withExtension(AFTER) {
            if (it.applyOpenapiPlugin) {
                applyPlugin ProcessesPlugin, OpenApiGradlePlugin
            }
        }
    }

}
