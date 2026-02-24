# ihub

::: info 插件说明
`ihub`插件是基础插件，用于配置[组件仓库](#组件仓库)以及一些其他[扩展属性](#扩展属性)，配置与`build.gradle`。
:::

| 信息 | 描述 |
| --- | --- |
| 插件ID | `pub.ihub.plugin` |
| 插件名称 | `基础插件` |
| 插件类型 | `Project`[^Project] |
| 扩展名称 | `iHub` |
| 插件依赖 | [ihub-git-hooks](iHubGitHooks)、[ihub-bom](iHubBom)、[ihub-version](iHubVersion) |

::: tip 插件功能
1. 配置组件仓库，仓库明细[见](#组件仓库)
2. 当主项目不含[java-platform](https://docs.gradle.org/current/userguide/java_platform_plugin.html)和[version-catalog](https://docs.gradle.org/current/userguide/platforms.html)插件时，会默认导入`ihub-bom`插件
3. 如果项目包含子项目，子项目也会引入本插件
:::

## 扩展属性

| Extension | Description | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `mavenLocalEnabled` | 是否启用本地仓库 | `false` | ✔ | ✔ | ❌ | ❌ |
| `mavenAliYunEnabled` | 是否启用阿里云代理仓库 | `false` | ✔ | ✔ | ✔ | ✔ |
| `mavenSpringMilestoneEnabled` | 是否启用SpringMilestone仓库 | `false` | ✔ | ✔ | ✔ | ✔ |
| `mavenPrivateEnabled` | 是否启用私有仓库（组件发布仓库） | `true` | ✔ | ✔ | ✔ | ✔ |
| `releaseRepoUrl` | 正式版本仓库 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `snapshotRepoUrl` | 快照版本仓库 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `repoAllowInsecureProtocol` | 是否允许不安全协议（是否允许http） | `false` | ✔ | ✔ | ❌ | ❌ |
| `repoIncludeGroup` | 仓库包含组（用于限制仓库范围） | ❌ | ✔ | ✔ | ❌ | ❌ |
| `repoIncludeGroupRegex` | 仓库包含组正则（用于限制仓库范围） | `.*` | ✔ | ✔ | ❌ | ❌ |
| `repoUsername` | 仓库用户名 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `repoPassword` | 仓库密码 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `customizeRepoUrl` | 自定义仓库 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `profile` | 配置文件，多个配置用逗号分隔，优先级从右到左 | ❌ | ❌ | ✔ | ✔ | ❌ |

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin'
}
```

:::

## 配置示例

```properties
iHub.mavenLocalEnabled=true
iHub.mavenAliYunEnabled=true
```

## 组件仓库

为适应国内网络环境，配置组件仓库如下

| Name | Description | Url |
| ---- | ----------- |-----|
| `ProjectDirs` | 项目本地组件 | `{rootProject.projectDir}/libs` |
| `MavenLocal` | 本地仓库 | `{local}/.m2/repository` |
| `AliYunPublic` | 阿里云聚合公有仓库 | https://maven.aliyun.com/repository/public <br> artifactUrls: https://repo1.maven.org/maven2 |
| `ReleaseRepo` | 私有Release仓库 | https://repo.xxx.com/release |
| `SnapshotRepo` | 私有Snapshot仓库 | https://repo.xxx.com/snapshot |
| `CustomizeRepo` | 自定义仓库仓库 | https://repo.xxx.com/repo |
| `MavenRepo` | Maven中央仓库 |     |

@include(../snippet/footnote.md)
