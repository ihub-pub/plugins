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
package pub.ihub.plugin.bom.specs

/**
 * Config Spec
 * @author henry
 */
@Suppress("UNCHECKED_CAST")
interface ConfigSpec<T : ConfigSpec<T>> {

    /**
     * 用于重复比较的属性
     * @return 比较属性
     */
    val comparedProperties: List<String>

    /**
     * 追加配置
     * @param specs 配置
     */
    fun appendTo(specs: MutableSet<T>) {
        val existingSpec = specs.find { it == this } // Relies on correct equals/hashCode in implementations
        if (existingSpec != null) {
            renewSpec(existingSpec)
        } else {
            specs.add(this as T)
        }
    }

    /**
     * 调整存在配置
     * @param spec 配置
     */
    fun renewSpec(spec: T)

    /**
     * 追加打印配置
     * @param data 配置信息
     */
    fun appendToPrintData(data: MutableList<List<*>>)

    /**
     * 追加打印配置
     * @param commonSpecs 配置
     * @param data 配置信息
     */
    fun appendToPrintData(commonSpecs: Set<T>, data: MutableList<List<*>>) {
        appendToPrintData(data) // Default implementation matches Groovy
    }
}
