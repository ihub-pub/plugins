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
import static pub.ihub.plugin.IHubPluginMethods.printLineConfigContent
import static pub.ihub.plugin.IHubPluginMethods.printMapConfigContent



/**
 * @author henry
 */
@IHubPlugin(IHubPrintExtension)
class IHubPrintPlugin extends IHubProjectPluginAware<IHubPrintExtension> {

    @Override
    void apply() {
        printConfigContent 'test', []
        printConfigContent 'test', [['123', '456', '789']]
        printConfigContent 'test', [
            ['123', '12345', '123'],
            ['12345', '123', '1234567'],
            ['1', '1234567890', '123']
        ], 't1', 't2', 't3'
        printLineConfigContent 'test', [
            '1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890'
        ], 't'
        printMapConfigContent 'test', 'ID', 'Version', [a: '1', b: '2', c: ['3', '4']]
    }

}
