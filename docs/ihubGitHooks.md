> `ihub-git-hooks`插件用于配置GitHooks，可以为`git`操作配置一些钩子命令，比如：在提交代码的时候可以做一些代码检查。

| 插件ID | 插件名称 | 插件类型 | 扩展名称 |
|-------|---------|--------|---------|
| `pub.ihub.plugin.ihub-git-hooks` | `GitHooks插件` | `Project` | `iHubGitHooks` |

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

## 配置示例

```groovy
iHubGitHooks {
    hooks = [
        'pre-commit': './gradlew build'
    ]
}
```