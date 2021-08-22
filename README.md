<a target="_blank" href="https://ihub.pub/">
    <img src="https://cdn.jsdelivr.net/gh/ihub-pub/ihub-pub.github.io/ihub.svg" height="150" alt="IHub">
    <img src="https://cdn.jsdelivr.net/gh/ihub-pub/ihub-pub.github.io/ihub_plugins.svg" height="150" alt="IHub">
</a>

---

[![Gradle Build](https://github.com/ihub-pub/plugins/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/ihub-pub/plugins/actions/workflows/gradle-build.yml)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f866ca35cbb44347a210722a2da8aabc)](https://app.codacy.com/gh/ihub-pub/plugins?utm_source=github.com&utm_medium=referral&utm_content=ihub-pub/plugins&utm_campaign=Badge_Grade_Settings)
[![codecov](https://codecov.io/gh/ihub-pub/plugins/branch/main/graph/badge.svg?token=ZQ0WR3ZSWG)](https://codecov.io/gh/ihub-pub/plugins)
[![GitHub](https://img.shields.io/badge/GitHub-181717.svg?style=flat&logo=GitHub)](https://github.com/ihub-pub "IHubPub")
[![Gitee](https://img.shields.io/badge/Gitee-C71D23.svg?style=flat&logo=Gitee)](https://gitee.com/ihub-pub "IHubPub")
[![Docs](https://img.shields.io/badge/Docs-8CA1AF.svg?style=flat&logo=Read+the+Docs&logoColor=white)](https://doc.ihub.pub/plugins "Docs")
[![Join the chat at https://gitter.im/ihub-pub/plugins](https://img.shields.io/badge/Gitter-45af90.svg?style=flat&logo=Gitter&logoColor=white&)](https://gitter.im/ihub-pub/plugins?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge "Chat On Gitter")
[![JetBrains](https://img.shields.io/badge/JetBrains-black.svg?style=flat&logo=JetBrains&logoColor=white)](https://www.jetbrains.com "Thanks to JetBrains for sponsoring")
[![Gradle Release](https://img.shields.io/maven-metadata/v?color=0A7891&label=Gradle&labelColor=02303A&logo=Gradle&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fpub%2Fihub%2Fplugin%2Fihub-plugins%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/pub.ihub.plugin "IHub Plugins Gradle Plugin")

一套Gradle插件集，封装了常用Gradle插件，并做了一些个性化缺省配置，极大的简化项目管理配置。

## 📦 插件列表

| 插件ID | 插件名称 | 插件描述 |
|----|-------------|-------------|
| [pub.ihub.plugin.ihub-settings](https://doc.ihub.pub/plugins/#/iHubSettings) | `设置插件` | 配置插件仓库以及子项目 |
| [pub.ihub.plugin](https://doc.ihub.pub/plugins/#/iHub) | `基础插件` | 配置组件仓库 |
| [pub.ihub.plugin.ihub-bom](https://doc.ihub.pub/plugins/#/iHubBom) | `组件依赖管理` | 配置组件默认依赖版本以及兼容性管理 |
| [pub.ihub.plugin.ihub-java](https://doc.ihub.pub/plugins/#/iHubJava) | `Java插件` | 配置一些默认依赖以及兼容性配置 |
| [pub.ihub.plugin.ihub-groovy](https://doc.ihub.pub/plugins/#/iHubGroovy) | `Groovy插件` | 配置Groovy组件依赖 |
| [pub.ihub.plugin.ihub-publish](https://doc.ihub.pub/plugins/#/iHubPublish) | `发布插件` | 配置组件发布仓库以及其他个性化组件配置 |
| [pub.ihub.plugin.ihub-test](https://doc.ihub.pub/plugins/#/iHubTest) | `测试插件` | 测试相关插件 |
| [pub.ihub.plugin.ihub-verification](https://doc.ihub.pub/plugins/#/iHubVerification) | `验证插件` | 配置代码静态检查以及测试用例覆盖等 |
| [pub.ihub.plugin.ihub-boot](https://doc.ihub.pub/plugins/#/iHubBoot) | `Boot插件` | 用于镜像个性化配置 |
| [pub.ihub.plugin.ihub-native](https://doc.ihub.pub/plugins/#/iHubNative) | `Native插件` | 用于原生镜像个性化配置 |
| [pub.ihub.plugin.ihub-git-hooks](https://doc.ihub.pub/plugins/#/ihubGitHooks) | `GitHooks插件` | 用于配置GitHooks |

## 🎉 上手指南

### 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.1'
}
```

使用详见[samples](https://github.com/ihub-pub/plugins/tree/main/samples)

## 👨‍💻 开源贡献指南

请阅读 [贡献指南](https://github.com/ihub-pub/.github/blob/main/CONTRIBUTING.md) 为该项目做出贡献