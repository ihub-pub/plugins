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
description = "IHub Verification Gradle Plugins"

dependencies {
    implementation(project(":ihub-plugins"))
    implementation(project(":ihub-bom"))
}

gradlePlugin {
    plugins {
        create("iHubTest") {
            id = "pub.ihub.plugin.ihub-test"
            displayName = "IHub Test"
            description = "IHub Test Gradle Plugin"
            implementationClass = "pub.ihub.plugin.verification.IHubTestPlugin"
            tags.set(listOf("ihub", "java", "test"))
        }
        create("iHubVerification") {
            id = "pub.ihub.plugin.ihub-verification"
            displayName = "IHub Verification"
            description = "IHub Verification Gradle Plugin"
            implementationClass = "pub.ihub.plugin.verification.IHubVerificationPlugin"
            tags.set(listOf("ihub", "java", "verification"))
        }
    }
}
