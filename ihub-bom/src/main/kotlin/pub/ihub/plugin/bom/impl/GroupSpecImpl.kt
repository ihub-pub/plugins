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

import pub.ihub.plugin.bom.specs.GroupSpec

/**
 * Group Spec Impl
 * @author henry
 */
@Suppress("UNCHECKED_CAST")
open class GroupSpecImpl<T : Group<T>> : GroupSpec<T> {

    override val specs: MutableList<T> = mutableListOf()

    override fun group(group: String): T {
        val spec = newInstance(group)
        specs.add(spec)
        return spec
    }

    // This method might need to be overridden in subclasses if T is a more specific type of Group
    // that requires a different constructor or if T cannot be simply instantiated as Group(group).
    // For the direct translation of the Groovy code, this is the approach.
    protected open fun newInstance(group: String): T {
        return Group<T>(group) as T
    }
}
