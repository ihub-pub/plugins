# ihub-bom

::: info 插件说明
`ihub-bom`插件用于配置项目依赖组件版本以及兼容性管理。
:::

| 信息   | 描述                                                                                                                            |
| ---- | ----------------------------------------------------------------------------------------------------------------------------- |
| 插件ID | `pub.ihub.plugin.ihub-bom`                                                                                                    |
| 插件名称 | `Bom插件`                                                                                                                       |
| 插件类型 | `Project`[^Project]                                                                                                           |
| 扩展名称 | `iHubBom`                                                                                                                     |
| 插件依赖 | [ihub](iHub)、[io.spring.dependency-management](https://github.com/spring-gradle-plugins/dependency-management-plugin)（`按需使用`） |

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-bom")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-bom'
}
```

:::

::: note
如果已经安装基础插件`ihub`，则无需单独安装`ihub-bom`插件，`ihub`插件已经集成了`ihub-bom`插件。
:::

## 配置示例

### 导入mavenBom

::: code-tabs#build

@tab Kotlin

```kotlin
iHubBom {
    importBoms {
        group("pub.ihub.lib").module("ihub-bom").version("ihub.lib.version")
    }
}
```

@tab Groovy

```groovy
iHubBom {
    importBoms {
        group 'pub.ihub.lib' module 'ihub-bom' version 'ihub.lib.version'
    }
}
```

:::

### 配置依赖默认版本

::: code-tabs#build

@tab Kotlin

```kotlin
iHubBom {
    importBoms {
        dependencyVersions {
            group("pub.ihub.lib").modules("ihub-core", "ihub-process").version("ihub.lib.version")
        }
    }
}
```

@tab Groovy

```groovy
iHubBom {
    importBoms {
        dependencyVersions {
            group 'pub.ihub.lib' modules 'ihub-core', 'ihub-process' version 'ihub.lib.version'
        }
    }
}
```

:::

### 配置组版本策略

::: code-tabs#build

@tab Kotlin

```kotlin
iHubBom {
    importBoms {
        groupVersions {
            group("pub.ihub.lib").version("ihub.lib.version")
        }
    }
}
```

@tab Groovy

```groovy
iHubBom {
    importBoms {
        groupVersions {
            group 'pub.ihub.lib' version 'ihub.lib.version'
        }
    }
}
```

:::

### 排除组件依赖

::: code-tabs#build

@tab Kotlin

```kotlin
iHubBom {
    importBoms {
        excludeModules {
            group("org.slf4j").modules("slf4j-api")
            // 支持排除整个组
            group("pub.ihub")
        }
    }
}
```

@tab Groovy

```groovy
iHubBom {
    importBoms {
        excludeModules {
            group 'org.slf4j' modules 'slf4j-api'
            // 支持排除整个组
            group 'pub.ihub'
        }
    }
}
```

:::

### 配置组件依赖

::: code-tabs#build

@tab Kotlin

```kotlin
iHubBom {
    importBoms {
        dependencies {
            implementation("pub.ihub.lib:ihub-core")
        }
    }
}
```

@tab Groovy

```groovy
iHubBom {
    importBoms {
        dependencies {
            implementation 'pub.ihub.lib:ihub-core'
        }
    }
}
```

:::

### 配置组件能力 [参见](https://docs.gradle.org/current/userguide/feature_variants.html#sec::consuming_feature_variants)

::: code-tabs#build

@tab Kotlin

```kotlin
iHubBom {
    importBoms {
        capabilities {
            // 支持单个组件(org.slf4j:slf4j-ext)、按组(org.slf4j)、按模块名(slf4j-ext)进行配置
            requireCapability("org.slf4j:slf4j-ext", "org.javassist:javassist")
            // 能力可以省略组，默认同组件
            requireCapability(
                "org.springframework.cloud:spring-cloud-starter-openfeign",
                "spring-cloud-starter-loadbalancer"
            )
        }
    }
}
```

@tab Groovy

```groovy
iHubBom {
    importBoms {
        capabilities {
            // 支持单个组件(org.slf4j:slf4j-ext)、按组(org.slf4j)、按模块名(slf4j-ext)进行配置
            requireCapability 'org.slf4j:slf4j-ext', 'org.javassist:javassist'
            // 能力可以省略组，默认同组件
            requireCapability 'org.springframework.cloud:spring-cloud-starter-openfeign', 'spring-cloud-starter-loadbalancer'
        }
    }
}
```

:::

## 默认platforms

插件内置默认BOM [pub.ihub.lib:ihub-dependencies](https://mvnrepository.com/artifact/pub.ihub.lib/ihub-dependencies) ，用于维护组件版本

@include(../snippet/explanation.md)

## 默认catalog

插件内置默认catalog [pub.ihub.lib:ihub-libs](https://mvnrepository.com/artifact/pub.ihub.lib/ihub-libs) ，用于维护项目组件别名与版本，与platforms区别[详见](https://docs.gradle.org/current/userguide/platforms.html#sub:platforms-vs-catalog)