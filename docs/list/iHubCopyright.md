# ihub-copyright

::: info 插件说明
`ihub-copyright`插件是版权插件，基于[Spotless](https://github.com/diffplug/spotless)实现自动为源代码添加或更新版权信息。支持Java、Groovy、Kotlin等语言，可通过Gradle任务自动执行，并可集成到Git Hooks中。
:::

| 信息 | 描述 |
|------|------|
| 插件ID | `pub.ihub.plugin.ihub-copyright` |
| 插件名称 | `版权插件` |
| 插件类型 | `Project`[^Project] |
| 扩展名称 | `iHubCopyright` |
| 插件依赖 | [ihub-base](iHub) |

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-copyright")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-copyright'
}
```

:::

## 配置示例

### 版权配置文件

1. 通过`COPYRIGHT`文件配置版权信息，文件放置在项目根目录下：

```text
Copyright (c) 2025 the original author or authors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0
    
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

2. 通过`LICENSE`文件提取版权信息，目前支持识别：
   - `Apache License`
   - `MIT License`

### 扩展配置

::: code-tabs#build

@tab Kotlin

```kotlin
iHubCopyright {
    // 是否启用IDEA版权配置（默认：true）
    enableIdea.set(true)
}
```

@tab Groovy

```groovy
iHubCopyright {
    // 是否启用IDEA版权配置（默认：true）
    enableIdea = true
}
```

:::

## 可用任务

插件基于Spotless提供以下任务：

| 任务 | 说明 |
|-----|-----|
| `spotlessApply` | 自动为源代码添加/更新版权头 |
| `spotlessCheck` | 检查源代码是否包含正确的版权头 |

## 集成Git Hooks

当与[iHubGitHooks](iHubGitHooks)插件配合使用时，可以在提交代码前自动执行版权检查：

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-copyright")
    id("pub.ihub.plugin.ihub-git-hooks")
}

iHubGitHooks {
    hooks.set(mapOf(
        "pre-commit" to "./gradlew spotlessCheck"
    ))
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-copyright'
    id 'pub.ihub.plugin.ihub-git-hooks'
}

iHubGitHooks {
    hooks = [
        'pre-commit': './gradlew spotlessCheck'
    ]
}
```

:::

## 支持的语言

- Java
- Groovy
- Kotlin

自动排除`**/generated/**`目录下的生成代码。

@include(../snippet/footnote.md)
