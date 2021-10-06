> 配置一些默认依赖以及兼容性配置

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-java' version '1.1.6'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.6'
}

apply {
    plugin 'pub.ihub.plugin.ihub-java'
}
```

## 默认配置

> 可以启用[javaJaxbRuntime](/iHub?id=扩展属性)添加jaxb运行时依赖配置。

| DependencyType | Dependencies |
| -------------- | ------------ |
| exclude | `com.sun.xml.bind:jaxb-core` |
| runtimeOnly | `javax.xml.bind:jaxb-api` |
| runtimeOnly | `org.glassfish.jaxb:jaxb-runtime` |

> 可以配置[javaCompatibility](/iHub?id=扩展属性)配置兼容性。
