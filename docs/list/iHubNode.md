# ihub-node

::: info 插件信息
`ihub-node`插件为独立插件，基于 [com.github.node-gradle.node](https://plugins.gradle.org/plugin/com.github.node-gradle.node) 插件支持构建node.js项目，并扩展支持`cnpm`
:::

| 插件ID | 插件名称 | 插件类型 | 扩展名称 | 插件依赖                                                                                                            |
|-------|---------|--------|---------|-----------------------------------------------------------------------------------------------------------------|
| `pub.ihub.plugin.ihub-node` | `Node插件` | `Project`[^Project] | `iHubNode` | [com.github.node-gradle.node](https://plugins.gradle.org/plugin/com.github.node-gradle.node) |

## 扩展属性

| Extension | Description | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| --------- |---------| ----- | --- | ------- | ------ | --- |
| `version` | Node.js版本 | `latest` | ✔ | ✔ | ✔ | ❌ |
| `npmVersion` | npm版本，如果不指定则使用Node.js绑定版本 | `latest` | ✔ | ✔ | ✔ | ❌ |
| `pnpmVersion` | pnpm版本，如果不指定则使用最新版本 | `latest` | ✔ | ✔ | ✔ | ❌ |
| `yarnVersion` | yarn版本，如果不指定则使用最新版本 | `latest` | ✔ | ✔ | ✔ | ❌ |
| `cnpmVersion` | cnpm版本，如果不指定则使用最新版本 | `latest` | ✔ | ✔ | ✔ | ❌ |
| `distBaseUrl` | Node.js下载地址 | ❌ | ✔ | ✔ | ✔ | ❌ |
| `allowInsecureProtocol` | 是否允许不安全的协议 | `false` | ✔ | ✔ | ✔ | ❌ |
| `download` | 是否下载并安装特定的 Node.js 版本 | `false` | ✔ | ✔ | ✔ | ❌ |
| `workDir` | Node.js安装目录 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `npmWorkDir` | NPM安装目录 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `pnpmWorkDir` | PNPM安装目录 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `yarnWorkDir` | Yarn安装目录 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `cnpmWorkDir` | cNpm安装目录 | ❌ | ✔ | ✔ | ✔ | ✔ |

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-node")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-node'
}
```

:::

## 插件扩展配置使用示例

::: code-tabs#build

@tab Kotlin

```kotlin
iHubNode {
    version.set("18.16.0")
}
```

@tab Groovy

```groovy
iHubNode {
    version = '18.16.0'
}
```

:::

## 插件扩展任务

### cnpm

> 任务扩展配置

::: code-tabs#build

@tab Kotlin

```kotlin
import pub.ihub.plugin.node.cnpm.task.CnpmTask

tasks.create("cnpm_run_dev", CnpmTask::class) {
    args.set(listOf("run", "dev"))
}
```

@tab Groovy

```groovy
task cnpm_run_dev(type: pub.ihub.plugin.node.cnpm.task.CnpmTask) {
    args = ['run','dev']
}
```

:::

> 任务命令

```cmd
cnpm run dev
```

### cnpmSetup

> 任务命令

```cmd
npm install --global --no-save --prefix cnpm --registry=https://registry.npm.taobao.org
```

### cnpmInstall

> 任务命令

```cmd
cnpm install
```

### cnpmSync

> 任务命令

```cmd
cnpm sync cnpmcore
```

@include(../snippet/footnote.md)