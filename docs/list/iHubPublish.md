# ihub-publish

::: info 插件说明
`ihub-publish`插件用于集成组件发布相关插件环境，配置发布仓库以及其他默认配置。
:::

| 信息 | 描述 |
|------ |----|
| 插件ID | `pub.ihub.plugin.ihub-publish` |
| 插件名称 | `发布插件` |
| 插件类型 | `Project`[^Project] |
| 扩展名称 | `iHubPublish` |
| 插件依赖 | [ihub](iHub)、[maven-publish](https://docs.gradle.org/current/userguide/publishing_maven.html)、[io.freefair.github.pom](https://plugins.gradle.org/plugin/io.freefair.github.pom)（`OnGithub`） |

## 扩展属性

| Extension | Description | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| --------- | ----------- | ------- |---|----|---|---|
| `publishNeedSign` | 组件发布是否需要签名 | `false` | ✔ | ✔  | ✔ | ❌ |
| `signingKeyId` | 签名key | ❌ | ✔ | ✔  | ✔ | ✔ |
| `signingSecretKey` | 签名密钥 | ❌ | ✔ | ✔  | ✔ | ✔ |
| `signingPassword` | 签名密码 | ❌ | ✔ | ✔  | ✔ | ✔ |
| `publishSources` | 是否发布源码 | `true` | ✔ | ✔  | ✔ | ❌ |
| `publishDocs` | 是否发布文档 | `false` | ✔ | ✔  | ✔ | ❌ |
| `applyGithubPom` | 是否应用GithubPom插件 | `false` | ✔ | ✔  | ✔ | ❌ |
| `publishMavenCentral` | 是否发布到Maven中央仓库 | `false` | ✔ | ✔  | ✔ | ❌ |
| `addConfigurationMetaInformation` | 是否添加配置元信息 | `true` | ✔ | ✔  | ✔ | ❌ |

## 插件安装

::: code-tabs#build

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

## 配置示例

::: code-tabs#build

@tab Kotlin

```kotlin
iHubPublish {
    publishNeedSign.set(true)
    publishDocs.set(true)
}
```

@tab Groovy

```groovy
iHubPublish {
    publishNeedSign = true
    publishDocs = true
}
```

:::

## 默认配置

::: tip
- 配置组件发布仓库，私有仓库配置[参见](iHub#扩展属性)
- 在Github Actions环境时引入`io.freefair.github.pom`插件，用于获取Github仓库信息并配置`pom`，本插件会另外获取仓库贡献者信息，用于配置`pom`开发人员信息
- Java环境时添加`配置元信息`依赖，使组件可以自动生成配置提示信息，[参见](https://docs.spring.io/spring-boot/docs/2.5.5/reference/html/configuration-metadata.html#configuration-metadata)
:::

```groovy
dependencies {
    annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
}
```

@include(../snippet/footnote.md)
