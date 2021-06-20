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
package pub.ihub.plugin.groovy

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import pub.ihub.plugin.IHubProjectPlugin
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.java.IHubJavaBasePlugin



/**
 * Groovy插件
 * @author liheng
 */
class IHubGroovyPlugin extends IHubProjectPlugin<IHubGroovyExtension> {

    Class<? extends Plugin<Project>>[] beforeApplyPlugins = [IHubJavaBasePlugin, GroovyPlugin]
    String extensionName = 'iHubGroovy'

    @Override
    void apply() {
        withExtension(IHubBomExtension) {
            String groovyGroup = 'org.codehaus.groovy'
            String groovyVersion = '3.0.8'
            it.importBoms {
                group groovyGroup module 'groovy-bom' version groovyVersion
            }
            it.dependencyVersions {
                group groovyGroup modules 'groovy-all' version groovyVersion
            }
            // 由于codenarc插件内强制指定了groovy版本，groovy3.0需要强制指定版本
            if (it.findVersion(groovyGroup, groovyVersion).startsWith('3.')) {
                it.groupVersions {
                    group groovyGroup version groovyVersion
                }
            }
            withExtension(IHubGroovyExtension) { ext ->
                it.dependencies {
                    implementation ext.modules.unique().collect { "org.codehaus.groovy:$it" } as String[]
                }
            }
        }
    }

}
