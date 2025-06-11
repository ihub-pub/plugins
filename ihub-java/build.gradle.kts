/*
 * Copyright (c) 2021-2024 the original author or authors.
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

description = "IHub Java Gradle Plugins"

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":ihub-plugins"))
    api(project(":ihub-bom"))
    implementation(libs.freefair.lombok)
    implementation(libs.byte.buddy)
    implementation(libs.jmolecules.bytebuddy)
    implementation(libs.johnrengelman.processes)

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
        create("iHubJavaBase") {
            id = "pub.ihub.plugin.ihub-java-base"
            displayName = "IHub Java Base"
            description = "IHub Java Base Gradle Plugin"
            implementationClass = "pub.ihub.plugin.java.IHubJavaBasePlugin"
            tags.set(listOf("ihub", "java"))
        }
        create("iHubJava") {
            id = "pub.ihub.plugin.ihub-java"
            displayName = "IHub Java"
            description = "IHub Java Gradle Plugin"
            implementationClass = "pub.ihub.plugin.java.IHubJavaPlugin"
            tags.set(listOf("ihub", "java"))
        }
    }
}
