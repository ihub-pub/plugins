/*
 * Copyright (c) 2022 Henry 李恒 (henry.box@outlook.com).
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
package pub.ihub.plugin.bom.specs


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

    abstract void compile(String type, String... dependencies)

    void api(String... dependencies) {
        compile API_CONFIGURATION_NAME, dependencies
    }

    void implementation(String... dependencies) {
        compile IMPLEMENTATION_CONFIGURATION_NAME, dependencies
    }

    void compileOnly(String... dependencies) {
        compile COMPILE_ONLY_CONFIGURATION_NAME, dependencies
    }

    void compileOnlyApi(String... dependencies) {
        compile COMPILE_ONLY_API_CONFIGURATION_NAME, dependencies
    }

    void runtimeOnly(String... dependencies) {
        compile RUNTIME_ONLY_CONFIGURATION_NAME, dependencies
    }

    void testImplementation(String... dependencies) {
        compile TEST_IMPLEMENTATION_CONFIGURATION_NAME, dependencies
    }

    void testCompileOnly(String... dependencies) {
        compile TEST_COMPILE_ONLY_CONFIGURATION_NAME, dependencies
    }

    void testRuntimeOnly(String... dependencies) {
        compile TEST_RUNTIME_ONLY_CONFIGURATION_NAME, dependencies
    }

    void annotationProcessor(String... dependencies) {
        compile ANNOTATION_PROCESSOR_CONFIGURATION_NAME, dependencies
    }

}
