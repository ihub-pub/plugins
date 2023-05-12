---
title: Configuration Description
icon: Notes
---

## Attribute profile description

> Plugin Configuration Properties get currently supported 4 ways：extension properties -`Ext`, Project properties -`Prj`, System properties -`Sys`, Environment properties -`Env`, Sexual priority：`Sys` > `Env` > `Ext` > `Prj`

### `Ext`(Extension)
> Plugin custom extension attributes, attached to`build.gradle`file configuration, see[samples](https://github.com/ihub-pub/plugins/tree/main/samples)

### `Prj`(Project)
> Project properties, set to`gradle.properties`file, configuration format`extension`.`property name,`, e.g.`iHub.mavenLocalEnabled=true`; if you want to pass on attributes through the command line, you can use`-P`parameters, e.g.`-PiHub.mavenLocalEnabled=true`

### `Systems`(System)
> 系统属性，如命令行传递的信息等，配置格式`扩展名`.`属性名`，如`-DiHub.mavenLocalEnabled=true`

### `Env`(Environment)
> Environment variable properties, config format for all capitals, multiple words, separated by`_`eg.`MAVEN_LOCAL_ENABLED=true`

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