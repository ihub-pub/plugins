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
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import groovy.transform.TypeCheckingMode
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.Project
import pub.ihub.plugin.bom.specs.ModulesSpec

/**
 * @author henry
 */
@CompileStatic
@TupleConstructor(callSuper = true, includeSuperProperties = true, includeSuperFields = true, includes = 'id,project')
@EqualsAndHashCode(callSuper = true, allProperties = true, excludes = 'modules,project')
@SuppressWarnings('ConfusingMethodName')
final class Modules extends Group<Modules> implements ModulesSpec {

    final Project project

    Set<String> modules

    final List<String> comparedProperties = ['id', 'modules', 'version']

    @Override
    Modules modules(String... modules) {
        this.modules = modules as Set<String>
        this
    }

    @Override
    void renewSpec(Modules spec) {
        spec.version version
        spec.modules.addAll modules
    }

    @Override
    void appendToPrintData(List<List<?>> data) {
        data.addAll modules.collect { [id, it, version] }
    }

    @Override
    @CompileStatic(TypeCheckingMode.SKIP)
    Modules version(String version) {
        // 配置组件版本
        project.extensions.getByType(DependencyManagementExtension).dependencies {
            dependencySet(group: id, version: version) {
                modules.each { entry it }
            }
        }
        super.version(version)
    }

}
