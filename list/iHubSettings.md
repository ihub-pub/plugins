# ihub-settings

::: info 插件说明
`ihub-settings`插件用于配置插件仓库、插件版本以及子项目管理，配置与`settings.gradle`。
:::

| 插件ID | 插件名称 | 插件类型 | 扩展名称 |
|-------|---------|------|---------|
| `pub.ihub.plugin.ihub-settings` | `设置插件` | `Settings`[^Settings] | `iHubSettings` |

## 扩展属性

### DSL扩展配置支持配置

| 扩展方法 | 扩展描述 |
| --------- | ----------- |
| `includeProjects` | 添加项目（支持多个项目） |
| `prefix` | 项目前缀 |
| `noPrefix` | 无项目前缀 |
| `suffix` | 项目后缀 |
| `subproject` | 包含三级子项目 |
| `onlySubproject` | 仅含三级子项目（不含当前项目，三级项目为`主项目`的子项目） |
| `skippedDirs` | 忽略三级子项目目录 |

### `gradle.properties`配置支持属性

| Property  | Description |
| --------- | ----------- |
| `name` | 配置主项目名称 |
| `iHubSettings.includeDirs` | 包含项目路径，多目录“,”分割 |
| `iHubSettings.skippedDirs` | 排除项目路径，多目录“,”分割 |
| `iHubSettings.includeBom` | 用于配置bom组件，包含所有含[ihub-publish](iHubPublish)组件的子项目 |

> 配置如下：

```properties
name=demo
iHubSettings.includeDirs=rest,service
iHubSettings.includeBom=ihub-bom
```

## 插件安装

@include(../snippet/setting.gradle.md)

## 配置示例

### 配置子项目

- 目录结构：

```
+---rest
+---sdk
\---service
```

- 配置：

::: code-tabs#build

@tab Kotlin

```kotlin
import pub.ihub.plugin.IHubSettingsExtension

configure<IHubSettingsExtension> {
    includeProjects("rest", "sdk", "service")
}
```

@tab Groovy

```groovy
iHubSettings {
    includeProjects 'rest', 'sdk', 'service'
}
```

:::

### 配置三级子项目

- 目录结构：

```
+---rest
+---service
+---test
\---other
    +---a
    +---b
    \---c
```

- 配置：

::: code-tabs#build

@tab Kotlin

```kotlin
import pub.ihub.plugin.IHubSettingsExtension

configure<IHubSettingsExtension> {
    includeProjects("rest", "service").suffix("-suffix")
    includeProjects("test").noPrefix
    includeProjects("other").prefix("prefix-").skippedDirs("c").subproject
    includeProjects("subproject").prefix("prefix-").suffix("-suffix").onlySubproject
}
```

@tab Groovy

```groovy
iHubSettings {
    includeProjects 'rest', 'service' suffix '-suffix'
    includeProjects 'test' noPrefix
    includeProjects 'other' prefix 'prefix-' skippedDirs 'c' subproject
    includeProjects 'subproject' prefix 'prefix-' suffix '-suffix' onlySubproject
}
```

:::

::: warning
插件默认排除了常见非项目目录，`build`, `src`, `conf`, `libs`, `logs`, `docs`, `classes`, `target`, `out`, `node_modules`, `db`, `gradle`
:::

## 默认插件仓库

私有仓库、自定义仓库配置参见[扩展属性](iHub#扩展属性)

| Name | Description | Url |
| ---- | ----------- | --- |
| `ProjectDirs` | 项目本地插件 | `{rootProject.projectDir}/gradle/plugins` |
| `MavenLocal` | 本地仓库 | `{local}/.m2/repository` |
| `AliYunGradle` | 阿里云Gradle代理仓库 | https://maven.aliyun.com/repository/gradle-plugin |
| `ReleaseRepo` | 私有Release仓库 | https://repo.xxx.com/release |
| `SnapshotRepo` | 私有Snapshot仓库 | https://repo.xxx.com/snapshot |
| `CustomizeRepo` | 自定义仓库仓库 | https://repo.xxx.com/repo |

## 默认版本

插件配置了`ihub系列插件`及以下插件默认版本：

| Plugin                      | Version                                                                             |
|-----------------------------|-------------------------------------------------------------------------------------|
| `com.gradle.plugin-publish` | [1.2.0](https://plugins.gradle.org/plugin/com.gradle.plugin-publish)                |
| `pub.ihub.plugin.*`         | [1.3.4](https://plugins.gradle.org/plugin/pub.ihub.plugin)                          |
| `io.freefair.*`             | [8.0.1](https://docs.freefair.io/gradle-plugins/8.0.1/reference/#_settings_plugins) |

使用插件时可以不用加版本号，配置如下：

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin")
    id("com.gradle.plugin-publish")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin'
    id 'com.gradle.plugin-publish'
}
```

:::

## 配置catalog

- 配置默认版本目录组件`ihubLibs`

```groovy
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        ihubLibs {
            from "pub.ihub.lib:ihub-libs:${IHubLibsVersion.version}"
        }
    }
}
```

- `gradle/libs.versions.toml`为标准配置，gradle会自动导入，本插件也会自动配置gradle/目录下的其他`.versions.toml`文件，如：`myLibs.versions.toml`，一般使用标准配置即可

@include(../snippet/footnote.md)
