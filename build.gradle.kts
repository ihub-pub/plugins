/*
 * Copyright (c) 2021-2026 the original author or authors.
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
plugins {
    alias(ihub.plugins.root)
    alias(ihub.plugins.copyright)
    alias(ihub.plugins.git.hooks)
}

iHubGitHooks {
    hooks.set(
        mapOf(
            "pre-commit" to "./gradlew syncIHubPluginsVersion clean check -x test spotlessCheck spotlessApply",
            "commit-msg" to "./gradlew commitCheck"
        )
    )
}

// 检查 settings.gradle.kts 与 buildSrc/build.gradle.kts 中的 IHub 插件版本是否一致，不一致时自动同步。
// 由 pre-commit hook 触发，确保 Dependabot 自动升级 settings 版本后 buildSrc 不会滞后。
tasks.register("syncIHubPluginsVersion") {
    group = "ihub"
    description = "Sync IHub plugins version from settings.gradle.kts to buildSrc/build.gradle.kts"
    doLast {
        val settingsFile = file("settings.gradle.kts")
        val buildSrcFile = file("buildSrc/build.gradle.kts")

        val settingsVersion = Regex("""id\("pub\.ihub\.plugin\.ihub-settings"\)\s+version\s+"([^"]+)"""")
            .find(settingsFile.readText())?.groupValues?.get(1)
            ?: error("无法从 settings.gradle.kts 解析 ihub-settings 版本")

        val buildSrcText = buildSrcFile.readText()
        val buildSrcVersion = Regex("""val iHubPluginsVersion = "([^"]+)"""")
            .find(buildSrcText)?.groupValues?.get(1)
            ?: error("无法从 buildSrc/build.gradle.kts 解析 iHubPluginsVersion")

        if (settingsVersion != buildSrcVersion) {
            logger.lifecycle("同步 iHubPluginsVersion: $buildSrcVersion → $settingsVersion")
            buildSrcFile.writeText(
                buildSrcText.replace(
                    """val iHubPluginsVersion = "$buildSrcVersion"""",
                    """val iHubPluginsVersion = "$settingsVersion""""
                )
            )
        } else {
            logger.lifecycle("iHubPluginsVersion 已是最新：$settingsVersion")
        }
    }
}

// 注意：原 subprojects {} 块已迁移至 buildSrc/src/main/kotlin/ihub.module-conventions.gradle.kts
// （为 Gradle Isolated Projects 兼容做准备）。
// 每个 ihub-* 子模块在自身 build.gradle.kts 顶部 plugins 块应用 id("ihub.module-conventions") 即可。

