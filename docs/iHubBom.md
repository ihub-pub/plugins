> `ihub-bom`插件用于配置项目依赖组件版本以及兼容性管理。

| 插件ID | 插件名称 | 插件类型 | 扩展名称 | 插件依赖 |
|-------|---------|--------|---------|--------|
| `pub.ihub.plugin.ihub-bom` | `Bom插件` | `Project` | `iHubBom` | [ihub](iHub) |

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
        group 'cn.hutool' module 'hutool-bom' version '5.6.5'
        // 强制版本
        group 'cn.hutool' module 'hutool-bom' version '5.6.5' enforced true
    }
    // 配置依赖默认版本
    dependencyVersions {
        group 'cn.hutool' modules 'core', 'aop' version '5.6.5'
        // 强制版本
        group 'org.codehaus.groovy' modules 'groovy-xml' version '2.5.14' enforced true
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

> 默认`编译`依赖`hutool-all`组件，便于编码时使用相关工具，如果实际需要相关组件，添加`运行时`依赖即可，组件[详见](https://www.hutool.cn/docs)。

```groovy
dependencies {
    compileOnly 'cn.hutool:hutool-all'
    runtimeOnly 'cn.hutool:hutool-core'
}
```