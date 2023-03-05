> `ihub`插件是基础插件，用于配置[组件仓库](/iHub?id=组件仓库)以及一些其他[扩展属性](/iHub?id=扩展属性)，配置与`build.gradle`。

| 插件ID | 插件名称 | 插件类型 | 扩展名称 | `主项目`插件依赖                                                                                                                                                                                                                                    |
|-------|---------|--------|---------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `pub.ihub.plugin` | `基础插件` | `Project` | `iHub` | [ihub-git-hooks](iHubGitHooks)、[ihub-bom](iHubBom)、[ihub-version](iHubVersion) |

> 插件功能

| | 功能描述 |
|--|--|
| 1 | 配置组件仓库，仓库明细[见](/iHub?id=组件仓库) |
| 2 | 当主项目不含[java-platform](https://docs.gradle.org/current/userguide/java_platform_plugin.html)插件时，会默认导入`ihub-bom`插件 |
| 3 | 如果项目包含子项目，子项目也会引入本插件 |

## 扩展属性

> 属性使用说明[详见](/explanation?id=属性配置说明)

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `mavenLocalEnabled` | 是否启用本地仓库 | `false` | ❌ | ✔ | ❌ | ❌ |
| `mavenAliYunEnabled` | 是否启用阿里云代理仓库 | `false` | ❌ | ✔ | ✔ | ✔ |
| `releaseRepoUrl` | 正式版本仓库 | ❌ | ❌ | ✔ | ❌ | ❌ |
| `snapshotRepoUrl` | 快照版本仓库 | ❌ | ❌ | ✔ | ❌ | ❌ |
| `repoAllowInsecureProtocol` | 是否允许不安全协议（是否允许http） | `false` | ❌ | ✔ | ❌ | ❌ |
| `repoIncludeGroup` | 仓库包含组（用于限制仓库范围） | ❌ | ❌ | ✔ | ❌ | ❌ |
| `repoIncludeGroupRegex` | 仓库包含组正则（用于限制仓库范围） | `.*` | ❌ | ✔ | ❌ | ❌ |
| `repoUsername` | 仓库用户名 | ❌ | ❌ | ✔ | ✔ | ✔ |
| `repoPassword` | 仓库密码 | ❌ | ❌ | ✔ | ✔ | ✔ |
| `customizeRepoUrl` | 自定义仓库 | ❌ | ❌ | ✔ | ❌ | ❌ |

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin' version '${ihub.plugin.version}'
}
```

## 配置示例

```properties
iHub.mavenLocalEnabled=true
iHub.mavenAliYunEnabled=true
```

## 组件仓库

> 为适应国内网络环境，配置组件仓库如下

| Name | Description | Url | artifactUrls |
| ---- | ----------- | --- | ------------ |
| `ProjectDirs` | 项目本地组件 | `{rootProject.projectDir}/libs` |
| `MavenLocal` | 本地仓库 | `{local}/.m2/repository` |
| `AliYunPublic` | 阿里云聚合公有仓库 | https://maven.aliyun.com/repository/public | https://repo1.maven.org/maven2 |
| `AliYunGoogle` | 阿里云Google代理仓库 | https://maven.aliyun.com/repository/google | https://maven.google.com |
| `AliYunSpring` | 阿里云Spring代理仓库 | https://maven.aliyun.com/repository/spring | https://repo.spring.io/release |
| `SpringRelease` | Spring Release仓库 | https://repo.spring.io/release |
| `ReleaseRepo` | 私有Release仓库 | https://repo.xxx.com/release |
| `SnapshotRepo` | 私有Snapshot仓库 | https://repo.xxx.com/snapshot |
| `CustomizeRepo` | 自定义仓库仓库 | https://repo.xxx.com/repo |
| `MavenRepo` | Maven中央仓库 |  |
