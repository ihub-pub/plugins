/*
 * Copyright (c) 2021-2023 the original author or authors.
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
package pub.ihub.plugin.githooks

import cn.hutool.core.util.URLUtil
import pub.ihub.plugin.IHubPlugin
import org.gradle.api.tasks.TaskProvider
import pub.ihub.plugin.IHubProjectPluginAware

import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER


/**
 * IHub Git Hooks插件
 * @author henry
 */
@IHubPlugin(IHubGitHooksExtension)
class IHubGitHooksPlugin extends IHubProjectPluginAware<IHubGitHooksExtension> {

    @Override
    void apply() {
        // 默认提交规则在配置阶段补齐
        withExtension(AFTER) {
            it.configDefaultGitCommitCheck()
        }

        // git config 调用挪到执行阶段的 Task 中（Configuration Cache 友好）
        TaskProvider<IHubGitHooksSetupTask> setupTask = registerTask(
            'iHubGitHooksSetup', IHubGitHooksSetupTask) { task ->
            task.group = 'ihub'
            task.description = 'Configure git hooks path and write hook scripts.'
            task.hooksPath.set extension.hooksPath
            task.hooks.set extension.hooks
            task.extension = extension
        }
        // 让任意构建调用都先执行 hooks 安装
        namedTask('help').configure { it.dependsOn setupTask }

        // 通过 ProjectLayout 解析 commit msg 文件，避免 task 体内引用 project（CC 友好）
        File commitMsgFile = project.rootProject.layout.projectDirectory.file('.git/COMMIT_EDITMSG').asFile
        registerTask('commitCheck', IHubCommitCheckTask) { task ->
            task.group = 'ihub'
            task.description = 'Validate the latest commit message against IHub git-hooks conventions.'
            if (commitMsgFile.exists()) {
                task.commitMsgFile.set project.layout.file(project.provider { commitMsgFile })
            }
            task.missingFileHint.set commitMsgFile.toURI().toString()
            task.extension = extension
        }

        // 如果IDEA安装了Conventional Commit插件，自动生成并配置自定义配置
        String ideaPluginsPath = System.getProperty 'idea.plugins.path'
        if (ideaPluginsPath && URLUtil.url(ideaPluginsPath).readLines().contains('idea-conventional-commit')) {
            withExtension(AFTER) {
                it.configConventionalCommit()
            }
        }
    }

}
