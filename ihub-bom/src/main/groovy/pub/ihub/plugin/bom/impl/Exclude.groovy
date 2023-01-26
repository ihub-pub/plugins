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
import org.gradle.api.GradleException
import pub.ihub.plugin.bom.specs.ConfigSpec
import pub.ihub.plugin.bom.specs.ModulesSpec



/**
 * @author henry
 */
@CompileStatic
@TupleConstructor(includes = 'group')
@EqualsAndHashCode(includes = 'group')
@SuppressWarnings('ConfusingMethodName')
final class Exclude implements ConfigSpec<Exclude>, ModulesSpec {

    String group
    Set<String> modules

    final List<String> comparedProperties = ['group', 'modules']

    @Override
    Exclude version(String version) {
        throw new GradleException('Does not support \'version\' method!')
    }

    @Override
    Exclude modules(String... modules) {
        this.modules = modules as Set<String>
        this
    }

    @Override
    void appendTo(Set<Exclude> specs) {
        if (!modules) {
            modules = new HashSet<>(['all'])
        }
        Exclude spec = specs.find { this == it }
        if (spec as boolean) {
            renewSpec spec
        } else {
            specs << this
        }
    }

    @Override
    void renewSpec(Exclude spec) {
        spec.modules.addAll modules
    }

    @Override
    void appendToPrintData(List<List<?>> data) {
        data.addAll(modules.collect { [group, it] })
    }

    @Override
    void appendToPrintData(Set<Exclude> commonSpecs, List<List<?>> data) {
        new Exclude(group).modules(modules - commonSpecs.find { r -> group == r.group }?.modules as String[])
            .appendToPrintData data
    }

}
