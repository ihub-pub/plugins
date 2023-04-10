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
        withExtension(AFTER) {
            it.configDefaultGitCommitCheck()
            it.execute it.hooksPath.orNull, it.hooks.get()
        }

        project.task('commitCheck') {
            it.group = 'ihub'
            String header
            Map footers

            it.doFirst {
                File commitMsgFile = project.rootProject.file '.git/COMMIT_EDITMSG'
                if (!commitMsgFile.exists()) {
                    logger.warn 'Not found file: {}', commitMsgFile.toURI()
                    return
                }

                List<String> lines = commitMsgFile.readLines()
                extension.assertRule !lines.empty, 'Commit msg is empty!'
                logger.lifecycle 'Extract commit msg:'
                logger.lifecycle '---------------------------------------------'
                lines.each { logger.lifecycle it }
                logger.lifecycle '---------------------------------------------'
                header = lines.first()
                footers = lines.findAll {
                    it ==~ /^(${extension.footers*.name.join('|')}): .+/
                }.collectEntries { it.split ': ' }
            }

            it.doLast {
                // 信息头整体格式检查
                def (type, scope) = extension.checkHeader header
                // 范围检查
                extension.types.find { it.name == type }.checkScope scope
                extension.footers.each {
                    String footerValue = footers[it.name]
                    // 注脚必填检查
                    it.checkRequired footerValue
                    // 注脚类型必填检查
                    it.checkRequiredWithType type, footerValue
                    // 注脚值正则校验
                    it.checkValue footerValue
                }
            }
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
