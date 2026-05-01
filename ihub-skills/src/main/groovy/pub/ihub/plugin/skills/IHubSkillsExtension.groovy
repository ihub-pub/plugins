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

import groovy.transform.CompileStatic
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

import javax.inject.Inject

/**
 * IHub AI Skills 插件扩展，支持多种 AI 开发工具的技能文件配置。
 *
 * @author henry
 */
@CompileStatic
class IHubSkillsExtension {

    /**
     * 是否启用 AI 技能安装（默认开启）
     */
    final Property<Boolean> enabled

    /**
     * 是否安装 Claude Code 技能（.claude/commands/）
     */
    final Property<Boolean> claudeEnabled

    /**
     * 是否安装 GitHub Copilot 技能（.github/copilot-instructions.md）
     */
    final Property<Boolean> copilotEnabled

    /**
     * 是否安装 OpenCode 技能（AGENTS.md）
     */
    final Property<Boolean> openCodeEnabled

    /**
     * Claude Code commands 目录（默认：{rootProject}/.claude/commands/）
     */
    final DirectoryProperty claudeCommandsDir

    /**
     * GitHub Copilot instructions 文件（默认：{rootProject}/.github/copilot-instructions.md）
     */
    final RegularFileProperty copilotInstructionsFile

    /**
     * OpenCode AGENTS.md 文件（默认：{rootProject}/AGENTS.md）
     */
    final RegularFileProperty agentsMdFile

    @Inject
    IHubSkillsExtension(ObjectFactory objects) {
        enabled = objects.property(Boolean).convention(true)
        claudeEnabled = objects.property(Boolean).convention(true)
        copilotEnabled = objects.property(Boolean).convention(true)
        openCodeEnabled = objects.property(Boolean).convention(true)
        claudeCommandsDir = objects.directoryProperty()
        copilotInstructionsFile = objects.fileProperty()
        agentsMdFile = objects.fileProperty()
    }

}
