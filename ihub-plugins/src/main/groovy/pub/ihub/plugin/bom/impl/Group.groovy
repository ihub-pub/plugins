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
import pub.ihub.plugin.bom.specs.VersionSpec



/**
 * @author henry
 */
@CompileStatic
@TupleConstructor(includes = 'id')
@EqualsAndHashCode(includes = 'id')
@SuppressWarnings('ConfusingMethodName')
class Group<T extends Group> implements ConfigSpec<T>, VersionSpec {

    final String id
    String version

    final List<String> comparedProperties = ['id', 'version']

    @Override
    T version(String version) {
        this.version = version
        this as T
    }

    @Override
    void renewSpec(T spec) {
        spec.version version
    }

    @Override
    void appendToPrintData(List<List<?>> data) {
        data << [id, version]
    }

}
