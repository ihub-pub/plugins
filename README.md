# IHub Plugins
[![Gradle Build](https://github.com/ihub-pub/plugins/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/ihub-pub/plugins/actions/workflows/gradle-build.yml)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f866ca35cbb44347a210722a2da8aabc)](https://app.codacy.com/gh/ihub-pub/plugins?utm_source=github.com&utm_medium=referral&utm_content=ihub-pub/plugins&utm_campaign=Badge_Grade_Settings)
[![codecov](https://codecov.io/gh/ihub-pub/plugins/branch/main/graph/badge.svg?token=ZQ0WR3ZSWG)](https://codecov.io/gh/ihub-pub/plugins)
[![GitHub](https://img.shields.io/badge/GitHub-181717.svg?style=flat&logo=GitHub)](https://github.com/ihub-pub "IHubPub")
[![Gitee](https://img.shields.io/badge/Gitee-C71D23.svg?style=flat&logo=Gitee)](https://gitee.com/ihub-pub "IHubPub")
[![Docs](https://img.shields.io/badge/Docs-8CA1AF.svg?style=flat&logo=Read+the+Docs&logoColor=white)](https://doc.ihub.pub/plugins "Docs")
[![Join the chat at https://gitter.im/ihub-pub/plugins](https://img.shields.io/badge/Gitter-45af90.svg?style=flat&logo=Gitter&logoColor=white&)](https://gitter.im/ihub-pub/plugins?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge "Chat On Gitter")
[![JetBrains](https://img.shields.io/badge/JetBrains-white.svg?style=flat&logo=JetBrains&logoColor=black)](https://www.jetbrains.com "Thanks to JetBrains for sponsoring")
[![GitHub release](https://img.shields.io/github/v/release/ihub-pub/plugins?color=02303A&label&labelColor=02303A&logo=Gradle)](https://plugins.gradle.org/plugin/pub.ihub.plugin "IHub Plugins Gradle Plugin")

本插件用于封装常用Gradle插件，并做了一些个性化缺省配置，便于项目集成，使用详见[samples](https://github.com/henry-hub/plugins/tree/main/samples)，该项目包含插件如下：

| 插件ID | 插件名称 | 插件描述 |
|----|-------------|-------------|
| `pub.ihub.plugin.ihub-settings` | [设置插件](https://doc.ihub.pub/plugins/#/iHubSettings) | 配置插件仓库以及子项目 |
| `pub.ihub.plugin` | [基础插件](https://doc.ihub.pub/plugins/#/iHub) | 配置组件仓库 |
| `pub.ihub.plugin.ihub-bom` | [组件依赖管理](https://doc.ihub.pub/plugins/#/iHubBom) | 配置组件默认依赖版本以及兼容性管理 |
| `pub.ihub.plugin.ihub-java` | [Java插件](https://doc.ihub.pub/plugins/#/iHubJava) | 配置一些默认依赖以及兼容性配置 |
| `pub.ihub.plugin.ihub-groovy` | [Groovy插件](https://doc.ihub.pub/plugins/#/iHubGroovy) | 配置Groovy组件依赖 |
| `pub.ihub.plugin.ihub-publish` | [发布插件](https://doc.ihub.pub/plugins/#/iHubPublish) | 配置组件发布仓库以及其他个性化组件配置 |
| `pub.ihub.plugin.ihub-test` | [测试插件](https://doc.ihub.pub/plugins/#/iHubTest) | 测试相关插件 |
| `pub.ihub.plugin.ihub-verification` | [验证插件](https://doc.ihub.pub/plugins/#/iHubVerification) | 配置代码静态检查以及测试用例覆盖等 |
| `pub.ihub.plugin.ihub-boot` | [Boot插件](https://doc.ihub.pub/plugins/#/iHubBoot) | 用于镜像个性化配置 |
| `pub.ihub.plugin.ihub-native` | [Native插件](https://doc.ihub.pub/plugins/#/iHubNative) | 用于原生镜像个性化配置 |

## 属性配置表
插件配置属性获取目前支持4种方式：`扩展属性`（`Ext`）、`项目属性`（`Prj`）、`系统属性`（`Sys`）、`环境属性`（`Env`）， 
属性优先级：`Sys` > `Env` > `Prj` > `Ext`
> - `Ext`（Extension）：插件自定义扩展属性，配置于`build.gradle`文件，配置方式详见[samples](https://github.com/henry-hub/plugins/tree/main/samples)
> - `Prj`（Project）：项目属性，配置于`gradle.properties`文件，配置格式`扩展名`.`属性名`，如`iHub.mavenLocalEnabled=true`
> - `Sys`（System）：系统属性，如命令行传递的信息等，配置格式`扩展名`.`属性名`，如`-DiHub.mavenLocalEnabled=true`
> - `Env`（Environment）：环境变量属性，配置格式全部大写，多个单词，用`_`分隔，如`MAVEN_LOCAL_ENABLED=true`

## Contributing

Contributors are welcomed to join IHub Plugins project. Please check [CONTRIBUTING](https://github.com/ihub-pub/plugins/blob/main/CONTRIBUTING.md) about how to contribute to this project.
