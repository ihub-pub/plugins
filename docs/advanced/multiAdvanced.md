> 一个主多子项目配置，参见[项目模板](https://github.com/ihub-pub/multi-template)

## 配置 wrapper

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-7.5.1-bin.zip
```

## 配置 setting.gradle

> `rest`、`service`、`client`为子项目目录，更多配置见[ihub-settings](/iHubSettings)插件：

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-settings' version '${ihub.plugin.version}'
}

iHubSettings {
    includeProjects 'rest', 'service', 'client'
}
```

## 配置 build.gradle

> 子项目引入Java插件（[ihub-java](/iHubJava)）、测试插件（[ihub-test](/iHubTest)）以及验证插件（[ihub-verification](/iHubVerification)），配置[ihub-git-hooks](/iHubGitHooks)插件钩子命令：

```groovy
plugins {
    id 'pub.ihub.plugin'
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

> 配置项目名称以及group，其中`name`为[ihub-settings](/iHubSettings)插件[扩展属性](/iHubSettings?id=扩展属性)，`group`为原生项目属性

```properties
name=demo
group=pub.ihub.demo
```