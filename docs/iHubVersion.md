> `ihub-version`插件是版本插件，集成并加强了第三方version插件，用于设置项目版本。

| 插件ID | 插件名称 | 插件类型 | 扩展名称 | 插件依赖                                                                                                                                                                                                                                    |
|-------|---------|--------|---------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `pub.ihub.plugin.ihub-version` | `版本插件` | `Project` | `iHubVersion` | [io.freefair.git-version](https://plugins.gradle.org/plugin/io.freefair.git-version)、[com.github.ben-manes.versions](https://plugins.gradle.org/plugin/com.github.ben-manes.versions) |

> 插件功能

| | 功能描述 |
|--|--|
| 1 | 引入`git-version`插件自动配置项目版本，`增强支持推断版本号` |
| 2 | 引入`ben-manes.versions`插件用于检查组件版本号，`增强支持自动替换最新版本` |

## 扩展属性

> 属性使用说明[详见](/explanation?id=属性配置说明)

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `autoReplaceLaterVersions` | 自动替换最新版本（[versions](https://plugins.gradle.org/plugin/com.github.ben-manes.versions)插件增强） | `false` | ✔ | ✔ | ✔ | ❌ |
| `useInferringVersion` | 使用推断版本号，根据最新`git tag`推断下一个版本号，支持tag格式`{major}.{minor}.{patch}`或`v{major}.{minor}.{patch}`，推断方式`patch + 1` | `false` | ✔ | ✔ | ✔ | ✔ |

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-version' version '${ihub.plugin.version}'
}
```

## 配置示例

```groovy
iHub {
    autoReplaceLaterVersions = true
}
```
