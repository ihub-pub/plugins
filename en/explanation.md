---
title: Configuration Description
icon: Notes
---

## Attribute profile description

> 插件配置属性获取目前支持4种方式：`扩展属性`（`Ext`）、`项目属性`（`Prj`）、`系统属性`（`Sys`）、`环境属性`（`Env`）， 属性优先级：`Sys` > `Env` > `Ext` > `Prj` - `Ext`（Extension）：插件自定义扩展属性，配置于`build.gradle`文件，配置方式详见[samples](https://github.com/ihub-pub/plugins/tree/main/samples) - `Prj`（Project）：项目属性，配置于`gradle.properties`文件，配置格式`扩展名`.`属性名`，如`iHub.mavenLocalEnabled=true`；如果需要通过命令行传递属性，可以使用`-P`参数，如`-PiHub.mavenLocalEnabled=true` - `Sys`（System）：系统属性，如命令行传递的信息等，配置格式`扩展名`.`属性名`，如`-DiHub.mavenLocalEnabled=true` - `Env`（Environment）：环境变量属性，配置格式全部大写，多个单词，用`_`分隔，如`MAVEN_LOCAL_ENABLED=true`

## System Attribute Extension

> You can add system properties configuration to the application.

### runProperties

> Attribute：on task runtime is configured for running properties, following：

```groovy
iHubBoot {
    runProperties = [
        'spring.profiles.active': 'dev'
    ]
}
```

### runIncludePropNames

> Running with system property name (``separated, supported wildcard`*`)：used to configure the specified system properties, and[runProperties](explanation#runproperties)mutually exclusive configured, following：

```groovy
iHubBoot {
    runIncludePropNames = 'xxx,abc*'
}
```

### runSkippedPropNames

> Exclude system property name on runtime (`,`separated, supported wildcard`*`)：used to exclude system properties, configuration below：

```groovy
iHubBoot {
    runSkippedPropNames = 'xxx,abc*'
}
```

### EnabledLocalProperties

> Local properties：can be configured at the root of the project`.java-local.properties`Attribute files, enabling the properties to add the configuration in the property file as follows：

`gradle.properties`configuration

```groovy
iHubBoot {
    enabledLocalProperties = true
}
```

`.java-local.properties`Configuration

```properties
spring.profiles.active=dev
```

Another property file supports`.boot-java-local.properties`and`.test-java-local.properties`, extension`bootRun`and`test`attributes, priority is higher than`.java-local.properties`