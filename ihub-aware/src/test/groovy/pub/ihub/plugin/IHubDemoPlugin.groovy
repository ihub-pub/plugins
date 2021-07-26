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
package pub.ihub.plugin

import org.gradle.api.DefaultTask

import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.BEFORE



/**
 * @author henry
 */
@IHubPlugin(value = IHubDemoExtension, beforeApplyPlugins = IHubSimplePlugin)
class IHubDemoPlugin extends IHubProjectPluginAware<IHubDemoExtension> {

    @Override
    void apply() {
        withExtension(BEFORE) {
            logger.info 'before extension'
        }
        withExtension {
            logger.info 'with extension'
        }
        withExtension(AFTER) {
            logger.info 'after extension'
        }

        registerTask('demo', DefaultTask) {
            logger.info it.name
        }
        withTask('demo') {
            logger.info it.name
        }
        withTask(DefaultTask) {
            logger.info it.name
        }
        withTask 'demo'
    }

}
