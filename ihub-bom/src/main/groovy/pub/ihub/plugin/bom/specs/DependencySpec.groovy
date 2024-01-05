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
package pub.ihub.plugin.bom.specs

import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import pub.ihub.plugin.bom.impl.Dependency

import static org.gradle.api.plugins.JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.API_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.COMPILE_ONLY_API_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.RUNTIME_ONLY_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.TEST_RUNTIME_ONLY_CONFIGURATION_NAME

/**
 * Dependencies Spec
 * @author henry
 */
trait DependencySpec implements ActionSpec<Dependency> {

    abstract Project getProject()

    abstract void compile(String type, ... dependencies)

    void api(... dependencies) {
        compile API_CONFIGURATION_NAME, dependencies
    }

    void implementation(... dependencies) {
        compile IMPLEMENTATION_CONFIGURATION_NAME, dependencies
    }

    void compileOnly(... dependencies) {
        compile COMPILE_ONLY_CONFIGURATION_NAME, dependencies
    }

    void compileOnlyApi(... dependencies) {
        compile COMPILE_ONLY_API_CONFIGURATION_NAME, dependencies
    }

    void runtimeOnly(... dependencies) {
        compile RUNTIME_ONLY_CONFIGURATION_NAME, dependencies
    }

    void testImplementation(... dependencies) {
        compile TEST_IMPLEMENTATION_CONFIGURATION_NAME, dependencies
    }

    void testCompileOnly(... dependencies) {
        compile TEST_COMPILE_ONLY_CONFIGURATION_NAME, dependencies
    }

    void testRuntimeOnly(... dependencies) {
        compile TEST_RUNTIME_ONLY_CONFIGURATION_NAME, dependencies
    }

    void annotationProcessor(... dependencies) {
        // Groovy增量编译与Java注释处理器不能同时使用
        if (project.plugins.hasPlugin(GroovyPlugin) && 'false' != project
            .findProperty('iHubJava.gradleCompilationIncremental')?.toString()) {
            return
        }
        compile ANNOTATION_PROCESSOR_CONFIGURATION_NAME, dependencies
    }

}
