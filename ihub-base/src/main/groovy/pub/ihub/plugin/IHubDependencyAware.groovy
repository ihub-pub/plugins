/*
 * Copyright (c) 2023-2024 the original author or authors.
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
package pub.ihub.plugin

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin

/**
 * IHub项目依赖扩展接口
 */
@CompileStatic
trait IHubDependencyAware {

    abstract Project getProject()

    void compile(String configuration, ... dependencyNotation) {
        compile project, configuration, dependencyNotation
    }

    static void compile(Project project, String configuration, ... dependencyNotation) {
        // Groovy增量编译与Java注释处理器不能同时使用
        if (project.plugins.hasPlugin(GroovyPlugin) && 'false' != project
            .findProperty('iHubJava.gradleCompilationIncremental')?.toString() && 'annotationProcessor' == configuration) {
            return
        }
        project.configurations.maybeCreate(configuration).dependencies.addAll dependencyNotation.collect {
            project.dependencies.create it
        }
    }

}
