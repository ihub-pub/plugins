# ihu-boot

::: info plugin description
`ihub-boot`plugin for integration`spring-boot`plugin and mirror default configuration.
:::

| Information         | Description                                                                                              |
| ------------------- | -------------------------------------------------------------------------------------------------------- |
| Plugin ID           | `pub.ihub.plugin.ihub-boot`                                                                              |
| Plugin Name         | `Boot Plugin`                                                                                            |
| Plugin Type         | `Project`[^Project]                                                                                      |
| Extension Name      | `iHubBoot`                                                                                               |
| Plugin Dependencies | [ihub-java](iHubJava),[org.springframe.boot](https://plugins.gradle.org/plugin/org.springframework.boot) |

## Extended Properties

`run`at the beginning of`property`,`bootJar`at the beginning of`pack property`,`bp`at the beginning of image construction`attribute when image is constructed`,`bpl`starts with`startup properties`,`docker`starts with`Docker repository associated properties`,[reference](https://docs.spring.io/spring-boot/docs/2.5.3/gradle-plugin/reference/htmlsingle/)

| Extension                 | Description                                                                                                         | Default                   | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| ------------------------- | ------------------------------------------------------------------------------------------------------------------- | ------------------------- | --------- | --------- | --------- | --------- |
| `runProperties`           | bootRun properties[See](explanation#runproperties)                                                                  | ❌                         | ✔         | ❌         | ❌         | ❌         |
| `runIncludePropNames`     | Running with system property name (`,`separated, supported wildcard`*`)[See](explanation#runincludepropnames)       | ❌                         | ✔         | ✔         | ✔         | ❌         |
| `runSkippedPropNames`     | Exclude system property name on runtime (`,`separated, supported wildcard`*`)[See](explanation#runskippedpropnames) | ❌                         | ✔         | ✔         | ✔         | ❌         |
| `EnabledLocalProperties`  | Enable local properties[See](explanation#enabledlocalproperties)                                                    | `true`                    | ✔         | ✔         | ❌         | ❌         |
| `runOptimizedLaunch`      | Optimize start                                                                                                      | `true`                    | ✔         | ✔         | ✔         | ❌         |
| `Boot JarRequires Unpack` | Configure libraries to be removed                                                                                   | ❌                         | ✔         | ✔         | ❌         | ❌         |
| `bpJvmVersion`            | JVM Version                                                                                                         | `Default current version` | ✔         | ✔         | ❌         | ❌         |
| `bpCleanCache`            | Whether to clean the cache before building                                                                          | `false`                   | ✔         | ✔         | ❌         | ❌         |
| `bpVerboseLogging`        | Enable detailed logging of builder operations                                                                       | `false`                   | ✔         | ✔         | ❌         | ❌         |
| `bpPubant`                | Whether to publish the generated mirrors to the Docker repository                                                   | `false`                   | ✔         | ✔         | ❌         | ❌         |
| `httpProxy`               | httpProxy                                                                                                           | ❌                         | ✔         | ✔         | ❌         | ❌         |
| `httpsProxy`              | https proxy                                                                                                         | ❌                         | ✔         | ✔         | ❌         | ❌         |
| `bplJvmHeadRoom`          | JVM Memory                                                                                                          | `8G`                      | ✔         | ✔         | ❌         | ❌         |
| `bplJvmLoadedClassCount`  | Number of categories loaded while JVM is running                                                                    | `35% of classes`          | ✔         | ✔         | ❌         | ❌         |
| `bplJvmThreadCount`       | Number of user threads when JVM is running                                                                          | `250`                     | ✔         | ✔         | ❌         | ❌         |
| `javaToolOptions`         | JVM Environment Variables                                                                                           | ❌                         | ✔         | ✔         | ❌         | ❌         |
| `bpeEnvironment`          | JVM Runtime Variable,[Reference](https://paketo.io/docs/reference/configuration/)                                   | ❌                         | ✔         | ❌         | ❌         | ❌         |
| `dockerHost`              | Url of the docker daemon host and port                                                                              | ❌                         | ✔         | ✔         | ❌         | ❌         |
| `dockerTlsVerify`         | Enable secure https' protocol                                                                                       | `false`                   | ✔         | ✔         | ❌         | ❌         |
| `dockerCertPath`          | Path to https' certificate and key files                                                                            | ❌                         | ✔         | ✔         | ❌         | ❌         |
| `dockerUrl`               | Docker Private Mirror Repository Address                                                                            | ❌                         | ✔         | ✔         | ❌         | ❌         |
| `dockerUsername`          | Docker Private Image Repository Username                                                                            | ❌                         | ✔         | ✔         | ✔         | ✔         |
| `dockerPassword`          | Docker Private Mirror Repository Password                                                                           | ❌                         | ✔         | ✔         | ✔         | ✔         |
| `docker Email`            | Docker Private Image Repository Mailbox                                                                             | ❌                         | ✔         | ✔         | ❌         | ❌         |
| `dockerToken`             | Docker Private Image Repository Identity Token                                                                      | ❌                         | ✔         | ✔         | ✔         | ✔         |

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LO
    id("pub.ihub.plugin.ihu-boot")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-boot'
}
```

:::

## Configuration Example

:::code-tabs#build

@tab Kotlin

```kotlin
iHubBoot {
    runProperties.set(mapOf("spring.profiles.activ" to "dev")
}
```

@tab Groovy

```groovy
iHubBoot {
    runProperties = [
        'spring.profiles.active': 'dev'
    ]
}
```

:::

@include(../snippet/footnote.md)