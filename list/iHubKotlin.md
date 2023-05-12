# ihub-kotlin

::: info 插件说明
`ihub-kotlin`插件用于集成Kotlin相关插件环境
:::

| 插件ID | 插件名称 | 插件类型 | 插件依赖 |
|-------|---------|--------|---------|
| `pub.ihub.plugin.ihub-kotlin` | `Kotlin插件` | `Project`[^Project] | [ihub-java](iHubJava)、[org.jetbrains.kotlin.jvm](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm) |

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-kotlin")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-kotlin'
}
```

:::

@include(../snippet/explanation.md)