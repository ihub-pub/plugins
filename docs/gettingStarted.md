> 开始使用本插件之前先要了解`Gradle`相关的基础配置，可移步至入门篇-[Gradle基础入门](/basics/gradleBasic)，本插件主要用于简化`setting.gradle`和`build.gradle`配置

## setting.gradle 配置

> 引入`pub.ihub.plugin.ihub-settings`插件，该插件提供了常用的插件仓库配置以及多项目配置，详细使用见[文档](/iHubSettings)：

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-settings' version '${ihub.plugin.version}'
}
```

## build.gradle 配置

> 引入`pub.ihub.plugin`基础插件，该插件提供了常用组件仓库配置以及集成了一些其他扩展设置，详细使用见[文档](/iHub)，此外还有一些其他的插件，见`插件集`各插件说明：

```groovy
plugins {
    id 'pub.ihub.plugin'
}
```
> 另外：`ihub-settings`插件不是必须的，如果你的项目不需要多项目配置，可以直接引入`pub.ihub.plugin`插件，注意需要设置版本号（引入`ihub-settings`插件时会[自动配置版本号](/iHubSettings?id=默认版本)），配置如下：

```groovy
plugins {
    id 'pub.ihub.plugin' version '${ihub.plugin.version}'
}
```
