# ihub-java

:::info plugin description
`ihub-java`plugin for integrating Java related plugin environment, configuration some default dependencies, and compatibility configuration
:::

| Information         | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Plugin ID           | `pub.ihub.plugin.ihub-java`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| Plugin Name         | `Java Plugin`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| Plugin Type         | `Project`[^Project]                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| Extension Name      | `iHubJava`                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| Plugin Dependencies | [ihub-bom](iHubBom),[java](https://docs.gradle.org/current/userguide/java_plugin.html),[java-library](https://docs.gradle.org/current/userguide/java_library_plugin.html),[lombok](https://plugins.gradle.org/plugin/io.freefair.lombok),[project-report](https://docs.gradle.org/current/userguide/project_report_plugin.html),[build-dashboard](https://docs.gradle.org/current/userguide/build_dashboard_plugin.html),<br>[net. ytebuddy.byte-buddy-gradle-plugin](https://bytebuddy.net),[org.springdoc.openapi-gradle-plugin](https://github.com/springdoc/springdoc-openapi-gradle-plugin) |

::: note
启用`JMoleculesPlugin`插件时，`org.gradle.parallel`设置`false`，可以消除`prepareKotlinBuildScriptModel`任务警告
:::

## Extended Properties

| Extension                 | Description                                                                                                | Default  | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| ------------------------- | ---------------------------------------------------------------------------------------------------------- | -------- | --------- | --------- | --------- | --------- |
| `Default Dependencies`    | Default dependency (',' separated)[See](#默认依赖)for details, set up`false`to close the default configuration | `log`    | ✔         | ✔         | ❌         | ❌         |
| `CompileEncoding`         | JavaScript Code                                                                                            | `UTF-8`  | ✔         | ✔         | ❌         | ❌         |
| `Compatibility`           | Java Compatibility Configuration                                                                           | ❌        | ✔         | ✔         | ❌         | ❌         |
| `gradleCompile cremental` | gradle increment compilation                                                                               | `true`   | ✔         | ✔         | ❌         | ❌         |
| `compilerArgs`            | Compiles extended attributes, multiple parameters separated by spaces, e.g.：- parameters -Xlint:unchecked  | ❌        | ✔         | ✔         | ❌         | ❌         |
| `jvmArgs`                 | JVM extension attributes, multiple arguments separated by spaces, e.g.：- XX:+UseG1GC -Xms128m -Xmx512m     | ❌        | ✔         | ✔         | ❌         | ❌         |
| `applyOpenapiPlugin`      | Enable [SpringDoc](https://github.com/springdoc/springdoc-openapi-gradle-plugin) plugin                    | `false`  | ✔         | ✔         | ✔         | ❌         |
| `jmoleculesArchitecture`  | JMolecules Architecture (optional type：cqrs, layered, onion)[See](https://jmolecules.org)                  | `monion` | ✔         | ✔         | ❌         | ❌         |

::: warning
Since plugins will process some configuration at`beforEvalate`, root items will not handle this stage,`Ext`[^Ext]configuration may lapse, priority is recommended`Prj`[^Prj]configuration
:::

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LOR
    id("pub.ihub.plugin.ihu-java")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-java'
}
```

:::

## Configuration Example

:::code-tabs#build

@tab Kotlin

```kotlin
iHubJava has
    defaultDependencies.set("jaxb,log,doc,map,jmolecules")
    compatibility.set("11")
}
```

@tab Groovy

```groovy
iHubJava has
    defaultDependencies = 'jaxb,log,doc,map,jmolecules'
    compatibility = '11'
}
```

:::

## Default Dependencies

### `jaxb`：Depends on configuration when adding jaxb runs.

| DependencyType | Dependencies                      |
| -------------- | --------------------------------- |
| Exclude        | `com.sun.xml.bind:jaxb-core`      |
| runtimeOnly    | `javax.xml.bind:jaxb-api`         |
| runtimeOnly    | `org.glassfish.jaxb:jaxb-runtime` |

### `log`：add default log dependency configuration.

| DependencyType | Dependencies                          |
| -------------- | ------------------------------------- |
| Exclude        | `commons-logging: commons-logging`    |
| Exclude        | `log4j:log4j`                         |
| Exclude        | `org.apache.logging.log4j:log4j-core` |
| Exclude        | `org.slf4j:slf4j-log4j12`             |
| Exclude        | `org.slf4j: slf4j-jcl`                |
| runtimeOnly    | `org.slf4j:jul-to-slf4j`              |
| runtimeOnly    | `org.slf4j:log4j-over-slf4j`          |
| runtimeOnly    | `org.slf4j:jcl-over-slf4j`            |
| Implementation | `org.slf4j: slf4j-api`                |

### `mapstruct`：add new MapStruct dependency configuration.

| DependencyType      | Dependencies                        |
| ------------------- | ----------------------------------- |
| Implementation      | `org.mapstruct:mapstruct`           |
| annotationProcessor | `org.mapstruct:mapstruct-processor` |

### `doc`：add docannotation dependence.

| DependencyType      | Dependencies                               |
| ------------------- | ------------------------------------------ |
| Compilation Only    | `io.swagger.core.v3:swagger-announcements` |
| annotationProcessor | `pub.ihub.lib:ihub-process-doc`            |

### `jmolecules`：add jMolecules dependency.

| DependencyType | Dependencies                                          |
| -------------- | ----------------------------------------------------- |
| Implementation | `org.jmolecules:jmolecules-dd`                        |
| Implementation | `org.jmolecules:jmolecules-events`                    |
| Implementation | `org.jmolecules:jmolecules-cqrs-archive`(`optional`)  |
| Implementation | `org.jmolecules:jmolecules-layered-archive`(`option`) |
| Implementation | `org.jmolecules:jmolecules-onion-archive`(`optional`) |

### `jmolecules-integrations`：add jMolecules-integration dependence.

| DependencyType     | Dependencies                                      |
| ------------------ | ------------------------------------------------- |
| Implementation     | `org.jmolecules.integrations: jmolecules-spring`  |
| Implementation     | `org.jmolecules.integrations: jmolecules-jpa`     |
| Implementation     | `org.jmolecules.integrations: jmolecules-jackson` |
| testImplementation | `org.jmolecules.integrations: jmolecules-archive` |

## Default configuration

Since the Lombok plugin 6.1.0 does not auto-generate`lobok.config`file[See](https://github.com/freefair/gradle-plugins/issues/379),**when there is no lobok.config**the following configuration will be generated automatically：

```lombok.config
config.stopBubbling = true
lobok.addLombokGeneratedAnnotation = true
```

### Configure Jar Properties

| Properties                 | Value                                |
| -------------------------- | ------------------------------------ |
| `Implementation - Title`   | `{project.name}`                     |
| `Automatic-Module-Name`    | `{project.name}`                     |
| `Implementation-Version`   | `{project.version}`                  |
| `Implementation-Vendor-Id` | `{project.group}`                    |
| `Created-By`               | `Java` + `Current Java Main Version` |

Generate configuration below：

```manifest
Manifest-Version: 1.0
Implementation-Title: ihub-plugins
Automatic-Module-Name: ihub.plugins
Implementation-Version: 1.0.0
Implementation-Vendor-Id: pub.ihub.plugin
Created-By: Java 11

```

### 可以通过[compatibility](#扩展属性)配置兼容性。

## Optional feature configuration

`registerFeature`：supports configuration options,[See](https://docs.gradle.org/current/userguide/feature_variants.html)

| Field          | Dependencies |
| -------------- | ------------ |
| `Feature`      | Function     |
| `Capabilities` | Capabilities |

> Configuration Example

:::code-tabs#build

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
iHubJava has
    registerFeature 'servlet', 'cloud-support', 'servlet-support'
    registerFeature 'reactor', 'cloud-support', 'reactor-support'
}
dependagencies have {
    servletApi 'org.springframe.boot: spring-boot-starter-web'
    reactorApi 'org.springframe.boot: spring-boot-boot-starter-webinflux'
}
```

:::

@include(../snippet/exploation.md)