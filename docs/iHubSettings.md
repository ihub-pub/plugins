> `pub.ihub.plugin.ihub-settings`插件用于配置插件仓库、插件版本以及子项目管理，该插件属于[设置插件](https://docs.gradle.org/current/dsl/org.gradle.api.initialization.Settings.html#org.gradle.api.initialization.Settings)，配置与`settings.gradle`。

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-settings' version '1.1.7'
}
```

## 配置示例

> 配置子项目：

目录结构：

```
+---rest
+---sdk
\---service
```

配置：

```groovy
iHubSettings {
    includeProjects 'rest', 'sdk', 'service'
}
```

> 配置三级子项目：

目录结构：

```
+---rest
+---service
+---test
\---other
    +---a
    +---b
    \---c
```

配置：

```groovy
iHubSettings {
    includeProjects 'rest', 'service' suffix '-suffix'
    includeProjects 'test' noPrefix
    includeProjects 'other' prefix 'prefix-' skippedDirs 'c' subproject
    includeProjects 'subproject' prefix 'prefix-' suffix '-suffix' onlySubproject
}
```

> `注意`：插件默认排除了常见非项目目录，`build`, `src`, `conf`, `libs`, `logs`, `docs`, `classes`, `target`, `out`, `node_modules`, `db`, `gradle`

## 默认插件仓库

> 私有仓库、自定义仓库配置参见[扩展属性](/iHub?id=扩展属性)

| Name | Description | Url |
| ---- | ----------- | --- |
| `ProjectDirs` | 项目本地插件 | `{rootProject.projectDir}/gradle/plugins` |
| `AliYunGradlePlugin` | 阿里云Gradle代理插件仓库 | https://maven.aliyun.com/repository/gradle-plugin |
| `AliYunSpringPlugin` | 阿里云Spring代理插件仓库 | https://maven.aliyun.com/repository/spring-plugin |
| `SpringRelease` | Spring Release仓库 | https://repo.spring.io/release |
| `ReleaseRepo` | 私有Release仓库 | https://repo.xxx.com/release |
| `SnapshotRepo` | 私有Snapshot仓库 | https://repo.xxx.com/snapshot |
| `CustomizeRepo` | 自定义仓库仓库 | https://repo.xxx.com/repo |

## 扩展属性

> `gradle.properties`配置支持如下属性：

| Property  | Description |
| --------- | ----------- |
| `name` | 配置主项目名称 |
| `iHubSettings.includeDirs` | 包含项目路径，多目录“,”分割 |
| `iHubSettings.skippedDirs` | 排除项目路径，多目录“,”分割 |

> 配置如下：

```properties
name=demo
iHubSettings.includeDirs=rest,service
```

## 默认版本

> 配置了以下插件版本：

| Plugin   | Version |
| -------- | ------- |
| `com.gradle.plugin-publish`     | 0.15.0 |
| `com.github.ben-manes.versions` | 0.39.0 |

> 使用插件时可以不用加版本号，配置如下：

```groovy
plugins {
    id 'com.gradle.plugin-publish'
}
```