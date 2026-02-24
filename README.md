<p align="center">
    <a target="_blank" href="https://ihub.pub/">
        <img src="https://doc.ihub.pub/ihub.svg" height="150" alt="IHub">
        <img src="https://doc.ihub.pub/ihub_plugins.svg" height="150" alt="IHub">
    </a>
</p>

---

<p align="center">
    <a target="_blank" href="https://bestpractices.coreinfrastructure.org/projects/6921">
        <img alt="CII Best Practices Level" src="https://badge.ihub.pub/cii/level/6921">
    </a>
    <a target="_blank" href="https://github.com/ihub-pub/plugins/actions/workflows/gradle-build.yml">
        <img src="https://badge.ihub.pub/github/actions/workflow/status/ihub-pub/plugins/gradle-build.yml?branch=main&label=Build&logo=GitHub+Actions&logoColor=white" alt="Gradle Build"/>
    </a>
    <a title="Test Cases" href="https://ihub-pub.testspace.com/spaces/219260?utm_campaign=metric&utm_medium=referral&utm_source=badge">
        <img alt="Space Metric" src="https://badge.ihub.pub/testspace/tests/ihub-pub/ihub-pub:plugins/main?compact_message&label=Tests&logo=GitHub+Actions&logoColor=white" />
    </a>
    <a target="_blank" href="https://www.codefactor.io/repository/github/ihub-pub/plugins">
        <img src="https://badge.ihub.pub/codefactor/grade/github/ihub-pub/plugins/main?color=white&label=Codefactor&labelColor=F44A6A&logo=CodeFactor&logoColor=white" alt="CodeFactor"/>
    </a>
    <a target="_blank" href="https://codecov.io/gh/ihub-pub/plugins">
        <img src="https://badge.ihub.pub/codecov/c/github/ihub-pub/plugins?token=ZQ0WR3ZSWG&color=white&label=Codecov&labelColor=F01F7A&logo=Codecov&logoColor=white" alt="Codecov"/>
    </a>
    <a target="_blank" href="https://github.com/ihub-pub/plugins/stargazers">
        <img src="https://badge.ihub.pub/github/stars/ihub-pub/plugins?color=white&style=flat&logo=GitHub&labelColor=181717" alt="GitHub Stars"/>
    </a>
    <a target="_blank" href='https://gitee.com/ihub-pub/plugins/stargazers'>
        <img src='https://badge.ihub.pub/badge/dynamic/json?url=https%3A%2F%2Fgitee.com%2Fapi%2Fv5%2Frepos%2Fihub-pub%2Fplugins&query=%24.stargazers_count&style=flat&logo=gitee&label=stars&labelColor=c71d23&color=white&cacheSeconds=5000' alt='Gitee Stars'/>
    </a>
    <a target="_blank" href="https://plugins.gradle.org/plugin/pub.ihub.plugin">
        <img src="https://badge.ihub.pub/maven-metadata/v?color=white&label=Gradle&labelColor=02303A&logo=Gradle&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fpub%2Fihub%2Fplugin%2Fihub-plugins%2Fmaven-metadata.xml" alt="IHub Plugins Gradle Plugin"/>
    </a>
</p>

> `IHub Plugins`是一套为Gradle项目提供基础设施的插件集，可以极大简化项目配置。包含插件如下：

<table>
<tr><td>项目目录</td><td>插件ID</td><td>插件名称</td><td>插件类型</td><td>插件描述</td></tr>
<tr><td>ihub-settings</td><td><a href="https://doc.ihub.pub/plugins/list/iHubSettings">pub.ihub.plugin.ihub-settings</a></td><td>设置插件</td><td>Settings</td><td>插件仓库、插件版本以及子项目管理</td></tr>
<tr><td rowspan="3">ihub-plugins</td><td><a href="https://doc.ihub.pub/plugins/list/iHub">pub.ihub.plugin</a></td><td>基础插件</td><td>Project</td><td>基础插件，用于配置组件仓库以及一些其他扩展属性</td></tr>
<tr><td><a href="https://doc.ihub.pub/plugins/list/iHubVersion">pub.ihub.plugin.ihub-version</a></td><td>版本插件</td><td>Project</td><td>集成并加强<a href="https://plugins.gradle.org/plugin/list/com.github.ben-manes.versions">ben-manes.versions</a>插件</td></tr>
<tr><td><a href="https://doc.ihub.pub/plugins/list/iHubProfiles">pub.ihub.plugin.ihub-profiles</a></td><td>配置文件插件</td><td>Project</td><td>Gradle版本 Maven POM Profiles</td></tr>
<tr><td>ihub-copyright</td><td><a href="https://doc.ihub.pub/plugins/list/iHubCopyright">pub.ihub.plugin.ihub-copyright</a></td><td>版权插件</td><td>Project</td><td>自动配置IDEA版权信息</td></tr>
<tr><td>ihub-bom</td><td><a href="https://doc.ihub.pub/plugins/list/iHubBom">pub.ihub.plugin.ihub-bom</a></td><td>Bom插件</td><td>Project</td><td>配置项目依赖组件版本以及兼容性管理</td></tr>
<tr><td>ihub-java</td><td><a href="https://doc.ihub.pub/plugins/list/iHubJava">pub.ihub.plugin.ihub-java</a></td><td>Java插件</td><td>Project</td><td>集成Java相关插件环境、配置一些默认依赖以及兼容性配置</td></tr>
<tr><td>ihub-groovy</td><td><a href="https://doc.ihub.pub/plugins/list/iHubGroovy">pub.ihub.plugin.ihub-groovy</a></td><td>Groovy插件</td><td>Project</td><td>集成Groovy相关插件环境以及配置Groovy默认组件依赖</td></tr>
<tr><td>ihub-kotlin</td><td><a href="https://doc.ihub.pub/plugins/list/iHubKotlin">pub.ihub.plugin.ihub-kotlin</a></td><td>Kotlin插件</td><td>Project</td><td>集成Kotlin相关插件环境</td></tr>
<tr><td>ihub-publish</td><td><a href="https://doc.ihub.pub/plugins/list/iHubPublish">pub.ihub.plugin.ihub-publish</a></td><td>发布插件</td><td>Project</td><td>集成组件发布相关插件环境，配置发布仓库以及其他默认配置</td></tr>
<tr><td rowspan="2">ihub-verification</td><td><a href="https://doc.ihub.pub/plugins/list/iHubTest">pub.ihub.plugin.ihub-test</a></td><td>测试插件</td><td>Project</td><td>配置测试任务</td></tr>
<tr><td><a href="https://doc.ihub.pub/plugins/list/iHubVerification">pub.ihub.plugin.ihub-verification</a></td><td>验证插件</td><td>Project</td><td>配置代码静态检查以及测试用例覆盖率等</td></tr>
<tr><td rowspan="2">ihub-spring</td><td><a href="https://doc.ihub.pub/plugins/list/iHubBoot">pub.ihub.plugin.ihub-boot</a></td><td>Boot插件</td><td>Project</td><td>集成spring-boot插件以及镜像默认配置</td></tr>
<tr><td><a href="https://doc.ihub.pub/plugins/list/iHubNative">pub.ihub.plugin.ihub-native</a></td><td>Native插件</td><td>Project</td><td>基于ihub-boot扩展引入org.graalvm.buildtools.native插件</td></tr>
<tr><td>ihub-shadow</td><td><a href="https://doc.ihub.pub/plugins/list/iHubShadow">pub.ihub.plugin.ihub-shadow</a></td><td>Shadow插件</td><td>Project</td><td>集成 <a href="https://imperceptiblethoughts.com/shadow">Shadow</a> 插件</td></tr>
<tr><td>ihub-javaagent</td><td><a href="https://doc.ihub.pub/plugins/list/iHubJavaagent">pub.ihub.plugin.ihub-javaagent</a></td><td>Javaagent插件</td><td>Project</td><td>集成 <a href="https://github.com/ryandens/javaagent-gradle-plugin">Javaagent</a> 插件</td></tr>
<tr><td>ihub-git-hooks</td><td><a href="https://doc.ihub.pub/plugins/list/iHubGitHooks">pub.ihub.plugin.ihub-git-hooks</a></td><td>GitHooks插件</td><td>Project</td><td>配置GitHooks，可以为git操作配置一些钩子命令</td></tr>
<tr><td>ihub-node</td><td><a href="https://doc.ihub.pub/plugins/list/iHubNode">pub.ihub.plugin.ihub-node</a></td><td>Node.js插件</td><td>Project</td><td>集成 <a href="https://plugins.gradle.org/plugin/com.github.node-gradle.node">node-gradle</a> 插件，扩展支持 cnmp</td></tr>
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
iHub.mavenAliYunEnabled=true
```

### 使用
> Gradle构建时会自动配置本地仓库以及阿里云代理仓库，控制台将会打印如下文本：

```text
┌──────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                       Gradle Project Repos                                       │
├──────────────────────────────────────────────────────────────────────────────────────────────────┤
│ MavenLocal(file:/C:/Users/Henry/.m2/repository/)                                                 │
│ AliYunPublic(https://maven.aliyun.com/repository/public)                                         │
│ MavenRepo                                                                                        │
└──────────────────────────────────────────────────────────────────────────────────────────────────┘
```

更多使用方式参见 [快速上手](https://doc.ihub.pub/plugins/)

## 🔖 版本历史

详细版本历史见 [Releases](https://github.com/ihub-pub/plugins/releases)，兼容性版本说明如下：

| Version | Java  | Gradle |
|---------|-------|--------|
| 1.9.3+  | 17~25 | 9.3.1  |
| 1.9.1+  | 17~25 | 9.1.0  |
| 1.9.0   | 17~24 | 9.0.0  |
| 1.7.6+  | 17~23 | 8.13   |
| 1.7.2+  | 17~23 | 8.4    |
| 1.5.6+  | 17~21 | 8.0    |
| 1.5.0+  | 11~21 | 8.0    |
| 1.4.0+  | 8~20  | 8.0    |
| 1.3.1+  | 17    | 8.0    |
| 1.3.0   | 17    | 7.5.1  |
| 1.2.4+  | 11    | 7.4.1  |

## ✅ 项目状态

![Alt](https://repobeats.axiom.co/api/embed/577279f67858fb89c702e0cf0bc604e42decca5d.svg "Repobeats analytics image")

## 👨‍💻 贡献指南
请阅读 [贡献指南](https://github.com/ihub-pub/.github/blob/main/CONTRIBUTING.md) 为该项目做出贡献

[![Contributors](https://contrib.rocks/image?repo=ihub-pub/plugins)](https://github.com/ihub-pub/plugins/graphs/contributors "Contributors")
