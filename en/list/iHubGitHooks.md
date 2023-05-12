# ihub-git-hooks

::: info 插件信息
- `ihub-git-hooks`插件用于配置GitHooks，可以为`git`操作配置一些钩子命令，比如：在提交代码的时候可以做一些代码检查。
- 通过修改git-hooks目录实现自定义hook命令（`git config core.hooksPath xxx`），不破坏原有hooks。
:::

| 插件ID                             | 插件名称         | 插件类型                | 扩展名称           |
| -------------------------------- | ------------ | ------------------- | -------------- |
| `pub.ihub.plugin.ihub-git-hooks` | `GitHooks插件` | `Project`[^Project] | `iHubGitHooks` |

::: tip 提交信息检查
插件基于[约定式提交](https://www.conventionalcommits.org/)规范提供了`commit-msg`检查提交信息功能，详见[功能](https://github.com/ihub-pub/plugins/issues/247)，提交信息规范如下：
```text
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```
:::

插件提供一个检查任务`commitCheck`，用于检查提交信息是否符合规范，使用如下：

```shell
#!/bin/bash
./gradlew commitCheck
```

也可以通过扩展属性配置，见[示例](#扩展配置git-hooks命令)

::: tip
IDEA环境下支持自动生成[`Conventional Commit`](https://plugins.jetbrains.com/plugin/13389-conventional-commit)IDEA插件配置文件`conventionalCommit.json`，并且完成自动配置
:::

## 扩展属性

| Extension          | Description      | Default      | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| ------------------ | ---------------- | ------------ | --------- | --------- | --------- | --------- |
| `hooksPath`        | 自定义hooks路径（优先级高） | ❌            | ✔         | ✔         | ✔         | ❌         |
| `hooks`            | 自定义hooks         | ❌            | ✔         | ❌         | ❌         | ❌         |
| `descriptionRegex` | 提交描述正则表达式        | `/.{1,100}/` | ✔         | ❌         | ❌         | ❌         |

::: note
如果两个hooks属性都不配置，会使用默认hooks目录
:::

### DSL扩展配置支持如下

| 扩展方法      | 扩展描述                       |
| --------- | -------------------------- |
| `types`   | 添加提交类型                     |
| `type`    | 添加单个提交类型，可详细配置`type`扩展属性   |
| `footers` | 添加注脚类型                     |
| `footer`  | 添加单个注脚类型，可详细配置`footer`扩展属性 |

### `type`扩展属性

| 扩展方法            | 扩展描述                            |
| --------------- | ------------------------------- |
| `scopes`        | 添加作用域                           |
| `scope`         | 添加单个作用域，可详细配置作用域`description`属性 |
| `requiredScope` | 配置是否启用作用域检查，默认`false`           |
| `description`   | 提交类型描述                          |

### `footer`扩展属性

| 扩展方法               | 扩展描述               |
| ------------------ | ------------------ |
| `required`         | 配置注脚是否必填，默认`false` |
| `requiredWithType` | 配置注脚是否在特定提交类型时必填   |
| `valueRegex`       | 注脚值正则校验            |
| `description`      | 注脚描述               |

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-git-hooks")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-git-hooks'
}
```

:::

## 自定义hooks路径使用示例

配置自定义hooks路径，并在自定义路径下添加相关hooks配置

```properties
iHubGitHooks.hooksPath=.hooks
```

## 插件扩展配置使用示例

### 扩展配置git-hooks命令

相关hooks命令会配置在`.gradle/pub.ihub.plugin.hooks`目录下

::: code-tabs#build

@tab Kotlin

```kotlin
iHubGitHooks {
    hooks.set(mapOf(
        "pre-commit" to "./gradlew build",
        "commit-msg" to "./gradlew commitCheck"
    ))
}
```

@tab Groovy

```groovy
iHubGitHooks {
    hooks = [
        'pre-commit': './gradlew build',
        'commit-msg': './gradlew commitCheck'
    ]
}
```

:::

### 扩展配置提交信息检查

::: code-tabs#build

@tab Kotlin

```kotlin
iHubGitHooks {
    // 添加提交类型
    types("type1", "type2", "type3")
    // 开启范围检查
    type("build").scopes("gradle").requiredScope(true)
    // Footer必填
    footer("Footer").required(true)
    // 提交类型是feat时Footer必填
    footer("Footer").requiredWithType("feat")
    // 注解值正则校验
    footer("Closes").valueRegex("\\d+")
    // 描述配置1
    type("type").scope("scope").description("Scope description")
    footer("Other").description("Other description")
}
```

@tab Groovy

```groovy
iHubGitHooks {
    // 添加提交类型
    types 'type1', 'type2', 'type3'
    // 开启范围检查
    type 'build' scopes 'gradle' requiredScope true
    // Footer必填
    footer 'Footer' required true
    // 提交类型是feat时Footer必填
    footer 'Footer' requiredWithType 'feat'
    // 注解值正则校验
    footer 'Closes' valueRegex '\\d+'
    // 描述配置1
    type 'type' scope 'scope' description 'Scope description'
    footer 'Other' description 'Other description'
}
```

:::

@include(../snippet/explanation.md)