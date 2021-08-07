测试相关插件，插件扩展名`iHubTest`，[属性说明](/explanation?id=属性配置说明)

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `enabled` | 启用测试 | `true` | ✔ | ✔ | ✔ | ❌ |
| `classes` | 包含测试类（“,”分割） | ❌ | ✔ | ✔ | ✔ | ❌ |
| `forkEvery` | 每跑x个测试类后重启fork进程 | `100` | ✔ | ✔ | ✔ | ❌ |
| `maxParallelForks` | 最多启动进程数 | `1` | ✔ | ✔ | ✔ | ❌ |
| `runProperties` | 任务运行时属性 | `System.properties` | ✔ | ❌ | ❌ | ❌ |
| `runIncludePropNames` | 运行时包含系统属性名称（“,”分割） | ❌ | ✔ | ✔ | ✔ | ❌ |
| `runSkippedPropNames` | 运行时排除系统属性名称（“,”分割） | ❌ | ✔ | ✔ | ✔ | ❌ |
| `enabledLocalProperties` | 启用本地属性 | `false` | ✔ | ✔ | ❌ | ❌ |
| `debug` | 启用测试调试 | `false` | ✔ | ✔ | ✔ | ❌ |
| `failFast` | 只要有一个测试失败就停止测试 | `false` | ✔ | ✔ | ✔ | ❌ |