# ihub-settings

::: info plugin description
`ihub-settings`plugin configuration for plugin repository, plugin version, and subproject management, configuration with`settings.gradle`.
:::

| Plugin ID                       | Plugin Name         | Plugin Type           | Extension Name |
| ------------------------------- | ------------------- | --------------------- | -------------- |
| `pub.ihub.plugin.ihub-settings` | `Configure Plugins` | `Settings`[^Settings] | `iHubSettings` |

## Extended Properties

### DSL extension support configuration

| Extended Method    | Extended Description                                                                            |
| ------------------ | ----------------------------------------------------------------------------------------------- |
| `include Projects` | Add Items (Support for Multiple Projects)                                                       |
| `Prefix`           | Project Prefix (default`main project name -`)                                                   |
| `noPrefix`         | No items prefix                                                                                 |
| `suffix`           | Project Suffix                                                                                  |
| `Subproject`       | Include 3 Level Subprojects                                                                     |
| `alone Subproject` | Only three tier subprojects (not including the current one, Level 3 are`the main item`sub-item) |
| `skippedDirs`      | Ignore Level 3 Subproject Directory                                                             |

### `gradle.properties`Configure support properties

| Property                   | Description                                                                                             |
| -------------------------- | ------------------------------------------------------------------------------------------------------- |
| `Name`                     | Configure primary project name                                                                          |
| `iHubSettings.includeDirs` | Include project paths, multiple directories", split                                                     |
| `iHubSettings.skippedDirs` | Exclude project paths, multiple directories", split                                                     |
| `iHubSettings.includeBom`  | Used to configure the bom component, including all subprojects with[ihub-public](iHubPublish)components |

> Configure the following：

```properties
name=demo
iHubSettings.includeDirs=res,service
iHubSettings.includeBom=ihub-bom
```

## Plugin Installation

@include(../snippet/setting.gradle.md)

## Configuration Example

### Configure Subprojects

- Directory Structure：

```
+--reset
+--sdk
\--service
```

- Configure：

:::code-tabs#build

@tab Kotlin

```kotlin
import pub.ihub.plugin.IHubSettingsextension

configure<IHubSettingsExtension> LO
    includeProjects ("rest", "sdk", "service")
}
```

@tab Groovy

```groovy
iHubSettings {
    includeProjects 'rest', 'sdk', 'service'
}
```

:::

### Configure Level 3 Subprojects

- Directory Structure：

```
+--reset
+--service
+--test
\--other
    +---a
    +---b
    \--c
```

- Configure：

:::code-tabs#build

@tab Kotlin

```kotlin
import pub.ihub.plugin.IHubSettingsExtension

configure<IHubSettingsExtension> {
    includeProjects("rest", "service").suffix("-suffix")
    includeProjects("test").noPrefix
    includeProjects("other").prefix("prefix-").skippedDirs("c").subproject
    includeProjects("subproject").prefix("prefix-").suffix("-suffix").onlySubproject
}
```

@tab Groovy

```groovy
iHubSettings {
    include Projects 'rest', 'service' suffix '-suffix'
    include Projects 'test' noPrefix
    includeProjects 'other' prefix 'prefix-' skippedDirs' 'c' subproject
    include Projects 'subject' prefix 'prefix-' suffix '-'suffix' onlySubproject
}
```

:::

::: warning
plugins exclude common non-project directories,`build`, `src`, `conf`, `libs`, `logs`, `docs`, `classes`, `target`, `out`, `node_modules`, `db`, `gradle` :::: `

## Default Plugin Repositories

Private repository, custom repository configuration see[extension attributes](iHub#扩展属性)

| Name            | Description                 | Url                                       |
| --------------- | --------------------------- | ----------------------------------------- |
| `ProjectDirs`   | Project Local Plugin        | `{rootProject.projectDir}/gradle/plugins` |
| `SpringRelease` | Spring Release Repository   | https://repo.spring.io/release            |
| `ReleaseRepo`   | Private Release Repository  | https://repo.xxx.com/release              |
| `SnapshotRepo`  | Private Snapshot repository | https://repo.xxx.com/snapshot             |
| `CustomizeRepo` | Custom repository           | https://repo.xxx.com/repo                 |

## Default Version

The plugin is configured with <code>ihub series plugins`` and the following plug-in default versions:

`com.gradle.plugin-published`

| Plugins                       | Default Version                                                      |
| ----------------------------- | -------------------------------------------------------------------- |
| `com.gradle.plugin-published` | [1.2.0](https://plugins.gradle.org/plugin/com.gradle.plugin-publish) |
| `pub.ihub.plugin.*`           | [1.3.2](https://plugins.gradle.org/plugin/pub.ihub.plugin)           |

Use plugins without plating numbers to configure below：

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LOR
    id("pub.ihub.plugin")
    id("com.gradle.plugin-publish")
}
```

@tab Groovy

```groovy
plugins LO
    id 'pub.ihub.plugin'
    id 'com.gradle.plugin-publish'
}
```

:::

## Configure catalog

Configure the default version directory components`ihubibs`

```groovy
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        ihubLibs {
            from "pub.ihub.lib:ihub-libs:${IHubLibsVersion.version}"
        }
    }
}
```

@include(../snippet/exploation.md)