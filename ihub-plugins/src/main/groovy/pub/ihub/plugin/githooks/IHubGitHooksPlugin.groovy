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
            try {
                if (it.hooksPath) {
                    "git config core.hooksPath $it.hooksPath".execute()
                    logger.lifecycle 'Set git hooks path: ' + it.hooksPath
                } else if (it.hooks) {
                    it.writeHooks()
                    'git config core.hooksPath .gradle/pub.ihub.plugin.hooks'.execute()
                    logger.lifecycle 'Set git hooks path: .gradle/pub.ihub.plugin.hooks'
                } else {
                    'git config --unset core.hooksPath'.execute()
                    logger.lifecycle 'Unset git hooks path, learn more see https://doc.ihub.pub/plugins/#/iHubGitHooks'
                }
            } catch (e) {
                logger.lifecycle 'Git hooks config fail: ' + e.message
            }
        }
    }

}
