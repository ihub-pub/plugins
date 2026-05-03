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
    id("ihub.module-conventions")
    id("pub.ihub.plugin.ihub-groovy")
    id("pub.ihub.plugin.ihub-test")
    id("pub.ihub.plugin.ihub-verification")
    id("pub.ihub.plugin.ihub-publish")
}

description = "IHub AI Metadata Gradle Plugin"

gradlePlugin {
    plugins {
        create("iHubMeta") {
            id = "pub.ihub.plugin.ihub-meta"
            displayName = "IHub Meta"
            description = "IHub AI Metadata Gradle Plugin - Generates project structure JSON for LLM consumption"
            implementationClass = "pub.ihub.plugin.meta.IHubMetaPlugin"
            tags.set(listOf("ihub", "ai", "meta", "json", "llm"))
        }
    }
}
