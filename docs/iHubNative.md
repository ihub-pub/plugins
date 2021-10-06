> 扩展名`iHubNative`，用于`spring-native`镜像个性化配置。

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-native' version '1.1.6'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.6'
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

## 扩展属性

> [属性说明](/explanation?id=属性配置说明)：`bp`开头为`构建镜像时属性`，`bpl`开头为`启动时属性`，`aot`开头为`AOT插件属性`

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `bpJvmVersion` | JVM版本 | `默认当前版本` | ✔ | ✔ | ❌ | ❌ |
| `bpNativeImage` | 是否启用原生映像构建 | `true` | ✔ | ✔ | ❌ | ❌ |
| `bpNativeImageBuildArguments` | 传递给原生映像命令的参数 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `bplJvmHeadRoom` | JVM内存 | `8G` | ✔ | ✔ | ❌ | ❌ |
| `bplJvmLoadedClassCount` | JVM运行时已加载类的数量 | `35% of classes` | ✔ | ✔ | ❌ | ❌ |
| `bplJvmThreadCount` | JVM运行时用户线程数 | `250` | ✔ | ✔ | ❌ | ❌ |
| `javaToolOptions` | JVM环境变量 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `aotMode` | native镜像编译器配置 | `NATIVE` | ✔ | ✔ | ❌ | ❌ |
| `aotDebugVerify` | 启用验证调试 | `false` | ✔ | ✔ | ✔ | ❌ |
| `aotRemoveXmlSupport` | 移除XML支持 | `true` | ✔ | ✔ | ❌ | ❌ |
| `aotRemoveSpelSupport` | 移除Spel支持 | `false` | ✔ | ✔ | ❌ | ❌ |
| `aotRemoveYamlSupport` | 移除Yaml支持 | `false` | ✔ | ✔ | ❌ | ❌ |
| `aotRemoveJmxSupport` | 移除Jmx支持 | `true` | ✔ | ✔ | ❌ | ❌ |
| `aotVerify` | 开启自动验证 | `true` | ✔ | ✔ | ❌ | ❌ |
| `aotRemoveUnusedConfig` | 移除未使用的配置 | `true` | ✔ | ✔ | ❌ | ❌ |
| `aotFailOnMissingSelectorHint` | 如果没有为活动选择器提供提示，则抛出错误 | `true` | ✔ | ✔ | ❌ | ❌ |