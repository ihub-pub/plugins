# ihub

::: info plugin description
`ihu`plugin is basic plugin to configure[component repository](#组件仓库)and some other[extension attributes](#扩展属性), configuration and`build.gradle`.
:::

| Information         | Description                                                                    |
| ------------------- | ------------------------------------------------------------------------------ |
| Plugin ID           | `pub.ihub.plugin`                                                              |
| Plugin Name         | `Base Plugin`                                                                  |
| Plugin Type         | `Project`[^Project]                                                            |
| Extension Name      | `iHub`                                                                         |
| Plugin Dependencies | [ihub-git-hooks](iHubGitHooks),[ihub-bom](iHubBom),[ihub-version](iHubVersion) |

:::tip plugin functionality
1. Configure Component Repository, repository details[See](#组件仓库)
2. When the main project does not have[java-platform](https://docs.gradle.org/current/userguide/java_platform_plugin.html)and[version-catalog](https://docs.gradle.org/current/userguide/platforms.html)plugins will import`ihub-bom`plugins by default
3. If the project contains a subproject, the subproject will also introduce this plugin
:::

## Extended Properties

| Extension                   | Description                                                        | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| --------------------------- | ------------------------------------------------------------------ | ------- | --------- | --------- | --------- | --------- |
| `MavenenLocalEnabled`       | Enable local repository                                            | `false` | ✔         | ✔         | ❌         | ❌         |
| `Mavenen AliYunEnabled`     | Enable Aliyun Proxy Repository                                     | `false` | ✔         | ✔         | ✔         | ✔         |
| `releaseRepoUrl`            | Official Repository                                                | ❌       | ✔         | ✔         | ❌         | ❌         |
| `snapshotRepoUrl`           | Snapshot Repository                                                | ❌       | ✔         | ✔         | ❌         | ❌         |
| `repoAllowInsecureProtocol` | Whether to allow the insecure protocol (whether http)              | `false` | ✔         | ✔         | ❌         | ❌         |
| `RepoInclusion Group`       | Repository contains groups (used to limit repository range)        | ❌       | ✔         | ✔         | ❌         | ❌         |
| `repoInclude GroupRegex`    | Repository contains group regular (used to limit repository range) | `.*`    | ✔         | ✔         | ❌         | ❌         |
| `repoUsername`              | Repository Username                                                | ❌       | ✔         | ✔         | ✔         | ✔         |
| `repoPassword`              | Repository Password                                                | ❌       | ✔         | ✔         | ✔         | ✔         |
| `customizeRepoUrl`          | Custom Repository                                                  | ❌       | ✔         | ✔         | ❌         | ❌         |

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins {
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

## Configuration Example

```properties
iHub.mavenLocalEnabled=true
iHub.mavenAliYunEnabled=true
```

## Component Repository

Configure component warehouses for adaptation to the domestic network environment as follows:

| Name            | Description                    | Url                                                                                                |
| --------------- | ------------------------------ | -------------------------------------------------------------------------------------------------- |
| `ProjectDirs`   | Project Local Component        | `{rootProject.projectDir}/libs`                                                                    |
| `MavenLocal`    | Local Repository               | `{local}/.m2/repository`                                                                           |
| `AliYunPublic`  | Aliyun Aggregate Repository    | https://maven.aliyun.com/repository/public <br> artifactUrls: https://repo1.maven.org/maven2 |
| `AliYunGoogle`  | Aliyun Google Proxy Repository | https://maven.aliun.com/repository/google <br> artifactUrls: https://maven.google.com        |
| `AliYunSpring`  | Aliyun Spring Proxy Repository | https://maven.aliyun.com/repository/spring <br> artifactUrls: https://repo.spring.io/release |
| `SpringRelease` | Spring Release Repository      | https://repo.spring.io/release                                                                     |
| `ReleaseRepo`   | Private Release Repository     | https://repo.xxx.com/release                                                                       |
| `SnapshotRepo`  | Private Snapshot repository    | https://repo.xxx.com/snapshot                                                                      |
| `CustomizeRepo` | Custom repository              | https://repo.xxx.com/repo                                                                          |
| `MavenRepo`     | Maven Central Repository       |                                                                                                    |

@include(../snippet/footnote.md)