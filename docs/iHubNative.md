# ihub-native

> `ihub-native`插件仅仅基于`spring-boot`插件扩展引入`org.graalvm.buildtools.native`插件，详细使用[参见](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.introducing-graalvm-native-images)。

| 插件ID | 插件名称 | 插件类型 | 扩展名称 | 插件依赖 |
|-------|---------|--------|---------|--------|
| `pub.ihub.plugin.ihub-native` | `Native插件` | `Project` | `iHubNative` | [ihub-boot](iHubBoot)、[org.graalvm.buildtools.native](https://github.com/graalvm/native-build-tools) |

## 扩展属性

> 属性使用说明[详见](explanation#属性配置说明)，`bp`开头为`构建镜像时属性`

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `bpNativeImage` | 是否启用原生映像构建 | `true` | ✔ | ✔ | ❌ | ❌ |
| `bpNativeImageBuildArguments` | 传递给原生映像命令的参数 | ❌ | ✔ | ✔ | ❌ | ❌ |

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-native' version '${ihub.plugin.version}'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '${ihub.plugin.version}'
}

apply {
    plugin 'pub.ihub.plugin.ihub-native'
}
```

## 配置示例

```groovy
iHubBoot {
    bpJvmVersion = '11'
}
iHubNative {
    bpNativeImage = true
}
```