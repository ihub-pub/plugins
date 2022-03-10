> `ihub-native`插件用于集成`spring-native`插件以及镜像默认配置，详细说明参见[官方文档](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/)。

| 插件ID | 插件名称 | 插件类型 | 扩展名称 | 插件依赖 |
|-------|---------|--------|---------|--------|
| `pub.ihub.plugin.ihub-native` | `Native插件` | `Project` | `iHubNative` | [ihub-boot](iHubBoot)、[org.springframework.experimental.aot](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#spring-aot-gradle) |

## 扩展属性

> 属性使用说明[详见](/explanation?id=属性配置说明)，继承[ihub-boot](iHubBoot?id=扩展属性)组件扩展属性，`bp`开头为`构建镜像时属性`，`aot`开头为`AOT插件属性`

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `bpNativeImage` | 是否启用原生映像构建 | `true` | ✔ | ✔ | ❌ | ❌ |
| `bpNativeImageBuildArguments` | 传递给原生映像命令的参数 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `aotMode` | native镜像编译器配置 | `NATIVE` | ✔ | ✔ | ❌ | ❌ |
| `aotDebugVerify` | 启用验证调试 | `false` | ✔ | ✔ | ✔ | ❌ |
| `aotRemoveXmlSupport` | 移除XML支持 | `true` | ✔ | ✔ | ❌ | ❌ |
| `aotRemoveSpelSupport` | 移除Spel支持 | `false` | ✔ | ✔ | ❌ | ❌ |
| `aotRemoveYamlSupport` | 移除Yaml支持 | `false` | ✔ | ✔ | ❌ | ❌ |
| `aotRemoveJmxSupport` | 移除Jmx支持 | `true` | ✔ | ✔ | ❌ | ❌ |
| `aotVerify` | 开启自动验证 | `true` | ✔ | ✔ | ❌ | ❌ |

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
iHubNative {
    bpJvmVersion = '11'
}
```