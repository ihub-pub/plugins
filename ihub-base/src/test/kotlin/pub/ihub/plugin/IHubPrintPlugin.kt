/*
 * Copyright (c) 2021-2024 the original author or authors.
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

import pub.ihub.plugin.IHubPluginMethods.printConfigContent
import pub.ihub.plugin.IHubPluginMethods.printLineConfigContent
import pub.ihub.plugin.IHubPluginMethods.printMapConfigContent

/**
 * @author henry
 */
@IHubPlugin(IHubPrintExtension::class)
class IHubPrintPlugin : IHubProjectPluginAware<IHubPrintExtension>() {

    override fun apply() {
        printConfigContent("test", emptyList<List<String>>())
        printConfigContent("test", listOf(listOf("123", "456", "798")))
        printConfigContent(
            "test",
            listOf(
                listOf("123", "12345", "123"),
                listOf("12345", "123", "1234567"),
                listOf("1", "1234567890", "123")
            ),
            "t1", "t2", "t3"
        )
        printLineConfigContent(
            "test",
            listOf(
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
            ),
            "t"
        )
        printMapConfigContent("test", "ID", "Version", mapOf("a" to "1", "b" to "2", "c" to listOf("3", "4")))
        registerTask("printConfig") {
            printConfigContent("task", emptyList<List<String>>())
        }
    }
}
