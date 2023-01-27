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
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import pub.ihub.plugin.bom.specs.ConfigSpec



/**
 * Dependencies
 * @author henry
 */
@CompileStatic
@TupleConstructor
@EqualsAndHashCode(includes = 'type')
final class Dependency implements ConfigSpec<Dependency> {

    String type
    Set<String> dependencies

    final List<String> comparedProperties = ['type', 'dependencies']

    @Override
    void renewSpec(Dependency spec) {
        spec.dependencies.addAll dependencies
    }

    @Override
    void appendToPrintData(List<List<?>> data) {
        data.addAll(dependencies.collect { [type, it] })
    }

    @Override
    void appendToPrintData(Set<Dependency> commonSpecs, List<List<?>> data) {
        new Dependency(type, dependencies - commonSpecs.find { r -> type == r.type }?.dependencies)
            .appendToPrintData data
    }

}
