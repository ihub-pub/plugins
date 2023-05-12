# ihub-version

:::info plugin description
`ihub-version`plugin is version plugin, integrated and strengthened third party version plugin to set project version.
:::

| Information         | Description                                                                                                                                                                          |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Plugin ID           | `pub.ihub.plugin.ihub-version`                                                                                                                                                       |
| Plugin Name         | `Version Plugin`                                                                                                                                                                     |
| Plugin Type         | `Project`[^Project]                                                                                                                                                                  |
| Extension Name      | `iHubVersion`                                                                                                                                                                        |
| Plugin Dependencies | [io.freeair.git-version](https://plugins.gradle.org/plugin/io.freefair.git-version),[com.github.ben-manes.versions](https://plugins.gradle.org/plugin/com.github.ben-manes.versions) |

:::tip plugin functionality
1. Introducing`git-version`plugin autoconfig project version,`Enhanced support to infer version`
2. Introducing`ben-manes.versions`plugin to check component versions,`enhance support for automatic replacement of the latest version`
:::

## Extended Properties

| Extension                  | Description                                                                                                                                                                                              | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| -------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------- | --------- | --------- | --------- | --------- |
| `autoReplaceLaterVersions` | Automatically replace the latest version ([versions](https://plugins.gradle.org/plugin/com.github.ben-manes.versions)plugin enhancement)                                                                 | `false` | ✔         | ✔         | ✔         | ❌         |
| `useInferingVersion`       | Using a extrapolated version number, extrapolating next version number from the latest`git tag`supports tag format`{major}.{minor}.{patch}`or`v{major}.{minor}.{patch}`, extrapolation method`patch + 1` | `false` | ✔         | ✔         | ✔         | ✔         |

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LOR
    id("pub.ihub.plugin.ihu-version")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-version'
}
```

:::

:::note
If base plugin`ihub`is installed, there is no need to install individually`ihub-version`plugin,`ihub`plugin is already integrated`ihub-version`plugin.
:::

## Configuration Example

:::code-tabs#build

@tab Kotlin

```kotlin
iHubVersion {
    autoReplaceLaterVersions.set(true)
}
```

@tab Groovy

```groovy
iHubVersion {
    autoReplaceLateVersions = true
}
```

:::

@include(../snippet/footnote.md)