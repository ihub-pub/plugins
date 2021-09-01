> 用于配置Groovy[默认版本](/iHubBom?id=默认版本)以及[组件依赖](/iHubGroovy?id=组件依赖)。

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-groovy' version '1.1.2'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.2'
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