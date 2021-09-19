> 扩展名`iHub`，用于配置[组件仓库](/iHub?id=组件仓库)以及一些其他[扩展属性](/iHub?id=扩展属性)。

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.4'
}
```

## 配置示例

> 配置java插件

```groovy
iHub {
    javaJaxbRuntime = true
    javaCompatibility = '11'
}
```

> 配置groovy插件

```groovy
iHub {
    compileGroovyAllModules = true
}
```

## 组件仓库

> 组件仓库配置顺序

| Name | Description | Url |
| ---- | ----------- | --- |
| `ProjectDirs` | 项目手动添加组件 | `{rootProject.projectDir}/libs` |
| `MavenLocal` | 本地仓库 | `{local}/.m2/repository` |
| `AliYunPublic` | 阿里云聚合公有仓库 | https://maven.aliyun.com/repository/public |
| `AliYunGoogle` | 阿里云Google代理仓库 | https://maven.aliyun.com/repository/google |
| `AliYunSpring` | 阿里云Spring代理仓库 | https://maven.aliyun.com/repository/spring |
| `SpringRelease` | Spring Release仓库 | https://repo.spring.io/release |
| `ReleaseRepo` | 私有Release仓库 | https://repo.xxx.com/release |
| `SnapshotRepo` | 私有Snapshot仓库 | https://repo.xxx.com/snapshot |
| `CustomizeRepo` | 自定义仓库仓库 | https://repo.xxx.com/repo |
| `MavenRepo` | Maven中央仓库 |  |

## 扩展属性

> [属性说明](/explanation?id=属性配置说明)：

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `mavenLocalEnabled` | 是否启用本地仓库 | `false` | ❌ | ✔ | ❌ | ❌ |
| `releaseRepoUrl` | 正式版本仓库 | ❌ | ❌ | ✔ | ❌ | ❌ |
| `snapshotRepoUrl` | 快照版本仓库 | ❌ | ❌ | ✔ | ❌ | ❌ |
| `repoAllowInsecureProtocol` | 是否允许不安全协议（是否允许http） | `false` | ✔ | ✔ | ❌ | ❌ |
| `repoIncludeGroup` | 仓库包含组（用于限制仓库范围） | ❌ | ✔ | ✔ | ❌ | ❌ |
| `repoIncludeGroupRegex` | 仓库包含组正则（用于限制仓库范围） | `.*` | ✔ | ✔ | ❌ | ❌ |
| `repoUsername` | 仓库用户名 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `repoPassword` | 仓库密码 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `customizeRepoUrl` | 自定义仓库 | ❌ | ❌ | ✔ | ❌ | ❌ |
| `javaJaxbRuntime` | Jaxb运行时配置 | `true` | ✔ | ✔ | ✔ | ❌ |
| `javaCompatibility` | Java兼容性配置 | ❌ | ✔ | ✔ | ✔ | ❌ |
| `gradleCompilationIncremental` | gradle增量编译 | `true` | ✔ | ✔ | ✔ | ❌ |
| `compileGroovyAllModules` | 是否添加groovy所有模块 | `false` | ✔ | ✔ | ❌ | ❌ |