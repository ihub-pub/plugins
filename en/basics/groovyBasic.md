# Groovy Project Base Configuration

Groovy Project configuration, see[Project Template](https://github.com/ihub-pub/groovy-template)

## Configure wrapper

@include(../nippet/gradle-wrapper.properties.md)

## Configure setting.gradle

@include(../snippet/setting.gradle.md)

## Configure build.gradle

Introducing Groovy plugin ([ihu-groovy](../iHubGroovy)), test plugin ([ihub-test](../iHubTest)) and validation plugin ([ihub-certification](../iHubVerification)), config[ihub-git-hooks](../iHubGitHooks)plugin hook command：

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LOR
    id("pub.ihub.plugin.ihu-groovy")
    id("pub.ihub.plugin. Hub-test")
    id("pub.ihub.plugin.ihub-version")
    id("pub.ihub.plugin. Hub-git-hooks")
}

iHubGitHooks {
    hooks.set(mapOf(
        "precommit" to ". gradlew build",
        "commit-msg" to "./gradlew committCheck"
    )
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-groovy'
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

:::

## Configure gradle.properties

Configure project names and groups, where`name`is[ihub-settings](../iHubSettings)plugin[extension properties](../iHubSettings#扩展属性),`group`native project properties

```properties
name=demo
group=pub.ihub.demo
```
