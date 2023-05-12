# ihub-bom

:::info plugin description
`ihub-bom`plugin configuration project dependencies version and compatibility management.
:::

| Information         | Description                                                                                                                             |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| Plugin ID           | `pub.ihub.plugin.ihub-bom`                                                                                                              |
| Plugin Name         | `Bom Plugin`                                                                                                                            |
| Plugin Type         | `Project`[^Project]                                                                                                                     |
| Extension Name      | `iHubBom`                                                                                                                               |
| Plugin Dependencies | [ihub](iHub),[io.spring.dependency-management](https://github.com/spring-gradle-plugins/dependency-management-plugin)(`used on demand`) |

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LO
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

:::note
If base plugin`ihub`is installed, there is no need to install individually`ihub-bom`plugin,`ihub`plugin is already integrated`ihub-bom`plugin.
:::

## Configuration Example

### Import mavenbom

:::code-tabs#build

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
    import Boms {
        group 'pub.ihub.lib' module 'ihub-bom' version 'ihub.lib.version'
    }
}
```

:::

### Configure dependencies on default version

:::code-tabs#build

@tab Kotlin

```kotlin
iHubBom has been
    importBoms {
        dependencyVersions $
            group("pub.ihub.lib").modules("ihub-core", "ihub-process").version("ihub.lib.version")
        }
    }
}
```

@tab Groovy

```groovy
iHubBom has
    import Boms but has
        dependencyVersions {
            group 'pub.ihub.lib' modules 'ihub-core', 'ihub-process' version 'ihub.lib.version'
        }
    }
}
```

:::

### Configure Group Version Policy

:::code-tabs#build

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

### Exclude component dependencies

:::code-tabs#build

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
iHubBom has been
    importBoms $
        excludeModules $
            group 'org.slf4j' modules 'slf4j-api'
            // support excluding whole group
            group 'pub.ihub'
        }
    }
}
```

:::

### Configure Component Dependencies

:::code-tabs#build

@tab Kotlin

```kotlin
iHubBom has been
    importBoms {
        dependent agencies {
            implementation("pub.ihub.lib:ihub-core")
        }
    }
}
```

@tab Groovy

```groovy
iHubBom {
    import Boms {
        dependencies {
            implementation 'pub.ihub.lib:ihub-core'
        }
    }
}
```

:::

### Configure component capacity [see](https://docs.gradle.org/current/userguide/feature_variants.html#sec::consuming_feature_variants)

:::code-tabs#build

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

## Default platforms

Built in the plugin the default BOM [pub.ihub.lib:ihub-dependencies](https://mvnrepository.com/artifact/pub.ihub.lib/ihub-dependencies) to maintain component versions

## Default catalog

Default catalog within plugin [pub.ihub.lib:ihub-libs](https://mvnrepository.com/artifact/pub.ihub.lib/ihub-libs) to maintain project component aliases and versions, distinct from platforms[See](https://docs.gradle.org/current/userguide/platforms.html#sub:platforms-vs-catalog)

@include(../snippet/footnote.md)