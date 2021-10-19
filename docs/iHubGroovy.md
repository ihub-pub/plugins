> `ihub-groovy`插件用于集成Groovy相关插件环境以及配置Groovy默认[组件依赖](/iHubGroovy?id=组件依赖)。

| 插件ID | 插件名称 | 插件类型 | 插件依赖 |
|-------|---------|--------|---------|
| `pub.ihub.plugin.ihub-groovy` | `Groovy插件` | `Project` | [ihub-java](iHubJava)、[groovy](https://docs.gradle.org/current/userguide/groovy_plugin.html) |

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-groovy' version '1.1.8'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.8'
}

apply {
    plugin 'pub.ihub.plugin.ihub-groovy'
}
```

## 组件依赖

> 项目默认会依赖如下组件，可以配置[compileGroovyAllModules](/iHub?id=配置示例)依赖`groovy-all`。

| 默认依赖组件 |
| --------- |
| `groovy` |
| `groovy-datetime` |
| `groovy-dateutil` |
| `groovy-groovydoc` |
| `groovy-json` |
| `groovy-nio` |
| `groovy-sql` |
| `groovy-templates` |
| `groovy-xml` |