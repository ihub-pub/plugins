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
package pub.ihub.plugin.profiles

import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.language.jvm.tasks.ProcessResources
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubPluginsExtension
import pub.ihub.plugin.IHubPluginsPlugin
import pub.ihub.plugin.IHubProjectPluginAware

/**
 * IHub配置文件插件
 * @author henry
 */
@IHubPlugin(value = IHubProfilesExtension, beforeApplyPlugins = IHubPluginsPlugin)
class IHubProfilesPlugin extends IHubProjectPluginAware<IHubProfilesExtension> {

    @Override
    protected void apply() {
        def profile = project.extensions.getByType(IHubPluginsExtension).profile
        if (profile.present) {
            extension.tokens.put 'profile', profile.get()
        }
        afterEvaluate {
            withTask(ProcessResources) {
                it.filter ReplaceTokens, tokens: extension.tokens.get()
            }
        }
    }

}
