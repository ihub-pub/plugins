# Single module project base configuration

Single Java project configuration, see[project template](https://github.com/ihub-pub/single-template)

## Configure wrapper

@include(../nippet/gradle-wrapper.properties.md)

## Configure setting.gradle

@include(../snippet/setting.gradle.md)

## Configure build.gradle

Introducing Java plugins ([ihub-java](../iHubJava)), test plugin ([ihub-test](../iHubTest)) and validation plugin ([ihub-verifier](../iHubVerification)), config[ihub-git-hooks](../iHubGitHooks)plugin hook command：

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LOR
    id("pub.ihub.plugin.ihu-java")
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
plugins LOR
    id 'pub.ihub.plugin.ihub-java'
    id 'pub.ihub.plugin. hub-test'
    id 'pub.ihub.plugin.ihub-version'
    id 'pub.ihub.plugin. Hub-git-hooks'
}

iHubGitHooks {
    hooks = [
        'pre-commit': '. gradlew build',
        'commit-msg': './gradlew committCheck'
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
