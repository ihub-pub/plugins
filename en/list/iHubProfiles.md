# ihub-profiles

::: info plugin description
`ihub-profiles`Gradle version Maven POM Profiles
:::

| Info                | Description                     |
| ------------------- | ------------------------------- |
| Plugin Id           | `pub.ihub.plugin.ihub-profiles` |
| Plugin Name         | `Profile Plugin`                |
| Plugin Type         | `Project`[^Project]             |
| Extension Name      | `ihubProfiles`                  |
| Plugin dependencies | [ihub](iHub)                    |

::: tip plugin feature
based on`iHub.profile`attributes, different`profile`extension configuration
:::

## Plugin Installation

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-profiles")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-profiles'
}
```

:::

::: note
If the base plugin`ihub`is installed, there is no need to install`ihub-profiles`plugin,`ihub`plugin is already integrated with`ihu-profiles`plugin.
:::

## Configuration Example

::: code-tabs#build

@tab Kotlin

```kotlin
ihubProfiles {
    profile("dev") {
        println("dev")
    }
    profile("test") {
        println("test")
    }
    profile("prod") {
        println("prod")
    }
}
```

@tab Groovy

```groovy
ihubProfiles {
    profile('dev') {
        println 'dev'
    }
    profile('test') {
        println 'test'
    }
    profile('prod') {
        println 'prod'
    }
}
```

:::

@include(../snippet/footnote.md)