> `ihub-bom`插件用于配置项目依赖组件版本以及兼容性管理。

| 插件ID | 插件名称 | 插件类型 | 扩展名称 | 插件依赖 |
|-------|---------|--------|---------|--------|
| `pub.ihub.plugin.ihub-bom` | `Bom插件` | `Project` | `iHubBom` | [ihub](iHub)、[io.spring.dependency-management](https://github.com/spring-gradle-plugins/dependency-management-plugin)（`按需使用`） |

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin' version '${ihub.plugin.version}'
}
```

## 配置示例

```groovy
iHubBom {
    // 导入mavenBom
    importBoms {
        group 'pub.ihub.lib' module 'ihub-bom' version '${ihub.lib.version}'
    }
    // 配置依赖默认版本
    dependencyVersions {
        group 'pub.ihub.lib' modules 'ihub-core', 'ihub-process' version '${ihub.lib.version}'
    }
    // 配置组版本策略
    groupVersions {
        group 'pub.ihub.lib' version '${ihub.lib.version}'
    }
    // 排除组件依赖
    excludeModules {
        group 'org.slf4j' modules 'slf4j-api'
        // 支持排除整个组
        group 'pub.ihub'
    }
    // 配置组件依赖
    dependencies {
        api 'pub.ihub.lib:ihub-core'
        // 支持依赖其他项目模块
//        api ':a', ':b', ':c'
    }
    // 配置组件能力，参见 https://docs.gradle.org/current/userguide/feature_variants.html#sec::consuming_feature_variants
    capabilities {
        // 支持单个组件(org.slf4j:slf4j-ext)、按组(org.slf4j)、按模块名(slf4j-ext)进行配置
        requireCapability 'org.slf4j:slf4j-ext', 'org.javassist:javassist'
        // 能力可以省略组，默认同组件
        requireCapability 'org.springframework.cloud:spring-cloud-starter-openfeign', 'spring-cloud-starter-loadbalancer'
    }
}
```

## 默认BOM

> 插件内置默认BOM配置，配置组件版本[详见](https://mvnrepository.com/artifact/pub.ihub.lib)。

```groovy
importBoms {
    group 'pub.ihub.lib' module 'ihub-libs' version '${ihub.lib.version}'
}
```