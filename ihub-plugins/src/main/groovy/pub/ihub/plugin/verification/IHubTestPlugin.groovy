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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.tasks.testing.Test
import pub.ihub.plugin.IHubProjectPlugin
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.bom.IHubBomPlugin

import static pub.ihub.plugin.IHubProjectPlugin.EvaluateStage.AFTER



/**
 * 测试插件
 * @author henry
 */
@SuppressWarnings('UnnecessaryObjectReferences')
class IHubTestPlugin extends IHubProjectPlugin<IHubTestExtension> {

    Class<? extends Plugin<Project>>[] beforeApplyPlugins = [IHubBomPlugin]
    String extensionName = 'iHubTest'

    @Override
    void apply() {
        withExtension(IHubBomExtension) {
            if (project.plugins.hasPlugin(GroovyPlugin)) {
                it.importBoms {
                    group 'org.spockframework' module 'spock-bom' version '2.0-M4-groovy-3.0'
                }
                it.dependencyVersions {
                    group 'com.athaydes' modules 'spock-reports' version '2.0.1-RC3'
                }
                it.dependencies {
                    testImplementation 'org.spockframework:spock-spring'
                    testRuntimeOnly 'com.athaydes:spock-reports'
                }
            } else {
                it.dependencies {
                    testImplementation 'org.junit.jupiter:junit-jupiter'
                }
            }
        }
        withExtension(AFTER) { ext ->
            withTask('test') { Test it ->
                ext.systemProperties it

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
