/*
 * Copyright (c) 2021-2024 the original author or authors.
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
package pub.ihub.plugin.meta

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * IHub AI Metadata 插件
 * 当项目应用此插件后，可使用 {@code ./gradlew iHubMeta} 生成项目结构 JSON 文件，
 * 供 LLM / AI 开发工具读取并理解项目结构。
 *
 * <h4>基本用法</h4>
 * <pre>{@code
 * plugins {
 *     id("pub.ihub.plugin.ihub-meta")
 * }
 * }</pre>
 *
 * <h4>自定义配置</h4>
 * <pre>{@code
 * iHubMeta {
 *     includeDependencies.set(false)
 *     outputFile.set(layout.buildDirectory.file("custom-meta.json"))
 * }
 * }</pre>
 *
 * @author henry
 */
class IHubMetaPlugin implements Plugin<Project> {

    static final String EXTENSION_NAME = 'iHubMeta'
    static final String TASK_GROUP = 'ihub'
    static final String TASK_NAME = 'iHubMeta'

    @Override
    void apply(Project project) {
        IHubMetaExtension ext = project.extensions.create(EXTENSION_NAME, IHubMetaExtension)

        ext.outputFile.convention(
            project.layout.buildDirectory.file('ihub/project-meta.json')
        )

        project.tasks.register(TASK_NAME, IHubMetaTask) { IHubMetaTask task ->
            task.group = TASK_GROUP
            task.description = 'Generate project structure metadata JSON for AI/LLM consumption.'
            task.outputFile.set(ext.outputFile)
            task.metaEnabled.set(ext.enabled)
            task.includeDependencies.set(ext.includeDependencies)
            task.includeSourceSets.set(ext.includeSourceSets)
        }
    }

}
