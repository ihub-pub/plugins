<p align="center">
    <a target="_blank" href="https://ihub.pub/">
        <img src="https://cdn.jsdelivr.net/gh/ihub-pub/ihub-pub.github.io/ihub.svg" height="150" alt="IHub">
        <img src="https://cdn.jsdelivr.net/gh/ihub-pub/ihub-pub.github.io/ihub_plugins.svg" height="150" alt="IHub">
    </a>
</p>

---

<p align="center">
    <a target="_blank" href="https://github.com/ihub-pub/plugins/actions/workflows/gradle-build.yml">
        <img src="https://img.shields.io/github/actions/workflow/status/ihub-pub/plugins/gradle-build.yml?branch=main&label=Build&logo=GitHub+Actions&logoColor=white" alt="Gradle Build"/>
    </a>
    <a target="_blank" href="https://www.codefactor.io/repository/github/ihub-pub/plugins">
        <img src="https://img.shields.io/codefactor/grade/github/ihub-pub/plugins/main?color=white&label=Codefactor&labelColor=F44A6A&logo=CodeFactor&logoColor=white" alt="CodeFactor"/>
    </a>
    <a target="_blank" href="https://codecov.io/gh/ihub-pub/plugins">
        <img src="https://img.shields.io/codecov/c/github/ihub-pub/plugins?token=ZQ0WR3ZSWG&color=white&label=Codecov&labelColor=F01F7A&logo=Codecov&logoColor=white" alt="Codecov"/>
    </a>
    <a target="_blank" href="https://github.com/ihub-pub/plugins">
        <img src="https://img.shields.io/github/stars/ihub-pub/plugins?color=white&logo=GitHub&labelColor=181717" alt="IHubPub"/>
    </a>
    <a target="_blank" href="https://gitee.com/ihub-pub">
        <img src="https://img.shields.io/badge/Gitee-C71D23.svg?style=flat&logo=Gitee" alt="IHubPub"/>
    </a>
    <a target="_blank" href="https://doc.ihub.pub/plugins">
        <img src="https://img.shields.io/badge/Docs-8CA1AF.svg?style=flat&logo=Read+the+Docs&logoColor=white" alt="Docs"/>
    </a>
    <a target="_blank" href="https://gitter.im/ihub-pub/plugins?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge">
        <img src="https://img.shields.io/badge/Gitter-45af90.svg?style=flat&logo=Gitter&logoColor=white&" alt="Chat On Gitter"/>
    </a>
    <a target="_blank" href="https://www.jetbrains.com">
        <img src="https://img.shields.io/badge/JetBrains-white.svg?style=flat&logo=JetBrains&logoColor=black" alt="Thanks to JetBrains for sponsoring"/>
    </a>
    <a target="_blank" href="https://plugins.gradle.org/plugin/pub.ihub.plugin">
        <img src="https://img.shields.io/maven-metadata/v?color=white&label=Gradle&labelColor=02303A&logo=Gradle&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fpub%2Fihub%2Fplugin%2Fihub-plugins%2Fmaven-metadata.xml" alt="IHub Plugins Gradle Plugin"/>
    </a>
</p>

一套Gradle插件集，封装了常用Gradle插件，并做了一些个性化缺省配置，极大的简化项目管理配置。

## 📦 插件列表

| 插件ID                                                                                 | 插件名称 | 插件类型 | 插件描述 |
|--------------------------------------------------------------------------------------|-------------|-------------|-------------|
| [pub.ihub.plugin.ihub-settings](https://doc.ihub.pub/plugins/#/iHubSettings)         | `设置插件` | `Settings` | 置插件仓库、插件版本以及子项目管理 |
| [pub.ihub.plugin](https://doc.ihub.pub/plugins/#/iHub)                               | `基础插件` | `Project` | 基础插件，用于配置组件仓库以及一些其他扩展属性 |
| [pub.ihub.plugin.ihub-bom](https://doc.ihub.pub/plugins/#/iHubBom)                   | `Bom插件` | `Project` | 配置项目依赖组件版本以及兼容性管理 |
| [pub.ihub.plugin.ihub-java](https://doc.ihub.pub/plugins/#/iHubJava)                 | `Java插件` | `Project` | 集成Java相关插件环境、配置一些默认依赖以及兼容性配置 |
| [pub.ihub.plugin.ihub-groovy](https://doc.ihub.pub/plugins/#/iHubGroovy)             | `Groovy插件` | `Project` | 集成Groovy相关插件环境以及配置Groovy默认组件依赖 |
| [pub.ihub.plugin.ihub-publish](https://doc.ihub.pub/plugins/#/iHubPublish)           | `发布插件` | `Project` | 集成组件发布相关插件环境，配置发布仓库以及其他默认配置 |
| [pub.ihub.plugin.ihub-test](https://doc.ihub.pub/plugins/#/iHubTest)                 | `测试插件` | `Project` | 配置测试任务 |
| [pub.ihub.plugin.ihub-verification](https://doc.ihub.pub/plugins/#/iHubVerification) | `验证插件` | `Project` | 配置代码静态检查以及测试用例覆盖率等 |
| [pub.ihub.plugin.ihub-boot](https://doc.ihub.pub/plugins/#/iHubBoot)                 | `Boot插件` | `Project` | 集成spring-boot插件以及镜像默认配置 |
| [pub.ihub.plugin.ihub-native](https://doc.ihub.pub/plugins/#/iHubNative)             | `Native插件` | `Project` | 基于ihub-boot扩展引入org.graalvm.buildtools.native插件 |
| [pub.ihub.plugin.ihub-git-hooks](https://doc.ihub.pub/plugins/#/iHubGitHooks)        | `GitHooks插件` | `Project` | 配置GitHooks，可以为git操作配置一些钩子命令 |

## 🔰 支持版本

| Version | Java | Gradle |
|---------|------|--------|
| 1.3.0+  | 17   | 7.5.1  |
| 1.2.4+  | 11   | 7.4.1  |
| 1.2.3   | 11   | 7.4    |
| 1.1.1   | 11   | 7.2    |
| 1.1.0   | 11   | 7.0    |

## 🎉 上手指南

[快速上手](https://doc.ihub.pub/plugins/#/gettingStarted)

## 🧭 开源贡献指南

请阅读 [贡献指南](https://github.com/ihub-pub/.github/blob/main/CONTRIBUTING.md) 为该项目做出贡献

## 👨‍💻 Contributors

![Alt](https://repobeats.axiom.co/api/embed/577279f67858fb89c702e0cf0bc604e42decca5d.svg "Repobeats analytics image")

[![Contributors](https://contrib.rocks/image?repo=ihub-pub/plugins)](https://github.com/ihub-pub/plugins/graphs/contributors "Contributors")
