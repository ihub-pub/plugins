<p align="center">
    <a target="_blank" href="https://ihub.pub/">
        <img src="https://cdn.jsdelivr.net/gh/ihub-pub/ihub-pub.github.io/ihub.svg" height="150" alt="IHub">
        <img src="https://cdn.jsdelivr.net/gh/ihub-pub/ihub-pub.github.io/ihub_plugins.svg" height="150" alt="IHub">
    </a>
</p>

---

<p align="center">
    <a target="_blank" href="https://bestpractices.coreinfrastructure.org/projects/6921">
        <img alt="CII Best Practices Level" src="https://img.shields.io/cii/level/6921">
    </a>
    <a target="_blank" href="https://github.com/ihub-pub/plugins/actions/workflows/gradle-build.yml">
        <img src="https://img.shields.io/github/actions/workflow/status/ihub-pub/plugins/gradle-build.yml?branch=main&label=Build&logo=GitHub+Actions&logoColor=white" alt="Gradle Build"/>
    </a>
    <a target="_blank" href="https://www.codefactor.io/repository/github/ihub-pub/plugins">
        <img src="https://img.shields.io/codefactor/grade/github/ihub-pub/plugins/main?color=white&label=Codefactor&labelColor=F44A6A&logo=CodeFactor&logoColor=white" alt="CodeFactor"/>
    </a>
    <a target="_blank" href="https://codecov.io/gh/ihub-pub/plugins">
        <img src="https://img.shields.io/codecov/c/github/ihub-pub/plugins?token=ZQ0WR3ZSWG&color=white&label=Codecov&labelColor=F01F7A&logo=Codecov&logoColor=white" alt="Codecov"/>
    </a>
    <a target="_blank" href="https://github.com/ihub-pub/plugins/stargazers">
        <img src="https://img.shields.io/github/stars/ihub-pub/plugins?color=white&logo=GitHub&labelColor=181717" alt="GitHub Stars"/>
    </a>
    <a target="_blank" href='https://gitee.com/ihub-pub/plugins/stargazers'>
        <img src='https://gitee.com/ihub-pub/plugins/badge/star.svg?theme=dark' alt='Gitee Stars'/>
    </a>
    <a target="_blank" href="https://www.jetbrains.com">
        <img src="https://img.shields.io/badge/JetBrains-white.svg?style=flat&logo=JetBrains&logoColor=black" alt="Thanks to JetBrains for sponsoring"/>
    </a>
    <a target="_blank" href="https://plugins.gradle.org/plugin/pub.ihub.plugin">
        <img src="https://img.shields.io/maven-metadata/v?color=white&label=Gradle&labelColor=02303A&logo=Gradle&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fpub%2Fihub%2Fplugin%2Fihub-plugins%2Fmaven-metadata.xml" alt="IHub Plugins Gradle Plugin"/>
    </a>
</p>

> `IHub Plugins`是一套为Gradle项目提供基础设施的插件集，可以极大简化项目配置。包含插件如下：

<table>
<tr><td>项目目录</td><td>插件ID</td><td>插件名称</td><td>插件类型</td><td>插件描述</td></tr>
<tr><td>ihub-settings</td><td><a href="https://doc.ihub.pub/plugins/#/iHubSettings">pub.ihub.plugin.ihub-settings</a></td><td>设置插件</td><td>Settings</td><td>插件仓库、插件版本以及子项目管理</td></tr>
<tr><td rowspan="3">ihub-plugins</td><td><a href="https://doc.ihub.pub/plugins/#/iHub">pub.ihub.plugin</a></td><td>基础插件</td><td>Project</td><td>基础插件，用于配置组件仓库以及一些其他扩展属性</td></tr>
<tr><td><a href="https://doc.ihub.pub/plugins/#/iHubVersion">pub.ihub.plugin.ihub-version</a></td><td>版本插件</td><td>Project</td><td>集成并加强<a href="https://plugins.gradle.org/plugin/com.github.ben-manes.versions">ben-manes.versions</a>插件</td></tr>
<tr><td><a href="https://doc.ihub.pub/plugins/#/iHubCopyright">pub.ihub.plugin.ihub-copyright</a></td><td>版权插件</td><td>Project</td><td>自动配置IDEA版权信息</td></tr>
<tr><td>ihub-bom</td><td><a href="https://doc.ihub.pub/plugins/#/iHubBom">pub.ihub.plugin.ihub-bom</a></td><td>Bom插件</td><td>Project</td><td>配置项目依赖组件版本以及兼容性管理</td></tr>
<tr><td>ihub-java</td><td><a href="https://doc.ihub.pub/plugins/#/iHubJava">pub.ihub.plugin.ihub-java</a></td><td>Java插件</td><td>Project</td><td>集成Java相关插件环境、配置一些默认依赖以及兼容性配置</td></tr>
<tr><td>ihub-groovy</td><td><a href="https://doc.ihub.pub/plugins/#/iHubGroovy">pub.ihub.plugin.ihub-groovy</a></td><td>Groovy插件</td><td>Project</td><td>集成Groovy相关插件环境以及配置Groovy默认组件依赖</td></tr>
<tr><td>ihub-publish</td><td><a href="https://doc.ihub.pub/plugins/#/iHubPublish">pub.ihub.plugin.ihub-publish</a></td><td>发布插件</td><td>Project</td><td>集成组件发布相关插件环境，配置发布仓库以及其他默认配置</td></tr>
<tr><td rowspan="2">ihub-verification</td><td><a href="https://doc.ihub.pub/plugins/#/iHubTest">pub.ihub.plugin.ihub-test</a></td><td>测试插件</td><td>Project</td><td>配置测试任务</td></tr>
<tr><td><a href="https://doc.ihub.pub/plugins/#/iHubVerification">pub.ihub.plugin.ihub-verification</a></td><td>验证插件</td><td>Project</td><td>配置代码静态检查以及测试用例覆盖率等</td></tr>
<tr><td rowspan="2">ihub-spring</td><td><a href="https://doc.ihub.pub/plugins/#/iHubBoot">pub.ihub.plugin.ihub-boot</a></td><td>Boot插件</td><td>Project</td><td>集成spring-boot插件以及镜像默认配置</td></tr>
<tr><td><a href="https://doc.ihub.pub/plugins/#/iHubNative">pub.ihub.plugin.ihub-native</a></td><td>Native插件</td><td>Project</td><td>基于ihub-boot扩展引入org.graalvm.buildtools.native插件</td></tr>
<tr><td>ihub-githooks</td><td><a href="https://doc.ihub.pub/plugins/#/iHubGitHooks">pub.ihub.plugin.ihub-git-hooks</a></td><td>GitHooks插件</td><td>Project</td><td>配置GitHooks，可以为git操作配置一些钩子命令</td></tr>
</table>

## 🧭 使用指南

### 安装
> 在你的项目的`build.gradle`文件中添加以下内容：

```groovy
plugins {
    id 'pub.ihub.plugin' version '${ihub.plugin.version}'
}
```

### 配置
> 在你的项目的`gradle.properties`文件中可以添加如下配置：

```properties
# 是否启用本地仓库
iHub.mavenLocalEnabled=true
# 是否启用阿里云代理仓库
iHub.mavenCentralEnabled=true
```

### 使用
> Gradle构建时会自动配置本地仓库以及阿里云代理仓库，控制台将会打印如下文本：

```text
┌──────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                       Gradle Project Repos                                       │
├──────────────────────────────────────────────────────────────────────────────────────────────────┤
│ MavenLocal(file:/C:/Users/Henry/.m2/repository/)                                                 │
│ AliYunPublic(https://maven.aliyun.com/repository/public)                                         │
│ AliYunGoogle(https://maven.aliyun.com/repository/google)                                         │
│ AliYunSpring(https://maven.aliyun.com/repository/spring)                                         │
│ SpringRelease(https://repo.spring.io/release)                                                    │
│ MavenRepo                                                                                        │
└──────────────────────────────────────────────────────────────────────────────────────────────────┘
```

更多使用方式参见 [快速上手](https://doc.ihub.pub/plugins/#/gettingStarted)

## 🔖 版本历史

详细版本历史见 [CHANGELOG](https://doc.ihub.pub/plugins/#/CHANGELOG)，兼容性版本说明如下：

| Version | Java | Gradle |
|---------|------|--------|
| 1.3.1+  | 17   | 8.0    |
| 1.3.0   | 17   | 7.5.1  |
| 1.2.4+  | 11   | 7.4.1  |
| 1.2.3   | 11   | 7.4    |
| 1.1.1   | 11   | 7.2    |
| 1.1.0   | 11   | 7.0    |

## ✅ 项目状态

![Alt](https://repobeats.axiom.co/api/embed/577279f67858fb89c702e0cf0bc604e42decca5d.svg "Repobeats analytics image")

## 👨‍💻 贡献指南
请阅读 [贡献指南](https://github.com/ihub-pub/.github/blob/main/CONTRIBUTING.md) 为该项目做出贡献

[![Contributors](https://contrib.rocks/image?repo=ihub-pub/plugins)](https://github.com/ihub-pub/plugins/graphs/contributors "Contributors")
