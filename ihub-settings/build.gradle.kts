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
description = "IHub Gradle Plugins Settings"

dependencies {
    implementation(libs.ihub.core)
    implementation(libs.freefair.settings)
}

tasks.register("listPluginIds") {
    group = "ihub"
    val ids = mutableListOf<String>()
    rootProject.allprojects {
        this.extensions.findByType(GradlePluginDevelopmentExtension::class)?.plugins?.forEach {
            ids.add(it.id)
        }
    }
    file("build/plugin-ids").let {
        if (!it.exists()) {
            it.parentFile.mkdirs()
        }
        it.writeText(ids.joinToString("\n"))
    }
    outputs.file("build/plugin-ids")
}

tasks.named("processResources", ProcessResources::class).configure {
    into("META-INF/ihub") {
        from(tasks.named("listPluginIds"))
    }
}

gradlePlugin {
    plugins {
        create("iHubSettings") {
            id = "pub.ihub.plugin.ihub-settings"
            displayName = "IHub Settings"
            description = "IHub Gradle Plugins Settings"
            implementationClass = "pub.ihub.plugin.IHubSettingsPlugin"
            tags.set(listOf("ihub", "java", "settings"))
        }
    }
}
