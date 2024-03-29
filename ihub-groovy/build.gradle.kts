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
description = "IHub Groovy Gradle Plugins"

dependencies {
    implementation(project(":ihub-java"))
}

gradlePlugin {
    plugins {
        create("iHubGroovy") {
            id = "pub.ihub.plugin.ihub-groovy"
            displayName = "IHub Groovy"
            description = "IHub Groovy Gradle Plugin"
            implementationClass = "pub.ihub.plugin.groovy.IHubGroovyPlugin"
            tags.set(listOf("ihub", "groovy"))
        }
    }
}
