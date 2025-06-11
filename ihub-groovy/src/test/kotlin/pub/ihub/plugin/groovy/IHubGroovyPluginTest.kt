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
package pub.ihub.plugin.groovy

import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pub.ihub.plugin.test.IHubSpecification

/**
 * @author henry
 */
@DisplayName("IHubGroovyPlugin测试套件")
class IHubGroovyPluginTest : IHubSpecification() {

    @BeforeEach
    override fun setup() {
        super.setup() // Call super.setup() from IHubSpecification
        buildFile.appendText("""
            plugins {
                id("pub.ihub.plugin.ihub-groovy")
            }
        """.trimIndent())
    }

    @Test
    fun `Groovy插件配置测试`() {
        // when: 构建项目
        // The build output being checked implies that the 'dependencies' task or similar might be run,
        // or that the configuration phase output includes this.
        // For a more robust check, it might be better to inspect the actual dependencies of configurations.
        // However, sticking to the original test's intent of checking output:
        val result = gradleBuilder.withArguments("dependencies", "--configuration", "implementation").build()
        // If the output is from a different task or configuration, adjust the arguments above.
        // If it's just from general build output (less reliable), then use .build()

        // then: 检查结果
        // These checks assume the dependency report format includes these specific strings.
        result.output shouldContain "org.apache.groovy:groovy-xml"
        result.output shouldContain "org.apache.groovy:groovy-dateutil"
        result.output shouldContain "org.apache.groovy:groovy-templates"
        result.output shouldContain "org.apache.groovy:groovy-nio"
        result.output shouldContain "org.apache.groovy:groovy"
        result.output shouldContain "org.apache.groovy:groovy-json"
        result.output shouldContain "org.apache.groovy:groovy-groovydoc"
        result.output shouldContain "org.apache.groovy:groovy-sql"
        result.output shouldContain "org.apache.groovy:groovy-datetime"
        result.output shouldContain "BUILD SUCCESSFUL"
    }
}
