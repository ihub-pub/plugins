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
package pub.ihub.plugin

import pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER
import pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.BEFORE

/**
 * @author henry
 */
@IHubPlugin
class IHubSimplePlugin : IHubProjectPluginAware<IHubExtensionAware>() { // Specify IHubExtensionAware or a more specific type if applicable

    override fun apply() {
        withExtension(IHubPrintExtension::class.java, BEFORE) {
            logger.info("before print extension")
        }
        withExtension(IHubPrintExtension::class.java) {
            logger.info("with print extension")
        }
        withExtension(IHubPrintExtension::class.java, AFTER) {
            logger.info("after print extension")
        }
        logger.info("libs: $ihub")
    }
}
