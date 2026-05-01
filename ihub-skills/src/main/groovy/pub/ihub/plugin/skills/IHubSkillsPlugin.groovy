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
package pub.ihub.plugin.skills

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

/**
 * IHub AI Skills 插件
 * 当项目应用此插件后，会在构建时自动向项目目录写入 IHub 专属 AI 技能文件，
 * 使多种 AI 开发工具能够感知 IHub Plugins 的使用规范和最佳实践：
 *
 * - **Claude Code**：在 `.claude/commands/` 写入斜杠命令技能文件
 *   - `/ihub-diagnose` — 诊断 Gradle 构建问题
 *   - `/ihub-configure` — 插件配置向导
 * - **GitHub Copilot**：在 `.github/copilot-instructions.md` 写入使用规范
 * - **OpenCode**：在 `AGENTS.md` 写入使用规范
 *
 * ## 基本用法
 *
 * ```kotlin
 * plugins {
 *     id("pub.ihub.plugin.ihub-skills")
 * }
 * ```
 *
 * ## 自定义配置
 *
 * ```kotlin
 * iHubSkills {
 *     copilotEnabled.set(false)   // 不安装 Copilot 技能
 *     openCodeEnabled.set(false)  // 不安装 OpenCode 技能
 * }
 * ```
 *
 * @author henry
 */
class IHubSkillsPlugin implements Plugin<Project> {

    static final String EXTENSION_NAME = 'iHubSkills'
    static final String TASK_GROUP = 'ihub'

    @Override
    void apply(Project project) {
        Project rootProject = project.rootProject
        IHubSkillsExtension ext = project.extensions.create(EXTENSION_NAME, IHubSkillsExtension)

        // 设置默认目录（基于 rootProject layout）
        ext.claudeCommandsDir.convention(
            rootProject.layout.projectDirectory.dir('.claude/commands')
        )
        ext.copilotInstructionsFile.convention(
            rootProject.layout.projectDirectory.file('.github/copilot-instructions.md')
        )
        ext.agentsMdFile.convention(
            rootProject.layout.projectDirectory.file('AGENTS.md')
        )

        // 注册 Claude Code 技能安装任务
        TaskProvider<IHubClaudeSetupTask> claudeTask = project.tasks.register(
            'iHubClaudeSetup', IHubClaudeSetupTask) { IHubClaudeSetupTask task ->
            task.group = TASK_GROUP
            task.description = 'Install IHub AI skills to .claude/commands/ for Claude Code.'
            task.commandsDir.set(ext.claudeCommandsDir)
            task.skillEnabled.set(ext.enabled.zip(ext.claudeEnabled) { Boolean a, Boolean b -> a && b })
        }

        // 注册 GitHub Copilot 技能安装任务
        TaskProvider<IHubCopilotSetupTask> copilotTask = project.tasks.register(
            'iHubCopilotSetup', IHubCopilotSetupTask) { IHubCopilotSetupTask task ->
            task.group = TASK_GROUP
            task.description = 'Install IHub AI skills to .github/copilot-instructions.md for GitHub Copilot.'
            task.instructionsFile.set(ext.copilotInstructionsFile)
            task.skillEnabled.set(ext.enabled.zip(ext.copilotEnabled) { Boolean a, Boolean b -> a && b })
        }

        // 注册 OpenCode 技能安装任务
        TaskProvider<IHubOpenCodeSetupTask> openCodeTask = project.tasks.register(
            'iHubOpenCodeSetup', IHubOpenCodeSetupTask) { IHubOpenCodeSetupTask task ->
            task.group = TASK_GROUP
            task.description = 'Install IHub AI skills to AGENTS.md for OpenCode.'
            task.agentsMdFile.set(ext.agentsMdFile)
            task.skillEnabled.set(ext.enabled.zip(ext.openCodeEnabled) { Boolean a, Boolean b -> a && b })
        }

        // 挂载到 help 任务，确保任意构建都会触发安装（与 iHubGitHooksSetup 策略一致）
        project.tasks.named('help') { task ->
            task.dependsOn(claudeTask, copilotTask, openCodeTask)
        }
    }

}
