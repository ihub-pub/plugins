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
description = "IHub Publish Gradle Plugins"

dependencies {
    implementation(project(":ihub-bom"))
    implementation(project(":ihub-plugins"))
    compileOnly(libs.freefair.utils)
    implementation(libs.freefair.github)
    implementation("cn.hutool:hutool-http")
    // GithubPom插件运行时依赖
    runtimeOnly("com.google.code.gson:gson")
    implementation(libs.maven.central)
}

gradlePlugin {
    plugins {
        create("iHubPublish") {
            id = "pub.ihub.plugin.ihub-publish"
            displayName = "IHub Publish"
            description = "IHub Publish Gradle Plugin"
            implementationClass = "pub.ihub.plugin.publish.IHubPublishPlugin"
            tags.set(listOf("ihub", "java", "publish"))
        }
    }
}
