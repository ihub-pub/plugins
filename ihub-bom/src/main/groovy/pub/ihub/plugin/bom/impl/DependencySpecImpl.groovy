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
package pub.ihub.plugin.bom.impl

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.gradle.api.GradleException
import org.gradle.api.Project
import pub.ihub.plugin.bom.specs.DependencySpec

/**
 * Dependency Spec Impl
 * @author henry
 */
@CompileStatic
@TupleConstructor(includes = 'project')
final class DependencySpecImpl implements DependencySpec {

    final Project project

    final List<Dependency> specs = []

    @Override
    void compile(String type, ... dependencies) {
        assertProperty type as boolean, 'dependencies type not null!'
        assertProperty dependencies as boolean, type + ' dependencies not empty!'
        specs << new Dependency(type, dependencies as Set)
        project.configurations.maybeCreate(type).dependencies.addAll dependencies.collect {
            project.dependencies.create it
        }
    }

    private static void assertProperty(boolean condition, String message) {
        if (!condition) {
            throw new GradleException(message)
        }
    }

}
