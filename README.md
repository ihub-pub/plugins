# IHub Plugins
[![Gradle Build](https://github.com/ihub-pub/plugins/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/ihub-pub/plugins/actions/workflows/gradle-build.yml)
[![codecov](https://codecov.io/gh/ihub-pub/plugins/branch/main/graph/badge.svg?token=ZQ0WR3ZSWG)](https://codecov.io/gh/ihub-pub/plugins)
[![GitHub](https://img.shields.io/badge/IHubPub-181717.svg?style=flat&logo=GitHub)](https://github.com/ihub-pub "IHubPub")
[![Gitee](https://img.shields.io/badge/IHubPub-C71D23.svg?style=flat&logo=Gitee)](https://gitee.com/ihub-pub "IHubPub")
[![Gradle Plugin](https://img.shields.io/badge/IHubGradle-02303A.svg?style=flat&logo=Gradle)](https://plugins.gradle.org/u/henry-gradle "IHub Plugins Gradle Plugin")
[![Docs](https://img.shields.io/badge/Knowledge-white.svg?style=flat&logo=BookStack)](https://doc.ihub.pub "Knowledge")
[![JetBrains](https://img.shields.io/badge/JetBrains-white.svg?style=flat&logo=JetBrains&logoColor=black)](https://www.jetbrains.com "JetBrains")
![GitHub](https://img.shields.io/github/license/ihub-pub/plugins)
![Version](https://img.shields.io/badge/Gradle-7.1-brightgreen.svg?logo=Gradle)
![Version](https://img.shields.io/badge/Spring-5.3.8-6DB33F.svg?logo=Spring&logoColor=white)
![Version](https://img.shields.io/badge/SpringBoot-2.5.1-6DB33F.svg?logo=SpringBoot&logoColor=white)
![GitHub last commit](https://img.shields.io/github/last-commit/ihub-pub/plugins)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/ihub-pub/plugins)

本插件用于封装常用Gradle插件，并做了一些个性化缺省配置，便于项目集成，使用详见[samples](samples)，该项目包含插件如下：

| id | displayName | description |
|----|-------------|-------------|
| pub.ihub.plugin.ihub-settings | 设置插件 | 配置插件仓库以及子项目 |
| pub.ihub.plugin | 基础插件 | 配置组件仓库 |
| pub.ihub.plugin.ihub-bom | 组件依赖管理 | 配置组件默认依赖版本以及兼容性管理 |
| pub.ihub.plugin.ihub-java-base | Java基础插件 | 配置Jar基础属性 |
| pub.ihub.plugin.ihub-java | Java插件 | 配置一些默认依赖以及兼容性配置 |
| pub.ihub.plugin.ihub-groovy | Groovy插件 | 配置Groovy组件依赖 |
| pub.ihub.plugin.ihub-publish | 发布插件 | 配置组件发布仓库以及其他个性化组件配置 |
| pub.ihub.plugin.ihub-test | 测试插件 | 测试相关插件 |
| pub.ihub.plugin.ihub-verification | 验证插件 | 配置代码静态检查以及测试用例覆盖等 |
| pub.ihub.plugin.ihub-boot | Boot插件 | 用于镜像个性化配置 |
| pub.ihub.plugin.ihub-native | Native插件 | 用于原生镜像个性化配置 |

## Contributing

Contributors are welcomed to join IHub Plugins project. Please check [CONTRIBUTING](./CONTRIBUTING.md) about how to contribute to this project.
