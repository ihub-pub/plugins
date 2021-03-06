/*
 * Copyright (c) 2021 Henry 李恒 (henry.box@outlook.com).
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
package pub.ihub.plugin.spring

import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.java.IHubJavaPlugin

import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER



/**
 * IHub Spring Boot Plugin
 * @author henry
 */
@IHubPlugin(value = IHubBootExtension, beforeApplyPlugins = [IHubJavaPlugin, SpringBootPlugin])
class IHubBootPlugin extends IHubProjectPluginAware<IHubBootExtension> {

    @Override
    void apply() {
        withExtension(AFTER) { ext ->
            withTask(BootRun) {
                ext.systemProperties it
            }

            withTask(BootJar) {
                it.requiresUnpack ext.bootJarRequiresUnpack
            }
        }

        project.bootBuildImage {
            builder = 'paketobuildpacks/builder:tiny'
        }
    }

}
