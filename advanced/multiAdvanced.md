# 一主多子项目配置

一个主多子项目配置，参见[项目模板](https://github.com/ihub-pub/multi-template)

## 配置 wrapper

@include(../snippet/gradle-wrapper.properties.md)

## 配置 setting.gradle

`rest`、`service`、`client`为子项目目录，更多配置见[ihub-settings](../iHubSettings)插件：

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-settings' version '1.3.3'
}

iHubSettings {
    includeProjects 'rest', 'service', 'client'
}
```

## 配置 build.gradle

子项目引入Java插件（[ihub-java](../iHubJava)）、测试插件（[ihub-test](../iHubTest)）以及验证插件（[ihub-verification](../iHubVerification)），配置[ihub-git-hooks](../iHubGitHooks)插件钩子命令：

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

## 配置 gradle.properties

配置项目名称以及group，其中`name`为[ihub-settings](../iHubSettings)插件[扩展属性](../iHubSettings#扩展属性)，`group`为原生项目属性

```properties
name=demo
group=pub.ihub.demo
```