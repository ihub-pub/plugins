> `ihub-git-hooks`插件用于配置GitHooks，可以为`git`操作配置一些钩子命令，比如：在提交代码的时候可以做一些代码检查。
> 通过修改git-hooks目录实现自定义hook命令（`git config core.hooksPath xxx`），不破坏原有hooks。

| 插件ID | 插件名称 | 插件类型 | 扩展名称 |
|-------|---------|--------|---------|
| `pub.ihub.plugin.ihub-git-hooks` | `GitHooks插件` | `Project` | `iHubGitHooks` |

## 扩展属性

> 属性使用说明[详见](/explanation?id=属性配置说明)

| Extension | Description      | Default | Ext | Prj | Sys | Env |
| --------- |------------------| ----- | --- | ------- | ------ | --- |
| `hooksPath` | 自定义hooks路径（优先级高） | ❌ | ✔ | ✔ | ✔ | ❌ |
| `hooks` | 自定义hooks         | ❌ | ✔ | ❌ | ❌ | ❌ |

**注：如果两个属性都不配置，会使用默认hooks目录**

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-git-hooks' version '${ihub.plugin.version}'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '${ihub.plugin.version}'
}

apply {
    plugin 'pub.ihub.plugin.ihub-git-hooks'
}
```

## 自定义hooks路径使用示例

> 配置自定义hooks路径，并在自定义路径下添加相关hooks配置

```properties
iHubGitHooks.hooksPath=.hooks
```

## 插件扩展配置使用示例

> 通过插件扩展配置，相关hooks命令会配置在`.gradle/pub.ihub.plugin.hooks`目录下

```groovy
iHubGitHooks {
    hooks = [
        'pre-commit': './gradlew build'
    ]
}
```