/*
 * Copyright (c) 2021-2023 the original author or authors.
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
package pub.ihub.plugin.verification

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.attributes.TestSuiteType
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.TestReportAggregationPlugin
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.testing.AggregateTestReport
import org.gradle.api.tasks.testing.Test
import org.gradle.buildinit.plugins.internal.modifiers.BuildInitTestFramework
import pub.ihub.plugin.IHubDependencyAware
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.bom.IHubBomPlugin

import static org.gradle.api.plugins.JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.TEST_RUNTIME_ONLY_CONFIGURATION_NAME
import static org.gradle.api.plugins.TestReportAggregationPlugin.TEST_REPORT_AGGREGATION_CONFIGURATION_NAME
import static org.gradle.buildinit.plugins.internal.modifiers.BuildInitTestFramework.*
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER

/**
 * 测试插件
 * @author henry
 */
@IHubPlugin(value = IHubTestExtension, beforeApplyPlugins = [IHubBomPlugin])
@SuppressWarnings('UnnecessaryObjectReferences')
class IHubTestPlugin extends IHubProjectPluginAware<IHubTestExtension> implements IHubDependencyAware {

    private final Map<BuildInitTestFramework, Action<IHubBomExtension>> testDependenciesMapping = [
        (SPOCK)        : { IHubBomExtension iHubBom ->
            applyPlugin GroovyPlugin
            compile TEST_IMPLEMENTATION_CONFIGURATION_NAME, 'org.spockframework:spock-spring'
            compile TEST_RUNTIME_ONLY_CONFIGURATION_NAME, 'com.athaydes:spock-reports', 'org.springframework.boot:spring-boot-starter-test'
        },
        (JUNIT_JUPITER): { IHubBomExtension iHubBom ->
            compile TEST_IMPLEMENTATION_CONFIGURATION_NAME, 'org.junit.jupiter:junit-jupiter'
        }
    ]

    @Override
    void apply() {
        BuildInitTestFramework testFramework = extension.testFramework
            .getOrElse(hasPlugin(GroovyPlugin) ? SPOCK : hasPlugin(JavaPlugin) ? JUNIT_JUPITER : NONE)
        testDependenciesMapping[testFramework]?.with {
            withExtension IHubBomExtension, it
        }

        if (project != project.rootProject) {
            configTestAggregation project
        }

        withExtension(AFTER) { ext ->
            withTask('test') { Test it ->
                ext.systemProperties it, '.test-java-local.properties'

                it.useJUnitPlatform()
                if (ext.classes.present) {
                    ext.classes.get().tokenize(',').each { testClass ->
                        it.include testClass
                    }
                } else {
                    it.include '**/*Test*', '**/*FT*', '**/*UT*'
                }

                it.forkEvery = ext.forkEvery.get()
                it.maxParallelForks = ext.maxParallelForks.get()
                it.enabled = ext.enabled.get()
                it.debug = ext.debug.get()
                it.failFast = ext.failFast.get()

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

    private void configTestAggregation(Project project) {
        project.rootProject.with {
            pluginManager.apply TestReportAggregationPlugin
            compile it, TEST_REPORT_AGGREGATION_CONFIGURATION_NAME, project
            extensions.getByType(ReportingExtension).reports {
                testAggregateTestReport(AggregateTestReport) {
                    testType = TestSuiteType.UNIT_TEST
                }
            }
        }
    }

}
