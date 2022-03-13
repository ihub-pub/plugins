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
@SuppressWarnings('UnusedMethodParameter')
interface ConfigSpec<T extends ConfigSpec> {

    /**
     * 追加配置
     * @param specs 配置
     */
    default void appendTo(Set<T> specs) {
        T spec = specs.find { this == it }
        if (spec as boolean) {
            renewSpec spec
        } else {
            specs << (this as T)
        }
    }

    /**
     * 调整存在配置
     * @param spec 配置
     */
    void renewSpec(T spec)

    /**
     * 用于重复比较的属性
     * @return 比较属性
     */
    List<String> getComparedProperties()

    /**
     * 追加打印配置
     * @param data 配置信息
     */
    void appendToPrintData(List<List<?>> data)

    /**
     * 追加打印配置
     * @param commonSpecs 配置
     * @param data 配置信息
     */
    default void appendToPrintData(Set<T> commonSpecs, List<List<?>> data) {
        appendToPrintData data
    }

}
