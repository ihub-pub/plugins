> `ihub-java`插件用于集成Java相关插件环境、配置一些默认依赖以及兼容性配置

| 插件ID | 插件名称 | 插件类型 | 扩展名称 | 插件依赖 |
|-------|---------|--------|---------|--------|
| `pub.ihub.plugin.ihub-java` | `Java插件` | `Project` | `iHubJava` | [ihub-bom](iHubBom)、[java](https://docs.gradle.org/current/userguide/java_plugin.html)、[java-library](https://docs.gradle.org/current/userguide/java_library_plugin.html)、[lombok](https://plugins.gradle.org/plugin/io.freefair.lombok)、[project-report](https://docs.gradle.org/current/userguide/project_report_plugin.html)、[build-dashboard](https://docs.gradle.org/current/userguide/build_dashboard_plugin.html) |

## 扩展属性

> 属性使用说明[详见](/explanation?id=属性配置说明)

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `jaxbRuntime` | Jaxb运行时配置 | `true` | ✔ | ✔ | ✔ | ❌ |
| `logDependency` | 日志依赖配置 | `true` | ✔ | ✔ | ✔ | ❌ |
| `mapstructDependency` | MapStruct依赖配置 | `true` | ✔ | ✔ | ❌ | ❌ |
| `compatibility` | Java兼容性配置 | ❌ | ✔ | ✔ | ✔ | ❌ |
| `gradleCompilationIncremental` | gradle增量编译 | `true` | ✔ | ✔ | ✔ | ❌ |

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-java' version '${ihub.plugin.version}'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '${ihub.plugin.version}'
}

apply {
    plugin 'pub.ihub.plugin.ihub-java'
}
```

## 默认配置

> 由于Lombok插件6.1.0之后不再自动生成`lombok.config`文件[详见](https://github.com/freefair/gradle-plugins/issues/379)，**当本地没有lombok.config时**，会自动生成如下配置：

```lombok.config
config.stopBubbling = true
lombok.addLombokGeneratedAnnotation = true
```

> 可以启用[jaxbRuntime](/iHubJava?id=扩展属性)添加jaxb运行时依赖配置。

| DependencyType | Dependencies |
| -------------- | ------------ |
| exclude | `com.sun.xml.bind:jaxb-core` |
| runtimeOnly | `javax.xml.bind:jaxb-api` |
| runtimeOnly | `org.glassfish.jaxb:jaxb-runtime` |

> 可以启用[logDependency](/iHubJava?id=扩展属性)添加默认日志依赖配置。

| DependencyType | Dependencies |
| -------------- | ------------ |
| exclude | `commons-logging:commons-logging` |
| exclude | `log4j:log4j` |
| exclude | `org.apache.logging.log4j:log4j-core` |
| exclude | `org.slf4j:slf4j-log4j12` |
| exclude | `org.slf4j:slf4j-jcl` |
| compileOnly | `cn.hutool:hutool-all` |
| runtimeOnly | `org.slf4j:jul-to-slf4j` |
| runtimeOnly | `javax.xml.bind:jaxb-api` |
| runtimeOnly | `org.slf4j:log4j-over-slf4j` |
| runtimeOnly | `org.glassfish.jaxb:jaxb-runtime` |
| implementation | `org.slf4j:slf4j-api` |

> 可以启用[mapstructDependency](/iHubJava?id=扩展属性)添加添加MapStruct依赖配置。

| DependencyType | Dependencies |
| -------------- | ------------ |
| implementation | `org.mapstruct:mapstruct` |
| annotationProcessor | `org.mapstruct:mapstruct-processor` |

> 配置Jar属性

| 属性 | 值 |
| --- | --- |
| `Implementation-Title` | `{project.name}` |
| `Automatic-Module-Name` | `{project.name}` |
| `Implementation-Version` | `{project.version}` |
| `Implementation-Vendor-Id` | `{project.group}` |
| `Created-By` | `Java ` + `当前Java主版本号` |

生成配置如下：

```manifest
Manifest-Version: 1.0
Implementation-Title: ihub-plugins
Automatic-Module-Name: ihub.plugins
Implementation-Version: 1.0.0
Implementation-Vendor-Id: pub.ihub.plugin
Created-By: Java 11

```

> 可以通过[compatibility](/iHubJava?id=扩展属性)配置兼容性。