# ihub-version

::: info 插件说明
`ihub-version`插件是版本插件，集成并加强了第三方version插件，用于设置项目版本。
:::

| 信息 | 描述 |
|--|--|
| 插件ID | `pub.ihub.plugin.ihub-version` |
| 插件名称 | `版本插件` |
| 插件类型 | `Project`[^Project] |
| 扩展名称 | `iHubVersion` |
| 插件依赖 | [io.freefair.git-version](https://plugins.gradle.org/plugin/io.freefair.git-version)、[com.github.ben-manes.versions](https://plugins.gradle.org/plugin/com.github.ben-manes.versions) |

::: tip 插件功能
1. 引入`git-version`插件自动配置项目版本，`增强支持推断版本号`
2. 引入`ben-manes.versions`插件用于检查组件版本号，`增强支持自动替换最新版本`
:::

## 扩展属性

| Extension | Description | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `autoReplaceLaterVersions` | 自动替换最新版本（[versions](https://plugins.gradle.org/plugin/com.github.ben-manes.versions)插件增强） | `false` | ✔ | ✔ | ✔ | ❌ |
| `useInferringVersion` | 使用推断版本号，根据最新`git tag`推断下一个版本号，支持tag格式`{major}.{minor}.{patch}`或`v{major}.{minor}.{patch}`，推断方式`patch + 1` | `false` | ✔ | ✔ | ✔ | ✔ |

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-version")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-version'
}
```

:::

::: note
如果已经安装基础插件`ihub`，则无需单独安装`ihub-version`插件，`ihub`插件已经集成了`ihub-version`插件。
:::

## 配置示例

::: code-tabs#build

@tab Kotlin

```kotlin
iHubVersion {
    autoReplaceLaterVersions.set(true)
}
```

@tab Groovy

```groovy
iHubVersion {
    autoReplaceLaterVersions = true
}
```

:::

@include(../snippet/footnote.md)