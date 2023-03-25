# 单模块项目基础配置

单一Java项目配置，参见[项目模板](https://github.com/ihub-pub/single-template)

## 配置 wrapper

@include(../snippet/gradle-wrapper.properties.md)

## 配置 setting.gradle

@include(../snippet/setting.gradle.md)

## 配置 build.gradle

引入Java插件（[ihub-java](../iHubJava)）、测试插件（[ihub-test](../iHubTest)）以及验证插件（[ihub-verification](../iHubVerification)），配置[ihub-git-hooks](../iHubGitHooks)插件钩子命令：

::: code-tabs#build

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-java'
    id 'pub.ihub.plugin.ihub-test'
    id 'pub.ihub.plugin.ihub-verification'
    id 'pub.ihub.plugin.ihub-git-hooks'
}

iHubGitHooks {
    hooks = [
        'pre-commit': './gradlew build',
        'commit-msg': './gradlew commitCheck'
    ]
}
```

@tab Kotlin

```kotlin
plugins {
    id ("pub.ihub.plugin.ihub-java")
    id ("pub.ihub.plugin.ihub-test")
    id ("pub.ihub.plugin.ihub-verification")
    id ("pub.ihub.plugin.ihub-git-hooks")
}

iHubGitHooks {
    hooks = mapOf(
        "pre-commit" to "./gradlew build",
        "commit-msg" to "./gradlew commitCheck"
    )
}
```

:::

## 配置 gradle.properties

配置项目名称以及group，其中`name`为[ihub-settings](../iHubSettings)插件[扩展属性](../iHubSettings#扩展属性)，`group`为原生项目属性

```properties
name=demo
group=pub.ihub.demo
```
