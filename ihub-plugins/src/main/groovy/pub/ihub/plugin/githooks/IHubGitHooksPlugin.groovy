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
    @SuppressWarnings('UnusedVariable')
    void apply() {
        withExtension(AFTER) {
            it.execute it.hooksPath, it.hooks, it
        }

        File commitMsgFile = project.rootDir.toPath().resolve('.git').resolve('COMMIT_EDITMSG').toFile()
        if (!commitMsgFile.exists()) {
            logger.warn 'Not found file: {}', commitMsgFile.toURI()
            return
        }

        project.task('commitCheck') {
            it.group = 'ihub'
            String header
            Map footers
            // TODO 扩展信息取值
            List types = ['refactor', 'fix', 'feat', 'build', 'chore', 'style', 'test', 'docs', 'perf', 'ci', 'revert']
            List footerTypes = ['Closes']

            it.doFirst {
                List<String> lines = commitMsgFile.readLines()
                assertRule !lines.empty, 'Commit msg is empty!'
                logger.lifecycle 'Extract commit msg:'
                logger.lifecycle '---------------------------------------------'
                lines.each { logger.lifecycle it }
                logger.lifecycle '---------------------------------------------'
                header = lines.first()
                footers = lines.findAll { it ==~ /^(${footerTypes.join('|')}): .+/ }.collectEntries { it.split ': ' }
            }

            it.doLast {
                assertRule header ==~ /^(${types.join('|')})(\(.+\))?!?: .{1,100}/, 'Commit msg header check fail!'
                // TODO footerTypes必填校验
            }
        }
    }

    private static void assertRule(boolean condition, String message) {
        if (!condition) {
            throw new GradleException(message)
        }
    }

}
