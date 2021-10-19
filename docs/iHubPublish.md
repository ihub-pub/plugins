> `ihub-publish`插件用于集成组件发布相关插件环境，配置发布仓库以及其他默认配置。

| 插件ID | 插件名称 | 插件类型 | 扩展名称 | 插件依赖 |
|-------|---------|--------|---------|--------|
| `pub.ihub.plugin.ihub-publish` | `发布插件` | `Project` | `iHubPublish` | [maven-publish](https://docs.gradle.org/current/userguide/publishing_maven.html)、[io.freefair.github.pom](https://plugins.gradle.org/plugin/io.freefair.github.pom)（`OnGithub`） |

## 扩展属性

> 属性使用说明[详见](/explanation?id=属性配置说明)仓库配置[详见](/iHub?id=扩展属性)

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `publishNeedSign` | 组件发布是否需要签名 | `false` | ✔ | ✔ | ✔ | ❌ |
| `signingKeyId` | 签名key | ❌ | ✔ | ✔ | ✔ | ✔ |
| `signingSecretKey` | 签名密钥 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `signingPassword` | 签名密码 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `publishDocs` | 是否发布文档 | `false` | ✔ | ✔ | ✔ | ❌ |

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-publish' version '1.1.8'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.8'
}

apply {
    plugin 'pub.ihub.plugin.ihub-publish'
}
```

## 配置示例

```groovy
iHubTest {
    publishNeedSign = true
    publishDocs = true
}
```

## 默认配置

- 配置组件发布仓库，私有仓库配置[参见](iHub?id=扩展属性)
- 在Github Actions环境时引入`io.freefair.github.pom`插件，用于获取Github仓库信息并配置`pom`，本插件会另外获取仓库贡献者信息，用于配置`pom`开发人员信息
- Java环境时添加`配置元信息`
  依赖，使组件可以自动生成配置提示信息，[参见](https://docs.spring.io/spring-boot/docs/2.5.5/reference/html/configuration-metadata.html#configuration-metadata)

```groovy
dependencies {
    annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
}
```