# ihub-published

::: info plugin description
`ihub-publ.`Plugins are used to integrate the component to publish related plugins, configure release repositories and other default configurations.
:::

| Information         | Description                                                                                                                                                                                   |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Plugin ID           | `pub.ihub.plugin.ihub-published`                                                                                                                                                              |
| Plugin Name         | `Publish Plugin`                                                                                                                                                                              |
| Plugin Type         | `Project`[^Project]                                                                                                                                                                           |
| Extension Name      | `iHubPublish`                                                                                                                                                                                 |
| Plugin Dependencies | [ihub](iHub),[maven-published](https://docs.gradle.org/current/userguide/publishing_maven.html),[io.freeair.github.pom](https://plugins.gradle.org/plugin/io.freefair.github.pom)(`OnGithub`) |

## Extended Properties

| Extension            | Description                                     | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| -------------------- | ----------------------------------------------- | ------- | --------- | --------- | --------- | --------- |
| `publishNeedSigns`   | Whether component publishing needs to be signed | `false` | ✔         | ✔         | ✔         | ❌         |
| `signingKeyId`       | Signing key                                     | ❌       | ✔         | ✔         | ✔         | ✔         |
| `signingSecretarial` | Signing Key                                     | ❌       | ✔         | ✔         | ✔         | ✔         |
| `signingPassword`    | Signing password                                | ❌       | ✔         | ✔         | ✔         | ✔         |
| `publishSources`     | Publish source                                  | `true`  | ✔         | ✔         | ✔         | ❌         |
| `publishDocs`        | Publish document                                | `false` | ✔         | ✔         | ✔         | ❌         |
| `applyGithubPom`     | Whether to apply GithubPom plugin               | `false` | ✔         | ✔         | ✔         | ❌         |

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-publish")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-publish'
}
```

:::

## Configuration Example

::: code-tabs#build

@tab Kotlin

```kotlin
iHubPubPublish {
    publishNeedSign.set(true)
    publishDocs.set(true)
}
```

@tab Groovy

```groovy
iHubPubanish {
    publishNeedSign=true
    publishDocs = true
}
```

:::

## Default configuration

:::tip
- Configure Component Publish Repository, Private Repository Configuration[see](iHub#扩展属性)
- Adds`io.freeair.github.pom`to get GitHub repository information and configured`pom`, this plugin will get additional repository contributor information for configuration`pom`developer information
- Adds`configuration metadata information to the Java environment`dependence, allowing components to automatically generate configuration tips,[see](https://docs.spring.io/spring-boot/docs/2.5.5/reference/html/configuration-metadata.html#configuration-metadata)
:::

```groovy
Dependencies {
    annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
}
```

@include(../snippet/footnote.md)
