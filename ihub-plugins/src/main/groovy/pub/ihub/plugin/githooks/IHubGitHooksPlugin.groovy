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
package pub.ihub.plugin.githooks


import cn.hutool.core.util.URLUtil
import org.gradle.api.GradleException
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
            // TODO 确认默认模板
            // TODO 完善测试用例
            // TODO 完善文档
            it.configDefaultGitCommitCheck()
            it.execute it.hooksPath, it.hooks
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
                assertRule !lines.empty, 'Commit msg is empty!'
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
                def (type, scope) = (header =~ extension.headerRegex).with {
                    // 信息头整体格式检查
                    assertRule matches(), 'Commit msg header check fail!'
                    getAt(0)[1, 3]
                }
                // 范围检查
                extension.types.find { it.name == type }.with {
                    assertRule !checkScope || scopes*.name.contains(scope),
                        "Commit msg header scope not in [${it.scopes*.name.join(', ')}]!"
                }
                extension.footers.each {
                    String footerValue = footers[it.name]
                    // 注脚必填检查
                    assertRule !it.required || footerValue, "Commit msg footer missing '$it.name'!"
                    // 注脚类型必填检查
                    assertRule !it.types.contains(type) || footerValue,
                        "Commit msg footer missing '$it.name' where type is '$type'!"
                    // 注脚值正则校验
                    assertRule !it.valueRegex || !footerValue || footerValue ==~ it.valueRegex,
                        "Commit msg footer '$it.name' check fail with regex: '$it.valueRegex'!"
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

    private static void assertRule(boolean condition, String message) {
        if (!condition) {
            throw new GradleException(message + ' See https://doc.ihub.pub/plugins/#/iHubGitHooks')
        }
    }

}
