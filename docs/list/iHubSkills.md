# ihub-skills

::: info 插件信息
- `ihub-skills`插件用于自动安装 IHub AI 技能文件，使 AI 开发工具能够感知 IHub Plugins 的使用规范和最佳实践。
- 当项目应用此插件后，会在构建时自动向项目目录写入 IHub 专属 AI 技能文件。
:::

| 插件ID | 插件名称 | 插件类型 | 扩展名称 |
|-------|---------|--------|---------|
| `pub.ihub.plugin.ihub-skills` | `IHub Skills插件` | `Project`[^Project] | `iHubSkills` |

::: tip 支持的 AI 开发工具
- **Claude Code**：在 `.claude/commands/` 写入斜杠命令技能文件
  - `/ihub-diagnose` — 诊断 Gradle 构建问题
  - `/ihub-configure` — 插件配置向导
- **GitHub Copilot**：在 `.github/copilot-instructions.md` 写入使用规范
- **OpenCode**：在 `AGENTS.md` 写入使用规范
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
    alias(ihub.plugins.skills)  // 等价于 id("pub.ihub.plugin.ihub-skills")
}
```

版本目录别名规则：`pub.ihub.plugin.ihub-<name>` → `ihub.plugins.<name>`（例：`ihub-skills` → `ihub.plugins.skills`）；`pub.ihub.plugin`（根插件）→ `ihub.plugins.root`。
:::

若不使用版本目录，也可直接声明插件 ID：

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-skills") version "1.9.5"
}
```

执行任意构建（包括 `./gradlew help`）后，技能文件会自动写入项目目录。

## 扩展属性

| Extension | Description | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| --------- | ----------- | ------- | --- | --- | --- | --- |
| `enabled` | 是否启用 AI 技能安装 | `true` | ✔ | ✔ | ✔ | ❌ |
| `claudeEnabled` | 是否安装 Claude Code 技能 | `true` | ✔ | ✔ | ✔ | ❌ |
| `copilotEnabled` | 是否安装 GitHub Copilot 技能 | `true` | ✔ | ✔ | ✔ | ❌ |
| `openCodeEnabled` | 是否安装 OpenCode 技能 | `true` | ✔ | ✔ | ✔ | ❌ |
| `claudeCommandsDir` | Claude Code commands 目录 | `{rootProject}/.claude/commands` | ✔ | ❌ | ❌ | ❌ |
| `copilotInstructionsFile` | Copilot instructions 文件路径 | `{rootProject}/.github/copilot-instructions.md` | ✔ | ❌ | ❌ | ❌ |
| `agentsMdFile` | OpenCode AGENTS.md 文件路径 | `{rootProject}/AGENTS.md` | ✔ | ❌ | ❌ | ❌ |

## 示例配置

### 禁用部分 AI 工具

```kotlin
iHubSkills {
    copilotEnabled.set(false)   // 不安装 Copilot 技能
    openCodeEnabled.set(false)  // 不安装 OpenCode 技能
}
```

### 完全禁用

```kotlin
iHubSkills {
    enabled.set(false)
}
```

### 自定义 Claude commands 目录

```kotlin
iHubSkills {
    claudeCommandsDir.set(layout.projectDirectory.dir("my-ai/commands"))
}
```

[^Project]: Project插件
[^Ext]: 支持扩展属性配置
[^Prj]: 支持项目属性配置（`gradle.properties`）
[^Sys]: 支持系统属性配置（`-DiHub.xxx=value`）
[^Env]: 支持环境变量配置
