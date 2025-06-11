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
import org.gradle.api.plugins.JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME
import org.gradle.api.plugins.JavaPlugin.API_CONFIGURATION_NAME
import org.gradle.api.plugins.JavaPlugin.COMPILE_ONLY_API_CONFIGURATION_NAME
import org.gradle.api.plugins.JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME
import org.gradle.api.plugins.JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME
import org.gradle.api.plugins.JavaPlugin.RUNTIME_ONLY_CONFIGURATION_NAME
import org.gradle.api.plugins.JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME
import org.gradle.api.plugins.JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME
import org.gradle.api.plugins.JavaPlugin.TEST_RUNTIME_ONLY_CONFIGURATION_NAME
import pub.ihub.plugin.bom.impl.Dependency

/**
 * Dependencies Spec
 * @author henry
 */
interface DependencySpec : ActionSpec<Dependency> {

    val project: Project // Made abstract project a property

    fun compile(type: String, vararg dependencies: Any) // vararg matches spread operator in Groovy

    fun api(vararg dependencies: Any) {
        compile(API_CONFIGURATION_NAME, *dependencies)
    }

    fun implementation(vararg dependencies: Any) {
        compile(IMPLEMENTATION_CONFIGURATION_NAME, *dependencies)
    }

    fun compileOnly(vararg dependencies: Any) {
        compile(COMPILE_ONLY_CONFIGURATION_NAME, *dependencies)
    }

    fun compileOnlyApi(vararg dependencies: Any) {
        compile(COMPILE_ONLY_API_CONFIGURATION_NAME, *dependencies)
    }

    fun runtimeOnly(vararg dependencies: Any) {
        compile(RUNTIME_ONLY_CONFIGURATION_NAME, *dependencies)
    }

    fun testImplementation(vararg dependencies: Any) {
        compile(TEST_IMPLEMENTATION_CONFIGURATION_NAME, *dependencies)
    }

    fun testCompileOnly(vararg dependencies: Any) {
        compile(TEST_COMPILE_ONLY_CONFIGURATION_NAME, *dependencies)
    }

    fun testRuntimeOnly(vararg dependencies: Any) {
        compile(TEST_RUNTIME_ONLY_CONFIGURATION_NAME, *dependencies)
    }

    fun annotationProcessor(vararg dependencies: Any) {
        compile(ANNOTATION_PROCESSOR_CONFIGURATION_NAME, *dependencies)
    }
}
