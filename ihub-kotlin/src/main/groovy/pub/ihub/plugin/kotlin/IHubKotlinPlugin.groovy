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
package pub.ihub.plugin.kotlin

import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.java.IHubJavaBasePlugin

/**
 * Kotlin插件
 * @author henry
 */
@IHubPlugin(beforeApplyPlugins = IHubJavaBasePlugin)
class IHubKotlinPlugin extends IHubProjectPluginAware {

    @Override
    protected void apply() {
        project.pluginManager.apply 'org.jetbrains.kotlin.jvm'
    }

}
