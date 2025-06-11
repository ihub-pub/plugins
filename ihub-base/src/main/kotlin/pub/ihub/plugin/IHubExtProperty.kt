/*
 * Copyright (c) 2021 Henry 李恒 (henry.box@outlook.com).
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
package pub.ihub.plugin

import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension

/**
 * IHub项目扩展属性
 * @author henry
 */
interface IHubExtProperty {

    val project: Project

    val rootProject: Project
        get() = project.rootProject

    fun setExtProperty(project: Project = this.project, key: String, value: Any?) {
        project.extensions.extraProperties.set(key, value)
    }

    fun <V> findExtProperty(project: Project = this.project, key: String, defaultValue: V? = null): V? {
        val ext = project.extensions.extraProperties
        return if (ext.has(key)) ext.get(key) as V? else defaultValue
    }
}
