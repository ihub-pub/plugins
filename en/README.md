---
title: Quick start
icon: light
---

Start using this plugin before learning the base configuration`Gradle`that can be moved to[entry - Gradle basic startup](basics/gradleBasic), this plugin is mainly used to simplify`setting.gradle`and`build.gradle`Configuration

## setting.gradle Configuration

Adds`pub.ihub.plugin.ihub-settings`plugins that provide common plugin repository configuration and multiple project configurations, see[document for details](list/iHubSettings)：

@include(./snippet/setting.gradle.md)

## build.gradle configuration

Adding`pub.ihub.plugin`Basic plugins that provide common component repository configuration and integration of some other extension, see[document](list/iHub), in addition to some other plugins,`Plugins Set`Instructions for each plugin：

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LOR
    id("pub.ihub.plugin")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin'
}
```

:::

Additional：`ihub-setup`plugins are not required. If your project does not require multiple project configurations, you can directly introduce`pub.ihub.plugin`plugins and note the need to set version number (`ihub-settings plugins`plugins in[auto configure version size](list/iHubSettings#默认版本)) and configure the following：

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LO
    id("pub.ihub.plugin") version "${ihub.plugin.version}"
}
```

@tab Groovy

```groovy
plugins LO
    id 'pub.ihub.plugin' version ' '${ihub.plugin.version}'
}
```

:::