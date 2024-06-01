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
import pub.ihub.plugin.verification.IHubVerificationExtension
plugins {
    alias(ihub.plugins.root)
    alias(ihub.plugins.copyright)
    alias(ihub.plugins.git.hooks)
    alias(ihub.plugins.java) apply false
    alias(ihub.plugins.verification) apply false
    alias(ihub.plugins.publish) apply false
    alias(libs.plugins.plugin.publish) apply false
    // Jacoco暂不支持TestKit，如下插件用于集成Jacoco报告，详见：https://github.com/gradle/gradle/issues/1465
    alias(libs.plugins.testkit)
}

iHubGitHooks {
    hooks.set(mapOf(
        "pre-commit" to "./gradlew build -x test",
        "commit-msg" to "./gradlew commitCheck"
    ))
}

subprojects {
    apply {
        plugin("groovy")
        plugin("pub.ihub.plugin.ihub-java")
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

//<editor-fold desc="组件依赖版本管理">

    // gradle groovy版本没有升级至4.0，强制指定3.0版本
    iHubBom {
        groupVersions {
            group("org.codehaus.groovy").version("3.0.21")
            group("org.spockframework").version("2.3-groovy-3.0")
            group("com.athaydes").version("2.5.1-groovy-3.0")
        }
    }
    configure<IHubVerificationExtension> {
        codenarcVersion.set("3.3.0")
    }

//</editor-fold>

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
