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

import org.gradle.api.plugins.GroovyPlugin
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubPluginsExtension
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.java.IHubJavaPlugin



/**
 * Groovy插件
 * @author liheng
 */
@IHubPlugin(beforeApplyPlugins = [IHubJavaPlugin, GroovyPlugin])
class IHubGroovyPlugin extends IHubProjectPluginAware {

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
            withExtension(IHubPluginsExtension) { ext ->
                it.dependencies {
                    implementation((ext.compileGroovyAllModules ? ['groovy-all'] : [
                        'groovy',
                        'groovy-datetime',
                        'groovy-dateutil',
                        'groovy-groovydoc',
                        'groovy-json',
                        'groovy-nio',
                        'groovy-sql',
                        'groovy-templates',
                        'groovy-xml',
                    ]).collect { "org.codehaus.groovy:$it" } as String[])
                }
            }
        }
    }

}
