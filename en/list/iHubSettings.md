# ihub-settings

::: info plugin description
`ihub-settings`plugin configuration for plugin repository, plugin version, and subproject management, configuration with`settings.gradle`.
:::

| Plugin ID                       | Plugin Name         | Plugin Type           | Extension Name |
| ------------------------------- | ------------------- | --------------------- | -------------- |
| `pub.ihub.plugin.ihub-settings` | `Configure Plugins` | `Settings`[^Settings] | `iHubSettings` |

## Extended Properties

### DSL extension support configuration

| Extended Method   | Extended Description                                                                            |
| ----------------- | ----------------------------------------------------------------------------------------------- |
| `includeProjects` | Add Items (Support for Multiple Projects)                                                       |
| `prefix`          | Item Prefix                                                                                     |
| `noPrefix`        | No items prefix                                                                                 |
| `suffix`          | Project Suffix                                                                                  |
| `subproject`      | Include 3 Level Subprojects                                                                     |
| `onlySubproject`  | Only three tier subprojects (not including the current one, Level 3 are`the main item`sub-item) |
| `skippedDirs`     | Ignore Level 3 Subproject Directory                                                             |

### `gradle.properties`Configure support properties

| Property                           | Description                                                                                              |
| ---------------------------------- | -------------------------------------------------------------------------------------------------------- |
| `name`                             | Configure primary project name                                                                           |
| `iHubSettings.includeDirs`         | Include project paths, multiple directories ", " split                                                   |
| `iHubSettings.skippedDirs`         | Exclude project paths, multiple directories ", " split                                                   |
| `iHubSettings.includeBom`          | Used to configure the bom component, including all subprojects with[ihub-publish](iHubPublish)components |
| `iHubSettings.includeLibs`         | Publish catalog component switching,                                                                     |
| `iHubSettings.includeDependencies` | Used to configure dependencies' components, based on catalog component configuration                     |

> Configure the following:

```properties
name=demo
iHubSettings.includeDirs=rest,service
iHubSettings.includeBom=ihub-bom
```

## Plugin Installation

@include(../snippet/setting.gradle.md)

## Configuration Example

### Configure Subprojects

- Directory Structure:

```
+---rest
+---sdk
\---service
```

- Configure:

::: code-tabs#build

@tab Kotlin

```kotlin
import pub.ihub.plugin.IHubSettingsExtension

configure<IHubSettingsExtension> {
    includeProjects("rest", "sdk", "service")
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

- Directory Structure

```
+---rest
+---service
+---test
\---other
    +---a
    +---b
    \---c
```

- Configure

::: code-tabs#build

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
    includeProjects 'rest', 'service' suffix '-suffix'
    includeProjects 'test' noPrefix
    includeProjects 'other' prefix 'prefix-' skippedDirs 'c' subproject
    includeProjects 'subproject' prefix 'prefix-' suffix '-suffix' onlySubproject
}
```

:::

::: warning
plugins exclude common non-project directories, `build`, `src`, `conf`, `libs`, `logs`, `docs`, `classes`, `target`, `out`, `node_modules`, `db`, `gradle`
:::

## Default Plugin Repositories

Private repository, custom repository configuration see[extension attributes](iHub#扩展属性)

| Name            | Description                    | Url                                               |
| --------------- | ------------------------------ | ------------------------------------------------- |
| `ProjectDirs`   | Project Local Plugin           | `{rootProject.projectDir}/gradle/plugins`         |
| `MavenLocal`    | Local Repository               | `{local}/.m2/repository`                          |
| `AliYunGradle`  | Aliyun Gradle Agent Repository | https://maven.aliyun.com/repository/gradle-plugin |
| `ReleaseRepo`   | Private Release Repository     | https://repo.xxx.com/release                      |
| `SnapshotRepo`  | Private Snapshot repository    | https://repo.xxx.com/snapshot                     |
| `CustomizeRepo` | Custom repository              | https://repo.xxx.com/repo                         |

## Default Version

The plugin is configured with `ihub series plugins` and the following plug-in default versions:

| Plugin                      | Version                                                                             |
| --------------------------- | ----------------------------------------------------------------------------------- |
| `com.gradle.plugin-publish` | [1.2.0](https://plugins.gradle.org/plugin/com.gradle.plugin-publish)                |
| `pub.ihub.plugin.*`         | [1.4.1](https://plugins.gradle.org/plugin/pub.ihub.plugin)                          |
| `io.freefair.*`             | [8.0.1](https://docs.freefair.io/gradle-plugins/8.0.1/reference/#_settings_plugins) |

Use plugins without plating numbers to configure below:

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin")
    id("com.gradle.plugin-publish")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin'
    id 'com.gradle.plugin-publish'
}
```

:::

## Configure catalog

- Configure the default version directory components`ihubLibs`

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

- Private Snapshot repository
- `gradle/libs.versions.toml`for standard configurations, gradle will be automatically imported, and this plugin will automatically configure other`.versions.toml`files such as：`myLibs.versions.toml`, generally using standard configuration sufficient to configure
- Private Release Repository

@include(../snippet/footnote.md)
