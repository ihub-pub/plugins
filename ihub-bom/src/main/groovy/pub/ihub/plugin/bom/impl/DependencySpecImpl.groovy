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
package pub.ihub.plugin.bom.impl

import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import pub.ihub.plugin.bom.specs.DependencySpec



/**
 * Dependency Spec Impl
 * @author henry
 */
@CompileStatic
final class DependencySpecImpl implements DependencySpec {

    final List<Dependency> specs = []

    @Override
    void compile(String type, String... dependencies) {
        assertProperty type as boolean, 'dependencies type not null!'
        assertProperty dependencies as boolean, type + ' dependencies not empty!'
        specs << new Dependency(type, dependencies as Set<String>)
    }

    private static void assertProperty(boolean condition, String message) {
        if (!condition) {
            throw new GradleException(message)
        }
    }

}
