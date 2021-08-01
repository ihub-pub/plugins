# 基础插件
配置组件仓库，插件扩展名`iHub`，[属性说明](/explanation?id=属性配置说明)

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `mavenLocalEnabled` | 是否启用本地仓库 | `false` | ✔ | ✔ | ❌ | ❌ |
| `releaseRepoUrl` | 正式版本仓库 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `snapshotRepoUrl` | 快照版本仓库 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `repoAllowInsecureProtocol` | 是否允许不安全协议（是否允许http） | `false` | ✔ | ✔ | ❌ | ❌ |
| `repoIncludeGroup` | 仓库包含组（用于限制仓库范围） | ❌ | ✔ | ✔ | ❌ | ❌ |
| `repoIncludeGroupRegex` | 仓库包含组正则（用于限制仓库范围） | `.*` | ✔ | ✔ | ❌ | ❌ |
| `repoUsername` | 仓库用户名 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `repoPassword` | 仓库密码 | ❌ | ✔ | ✔ | ✔ | ✔ |
| `customizeRepoUrl` | 自定义仓库 | ❌ | ✔ | ✔ | ❌ | ❌ |
| `javaJaxbRuntime` | Jaxb运行时配置 | `true` | ✔ | ✔ | ✔ | ❌ |
| `javaCompatibility` | Java兼容性配置 | ❌ | ✔ | ✔ | ✔ | ❌ |
| `gradleCompilationIncremental` | gradle增量编译 | `true` | ✔ | ✔ | ✔ | ❌ |
| `compileGroovyAllModules` | 是否添加groovy所有模块 | `false` | ✔ | ✔ | ❌ | ❌ |