# ihub-kotlin

::: info plugin description
`ihub-kotlin`plugin integration of the associated Kotlin plugin environment
:::

| Plugin ID                     | Plugin Name     | Plugin Type         | Plugin Dependencies                                                                                          |
| ----------------------------- | --------------- | ------------------- | ------------------------------------------------------------------------------------------------------------ |
| `pub.ihub.plugin.ihub-kotlin` | `Kotlin Plugin` | `Project`[^Project] | [ihub-java](iHubJava)、[org.jetbrains.kotlin.jvm](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm) |

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-kotlin")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-kotlin'
}
```

:::

@include(../snippet/footnote.md)