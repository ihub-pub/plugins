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
import org.gradle.api.Task
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import java.io.File
import java.net.MalformedURLException
import java.io.IOException

/**
 * IHub Git Hooks插件
 * @author henry
 */
@IHubPlugin(IHubGitHooksExtension::class)
class IHubGitHooksPlugin : IHubProjectPluginAware<IHubGitHooksExtension>() {

    override fun apply() {
        withExtension(EvaluateStage.AFTER) { ext ->
            ext.configDefaultGitCommitCheck()
            ext.execute(ext.hooksPath.orNull, ext.hooks.get())
        }

        project.tasks.register("commitCheck") { task: Task ->
            task.group = "ihub"
            var header = ""
            var footers: Map<String, String> = emptyMap()

            task.doFirst {
                val commitMsgFile = project.rootProject.file(".git/COMMIT_EDITMSG")
                if (!commitMsgFile.exists()) {
                    logger.warn("Not found file: {}", commitMsgFile.toURI())
                    return@doFirst
                }

                val lines = commitMsgFile.readLines()
                IHubGitHooksExtension.assertRule(lines.isNotEmpty(), "Commit msg is empty!")

                logger.lifecycle("Extract commit msg:")
                logger.lifecycle("---------------------------------------------")
                lines.forEach { logger.lifecycle(it) }
                logger.lifecycle("---------------------------------------------")

                header = lines.first()
                footers = lines.filter { line ->
                    extension.footers.any { footerSpec -> line.startsWith("${footerSpec.name}: ") }
                }.associate { line ->
                    val parts = line.split(": ", limit = 2)
                    parts[0] to parts[1]
                }
            }

            task.doLast {
                if (header.isEmpty() && footers.isEmpty() && !project.rootProject.file(".git/COMMIT_EDITMSG").exists()) {
                    // Likely means doFirst didn't run or found no file, so skip doLast logic
                    logger.info("Skipping commitCheck's doLast as commit message was not processed.")
                    return@doLast
                }

                // 信息头整体格式检查
                val (type, scope) = extension.checkHeader(header)
                // 范围检查
                extension.types.find { it.name == type }?.checkScope(scope)
                extension.footers.forEach { footerSpec ->
                    val footerValue = footers[footerSpec.name]
                    // 注脚必填检查
                    footerSpec.checkRequired(footerValue)
                    // 注脚类型必填检查
                    footerSpec.checkRequiredWithType(type, footerValue)
                    // 注脚值正则校验
                    footerSpec.checkValue(footerValue)
                }
            }
        }

        // 如果IDEA安装了Conventional Commit插件，自动生成并配置自定义配置
        try {
            val ideaPluginsPath = System.getProperty("idea.plugins.path")
            if (!ideaPluginsPath.isNullOrEmpty()) {
                // Check if the path is a URL or a local file path
                val content = try {
                    URLUtil.url(ideaPluginsPath).readText()
                } catch (e: MalformedURLException) {
                    // Assume it's a local file path
                    File(ideaPluginsPath).readText()
                } catch (e: IOException) {
                    logger.debug("Could not read idea.plugins.path content: ${e.message}")
                    "" // Empty content if reading fails
                }

                if (content.contains("idea-conventional-commit")) {
                    withExtension(EvaluateStage.AFTER) {
                        it.configConventionalCommit()
                    }
                }
            }
        } catch (e: Exception) {
            logger.debug("Error checking for Conventional Commit IDEA plugin: ${e.message}")
        }
    }
}
