> 扩展名`iHubPublish`，配置组件发布仓库以及其他个性化组件配置。

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-publish' version '1.1.7'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.7'
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

## 扩展属性

> 属性使用说明[详见](/explanation?id=属性配置说明)仓库配置[详见](/iHub?id=扩展属性)

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `publishNeedSign` | 组件发布是否需要签名 | `false` | ✔ | ✔ | ✔ | ❌ |
| `signingKeyId` | 签名key | ❌ | ✔ | ✔ | ✔ | ✔ |
| `signingSecretKey` | 签名密钥 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `signingPassword` | 签名密码 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `publishDocs` | 是否发布文档 | `false` | ✔ | ✔ | ✔ | ❌ |