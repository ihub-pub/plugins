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
import pub.ihub.plugin.bom.specs.ModuleSpec

/**
 * @author henry
 */
@CompileStatic
@TupleConstructor(callSuper = true, includeSuperProperties = true, includeSuperFields = true, includes = 'id,project')
@EqualsAndHashCode(callSuper = true, includes = 'module,project')
@SuppressWarnings('ConfusingMethodName')
final class Module extends Group<Module> implements ModuleSpec {

    final Project project

    String module

    final List<String> comparedProperties = ['id', 'module', 'version']

    @Override
    Module module(String module) {
        this.module = module
        this
    }

    @Override
    void renewSpec(Module spec) {
        spec.version version
        spec.module module
    }

    @Override
    void appendToPrintData(List<List<?>> data) {
        data << [id, module, version]
    }

    @Override
    @CompileStatic(TypeCheckingMode.SKIP)
    Module version(String version) {
        // 导入bom配置
        project.extensions.getByType(DependencyManagementExtension).imports {
            mavenBom "$id:$module:$version"
        }
        super.version(version)
    }

}
