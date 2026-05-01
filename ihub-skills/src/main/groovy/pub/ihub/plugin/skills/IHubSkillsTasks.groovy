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
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

import java.nio.file.Files
import java.nio.file.Path

/**
 * IHub Claude Code 技能安装任务
 *
 * 将打包在 JAR 资源中的 AI 技能文件（.md）复制到项目的 .claude/commands/ 目录，
 * 使 Claude Code 能够感知并提供 /ihub-diagnose、/ihub-configure 等斜杠命令。
 *
 * @author henry
 */
@CompileStatic
@DisableCachingByDefault(because = '写入用户本地 .claude 目录，不适合构建缓存')
abstract class IHubClaudeSetupTask extends DefaultTask {

    private static final List<String> SKILL_NAMES = ['ihub-diagnose', 'ihub-configure']
    private static final String RESOURCE_PREFIX = '/META-INF/ihub/skills/claude/'

    @OutputDirectory
    @Optional
    abstract DirectoryProperty getCommandsDir()

    @Input
    abstract Property<Boolean> getSkillEnabled()

    @TaskAction
    void setup() {
        if (!skillEnabled.get()) {
            logger.lifecycle 'IHub Skills: Claude skill installation is disabled, skipping.'
            return
        }
        File dir = commandsDir.get().asFile
        dir.mkdirs()
        SKILL_NAMES.each { String name ->
            URL resource = IHubClaudeSetupTask.getResource("${RESOURCE_PREFIX}${name}.md")
            Path target = dir.toPath().resolve("${name}.md")
            Files.write(target, resource.bytes)
            logger.lifecycle "IHub Skills: installed Claude skill /${name}"
        }
    }

}

/**
 * IHub GitHub Copilot 技能安装任务
 *
 * 将 IHub 使用知识写入 .github/copilot-instructions.md，
 * 使 GitHub Copilot 能够感知 IHub Plugins 的最佳实践。
 *
 * @author henry
 */
@CompileStatic
@DisableCachingByDefault(because = '写入用户本地 .github 目录，不适合构建缓存')
abstract class IHubCopilotSetupTask extends DefaultTask {

    private static final String RESOURCE_PATH = '/META-INF/ihub/skills/copilot-instructions.md'

    @OutputFile
    @Optional
    abstract RegularFileProperty getInstructionsFile()

    @Input
    abstract Property<Boolean> getSkillEnabled()

    @TaskAction
    void setup() {
        if (!skillEnabled.get()) {
            logger.lifecycle 'IHub Skills: Copilot skill installation is disabled, skipping.'
            return
        }
        File file = instructionsFile.get().asFile
        file.parentFile.mkdirs()
        URL resource = IHubCopilotSetupTask.getResource(RESOURCE_PATH)
        Files.write(file.toPath(), resource.bytes)
        logger.lifecycle 'IHub Skills: installed Copilot instructions'
    }

}

/**
 * IHub OpenCode 技能安装任务
 *
 * 将 IHub 使用知识写入项目根目录的 AGENTS.md，
 * 使 OpenCode 能够感知 IHub Plugins 的最佳实践。
 *
 * @author henry
 */
@CompileStatic
@DisableCachingByDefault(because = '写入用户本地 AGENTS.md，不适合构建缓存')
abstract class IHubOpenCodeSetupTask extends DefaultTask {

    private static final String RESOURCE_PATH = '/META-INF/ihub/skills/AGENTS.md'

    @OutputFile
    @Optional
    abstract RegularFileProperty getAgentsMdFile()

    @Input
    abstract Property<Boolean> getSkillEnabled()

    @TaskAction
    void setup() {
        if (!skillEnabled.get()) {
            logger.lifecycle 'IHub Skills: OpenCode skill installation is disabled, skipping.'
            return
        }
        File file = agentsMdFile.get().asFile
        file.parentFile.mkdirs()
        URL resource = IHubOpenCodeSetupTask.getResource(RESOURCE_PATH)
        Files.write(file.toPath(), resource.bytes)
        logger.lifecycle 'IHub Skills: installed OpenCode AGENTS.md'
    }

}
