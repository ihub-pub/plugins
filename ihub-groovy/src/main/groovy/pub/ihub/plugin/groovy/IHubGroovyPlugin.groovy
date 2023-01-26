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
            if (withExtension(IHubPluginsExtension).enableGroovy3) {
                it.importBoms {
                    group 'org.codehaus.groovy' module 'groovy-bom' version '3.0.13'
                    group 'org.spockframework' module 'spock-bom' version '2.3-groovy-3.0'
                }
                it.dependencyVersions {
                    group 'org.codehaus.groovy' modules 'groovy-all' version '3.0.13'
                    group 'com.athaydes' modules 'spock-reports' version '2.3.2-groovy-3.0'
                }
            }
            it.dependencies {
                implementation((withExtension(IHubPluginsExtension).compileGroovyAllModules ? ['groovy-all'] : [
                    'groovy',
                    'groovy-datetime',
                    'groovy-dateutil',
                    'groovy-groovydoc',
                    'groovy-json',
                    'groovy-nio',
                    'groovy-sql',
                    'groovy-templates',
                    'groovy-xml',
                ]).collect { "${withExtension(IHubPluginsExtension).enableGroovy3 ? 'org.codehaus.groovy' : 'org.apache.groovy'}:$it" } as String[])
            }
        }
    }

}
