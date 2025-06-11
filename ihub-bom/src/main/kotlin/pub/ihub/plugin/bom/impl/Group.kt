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

import pub.ihub.plugin.bom.specs.ConfigSpec
import pub.ihub.plugin.bom.specs.VersionSpec

/**
 * @author henry
 */
@Suppress("ConfusingMethodName", "UNCHECKED_CAST")
open class Group<T : Group<T>>(val id: String) : ConfigSpec<T>, VersionSpec {

    var version: String? = null

    override val comparedProperties: List<String> = listOf("id", "version")

    override fun version(version: String): T {
        this.version = version
        return this as T
    }

    override fun renewSpec(spec: T) {
        this.version?.let { spec.version(it) }
    }

    override fun appendToPrintData(data: MutableList<List<*>>) {
        data.add(listOf(id, version ?: ""))
    }

    // Custom equals and hashCode to match Groovy's @EqualsAndHashCode(includes = 'id')
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Group<*>
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
