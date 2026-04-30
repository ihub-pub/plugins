/*
 * Copyright (c) 2026 the original author or authors.
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
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

// IHub Plugins 自身的版本（与 settings.gradle.kts 中 pub.ihub.plugin.ihub-settings 的版本保持一致）。
// 升级时同步修改根目录 settings.gradle.kts 中的 version 字段。
val iHubPluginsVersion = "1.9.5"
val pluginPublishVersion = libs.versions.plugin.publish.get()
val testkitVersion = libs.plugins.testkit.get().version.toString()

dependencies {
    // 让 precompiled script plugin 能用 type-safe 的 `libs` 访问器
    // （Gradle 已知 quirk：buildSrc 的 precompiled script 不会自动暴露 libs；这是社区标准 workaround）
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    // 仅 plugins {} 块需要的"中性"插件（不引用 IHub 版本目录、不会与 root build 自有
    // alias(ihub.plugins.root) 冲突）。IHub 自身的 ihub-groovy / ihub-test / ihub-verification /
    // ihub-publish 不在 buildSrc 拉取——子模块在 build.gradle.kts 顶部自行 apply。
    implementation("com.gradle.plugin-publish:com.gradle.plugin-publish.gradle.plugin:$pluginPublishVersion")
    implementation("pl.droidsonroids.jacoco.testkit:pl.droidsonroids.jacoco.testkit.gradle.plugin:$testkitVersion")

    // 让 convention 脚本能 import IHubBomExtension 并发出类型安全调用。
    // 用 implementation 而非 compileOnly，否则运行时（其实是 pluginManager.withPlugin 触发的回调内）
    // 找不到 IHubBomExtension 类。直接拉 ihub-bom + ihub-base jar，不会注册到 root build 的
    // plugin marker（避免与 alias(ihub.plugins.root) 冲突）。
    implementation("pub.ihub.plugin:ihub-bom:$iHubPluginsVersion")
    implementation("pub.ihub.plugin:ihub-base:$iHubPluginsVersion")
}
