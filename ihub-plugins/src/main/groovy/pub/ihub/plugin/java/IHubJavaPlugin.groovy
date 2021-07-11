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
package pub.ihub.plugin.java

import io.freefair.gradle.plugins.lombok.LombokPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.ProjectReportsPlugin
import org.gradle.api.reporting.plugins.BuildDashboardPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.AbstractCompile
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubPluginsExtension
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.bom.IHubBomPlugin



/**
 * Java插件
 * @author henry
 */
@IHubPlugin(beforeApplyPlugins = [
    IHubBomPlugin, JavaPlugin, JavaLibraryPlugin, LombokPlugin, ProjectReportsPlugin, BuildDashboardPlugin
])
class IHubJavaPlugin extends IHubProjectPluginAware {

    @Override
    void apply() {
        withExtension(IHubPluginsExtension) { iHubExt ->
            // 兼容性配置
            iHubExt.javaCompatibility?.with { version ->
                withTask(AbstractCompile) {
                    it.sourceCompatibility = version
                    it.targetCompatibility = version
                    it.options.encoding = 'UTF-8'
                    it.options.incremental = iHubExt.gradleCompilationIncremental
                }
            }

            // 添加jaxb运行时依赖
            if (iHubExt.javaJaxbRuntime) {
                withExtension(IHubBomExtension) {
                    it.excludeModules {
                        group 'com.sun.xml.bind' modules 'jaxb-core'
                    }
                    it.dependencies {
                        runtimeOnly 'javax.xml.bind:jaxb-api', 'org.glassfish.jaxb:jaxb-runtime'
                    }
                }
            }
        }

        // 配置Jar属性
        withTask(Jar) {
            it.manifest {
                attributes(
                    'Implementation-Title': project.name,
                    'Automatic-Module-Name': project.name.replaceAll('-', '.'),
                    'Implementation-Version': project.version,
                    'Implementation-Vendor': 'IHub',
                    'Created-By': 'Java ' + JavaVersion.current().majorVersion
                )
            }
        }
    }

}
