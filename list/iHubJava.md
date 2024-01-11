# ihub-java

::: info 插件说明
`ihub-java`插件用于集成Java相关插件环境、配置一些默认依赖以及兼容性配置
:::

| 信息 | 描述 |
|------|----|
| 插件ID | `pub.ihub.plugin.ihub-java` |
| 插件名称 | `Java插件` |
| 插件类型 | `Project`[^Project] |
| 扩展名称 | `iHubJava` |
| 插件依赖 | [ihub-bom](iHubBom)、[java](https://docs.gradle.org/current/userguide/java_plugin.html)、[java-library](https://docs.gradle.org/current/userguide/java_library_plugin.html)、[lombok](https://plugins.gradle.org/plugin/io.freefair.lombok)、[project-report](https://docs.gradle.org/current/userguide/project_report_plugin.html)、[build-dashboard](https://docs.gradle.org/current/userguide/build_dashboard_plugin.html)、<br>[net.bytebuddy.byte-buddy-gradle-plugin](https://bytebuddy.net)、[org.springdoc.openapi-gradle-plugin](https://github.com/springdoc/springdoc-openapi-gradle-plugin) |

::: note
启用`JMoleculesPlugin`插件时，`org.gradle.parallel`设置`false`，可以消除`prepareKotlinBuildScriptModel`任务警告
:::

## 扩展属性

| Extension | Description | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| --------- |-------------| ----- |---|---|---|---|
| `defaultDependencies` | 默认依赖（“,”分割）[详见](#默认依赖)，可以设置`false`关闭默认配置 | `log` | ✔ | ✔ | ❌ | ❌ |
| `compileEncoding` | Java编译编码 | `UTF-8` | ✔ | ✔ | ❌ | ❌ |
| `sourceCompatibility` | Java Source 兼容性配置   | ❌ | ✔ | ✔ | ❌ | ❌ |
| `targetCompatibility` | Java Target 兼容性配置   | ❌ | ✔ | ✔ | ❌ | ❌ |
| `gradleCompilationIncremental` | gradle增量编译  | `true` | ✔ | ✔ | ❌ | ❌ |
| `compilerArgs` | 编译扩展属性，多个参数用空格分隔，如：-parameters -Xlint:unchecked  | ❌ | ✔ | ✔ | ❌ | ❌ |
| `jvmArgs` | JVM扩展属性，多个参数用空格分隔，如：-XX:+UseG1GC -Xms128m -Xmx512m  | ❌ | ✔ | ✔ | ❌ | ❌ |
| `applyOpenapiPlugin` | 启用 [SpringDoc](https://github.com/springdoc/springdoc-openapi-gradle-plugin) 插件  | `false` | ✔ | ✔ | ✔ | ❌ |
| `jmoleculesArchitecture` | JMolecules架构（可选类型：cqrs、layered、onion）[详见](https://jmolecules.org) | `onion` | ✔ | ✔ | ❌ | ❌ |

::: warning
由于插件会在`beforeEvaluate`阶段处理一些配置，根项目时不会处理这个阶段，`Ext`[^Ext]配置可能会失效，建议优先使用`Prj`[^Prj]配置
:::

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-java")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-java'
}
```

:::

## 配置示例

::: code-tabs#build

@tab Kotlin

```kotlin
iHubJava {
    defaultDependencies.set("jaxb,log,doc,mapstruct,jmolecules")
    compatibility.set("11")
}
```

@tab Groovy

```groovy
iHubJava {
    defaultDependencies = 'jaxb,log,doc,mapstruct,jmolecules'
    compatibility = '11'
}
```

:::

## 默认依赖

### `jaxb`：添加jaxb运行时依赖配置。

| DependencyType | Dependencies |
| -------------- | ------------ |
| exclude | `com.sun.xml.bind:jaxb-core` |
| runtimeOnly | `javax.xml.bind:jaxb-api` |
| runtimeOnly | `org.glassfish.jaxb:jaxb-runtime` |

### `log`：添加默认日志依赖配置。

| DependencyType | Dependencies |
| -------------- | ------------ |
| exclude | `commons-logging:commons-logging` |
| exclude | `log4j:log4j` |
| exclude | `org.apache.logging.log4j:log4j-core` |
| exclude | `org.slf4j:slf4j-log4j12` |
| exclude | `org.slf4j:slf4j-jcl` |
| runtimeOnly | `org.slf4j:jul-to-slf4j` |
| runtimeOnly | `org.slf4j:log4j-over-slf4j` |
| runtimeOnly | `org.slf4j:jcl-over-slf4j` |
| implementation | `org.slf4j:slf4j-api` |

### `mapstruct`：添加添加MapStruct依赖配置。

| DependencyType | Dependencies |
| -------------- | ------------ |
| implementation | `org.mapstruct:mapstruct` |
| annotationProcessor | `org.mapstruct:mapstruct-processor` |

### `doc`：添加Doc注解依赖。

| DependencyType | Dependencies |
| -------------- | ------------ |
| compileOnly | `io.swagger.core.v3:swagger-annotations` |
| annotationProcessor | `pub.ihub.lib:ihub-process-doc` |

### `jmolecules`：添加jMolecules依赖。

| DependencyType | Dependencies |
| -------------- | ------------ |
| implementation | `org.jmolecules:jmolecules-ddd` |
| implementation | `org.jmolecules:jmolecules-events` |
| implementation | `org.jmolecules:jmolecules-cqrs-architecture`(`optional`) |
| implementation | `org.jmolecules:jmolecules-layered-architecture`(`optional`) |
| implementation | `org.jmolecules:jmolecules-onion-architecture`(`optional`) |

### `jmolecules-integrations`：添加jMolecules-integrations依赖。

| DependencyType | Dependencies |
| -------------- | ------------ |
| implementation | `org.jmolecules.integrations:jmolecules-spring` |
| implementation | `org.jmolecules.integrations:jmolecules-jpa` |
| implementation | `org.jmolecules.integrations:jmolecules-jackson` |
| testImplementation | `org.jmolecules.integrations:jmolecules-archunit` |

## 默认配置

由于Lombok插件6.1.0之后不再自动生成`lombok.config`文件[详见](https://github.com/freefair/gradle-plugins/issues/379)，**当本地没有lombok.config时**，会自动生成如下配置：

```lombok.config
config.stopBubbling = true
lombok.addLombokGeneratedAnnotation = true
```

### 配置Jar属性

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

### 可以通过[compatibility](#扩展属性)配置兼容性。

## 可选功能配置

`registerFeature`：支持配置可选功能，[详见](https://docs.gradle.org/current/userguide/feature_variants.html)

| Field        | Dependencies |
|--------------| ------------ |
| `feature`      | 功能 |
| `capabilities` | 能力 |

> 配置示例

::: code-tabs#build

@tab Kotlin

```kotlin
iHubJava {
    registerFeature("servlet", "cloud-support", "servlet-support")
    registerFeature("reactor", "cloud-support", "reactor-support")
}
dependencies {
    "servletApi"("org.springframework.boot:spring-boot-starter-web")
    "reactorApi"("org.springframework.boot:spring-boot-starter-webflux")
}
```

@tab Groovy

```groovy
iHubJava {
    registerFeature 'servlet', 'cloud-support', 'servlet-support'
    registerFeature 'reactor', 'cloud-support', 'reactor-support'
}
dependencies {
    servletApi 'org.springframework.boot:spring-boot-starter-web'
    reactorApi 'org.springframework.boot:spring-boot-starter-webflux'
}
```

:::

@include(../snippet/footnote.md)