# A master multi-subproject configuration

A primary multi-subproject configuration, see[project template](https://github.com/ihub-pub/multi-template)

## Configure wrapper

@include(../snippet/gradle-wrapper.properties.md)

## Configure setting.gradle

`rest`,`service`,`client`for subproject directories, more configuration see[ihub-settings](../iHubSettings)plugin：

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-settings' version '1.4.1'
}

iHubSettings {
    includeProjects 'rest', 'service', 'client'
}
```

## Configure build.gradle

Subprojects introduce Java plugins ([ihub-java](../iHubJava)), test plugin ([ihub-test](../iHubTest)) and validation plugin ([ihub-certification](../iHubVerification)), config[ihub-git-hooks](../iHubGitHooks)plugin hook command：

```groovy
plugins {
    id 'pub.ihub.plugin'
    id 'pub.ihub.plugin.ihub-git-hooks'
    id 'pub.ihub.plugin.ihub-java' apply false
    id 'pub.ihub.plugin.ihub-test' apply false
    id 'pub.ihub.plugin.ihub-verification' apply false
    id 'pub.ihub.plugin.ihub-publish' apply false
    id 'pub.ihub.plugin.ihub-boot' apply false
}

subprojects {
    apply {
        plugin 'pub.ihub.plugin.ihub-java'
        plugin 'pub.ihub.plugin.ihub-test'
        plugin 'pub.ihub.plugin.ihub-verification'
    }

    dependencies {
        testImplementation 'org.springframework.boot:spring-boot-starter-test'
    }
}

iHubGitHooks {
    hooks = [
        'pre-commit': './gradlew build',
        'commit-msg': './gradlew commitCheck'
    ]
}
```

## Configure gradle.properties

Configure project names and groups, where`name`is[ihub-settings](../iHubSettings)plugin[extension properties](../iHubSettings#扩展属性),`group`native project properties

```properties
name=demo
group=pub.ihub.demo
```