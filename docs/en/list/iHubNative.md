# ihub-native

::: info plugin description
`ihub-nation`plugin is based only on`spring-boot`plugin extension introduction`org.graalvm.buildtools.native`plugins for details[see](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.introducing-graalvm-native-images).
:::

| Information         | Description                                                                                          |
| ------------------- | ---------------------------------------------------------------------------------------------------- |
| Plugin ID           | `pub.ihub.plugin.ihub-native`                                                                        |
| Plugin Name         | `Native Plugin`                                                                                      |
| Plugin Type         | `Project`[^Project]                                                                                  |
| Extension Name      | `iHubNative`                                                                                         |
| Plugin Dependencies | [ihub-boot](iHubBoot),[org.graalvm.buildtools.native](https://github.com/graalvm/native-build-tools) |

## Extended Properties

| Extension                     | Description                                  | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| ----------------------------- | -------------------------------------------- | ------- | --------- | --------- | --------- | --------- |
| `bpNativeImage`               | Whether to enable original image build       | `true`  | ✔         | ✔         | ❌         | ❌         |
| `bpNativeImageBuildArguments` | Parameters passed to original image commands | ❌       | ✔         | ✔         | ❌         | ❌         |

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-native")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-native'
}
```

:::

## Configuration Example

:::code-tabs#build

@tab Kotlin

```kotlin
iHubBoot {
    bpJvmVersion.set('11')
}
iHubNative L
    bpNativeImage.set(true)
}
```

@tab Groovy

```groovy
iHubBoot {
    bpJvmVersion = '11'
}
iHubNative LO
    bpNative = true
 } }
```

:::

@include(../snippet/footnote.md)