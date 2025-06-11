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

import pub.ihub.plugin.bom.specs.ModuleSpec

/**
 * @author henry
 */
@Suppress("ConfusingMethodName")
class Module(id: String) : Group<Module>(id), ModuleSpec {

    var module: String? = null

    // Overriding to include 'module' in the comparison, in addition to 'id' and 'version' from Group
    override val comparedProperties: List<String> = listOf("id", "module", "version")

    override fun module(module: String): Module {
        this.module = module
        return this
    }

    override fun renewSpec(spec: Module) {
        super.renewSpec(spec) // Handles version
        this.module?.let { spec.module(it) }
    }

    override fun appendToPrintData(data: MutableList<List<*>>) {
        data.add(listOf(id, module ?: "", version ?: ""))
    }

    // Custom equals and hashCode to match Groovy's @EqualsAndHashCode(callSuper = true, includes = 'module')
    // This means it includes super.equals/hashCode (which is based on 'id') and its own 'module'.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false // Checks 'id' from Group
        other as Module
        return module == other.module
    }

    override fun hashCode(): Int {
        var result = super.hashCode() // Considers 'id' from Group
        result = 31 * result + (module?.hashCode() ?: 0)
        return result
    }
}
