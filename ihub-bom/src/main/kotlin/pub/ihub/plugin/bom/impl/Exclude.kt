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

import org.gradle.api.GradleException
import pub.ihub.plugin.bom.specs.ConfigSpec
import pub.ihub.plugin.bom.specs.ModulesSpec

/**
 * @author henry
 */
@Suppress("ConfusingMethodName")
class Exclude(var group: String) : ConfigSpec<Exclude>, ModulesSpec {

    var modules: MutableSet<String> = mutableSetOf()

    override val comparedProperties: List<String> = listOf("group", "modules")

    override fun version(version: String): Exclude {
        throw GradleException("Does not support 'version' method!")
    }

    override fun modules(vararg modules: String): Exclude {
        this.modules = modules.toMutableSet()
        return this
    }

    override fun appendTo(specs: MutableSet<Exclude>) {
        if (modules.isEmpty()) {
            modules = mutableSetOf("all")
        }
        val existingSpec = specs.find { this == it } // Equality is based on 'group' only due to custom equals
        if (existingSpec != null) {
            renewSpec(existingSpec)
        } else {
            specs.add(this)
        }
    }

    override fun renewSpec(spec: Exclude) {
        spec.modules.addAll(this.modules)
    }

    override fun appendToPrintData(data: MutableList<List<*>>) {
        modules.forEach { module ->
            data.add(listOf(group, module))
        }
    }

    override fun appendToPrintData(commonSpecs: Set<Exclude>, data: MutableList<List<*>>) {
        val commonModulesForGroup = commonSpecs.find { it.group == this.group }?.modules ?: emptySet()
        val uniqueModules = this.modules.filterNot { commonModulesForGroup.contains(it) }.toTypedArray()
        Exclude(group).modules(*uniqueModules).appendToPrintData(data)
    }

    // Custom equals and hashCode to match Groovy's @EqualsAndHashCode(includes = 'group')
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Exclude
        return group == other.group
    }

    override fun hashCode(): Int = group.hashCode()
}
