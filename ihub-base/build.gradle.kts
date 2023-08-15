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
description = "IHub Gradle Plugins Base"

gradlePlugin {
    plugins {
        create("ihubBase") {
            id = "pub.ihub.plugin.ihub-base"
            displayName = "IHub Base"
            description = "IHub Gradle Plugins Base"
            implementationClass = "pub.ihub.plugin.IHubPluginMethods"
            tags.set(listOf("ihub", "base"))
        }
    }
}

tasks.register("listLibsVersions") {
    group = "ihub"
    val ids = mutableListOf<String>()
    ids.add("ihub-libs=${libs.versions.ihub.get()}")
    ids.add("ihub-plugins=${version}")
    ids.add("plugin-publish=${libs.versions.plugin.publish.get()}")
    file("build/libs-versions").let {
        if (!it.exists()) {
            it.parentFile.mkdirs()
        }
        it.writeText(ids.joinToString("\n"))
    }
    outputs.file("build/libs-versions")
}

tasks.named("processResources", ProcessResources::class).configure {
    into("META-INF/ihub") {
        from(tasks.named("listLibsVersions"))
    }
}
