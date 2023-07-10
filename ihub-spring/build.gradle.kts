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
description = "IHub Spring Gradle Plugins"

dependencies {
    implementation(project(":ihub-java"))
    implementation(project(":ihub-verification"))
    implementation(libs.spring.boot)
    compileOnly("org.springframework.boot:spring-boot-buildpack-platform")
    implementation(libs.native)
}

gradlePlugin {
    plugins {
        create("iHubBoot") {
            id = "pub.ihub.plugin.ihub-boot"
            displayName = "IHub Boot"
            description = "IHub Boot Gradle Plugin"
            implementationClass = "pub.ihub.plugin.spring.IHubBootPlugin"
            tags.set(listOf("ihub", "java", "spring", "boot"))
        }
        create("iHubNative") {
            id = "pub.ihub.plugin.ihub-native"
            displayName = "IHub Native"
            description = "IHub Native Gradle Plugin"
            implementationClass = "pub.ihub.plugin.spring.IHubNativePlugin"
            tags.set(listOf("ihub", "java", "spring", "native"))
        }
    }
}
