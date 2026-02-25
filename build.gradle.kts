import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.testing.jacoco.tasks.JacocoReport

/*
 * Copyright (c) 2021-2025 the original author or authors.
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
    alias(ihub.plugins.root)
    alias(ihub.plugins.copyright)
    alias(ihub.plugins.git.hooks)
    alias(ihub.plugins.groovy) apply false
    alias(ihub.plugins.verification) apply false
    alias(ihub.plugins.publish) apply false
    alias(libs.plugins.plugin.publish) apply false
    // Jacoco暂不支持TestKit，如下插件用于集成Jacoco报告，详见：https://github.com/gradle/gradle/issues/1465
    alias(libs.plugins.testkit)
}

iHubGitHooks {
    hooks.set(
        mapOf(
            "pre-commit" to "./gradlew clean check -x test spotlessCheck spotlessApply",
            "commit-msg" to "./gradlew commitCheck"
        )
    )
}

subprojects {
    apply {
        plugin("pub.ihub.plugin.ihub-groovy")
        plugin("pub.ihub.plugin.ihub-test")
        plugin("pub.ihub.plugin.ihub-verification")
        plugin("pub.ihub.plugin.ihub-publish")
        plugin("java-gradle-plugin")
        plugin("com.gradle.plugin-publish")
        plugin("pl.droidsonroids.jacoco.testkit")
    }

    repositories {
        gradlePluginPortal()
    }

    // 与Gradle内置Groovy版本保持一致
    iHubBom {
        importBoms {
            group("org.apache.groovy").module("groovy-bom").version("4.0.29")
            group("org.spockframework").module("spock-bom").version("2.4-groovy-4.0")
        }
    }

    dependencies {
        "implementation"(gradleApi())
        if (!listOf("ihub-base", "ihub-base-test").contains(project.name)) {
            "implementation"(project(":ihub-base"))
            "testImplementation"(project(":ihub-base-test"))
        }
    }

    configure<GradlePluginDevelopmentExtension> {
        website.set("https://github.com/ihub-pub/plugins")
        vcsUrl.set("https://github.com/ihub-pub/plugins.git")
    }

    // 跳过Gradle元数据生成，详见：https://github.com/gradle/gradle/issues/11862
    tasks.withType(GenerateModuleMetadata::class) {
        enabled = false
    }
}
