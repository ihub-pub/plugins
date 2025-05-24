/*
 * Copyright (c) 2022-2024 the original author or authors.
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
package pub.ihub.plugin.spring

import org.gradle.api.provider.Property
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubPluginsExtension
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.java.IHubJavaPlugin

import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER

/**
 * IHub Spring Boot Plugin
 * 参考官方入门文档：https://docs.spring.io/spring-boot/docs/3.0.0/gradle-plugin/reference/htmlsingle/
 * @author henry
 */
@IHubPlugin(value = IHubBootExtension, beforeApplyPlugins = [IHubJavaPlugin, SpringBootPlugin])
@SuppressWarnings('UnnecessaryObjectReferences')
class IHubBootPlugin extends IHubProjectPluginAware<IHubBootExtension> {

    @Override
    void apply() {
        withExtension(IHubBomExtension).dependencies {
            testImplementation 'org.spockframework:spock-spring'
            testRuntimeOnly 'org.springframework.boot:spring-boot-starter-test'
        }
        withExtension(AFTER) { ext ->
            withTask(BootRun) {
                ext.systemProperties it, '.boot-java-local.properties'
                Property<String> profile = withExtension(IHubPluginsExtension).profile
                if (profile.present) {
                    it.systemProperties.put 'spring.profiles.active', profile.get()
                }
                it.optimizedLaunch = ext.runOptimizedLaunch.get()
            }

            withTask(BootJar) {
                it.requiresUnpack ext.bootJarRequiresUnpack.get()
            }
            withTask('jar') {
                it.enabled = false
            }
        }
    }

}
