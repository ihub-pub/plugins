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
description = "IHub Kotlin Gradle Plugins"

dependencies {
    implementation(project(":ihub-java"))
    implementation(libs.kotlin)
}

gradlePlugin {
    plugins {
        create("iHubKotlin") {
            id = "pub.ihub.plugin.ihub-kotlin"
            displayName = "IHub Kotlin"
            description = "IHub Kotlin Gradle Plugin"
            implementationClass = "pub.ihub.plugin.kotlin.IHubKotlinPlugin"
            tags.set(listOf("ihub", "kotlin"))
        }
    }
}
