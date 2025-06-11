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
plugins {
    kotlin("jvm")
}

description = "IHub Git Hooks Gradle Plugins"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("cn.hutool:hutool-core")
    implementation("com.google.code.gson:gson:2.10.1") // Added for JSON handling

    // Test dependencies
    testImplementation(project(":ihub-base-test"))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
    testImplementation("io.kotest:kotest-assertions-core:5.5.4")
    testImplementation("io.kotest:kotest-property:5.5.4")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("iHubGitHooks") {
            id = "pub.ihub.plugin.ihub-git-hooks"
            displayName = "IHub GitHooks"
            description = "IHub Gradle GitHooks Gradle Plugin"
            implementationClass = "pub.ihub.plugin.githooks.IHubGitHooksPlugin"
            tags.set(listOf("ihub", "git", "gitHooks", "idea"))
        }
    }
}
