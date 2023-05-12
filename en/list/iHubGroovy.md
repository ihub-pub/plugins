# ihub-groovy

::: info 插件说明
`ihub-groovy`插件用于集成Groovy相关插件环境以及配置Groovy默认[组件依赖](#组件依赖)。
:::

| 插件ID                          | 插件名称       | 插件类型                | 插件依赖                                                                                         |
| ----------------------------- | ---------- | ------------------- | -------------------------------------------------------------------------------------------- |
| `pub.ihub.plugin.ihub-groovy` | `Groovy插件` | `Project`[^Project] | [ihub-java](iHubJava)、[groovy](https://docs.gradle.org/current/userguide/groovy_plugin.html) |

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-groovy")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-groovy'
}
```

:::

## 组件依赖

项目默认会依赖如下组件：

| 默认依赖组件             |
| ------------------ |
| `groovy`           |
| `groovy-datetime`  |
| `groovy-dateutil`  |
| `groovy-groovydoc` |
| `groovy-json`      |
| `groovy-nio`       |
| `groovy-sql`       |
| `groovy-templates` |
| `groovy-xml`       |

@include(../snippet/explanation.md)