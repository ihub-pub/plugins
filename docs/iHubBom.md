> 用于配置项目依赖组件[默认版本](/iHubBom?id=默认版本)以及兼容性管理。

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.1'
}
```

## 配置示例

```groovy
iHubBom {
    // 导入mavenBom
    importBoms {
        group 'cn.hutool' module 'hutool-bom' version '5.6.5'
    }
    // 配置依赖默认版本
    dependencyVersions {
        group 'cn.hutool' modules 'core', 'aop' version '5.6.5'
    }
    // 配置组版本策略
    groupVersions {
        group 'cn.hutool' version '5.6.5'
    }
    // 排除组件依赖
    excludeModules {
        group 'org.slf4j' modules 'slf4j-api'
        // 支持排除整个组
        group 'pub.ihub'
    }
    // 配置组件依赖
    dependencies {
        api 'cn.hutool:hutool-aop'
        // 支持依赖其他项目模块
//        api ':a', ':b', ':c'
    }
}
```

## 默认版本

> 如下为插件内置常见组件默认版本，可在`gradle.properties`配置中覆盖默认版本，如`ihub-libs.version=1.0.0`。

| Property | Version |
| -------- | ------- |
| `ihub-libs.version` | 1.0.0-SNAPSHOT |
| `spring-boot.version` | 2.5.4 |
| `spring-cloud.version` | 2020.0.3 |
| `spring-statemachine.version` | 3.0.1 |
| `spring-cloud-alibaba.version` | 2021.1 |
| `spring-boot-admin.version` | 2.5.1 |
| `knife4j.version` | 3.0.3 |
| `jaxb.version` | 3.0.2 |
| `okhttp3.version` | 4.9.1 |
| `alibaba-fastjson.version` | 1.2.78 |
| `alibaba-druid.version` | 1.2.6 |
| `alibaba-p3c.version` | 2.1.1 |
| `mybatis-plus.version` | 3.4.3.1 |
| `knife4j-aggregation.version` | 2.0.9 |
| `hutool.version` | 5.7.10 |
| `groovy.version` | 3.0.8 |
| `spock.version` | 2.0-groovy-3.0 |
| `spock-reports.version` | 2.0-groovy-3.0 |