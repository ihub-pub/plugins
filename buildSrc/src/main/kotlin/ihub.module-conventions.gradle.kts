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
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.publish.tasks.GenerateModuleMetadata

/**
 * IHub 子模块公共配置 convention plugin。
 *
 * 替代旧版 root build.gradle.kts 中 subprojects {} 块的"中性"部分，
 * 为 Gradle Isolated Projects 兼容做准备。
 *
 * ## 设计取舍
 *
 * IHub 自身插件（`pub.ihub.plugin.ihub-groovy` / `ihub-test` / `ihub-verification` /
 * `ihub-publish`）依赖运行时由 `pub.ihub.plugin.ihub-settings` 注册的 `ihub` 版本目录
 * （含 `ihub.bundles.groovy` 等条目），且 root build 自身已 `alias(ihub.plugins.root)`
 * 引用了 `pub.ihub.plugin`。如果在 buildSrc 把这些 IHub jar 也拉到根 classpath，
 * 会触发 "plugin already on classpath with unknown version" 冲突。
 *
 * 所以 convention plugin 只承担：
 *
 * - `java-gradle-plugin` / `com.gradle.plugin-publish` / `pl.droidsonroids.jacoco.testkit`
 *   （非 IHub 的中性插件）
 * - 仓库声明、Groovy/Spock BOM、通用依赖、gradlePlugin website / vcsUrl、关闭 GenerateModuleMetadata
 *
 * 各子模块仍需在自身 `plugins {}` 块中显式 apply IHub 链：
 *
 * ```kotlin
 * plugins {
 *     id("ihub.module-conventions")
 *     id("pub.ihub.plugin.ihub-groovy")
 *     id("pub.ihub.plugin.ihub-test")
 *     id("pub.ihub.plugin.ihub-verification")
 *     id("pub.ihub.plugin.ihub-publish")
 * }
 * ```
 */
plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
    id("pl.droidsonroids.jacoco.testkit")
}

repositories {
    gradlePluginPortal()
}

val libsCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
val groovyVersion = libsCatalog.findVersion("groovy").get().toString()
val spockVersion = libsCatalog.findVersion("spock").get().toString()

dependencies {
    "implementation"(gradleApi())
    if (project.name !in listOf("ihub-base", "ihub-base-test")) {
        "implementation"(project(":ihub-base"))
        "testImplementation"(project(":ihub-base-test"))
    }
}

// 与 Gradle 内置 Groovy 版本保持一致
// 通过 `iHubBom { importBoms { ... } }` 让 Spring Dependency Management 强制对齐 4.0.29
// （直接用 `enforcedPlatform()` 无法压制 plugin-publish / testkit 拉入的 5.0.4 strict 请求）。
// `iHubBom` 由 ihub-bom 注册（被 ihub-publish 间接引入），用 pluginManager.withPlugin 推迟到
// ihub-bom 真正 apply 之后再调用。
pluginManager.withPlugin("pub.ihub.plugin.ihub-bom") {
    configure<pub.ihub.plugin.bom.IHubBomExtension> {
        importBoms(object : org.gradle.api.Action<pub.ihub.plugin.bom.specs.GroupSpec<pub.ihub.plugin.bom.specs.ModuleSpec>> {
            override fun execute(groupSpec: pub.ihub.plugin.bom.specs.GroupSpec<pub.ihub.plugin.bom.specs.ModuleSpec>) {
                val groovyBom: pub.ihub.plugin.bom.specs.ModuleSpec = groupSpec.group("org.apache.groovy").module("groovy-bom")
                groovyBom.version(groovyVersion)
                val spockBom: pub.ihub.plugin.bom.specs.ModuleSpec = groupSpec.group("org.spockframework").module("spock-bom")
                spockBom.version(spockVersion)
            }
        })
    }
}

configure<GradlePluginDevelopmentExtension> {
    website.set("https://github.com/ihub-pub/plugins")
    vcsUrl.set("https://github.com/ihub-pub/plugins.git")
}

// 跳过 Gradle 元数据生成，详见：https://github.com/gradle/gradle/issues/11862
tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}
