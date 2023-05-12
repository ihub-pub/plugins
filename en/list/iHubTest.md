# ihub-test

::: info 插件说明
`ihub-test`插件用于配置测试任务。
:::

| 信息   | 描述                                                                                                                                  |
| ---- | ----------------------------------------------------------------------------------------------------------------------------------- |
| 插件ID | `pub.ihub.plugin.ihub-test`                                                                                                         |
| 插件名称 | `测试插件`                                                                                                                              |
| 插件类型 | `Project`[^Project]                                                                                                                 |
| 扩展名称 | `iHubTest`                                                                                                                          |
| 插件依赖 | [ihub-bom](iHubBom)、[test-report-aggregation](https://docs.gradle.org/current/userguide/test_report_aggregation_plugin.html)（`主项目`） |

## 扩展属性

| Extension                | Description                                                      | Default                     | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| ------------------------ | ---------------------------------------------------------------- | --------------------------- | --------- | --------- | --------- | --------- |
| `enabled`                | 启用测试                                                             | `true`                      | ✔         | ✔         | ✔         | ❌         |
| `classes`                | 包含测试类（`,`分割，支持通配符`*`）                                            | `**/*Test*,**/*FT*,**/*UT*` | ✔         | ✔         | ✔         | ❌         |
| `forkEvery`              | 每跑x个测试类后重启fork进程                                                 | `100`                       | ✔         | ✔         | ✔         | ❌         |
| `maxParallelForks`       | 最多启动进程数                                                          | `1`                         | ✔         | ✔         | ✔         | ❌         |
| `runProperties`          | 任务运行时属性[详见](explanation#runproperties)                           | `{System.properties}`       | ✔         | ❌         | ❌         | ❌         |
| `runIncludePropNames`    | 运行时包含系统属性名称（`,`分割，支持通配符`*`）[详见](explanation#runincludepropnames) | ❌                           | ✔         | ✔         | ✔         | ❌         |
| `runSkippedPropNames`    | 运行时排除系统属性名称（`,`分割，支持通配符`*`）[详见](explanation#runskippedpropnames) | ❌                           | ✔         | ✔         | ✔         | ❌         |
| `enabledLocalProperties` | 启用本地属性[详见](explanation#enabledlocalproperties)                   | `true`                      | ✔         | ✔         | ❌         | ❌         |
| `debug`                  | 启用测试调试                                                           | `false`                     | ✔         | ✔         | ✔         | ❌         |
| `failFast`               | 只要有一个测试失败就停止测试                                                   | `false`                     | ✔         | ✔         | ✔         | ❌         |
| `testFramework`          | 测试框架                                                             | [详见](#测试框架)                 | ✔         | ✔         | ❌         | ❌         |

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-test")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-test'
}
```

:::

## 配置示例

::: code-tabs#build

@tab Kotlin

```kotlin
iHubTest {
    enabled.set(true)
    failFast.set(true)
}
```

@tab Groovy

```groovy
iHubTest {
    enabled = true
    failFast = true
}
```

:::

## 测试框架

::: tip
- 测试框架依赖配置目前支持：`SPOCK`、`JUNIT_JUPITER`、`NONE`
- `Groovy`环境默认`SPOCK`、`Java`环境默认`JUNIT_JUPITER`
:::

### SPOCK

```groovy
dependencies {
    testImplementation 'org.spockframework:spock-spring'
    testRuntimeOnly 'com.athaydes:spock-reports'
}
```

### JUNIT_JUPITER

```groovy
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter'
}
```

@include(../snippet/explanation.md)