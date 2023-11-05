# ihub-profiles

::: info 插件说明
`ihub-profiles`Gradle版本 Maven POM Profiles
:::

| 信息 | 描述 |
|--|--|
| 插件ID | `pub.ihub.plugin.ihub-profiles` |
| 插件名称 | `配置文件插件` |
| 插件类型 | `Project`[^Project] |
| 扩展名称 | `iHubProfiles` |
| 插件依赖 | [ihub](iHub) |

::: tip 插件功能
基于`iHub.profile`属性，支持不同`profile`下的扩展配置
:::

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-profiles")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-profiles'
}
```

:::

::: note
如果已经安装基础插件`ihub`，则无需单独安装`ihub-profiles`插件，`ihub`插件已经集成了`ihub-profiles`插件。
:::

## 配置示例

::: code-tabs#build

@tab Kotlin

```kotlin
iHubProfiles {
    profile("dev") {
        println("dev")
    }
    profile("test") {
        println("test")
    }
    profile("prod") {
        println("prod")
    }
}
```

@tab Groovy

```groovy
iHubProfiles {
    profile('dev') {
        println 'dev'
    }
    profile('test') {
        println 'test'
    }
    profile('prod') {
        println 'prod'
    }
}
```

:::

@include(../snippet/footnote.md)