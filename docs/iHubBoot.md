> `ihub-boot`插件用于集成`spring-boot`插件以及镜像默认配置。

| 插件ID | 插件名称 | 插件类型 | 扩展名称 | 插件依赖 |
|-------|---------|--------|---------|--------|
| `pub.ihub.plugin.ihub-boot` | `Boot插件` | `Project` | `iHubBoot` | [ihub-java](iHubJava)、[org.springframework.boot](https://plugins.gradle.org/plugin/org.springframework.boot) |

## 扩展属性

> 属性使用说明[详见](/explanation?id=属性配置说明)，`run`开头为`运行时属性`，`bootJar`开头为`打包Jar时属性`，`bp`开头为`构建镜像时属性`，`bpl`开头为`启动时属性`，`docker`开头为`Docker仓库相关属性`，[参考](https://docs.spring.io/spring-boot/docs/2.5.3/gradle-plugin/reference/htmlsingle/)

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `runProperties` | bootRun属性[详见](/explanation?id=runproperties) | ❌ | ✔ | ❌ | ❌ | ❌ |
| `runIncludePropNames` | 运行时包含系统属性名称（“,”分割）[详见](/explanation?id=runincludepropnames) | ❌ | ✔ | ✔ | ✔ | ❌ |
| `runSkippedPropNames` | 运行时排除系统属性名称（“,”分割）[详见](/explanation?id=runskippedpropnames) | ❌ | ✔ | ✔ | ✔ | ❌ |
| `enabledLocalProperties` | 启用本地属性[详见](/explanation?id=enabledlocalproperties) | `true` | ✔ | ✔ | ❌ | ❌ |
| `runOptimizedLaunch` | 优化启动 | `true` | ✔ | ✔ | ✔ | ❌ |
| `bootJarRequiresUnpack` | 配置需要移除的库 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `bpJvmVersion` | JVM版本 | `默认当前版本` | ✔ | ✔ | ❌ | ❌ |
| `bpCleanCache` | 是否在构建前清理缓存 | `false` | ✔ | ✔ | ❌ | ❌ |
| `bpVerboseLogging` | 启用构建器操作的详细日志记录 | `false` | ✔ | ✔ | ❌ | ❌ |
| `bpPublish` | 是否将生成的镜像发布到Docker仓库 | `false` | ✔ | ✔ | ❌ | ❌ |
| `httpProxy` | http代理 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `httpsProxy` | https代理 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `bplJvmHeadRoom` | JVM内存 | `8G` | ✔ | ✔ | ❌ | ❌ |
| `bplJvmLoadedClassCount` | JVM运行时已加载类的数量 | `35% of classes` | ✔ | ✔ | ❌ | ❌ |
| `bplJvmThreadCount` | JVM运行时用户线程数 | `250` | ✔ | ✔ | ❌ | ❌ |
| `javaToolOptions` | JVM环境变量 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `bpeEnvironment` | JVM运行时变量，[参考](https://paketo.io/docs/reference/configuration/) | ❌ | ✔ | ❌ | ❌ | ❌ |
| `dockerHost` | Docker守护程序的主机和端口的url | ❌ | ✔ | ✔ | ❌ | ❌ |
| `dockerTlsVerify` | 启用安全https协议 | `false` | ✔ | ✔ | ❌ | ❌ |
| `dockerCertPath` | https证书和密钥文件的路径 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `dockerUrl` | Docker私有镜像仓库地址 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `dockerUsername` | Docker私有镜像仓库用户名 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `dockerPassword` | Docker私有镜像仓库密码 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `dockerEmail` | Docker私有镜像仓库邮箱 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `dockerToken` | Docker私有镜像仓库身份令牌 | ❌ | ✔ | ✔ | ✔ | ✔ |

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-boot' version '${ihub.plugin.version}'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '${ihub.plugin.version}'
}

apply {
    plugin 'pub.ihub.plugin.ihub-boot'
}
```

## 配置示例

```groovy
iHubBoot {
    runProperties = [
        'spring.profiles.active': 'dev'
    ]
}
```