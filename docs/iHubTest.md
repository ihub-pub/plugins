> `ihub-test`插件用于配置测试任务。

| 插件ID | 插件名称 | 插件类型 | 扩展名称 | 插件依赖 |
|-------|---------|--------|---------|--------|
| `pub.ihub.plugin.ihub-test` | `测试插件` | `Project` | `iHubTest` | [ihub-bom](iHubBom) |

## 扩展属性

> 属性使用说明[详见](/explanation?id=属性配置说明)

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `enabled` | 启用测试 | `true` | ✔ | ✔ | ✔ | ❌ |
| `classes` | 包含测试类（“,”分割） | `**/*Test*,**/*FT*,**/*UT*` | ✔ | ✔ | ✔ | ❌ |
| `forkEvery` | 每跑x个测试类后重启fork进程 | `100` | ✔ | ✔ | ✔ | ❌ |
| `maxParallelForks` | 最多启动进程数 | `1` | ✔ | ✔ | ✔ | ❌ |
| `runProperties` | 任务运行时属性[详见](/explanation?id=runproperties) | `{System.properties}` | ✔ | ❌ | ❌ | ❌ |
| `runIncludePropNames` | 运行时包含系统属性名称（“,”分割）[详见](/explanation?id=runincludepropnames) | ❌ | ✔ | ✔ | ✔ | ❌ |
| `runSkippedPropNames` | 运行时排除系统属性名称（“,”分割）[详见](/explanation?id=runskippedpropnames) | ❌ | ✔ | ✔ | ✔ | ❌ |
| `enabledLocalProperties` | 启用本地属性[详见](/explanation?id=enabledlocalproperties) | `false` | ✔ | ✔ | ❌ | ❌ |
| `debug` | 启用测试调试 | `false` | ✔ | ✔ | ✔ | ❌ |
| `failFast` | 只要有一个测试失败就停止测试 | `false` | ✔ | ✔ | ✔ | ❌ |
| `testFramework` | 测试框架 | [详见](iHubTest?id=测试框架) | ❌ | ✔ | ❌ | ❌ |

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-test' version '1.1.8'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.8'
}

apply {
    plugin 'pub.ihub.plugin.ihub-test'
}
```

## 配置示例

```groovy
iHubTest {
    enabled = true
    failFast = true
}
```

## 测试框架

> 测试框架依赖配置目前支持：`SPOCK`、`JUNIT_JUPITER`、`NONE`，`Groovy`环境默认`SPOCK`、`Java`环境默认`JUNIT_JUPITER`

##### SPOCK

```groovy
dependencies {
    testImplementation 'org.spockframework:spock-spring'
    testRuntimeOnly 'com.athaydes:spock-reports'
}
```

##### JUNIT_JUPITER

```groovy
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter'
}
```