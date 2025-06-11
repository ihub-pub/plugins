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

import pub.ihub.plugin.bom.specs.ModulesSpec

/**
 * @author henry
 */
@Suppress("ConfusingMethodName")
class Modules(id: String) : Group<Modules>(id), ModulesSpec {

    var modules: MutableSet<String> = mutableSetOf()

    // Overriding to include 'modules' in the comparison list for completeness,
    // though actual equals/hashCode will exclude it.
    override val comparedProperties: List<String> = listOf("id", "modules", "version")

    override fun modules(vararg modules: String): Modules {
        this.modules = modules.toMutableSet()
        return this
    }

    override fun renewSpec(spec: Modules) {
        super.renewSpec(spec) // Handles version
        spec.modules.addAll(this.modules)
    }

    override fun appendToPrintData(data: MutableList<List<*>>) {
        modules.forEach { module ->
            data.add(listOf(id, module, version ?: ""))
        }
    }

    // Custom equals and hashCode to match Groovy's
    // @EqualsAndHashCode(callSuper = true, allProperties = true, excludes = 'modules')
    // This means it includes super.equals/hashCode (based on 'id') and its own 'version' (from Group),
    // but excludes 'modules'.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false // Checks 'id' from Group
        other as Modules
        // Check 'version' from Group, as 'allProperties = true' implies properties of this class
        return version == other.version
    }

    override fun hashCode(): Int {
        var result = super.hashCode() // Considers 'id' from Group
        result = 31 * result + (version?.hashCode() ?: 0) // Include version from Group
        // 'modules' is excluded
        return result
    }
}
