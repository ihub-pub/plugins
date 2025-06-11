/*
 * Copyright (c) 2022-2023 the original author or authors.
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

import pub.ihub.plugin.bom.specs.ConfigSpec

/**
 * Dependencies
 * @author henry
 */
data class Dependency(
    var type: String,
    var dependencies: MutableSet<Any> // Groovy Set without generics becomes Set<Any>
) : ConfigSpec<Dependency> {

    override val comparedProperties: List<String> = listOf("type", "dependencies")

    override fun renewSpec(spec: Dependency) {
        spec.dependencies.addAll(this.dependencies)
    }

    override fun appendToPrintData(data: MutableList<List<*>>) {
        dependencies.forEach { dependency ->
            data.add(listOf(type, dependency.toString()))
        }
    }

    override fun appendToPrintData(commonSpecs: Set<Dependency>, data: MutableList<List<*>>) {
        val commonDependenciesForType = commonSpecs.find { it.type == this.type }?.dependencies ?: emptySet()
        val uniqueDependencies = this.dependencies.filterNot { commonDependenciesForType.contains(it) }.toMutableSet()
        Dependency(type, uniqueDependencies).appendToPrintData(data)
    }

    // Custom equals and hashCode to match Groovy's @EqualsAndHashCode(includes = 'type')
    // if 'dependencies' should not be part of equality/hashcode.
    // However, the Groovy code includes 'dependencies' in comparedProperties, implying it should be part of comparison.
    // Default data class behavior is likely correct if 'dependencies' are part of the identity.
    // If only 'type' determined equality:
    // override fun equals(other: Any?): Boolean {
    //     if (this === other) return true
    //     if (javaClass != other?.javaClass) return false
    //     other as Dependency
    //     return type == other.type
    // }
    // override fun hashCode(): Int = type.hashCode()
    // Given `comparedProperties` includes 'dependencies', the default data class behavior is more aligned.
}
