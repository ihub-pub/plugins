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
package pub.ihub.plugin.verification

import org.gradle.api.Action
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.tasks.testing.Test
import org.gradle.buildinit.plugins.internal.modifiers.BuildInitTestFramework
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.bom.IHubBomPlugin

import static org.gradle.buildinit.plugins.internal.modifiers.BuildInitTestFramework.JUNIT_JUPITER
import static org.gradle.buildinit.plugins.internal.modifiers.BuildInitTestFramework.SPOCK
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER



/**
 * 测试插件
 * @author henry
 */
@IHubPlugin(value = IHubTestExtension, beforeApplyPlugins = [IHubBomPlugin])
@SuppressWarnings('UnnecessaryObjectReferences')
class IHubTestPlugin extends IHubProjectPluginAware<IHubTestExtension> {

    private final Map<BuildInitTestFramework, Action<IHubBomExtension>> testDependenciesMapping = [
        (SPOCK)        : { IHubBomExtension iHubBom ->
            applyPlugin GroovyPlugin
            iHubBom.dependencies {
                testImplementation 'org.spockframework:spock-spring'
                testRuntimeOnly 'com.athaydes:spock-reports'
            }
        },
        (JUNIT_JUPITER): { IHubBomExtension iHubBom ->
            iHubBom.dependencies {
                testImplementation 'org.junit.jupiter:junit-jupiter'
            }
        }
    ]

    @Override
    void apply() {
        testDependenciesMapping[extension.testFramework]?.with {
            withExtension IHubBomExtension, it
        }

        withExtension(AFTER) { ext ->
            withTask('test') { Test it ->
                ext.systemProperties it, '.test-java-local.properties'

                it.useJUnitPlatform()
                if (ext.classes) {
                    ext.classes.tokenize(',').each { testClass ->
                        it.include testClass
                    }
                } else {
                    it.include '**/*Test*', '**/*FT*', '**/*UT*'
                }

                it.forkEvery = ext.forkEvery
                it.maxParallelForks = ext.maxParallelForks
                it.enabled = ext.enabled
                it.debug = ext.debug
                it.failFast = ext.failFast

                it.onOutput { descriptor, event ->
                    it.logger.lifecycle event.message
                }
            }

            withTask(Test) {
                // 这是为了解决在项目根目录上执行test时Jacoco找不到依赖的类的问题
                it.systemProperties.'user.dir' = it.workingDir
            }
        }
    }

}
