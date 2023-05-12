# ihub-copyright

::: info 插件说明
`ihub-copyright`插件是版权插件，用于自动配置`IDEA`版权设置，自动伪代码添加或更新版权信息。
:::

| 插件ID                             | 插件名称   | 插件类型                | 插件依赖         |
| -------------------------------- | ------ | ------------------- | ------------ |
| `pub.ihub.plugin.ihub-copyright` | `版权插件` | `Project`[^Project] | [ihub](iHub) |

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

或

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin")
}

apply {
    plugin("pub.ihub.plugin.ihub-copyright")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin'
}

apply {
    plugin 'pub.ihub.plugin.ihub-copyright'
}
```

:::

## 配置示例

1. 通过`COPYRIGHT`文件获取版权信息，文件配置项目根目录下

```text
Copyright (c) 2023 the original author or authors.

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

2. 通过`LICENSE`文件提取版权信息，目前会识别`Apache License`、`MIT License`配置通用版权信息，其他版权会使用IDEA默认版权配置

@include(../snippet/explanation.md)