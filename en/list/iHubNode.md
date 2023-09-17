# ihub-node

::: info plugin information
`ihub-node`plugin is a stand-alone plugin based on [com.github.node-gradle.node](https://plugins.gradle.org/plugin/com.github.node-gradle.node) plugin supports building node.js projects, and extension support`cnpm`
:::

| Plugin Id                   | Plugin Name       | Plugin Type         | Extension Name | Plugin dependencies                                                                          |
| --------------------------- | ----------------- | ------------------- | -------------- | -------------------------------------------------------------------------------------------- |
| `pub.ihub.plugin.ihub-node` | `GitHooks Plugin` | `Project`[^Project] | `iHubNode`     | [com.github.node-gradle.node](https://plugins.gradle.org/plugin/com.github.node-gradle.node) |

## Extended Properties

| Extension               | Description                                               | Default  | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| ----------------------- | --------------------------------------------------------- | -------- | --------- | --------- | --------- | --------- |
| `version`               | Node.js Version                                           | `latest` | ✔         | ✔         | ✔         | ❌         |
| `npmVersion`            | npm version. If not specified, use Node.js bound version  | `latest` | ✔         | ✔         | ✔         | ❌         |
| `pnpmVersion`           | pnpm version, latest version if not specified             | `latest` | ✔         | ✔         | ✔         | ❌         |
| `yarnVersion`           | pnpm version, latest version if not specified             | `latest` | ✔         | ✔         | ✔         | ❌         |
| `cnpmVersion`           | pnpm version, latest version if not specified             | `latest` | ✔         | ✔         | ✔         | ❌         |
| `distBaseUrl`           | Node.js Download Address                                  | ❌        | ✔         | ✔         | ✔         | ❌         |
| `allowInsecureProtocol` | Whether unsafe protocols are allowed                      | `false`  | ✔         | ✔         | ✔         | ❌         |
| `download`              | Whether to download and install specific Node.js versions | `false`  | ✔         | ✔         | ✔         | ❌         |
| `workDir`               | Node.js Installation Directory                            | ❌        | ✔         | ✔         | ✔         | ✔         |
| `npmWorkDir`            | NPM installation directory                                | ❌        | ✔         | ✔         | ✔         | ✔         |
| `pnpmWorkDir`           | NPM installation directory                                | ❌        | ✔         | ✔         | ✔         | ✔         |
| `yarnWorkDir`           | Yarn Installation Directory                               | ❌        | ✔         | ✔         | ✔         | ✔         |
| `cnpmWorkDir`           | cNpm installation directory                               | ❌        | ✔         | ✔         | ✔         | ✔         |

## Plugin Installation

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

## Example plugin configuration usage

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

## Plugin Extension Task

### cnpm

> Task Extension Configuration

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

> Task command

```cmd
cnpm run dev
```

### cnpmSetup

> Task command

```cmd
npm install --global --no-save --prefix cnpm --registry=https://registry.npm.taobao.org
```

### cnpmInstall

> Task command

```cmd
cnpm install
```

### cnpmSync

> Task command

```cmd
cnpm sync cnpmcore
```

@include(../snippet/footnote.md)