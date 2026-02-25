<p align="center">
    <a target="_blank" href="https://ihub.pub/">
        <img src="https://doc.ihub.pub/ihub.svg" height="150" alt="IHub">
        <img src="https://doc.ihub.pub/ihub_plugins.svg" height="150" alt="IHub Plugins">
    </a>
</p>

<h1 align="center">IHub Plugins</h1>

<p align="center">
    <strong>一套为 Gradle 项目提供基础设施的插件集，极大简化项目配置</strong>
</p>

<p align="center">
    <a target="_blank" href="https://bestpractices.coreinfrastructure.org/projects/6921">
        <img alt="CII Best Practices" src="https://badge.ihub.pub/cii/level/6921">
    </a>
    <a target="_blank" href="https://github.com/ihub-pub/plugins/actions/workflows/gradle-build.yml">
        <img src="https://badge.ihub.pub/github/actions/workflow/status/ihub-pub/plugins/gradle-build.yml?branch=main&label=Build&logo=GitHub+Actions&logoColor=white" alt="Gradle Build"/>
    </a>
    <a title="Test Cases" href="https://ihub-pub.testspace.com/spaces/219260?utm_campaign=metric&utm_medium=referral&utm_source=badge">
        <img alt="Tests" src="https://badge.ihub.pub/testspace/tests/ihub-pub/ihub-pub:plugins/main?compact_message&label=Tests&logo=GitHub+Actions&logoColor=white" />
    </a>
    <a target="_blank" href="https://www.codefactor.io/repository/github/ihub-pub/plugins">
        <img src="https://badge.ihub.pub/codefactor/grade/github/ihub-pub/plugins/main?color=white&label=Codefactor&labelColor=F44A6A&logo=CodeFactor&logoColor=white" alt="CodeFactor"/>
    </a>
    <a target="_blank" href="https://codecov.io/gh/ihub-pub/plugins">
        <img src="https://badge.ihub.pub/codecov/c/github/ihub-pub/plugins?token=ZQ0WR3ZSWG&color=white&label=Codecov&labelColor=F01F7A&logo=Codecov&logoColor=white" alt="Codecov"/>
    </a>
    <a target="_blank" href="https://plugins.gradle.org/plugin/pub.ihub.plugin">
        <img src="https://badge.ihub.pub/maven-metadata/v?color=white&label=Gradle&labelColor=02303A&logo=Gradle&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fpub%2Fihub%2Fplugin%2Fihub-plugins%2Fmaven-metadata.xml" alt="Gradle Plugin"/>
    </a>
</p>

<p align="center">
    <a target="_blank" href="https://github.com/ihub-pub/plugins/stargazers">
        <img src="https://badge.ihub.pub/github/stars/ihub-pub/plugins?color=white&style=flat&logo=GitHub&labelColor=181717" alt="GitHub Stars"/>
    </a>
    <a target="_blank" href='https://gitee.com/ihub-pub/plugins/stargazers'>
        <img src='https://badge.ihub.pub/badge/dynamic/json?url=https%3A%2F%2Fgitee.com%2Fapi%2Fv5%2Frepos%2Fihub-pub%2Fplugins&query=%24.stargazers_count&style=flat&logo=gitee&label=stars&labelColor=c71d23&color=white&cacheSeconds=5000' alt='Gitee Stars'/>
    </a>
    <a href="https://github.com/ihub-pub/plugins/blob/main/LICENSE">
        <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" alt="License">
    </a>
</p>

---

## 特性

- **开箱即用**：零配置即可快速开始，默认配置遵循最佳实践
- **高度可扩展**：灵活的扩展属性系统，支持多种配置方式
- **依赖管理**：统一的 BOM 依赖版本管理，告别版本冲突
- **多语言支持**：支持 Java、Groovy、Kotlin 等 JVM 语言
- **Spring Boot 集成**：原生支持 Spring Boot 和 GraalVM Native
- **代码质量**：内置代码检查、测试覆盖率等验证工具
- **发布支持**：支持发布到 Maven Central、Gradle Plugin Portal

## 插件列表

### 核心插件

| 插件 ID | 名称 | 类型 | 描述 |
|---------|------|------|------|
| [pub.ihub.plugin.ihub-settings](https://doc.ihub.pub/plugins/list/iHubSettings) | Settings | Settings | 插件仓库、版本管理及子项目管理 |
| [pub.ihub.plugin](https://doc.ihub.pub/plugins/list/iHub) | Base | Project | 基础插件，配置组件仓库及扩展属性 |
| [pub.ihub.plugin.ihub-version](https://doc.ihub.pub/plugins/list/iHubVersion) | Version | Project | 依赖版本检查与更新 |
| [pub.ihub.plugin.ihub-profiles](https://doc.ihub.pub/plugins/list/iHubProfiles) | Profiles | Project | Gradle 版本 Maven POM Profiles |

### 语言插件

| 插件 ID | 名称 | 类型 | 描述 |
|---------|------|------|------|
| [pub.ihub.plugin.ihub-java](https://doc.ihub.pub/plugins/list/iHubJava) | Java | Project | Java 环境配置、默认依赖及兼容性 |
| [pub.ihub.plugin.ihub-groovy](https://doc.ihub.pub/plugins/list/iHubGroovy) | Groovy | Project | Groovy 环境配置及组件依赖 |
| [pub.ihub.plugin.ihub-kotlin](https://doc.ihub.pub/plugins/list/iHubKotlin) | Kotlin | Project | Kotlin 环境配置 |

### 依赖管理

| 插件 ID | 名称 | 类型 | 描述 |
|---------|------|------|------|
| [pub.ihub.plugin.ihub-bom](https://doc.ihub.pub/plugins/list/iHubBom) | BOM | Project | 依赖版本管理及兼容性配置 |

### Spring 生态

| 插件 ID | 名称 | 类型 | 描述 |
|---------|------|------|------|
| [pub.ihub.plugin.ihub-boot](https://doc.ihub.pub/plugins/list/iHubBoot) | Boot | Project | Spring Boot 插件及镜像配置 |
| [pub.ihub.plugin.ihub-native](https://doc.ihub.pub/plugins/list/iHubNative) | Native | Project | GraalVM Native 编译支持 |

### 验证与测试

| 插件 ID | 名称 | 类型 | 描述 |
|---------|------|------|------|
| [pub.ihub.plugin.ihub-test](https://doc.ihub.pub/plugins/list/iHubTest) | Test | Project | 测试任务配置 |
| [pub.ihub.plugin.ihub-verification](https://doc.ihub.pub/plugins/list/iHubVerification) | Verification | Project | 代码静态检查、测试覆盖率 |

### 构建与发布

| 插件 ID | 名称 | 类型 | 描述 |
|---------|------|------|------|
| [pub.ihub.plugin.ihub-publish](https://doc.ihub.pub/plugins/list/iHubPublish) | Publish | Project | 组件发布配置 |
| [pub.ihub.plugin.ihub-shadow](https://doc.ihub.pub/plugins/list/iHubShadow) | Shadow | Project | Shadow 打包支持 |
| [pub.ihub.plugin.ihub-javaagent](https://doc.ihub.pub/plugins/list/iHubJavaagent) | Javaagent | Project | Javaagent 集成 |

### 工具插件

| 插件 ID | 名称 | 类型 | 描述 |
|---------|------|------|------|
| [pub.ihub.plugin.ihub-copyright](https://doc.ihub.pub/plugins/list/iHubCopyright) | Copyright | Project | IDEA 版权信息配置 |
| [pub.ihub.plugin.ihub-git-hooks](https://doc.ihub.pub/plugins/list/iHubGitHooks) | GitHooks | Project | Git 钩子配置 |
| [pub.ihub.plugin.ihub-node](https://doc.ihub.pub/plugins/list/iHubNode) | Node.js | Project | Node.js 及 cnpm 支持 |

## 快速开始

### 环境要求

| 插件版本 | Java | Gradle |
|----------|------|--------|
| 1.9.3+ | 17~25 | 9.3.1 |
| 1.9.1+ | 17~25 | 9.1.0 |
| 1.9.0 | 17~24 | 9.0.0 |
| 1.7.6+ | 17~23 | 8.13 |
| 1.7.2+ | 17~23 | 8.4 |
| 1.5.6+ | 17~21 | 8.0 |

### 安装

在 `settings.gradle` 中配置 Settings 插件：

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-settings' version '1.9.4'
}
```

在 `build.gradle` 中应用插件：

```groovy
plugins {
    id 'pub.ihub.plugin'
}
```

### 基础配置

在 `gradle.properties` 中添加配置：

```properties
# 启用本地仓库
iHub.mavenLocalEnabled=true
# 启用阿里云代理仓库
iHub.mavenAliYunEnabled=true
```

### 使用示例

#### Java 项目

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-java'
}

iHubBom {
    importBoms {
        group('org.springframework.boot').module('spring-boot-dependencies').version('3.2.0')
    }
}
```

#### Spring Boot 项目

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-boot'
}

iHubBoot {
    enabled = true
    bootImage {
        imageName = 'my-app:latest'
    }
}
```

#### Groovy 项目

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-groovy'
}
```

构建时会自动配置仓库，控制台输出：

```text
┌──────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                       Gradle Project Repos                                       │
├──────────────────────────────────────────────────────────────────────────────────────────────────┤
│ MavenLocal(file:/C:/Users/User/.m2/repository/)                                                  │
│ AliYunPublic(https://maven.aliyun.com/repository/public)                                         │
│ MavenRepo                                                                                        │
└──────────────────────────────────────────────────────────────────────────────────────────────────┘
```

更多使用方式请参考 [快速上手](https://doc.ihub.pub/plugins/)

## 项目结构

```
plugins/
├── ihub-base/              # 核心基础设施
├── ihub-base-test/         # 测试基础设施
├── ihub-plugins/           # 核心插件（仓库、版本、Profiles）
├── ihub-settings/          # Settings 插件
├── ihub-bom/               # BOM 依赖管理
├── ihub-java/              # Java 插件
├── ihub-groovy/            # Groovy 插件
├── ihub-kotlin/            # Kotlin 插件
├── ihub-spring/            # Spring Boot 插件
├── ihub-verification/      # 验证插件（测试、代码质量）
├── ihub-publish/           # 发布插件
├── ihub-shadow/            # Shadow 插件
├── ihub-copyright/         # 版权插件
├── ihub-githooks/          # Git Hooks 插件
├── ihub-node/              # Node.js 插件
├── ihub-javaagent/         # Javaagent 插件
├── samples/                # 示例项目
└── docs/                   # 文档站点
```

## 技术栈

| 技术 | 版本 | 描述 |
|------|------|------|
| Groovy | 4.0.x | 主要开发语言 |
| Gradle | 9.x | 构建系统 |
| Spock | 2.4 | BDD 测试框架 |
| Gradle TestKit | - | 插件测试框架 |
| CodeNarc | 3.7 | Groovy 静态分析 |

## 构建命令

```bash
# 构建项目
./gradlew build

# 运行测试
./gradlew test

# 代码检查（跳过测试）
./gradlew check -x test

# 提交信息检查
./gradlew commitCheck

# 发布到本地 Maven 仓库
./gradlew publishToMavenLocal

# 清理构建
./gradlew clean
```

## 配置属性

属性支持多种配置方式，优先级从高到低：

1. **系统属性** `-DiHub.property=value`
2. **环境变量** `IHUB_PROPERTY=value`
3. **项目属性** `gradle.properties` 或 `-Pproperty=value`
4. **默认值**

常用配置示例：

| 配置项 | 描述 | 默认值 |
|--------|------|--------|
| `iHub.mavenLocalEnabled` | 启用本地仓库 | `false` |
| `iHub.mavenAliYunEnabled` | 启用阿里云代理 | `true` |
| `iHub.repoIncludeGroup` | 仓库包含组 | - |

## 文档

- [官方文档](https://doc.ihub.pub/plugins/)
- [快速上手](https://doc.ihub.pub/plugins/basics/gradleBasic.html)
- [插件列表](https://doc.ihub.pub/plugins/list/iHub.html)
- [高级配置](https://doc.ihub.pub/plugins/advanced/multiAdvanced.html)

## 项目状态

![Alt](https://repobeats.axiom.co/api/embed/577279f67858fb89c702e0cf0bc604e42decca5d.svg "Repobeats analytics image")

## 贡献指南

欢迎为项目做出贡献！请阅读 [贡献指南](https://github.com/ihub-pub/.github/blob/main/CONTRIBUTING.md) 了解详情。

[![Contributors](https://contrib.rocks/image?repo=ihub-pub/plugins)](https://github.com/ihub-pub/plugins/graphs/contributors "Contributors")

## 开源协议

本项目基于 [Apache License 2.0](LICENSE) 开源协议。
