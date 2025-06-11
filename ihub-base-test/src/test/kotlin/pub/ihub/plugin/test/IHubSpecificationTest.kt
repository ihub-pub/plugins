/*
 * Copyright (c) 2021 Henry 李恒 (henry.box@outlook.com).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pub.ihub.plugin.test

import io.kotest.matchers.string.shouldContain
import org.gradle.api.Project
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File

/**
 * @author henry
 */
@DisplayName("IHubSpecification测试套件")
class IHubSpecificationTest : IHubSpecification() {

    @Test
    fun `test gradle tasks execution`() {
        // Setup: Copy project files
        // Note: The original test mentions "示例配置脚本无法在本模块构建，以上代码仅作方法测试"
        // (Sample configuration scripts cannot be built in this module, the above code is only for method testing)
        // We'll replicate the file operations as they were, but the actual build might not be fully meaningful
        // if 'basic.gradle' or 'sample-groovy' are not self-contained or compatible with this test setup.

        copyProjectFromSamples("basic.gradle")
        copyProjectFromSamples("sample-groovy", "src", "conf")

        // As per original test, delete and recreate buildFile after copying.
        // This seems to indicate the copied build files might not be directly used for the 'tasks' run,
        // or it's ensuring a clean state for a default build file if the copied one is problematic.
        buildFile.delete()
        buildFile = File(testProjectDir, Project.DEFAULT_BUILD_FILE)
        buildFile.writeText("""
            // Minimal build file for 'tasks' test
            plugins {
                id("base") // Applying base plugin to have some tasks
            }
        """.trimIndent())


        // When: Run gradle tasks
        val result = gradleBuilder.withArguments("tasks").build()

        // Then: Check output
        result.output shouldContain "tasks - Displays the tasks runnable from root project"
    }
}
