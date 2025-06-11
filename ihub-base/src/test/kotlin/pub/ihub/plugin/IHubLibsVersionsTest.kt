/*
 * Copyright (c) 2023 the original author or authors.
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

import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import org.gradle.api.JavaVersion
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * @author henry
 */
@DisplayName("IHubLibsVersions测试套件")
class IHubLibsVersionsTest {

    @Test
    fun `测试插件版本号`() {
        IHubLibsVersions.getIHubPluginsVersion() shouldNotBe null
    }

    @ParameterizedTest
    @CsvSource(
        "8, ^\\d+.\\d+.\\d+-java11",
        "11, ^\\d+.\\d+.\\d+-java11",
        "17, ^\\d+.\\d+.\\d+$",
        "21, ^\\d+.\\d+.\\d+$"
    )
    fun `测试组件版本包含ihub`(javaVersion: String, expectedPattern: String) {
        System.setProperty("java.version", javaVersion)
        // Reset current version for each test run, if JavaVersion caches it.
        // This is a bit of a hack; ideally, JavaVersion would allow overriding for tests.
        // For now, we assume setting system property and creating new instances or specific method calls
        // in IHubLibsVersions would pick up the change.
        // A more robust way might involve reflection if JavaVersion.current() caches aggressively
        // or refactoring IHubLibsVersions to be more testable with Java versions.

        // Force reset of cached version in JavaVersion if possible (conceptual)
        // As of Gradle 8.x, JavaVersion.current() is sensitive to system property at call time.
        // However, to be absolutely sure across versions/internal caching, clearing and re-evaluating:
        JavaVersion::class.java.getDeclaredMethod("resetCurrent").apply {
            isAccessible = true
            invoke(null)
        }


        IHubLibsVersions.getIHubLibsVersion() shouldMatch Regex(expectedPattern)
    }

    @AfterEach
    fun cleanup() {
        // Reset current version after each test to avoid interference
        JavaVersion::class.java.getDeclaredMethod("resetCurrent").apply {
            isAccessible = true
            invoke(null)
        }
        System.clearProperty("java.version")
    }
}
