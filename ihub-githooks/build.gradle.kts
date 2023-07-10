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
description = "IHub Git Hooks Gradle Plugins"

dependencies {
    implementation("cn.hutool:hutool-core")
}

gradlePlugin {
    plugins {
        create("ihubGitHooks") {
            id = "pub.ihub.plugin.ihub-git-hooks"
            displayName = "IHub GitHooks"
            description = "IHub Gradle GitHooks Gradle Plugin"
            implementationClass = "pub.ihub.plugin.githooks.IHubGitHooksPlugin"
            tags.set(listOf("ihub", "git", "gitHooks", "idea"))
        }
    }
}
