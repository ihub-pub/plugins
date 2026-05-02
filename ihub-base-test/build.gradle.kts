/*
 * Copyright (c) 2022-2026 the original author or authors.
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

description = "IHub Gradle Plugins Test Fixtures (no Gradle plugin published)"

dependencies {
    implementation(gradleTestKit())
    implementation("org.spockframework:spock-core")
}

// 注意：本模块仅提供 Spock TestKit 测试基类（IHubSpecification），不发布任何 Gradle 插件。
// 因此故意不声明 gradlePlugin {} 块，避免 Plugin Portal 校验 implementationClass 时
// 因 IHubSpecification 不是 Plugin<?> 实现而失败。
// com.gradle.plugin-publish 由 ihub.module-conventions 引入，但本模块无需发布插件，故禁用 publishPlugins。
tasks.named("publishPlugins") {
    enabled = false
}