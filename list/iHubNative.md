# ihub-native

::: info 插件说明
`ihub-native`插件仅仅基于`spring-boot`插件扩展引入`org.graalvm.buildtools.native`插件，详细使用[参见](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.introducing-graalvm-native-images)。
:::

| 信息 | 描述 |
| ---- | ---- |
| 插件ID | `pub.ihub.plugin.ihub-native` |
| 插件名称 | `Native插件` |
| 插件类型 | `Project`[^Project] |
| 扩展名称 | `iHubNative` |
| 插件依赖 | [ihub-boot](iHubBoot)、[org.graalvm.buildtools.native](https://github.com/graalvm/native-build-tools) |

## 扩展属性

| Extension | Description | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `bpNativeImage` | 是否启用原生映像构建 | `true` | ✔ | ✔ | ❌ | ❌ |
| `bpNativeImageBuildArguments` | 传递给原生映像命令的参数 | ❌ | ✔ | ✔ | ❌ | ❌ |

## 插件安装

::: code-tabs#build

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

## 配置示例

::: code-tabs#build

@tab Kotlin

```kotlin
iHubBoot {
    bpJvmVersion.set('11')
}
iHubNative {
    bpNativeImage.set(true)
}
```

@tab Groovy

```groovy
iHubBoot {
    bpJvmVersion = '11'
}
iHubNative {
    bpNativeImage = true
}
```

:::

@include(../snippet/explanation.md)