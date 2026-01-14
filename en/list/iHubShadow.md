#

:::info Plugin Info:::| 信息   | 描述                                                                                                                                                |
| ---- | ------------------------------------------------------------------------------------------------------------------------------------------------- |
| 插件ID | `pub.ihub.plugin.ihub-shadow`                                                                                                                     |
| 插件名称 | `Shadow插件`                                                                                                                                        |
| 插件类型 | `Project`[^Project]                                                                           |
| 扩展名称 | `iHubShadow`                                                                                                                                      |
| 插件依赖 | [ihub-java](iHubJava)、[com.github.johnrengelman.shadow](https://imperceptiblethoughts.com/shadow) |## 扩展属性| Extension                | Description                                                      | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| ------------------------ | ---------------------------------------------------------------- | ------- | ------------------------------------------------------------- | ------------------------------------------------------------- | ------------------------------------------------------------- | ------------------------------------------------------------- |
| `runProperties`          | 运行属性[详见](explanation#runproperties)                              | ❌       | ✔                                                             | ❌                                                             | ❌                                                             | ❌                                                             |
| `runIncludePropNames`    | 运行时包含系统属性名称（`,`分割，支持通配符`*`）[详见](explanation#runincludepropnames) | ❌       | ✔                                                             | ✔                                                             | ✔                                                             | ❌                                                             |
| `runSkippedPropNames`    | 运行时排除系统属性名称（`,`分割，支持通配符`*`）[详见](explanation#runskippedpropnames) | ❌       | ✔                                                             | ✔                                                             | ✔                                                             | ❌                                                             |
| `enabledLocalProperties` | 启用本地属性[详见](explanation#enabledlocalproperties)                   | `true`  | ✔                                                             | ✔                                                             | ❌                                                             | ❌                                                             |## 插件安装::: code-tabs#build@tab Kotlin```kotlin
plugins {
    id("pub.ihub.plugin.ihub-shadow")
}
```@tab Groovy```groovy
plugins {
    id 'pub.ihub.plugin.ihub-shadow'
}
```:::

| 信息   | 描述                                                                                                                                                |
| ---- | ------------------------------------------------------------------------------------------------------------------------------------------------- |
| 插件ID | `pub.ihub.plugin.ihub-shadow`                                                                                                                     |
| 插件名称 | `Shadow插件`                                                                                                                                        |
| 插件类型 | `Project`[^Project]                                                                           |
| 扩展名称 | `iHubShadow`                                                                                                                                      |
| 插件依赖 | [ihub-java](iHubJava)、[com.github.johnrengelman.shadow](https://imperceptiblethoughts.com/shadow) |

## 扩展属性

| Extension                | Description                                                      | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| ------------------------ | ---------------------------------------------------------------- | ------- | ------------------------------------------------------------- | ------------------------------------------------------------- | ------------------------------------------------------------- | ------------------------------------------------------------- |
| `runProperties`          | 运行属性[详见](explanation#runproperties)                              | ❌       | ✔                                                             | ❌                                                             | ❌                                                             | ❌                                                             |
| `runIncludePropNames`    | 运行时包含系统属性名称（`,`分割，支持通配符`*`）[详见](explanation#runincludepropnames) | ❌       | ✔                                                             | ✔                                                             | ✔                                                             | ❌                                                             |
| `runSkippedPropNames`    | 运行时排除系统属性名称（`,`分割，支持通配符`*`）[详见](explanation#runskippedpropnames) | ❌       | ✔                                                             | ✔                                                             | ✔                                                             | ❌                                                             |
| `enabledLocalProperties` | 启用本地属性[详见](explanation#enabledlocalproperties)                   | `true`  | ✔                                                             | ✔                                                             | ❌                                                             | ❌                                                             |

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-shadow")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-shadow'
}
```

:::

@include(../snippet/footnote.md)