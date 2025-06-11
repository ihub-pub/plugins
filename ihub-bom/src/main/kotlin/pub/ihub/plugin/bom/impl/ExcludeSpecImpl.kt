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
 * @author henry
 */
class ExcludeSpecImpl : GroupSpec<Exclude> {

    override val specs: MutableList<Exclude> = mutableListOf()

    override fun group(group: String): Exclude {
        return Exclude(group).also {
            specs.add(it)
        }
    }
}
