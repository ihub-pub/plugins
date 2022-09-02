> 开始使用本插件之前先要了解`Gradle`相关的基础配置，可移步至入门篇-[Gradle基础入门](/basics/gradleBasic)

## setting.gradle 快速配置

> 引入`Settings`插件，该插件封装了常用的插件仓库配置以及多项目个性化配置，[详细文档](/iHubSettings)：

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-settings' version '${ihub.plugin.version}'
}
```

## build.gradle 快速配置

> 引入`pub.ihub.plugin`基础插件，该插件封装了一些常规项目配置以及一些个性化插件配置，[详细文档](/iHub)：

```groovy
plugins {
    id 'pub.ihub.plugin'
}
```
