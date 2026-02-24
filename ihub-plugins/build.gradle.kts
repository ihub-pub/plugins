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
description = "IHub Gradle Plugins"

dependencies {
    api(project(":ihub-bom"))
    implementation(libs.freefair.git)
    implementation(libs.ben.manes.versions)
    implementation("cn.hutool:hutool-core")
}

gradlePlugin {
    plugins {
        create("iHubPlugins") {
            id = "pub.ihub.plugin"
            displayName = "IHub Plugins"
            description = "IHub Plugins Gradle Plugin"
            implementationClass = "pub.ihub.plugin.IHubPluginsPlugin"
            tags.set(listOf("ihub", "groovy", "java"))
        }
        create("iHubVersion") {
            id = "pub.ihub.plugin.ihub-version"
            displayName = "IHub Version"
            description = "IHub Gradle Version Gradle Plugin"
            implementationClass = "pub.ihub.plugin.version.IHubVersionPlugin"
            tags.set(listOf("ihub", "git", "version"))
        }
        create("iHubProfiles") {
            id = "pub.ihub.plugin.ihub-profiles"
            displayName = "IHub Profiles"
            description = "IHub Profiles Gradle Plugin"
            implementationClass = "pub.ihub.plugin.profiles.IHubProfilesPlugin"
            tags.set(listOf("ihub", "java", "profiles"))
        }
    }
}
