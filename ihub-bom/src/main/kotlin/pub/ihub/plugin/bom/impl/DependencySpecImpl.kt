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

import org.gradle.api.GradleException
import org.gradle.api.Project
import pub.ihub.plugin.bom.specs.DependencySpec

/**
 * Dependency Spec Impl
 * @author henry
 */
class DependencySpecImpl(private val project: Project) : DependencySpec {

    override val specs: MutableList<Dependency> = mutableListOf()

    override fun compile(type: String, vararg dependencies: Any) {
        assertProperty(type.isNotEmpty(), "dependencies type not null!")
        assertProperty(dependencies.isNotEmpty(), "$type dependencies not empty!")

        val dependencySet = dependencies.toMutableSet()
        specs.add(Dependency(type, dependencySet))

        // 配置组件依赖
        val configuration = project.configurations.maybeCreate(type)
        dependencySet.forEach { dep ->
            configuration.dependencies.add(project.dependencies.create(dep))
        }
    }

    private fun assertProperty(condition: Boolean, message: String) {
        if (!condition) {
            throw GradleException(message)
        }
    }
}
