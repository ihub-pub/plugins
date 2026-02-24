# ihub-javaagent

::: info 插件信息
`ihub-javaagent`插件为独立插件，基于 [com.ryandens.javaagent-application](https://github.com/ryandens/javaagent-gradle-plugin) 插件支持Java应用代理配置
- 项目应用`application`插件时自动应用`com.ryandens.javaagent-application`，自动寻找并配置主类
- 项目应用`org.springframework.boot`插件时自动配置`bootRun`任务添加代理
:::

| 信息 | 描述 |
|----|----|
| 插件ID | `pub.ihub.plugin.ihub-javaagent` |
| 插件名称 | `Javaagent插件` |
| 插件类型 | `Project`[^Project] |
| 扩展名称 | `iHubJavaagent` |
| 插件依赖 | [com.ryandens.javaagent-application](https://github.com/ryandens/javaagent-gradle-plugin) |
## 扩展属性

| Extension   | Description | Default | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
|-------------|-------------|--------|-----------|-----------|-----------|-----------|
| `javaagent` | Java代理插件    | ❌ | ✔         | ✔         | ❌         | ❌         |
| `classifier` | 分类    | `all` | ✔         | ❌         | ❌         | ❌         |

## 插件安装

::: code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-javaagent")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-javaagent'
}
```

:::

@include(../snippet/footnote.md)