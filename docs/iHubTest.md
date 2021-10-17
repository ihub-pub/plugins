> 扩展名`iHubTest`，测试相关插件。

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-test' version '1.1.7'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.7'
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

## 扩展属性

> [属性说明](/explanation?id=属性配置说明)：

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `enabled` | 启用测试 | `true` | ✔ | ✔ | ✔ | ❌ |
| `classes` | 包含测试类（“,”分割） | ❌ | ✔ | ✔ | ✔ | ❌ |
| `forkEvery` | 每跑x个测试类后重启fork进程 | `100` | ✔ | ✔ | ✔ | ❌ |
| `maxParallelForks` | 最多启动进程数 | `1` | ✔ | ✔ | ✔ | ❌ |
| [`runProperties`](/explanation?id=runproperties) | 任务运行时属性 | `System.properties` | ✔ | ❌ | ❌ | ❌ |
| [`runIncludePropNames`](/explanation?id=runincludepropnames) | 运行时包含系统属性名称（“,”分割） | ❌ | ✔ | ✔ | ✔ | ❌ |
| [`runSkippedPropNames`](/explanation?id=runskippedpropnames) | 运行时排除系统属性名称（“,”分割） | ❌ | ✔ | ✔ | ✔ | ❌ |
| [`enabledLocalProperties`](/explanation?id=enabledlocalproperties) | 启用本地属性 | `false` | ✔ | ✔ | ❌ | ❌ |
| `debug` | 启用测试调试 | `false` | ✔ | ✔ | ✔ | ❌ |
| `failFast` | 只要有一个测试失败就停止测试 | `false` | ✔ | ✔ | ✔ | ❌ |