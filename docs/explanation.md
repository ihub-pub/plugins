# 属性配置说明

> 插件配置属性获取目前支持4种方式：`扩展属性`（`Ext`）、`项目属性`（`Prj`）、`系统属性`（`Sys`）、`环境属性`（`Env`）， 属性优先级：`Sys` > `Env` > `Prj` > `Ext`
> - `Ext`（Extension）：插件自定义扩展属性，配置于`build.gradle`文件，配置方式详见[samples](https://github.com/ihub-pub/plugins/tree/main/samples)
> - `Prj`（Project）：项目属性，配置于`gradle.properties`文件，配置格式`扩展名`.`属性名`，如`iHub.mavenLocalEnabled=true`
> - `Sys`（System）：系统属性，如命令行传递的信息等，配置格式`扩展名`.`属性名`，如`-DiHub.mavenLocalEnabled=true`
> - `Env`（Environment）：环境变量属性，配置格式全部大写，多个单词，用`_`分隔，如`MAVEN_LOCAL_ENABLED=true`

# 系统属性扩展

> 可以给应用程序添加系统属性配置。

## runProperties

> 任务运行时属性：用于配置运行时属性，配置如下：

```groovy
iHubBoot {
    runProperties = [
        'spring.profiles.active': 'dev'
    ]
}
```

## runIncludePropNames

> 运行时包含系统属性名称（`,`分割，支持通配符`*`）：用于配置指定系统属性，与[runProperties](/explanation?id=runproperties)互斥，配置如下：

```groovy
iHubBoot {
    runIncludePropNames = 'xxx,abc*'
}
```

## runSkippedPropNames

> 运行时排除系统属性名称（`,`分割，支持通配符`*`）：用于排除系统属性，配置如下：

```groovy
iHubBoot {
    runSkippedPropNames = 'xxx,abc*'
}
```

## enabledLocalProperties

> 本地属性：可在项目根目录配置`.java-local.properties`属性文件，启用属性后会将属性文件中的配置添加的系统配置，配置如下：

`gradle.properties`配置

```groovy
iHubBoot {
    enabledLocalProperties = true
}
```

`.java-local.properties`配置

```properties
spring.profiles.active=dev
```

另外属性文件支持`.boot-java-local.properties`和`.test-java-local.properties`，分别用于扩展`bootRun`和`test`属性，优先级高于`.java-local.properties`