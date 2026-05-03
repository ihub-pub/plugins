# ihub-meta

::: info 插件信息
- `ihub-meta`插件用于生成项目结构元数据 JSON 文件，供 LLM/AI 开发工具读取并理解项目结构。
- 当项目应用此插件后，运行 `./gradlew iHubMeta` 即可在 `build/ihub/project-meta.json` 生成项目元数据。
:::

| 插件ID | 插件名称 | 插件类型 | 扩展名称 |
|-------|---------|--------|---------|
| `pub.ihub.plugin.ihub-meta` | `IHub Meta插件` | `Project`[^Project] | `iHubMeta` |

::: tip 输出内容
生成的 `project-meta.json` 包含以下信息：
- **project** — 项目名称、group、版本、描述、根目录
- **gradle** — Gradle 版本、已应用插件列表
- **sourceSets** — 源码目录结构（main/test，支持 Java/Groovy/Kotlin/Resources）
- **dependencies** — 依赖信息（implementation、testImplementation 等）
- **modules** — 子项目/模块列表
:::

## 快速开始

::: tip 推荐：通过版本目录别名引用
当项目已应用 `pub.ihub.plugin.ihub-settings` 后，IHub 会自动注册 `ihub` 版本目录，所有插件均可通过 `ihub.plugins.*` 别名引用，无需手动指定版本：

```kotlin
// settings.gradle.kts
plugins {
    id("pub.ihub.plugin.ihub-settings") version "1.9.5"
}

// build.gradle.kts
plugins {
    alias(ihub.plugins.meta)  // 等价于 id("pub.ihub.plugin.ihub-meta")
}
```

版本目录别名规则：`pub.ihub.plugin.ihub-<name>` → `ihub.plugins.<name>`（例：`ihub-meta` → `ihub.plugins.meta`）。
:::

若不使用版本目录，也可直接声明插件 ID：

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-meta") version "1.9.5"
}
```

应用插件后，运行 `./gradlew iHubMeta` 生成元数据 JSON。

## 扩展属性

| Extension | Description | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| --------- | ----------- | ------- | --- | --- | --- | --- |
| `enabled` | 是否启用元数据生成 | `true` | ✔ | ✔ | ✔ | ❌ |
| `outputFile` | JSON 输出文件路径 | `{buildDir}/ihub/project-meta.json` | ✔ | ❌ | ❌ | ❌ |
| `includeDependencies` | 是否包含依赖信息 | `true` | ✔ | ✔ | ✔ | ❌ |
| `includeSourceSets` | 是否包含源码目录信息 | `true` | ✔ | ✔ | ✔ | ❌ |

## 示例配置

### 禁用依赖信息收集

```kotlin
iHubMeta {
    includeDependencies.set(false)
}
```

### 自定义输出路径

```kotlin
iHubMeta {
    outputFile.set(layout.buildDirectory.file("custom/ai-meta.json"))
}
```

### 完全禁用

```kotlin
iHubMeta {
    enabled.set(false)
}
```

[^Project]: Project插件
[^Ext]: 支持扩展属性配置
[^Prj]: 支持项目属性配置（`gradle.properties`）
[^Sys]: 支持系统属性配置（`-DiHub.xxx=value`）
[^Env]: 支持环境变量配置
