# ihu-groovy

::: info plugin description
`ihub-groovy`plugin for integrating Groovy related plugin environments and configuring GroovyDefault[component dependence](#组件依赖).
:::

| Plugin ID                     | Plugin Name     | Plugin Type         | Plugin Dependencies                                                                          |
| ----------------------------- | --------------- | ------------------- | -------------------------------------------------------------------------------------------- |
| `pub.ihub.plugin.ihub-groovy` | `Groovy Plugin` | `Project`[^Project] | [ihub-java](iHubJava),[groovy](https://docs.gradle.org/current/userguide/groovy_plugin.html) |

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-groovy")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-groovy'
}
```

:::

## Component Dependencies

Project default will depend on the following components：

| Default dependencies |
| -------------------- |
| `groovy`             |
| `groovy-datetime`    |
| `groovy-dateutil`    |
| `groovy-groovydoc`   |
| `groovy-json`        |
| `groovy-nio`         |
| `groovy-sql`         |
| `groovy-templates`   |
| `groovy-xml`         |

@include(../snippet/footnote.md)