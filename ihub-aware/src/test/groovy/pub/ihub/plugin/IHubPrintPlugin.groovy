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

import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.IHubPluginMethods.tap



/**
 * @author henry
 */
@IHubPlugin(IHubPrintExtension)
class IHubPrintPlugin extends IHubProjectPluginAware<IHubPrintExtension> {

    @Override
    void apply() {
        printConfigContent 'test', tap('t1', 30), tap('t2'), [d1: [1, 2, 3], d2: [4, 5, 6]]
        printConfigContent 'test', []
        printConfigContent 'test', tap('t1')
    }

}
