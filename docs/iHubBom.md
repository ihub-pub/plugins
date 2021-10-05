> 用于配置项目依赖组件[默认版本](/iHubBom?id=默认版本)以及兼容性管理。

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.5'
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

## 默认BOM

> 插件内置默认BOM配置，配置组件版本[详见](https://mvnrepository.com/artifact/pub.ihub.lib)。

```groovy
importBoms {
    group 'pub.ihub.lib' module 'ihub-bom' version '1.0.0'
}
```