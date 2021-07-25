# IHub Plugins
[![Gradle Build](https://github.com/ihub-pub/plugins/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/ihub-pub/plugins/actions/workflows/gradle-build.yml)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f866ca35cbb44347a210722a2da8aabc)](https://app.codacy.com/gh/ihub-pub/plugins?utm_source=github.com&utm_medium=referral&utm_content=ihub-pub/plugins&utm_campaign=Badge_Grade_Settings)
[![codecov](https://codecov.io/gh/ihub-pub/plugins/branch/main/graph/badge.svg?token=ZQ0WR3ZSWG)](https://codecov.io/gh/ihub-pub/plugins)
[![Join the chat at https://gitter.im/ihub-pub/plugins](https://badges.gitter.im/ihub-pub/plugins.svg)](https://gitter.im/ihub-pub/plugins?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![GitHub](https://img.shields.io/badge/IHubPub-181717.svg?style=flat&logo=GitHub)](https://github.com/ihub-pub "IHubPub")
[![Gitee](https://img.shields.io/badge/IHubPub-C71D23.svg?style=flat&logo=Gitee)](https://gitee.com/ihub-pub "IHubPub")
[![Gradle Plugin](https://img.shields.io/badge/IHubGradle-02303A.svg?style=flat&logo=Gradle)](https://plugins.gradle.org/u/henry-gradle "IHub Plugins Gradle Plugin")
[![GitBook](https://img.shields.io/badge/GitBook-white.svg?style=flat&logo=GitBook&logoColor=3884FF)](https://doc.ihub.pub "GitBook")
[![JetBrains](https://img.shields.io/badge/JetBrains-white.svg?style=flat&logo=JetBrains&logoColor=black)](https://www.jetbrains.com "JetBrains")
![GitHub](https://img.shields.io/github/license/ihub-pub/plugins)
![Version](https://img.shields.io/badge/Gradle-7.1-brightgreen.svg?logo=Gradle)
![Version](https://img.shields.io/badge/SpringBoot-2.5.1-6DB33F.svg?logo=SpringBoot&logoColor=white)
![Version](https://img.shields.io/badge/SpringCloud-2020.0.3-6DB33F.svg?logo=Spring&logoColor=white)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/ihub-pub/plugins)

本插件用于封装常用Gradle插件，并做了一些个性化缺省配置，便于项目集成，使用详见[samples](samples)，该项目包含插件如下：

| Id | DisplayName | Extension | Description |
|----|-------------|-----------|-------------|
| pub.ihub.plugin.ihub-settings | 设置插件 | [`iHubSettings`](#ihubsettings) | 配置插件仓库以及子项目 |
| pub.ihub.plugin | 基础插件 | [`iHub`](#ihub) | 配置组件仓库 |
| pub.ihub.plugin.ihub-bom | 组件依赖管理 | [`iHubBom`](samples/sample-extensions/bom.gradle) | 配置组件默认依赖版本以及兼容性管理 |
| pub.ihub.plugin.ihub-java | Java插件 | | 配置一些默认依赖以及兼容性配置 |
| pub.ihub.plugin.ihub-groovy | Groovy插件 | | 配置Groovy组件依赖 |
| pub.ihub.plugin.ihub-publish | 发布插件 | [`iHubPublish`](#ihubpublish) | 配置组件发布仓库以及其他个性化组件配置 |
| pub.ihub.plugin.ihub-test | 测试插件 | [`iHubTest`](#ihubtest) | 测试相关插件 |
| pub.ihub.plugin.ihub-verification | 验证插件 | [`iHubVerification`](#ihubverification) | 配置代码静态检查以及测试用例覆盖等 |
| pub.ihub.plugin.ihub-boot | Boot插件 | [`iHubBoot`](#ihubboot) | 用于镜像个性化配置 |
| pub.ihub.plugin.ihub-native | Native插件 | [`iHubNative`](#ihubnative) | 用于原生镜像个性化配置 |

## 属性配置表
插件配置属性获取目前支持4种方式：`扩展属性`（`Ext`）、`项目属性`（`Prj`）、`系统属性`（`Sys`）、`环境属性`（`Env`）， 
属性优先级：`Sys` > `Env` > `Prj` > `Ext`
> - `Ext`（Extension）：插件自定义扩展属性，配置与`build.gradle`文件，配置方式详见[samples](samples)
> - `Prj`（Project）：项目属性，配置与`gradle.properties`文件，配置格式`扩展名`.`属性名`，如`iHub.mavenLocalEnabled=true`
> - `Sys`（System）：系统属性，如命令行传递的信息等，配置格式`扩展名`.`属性名`，如`-DiHub.mavenLocalEnabled=true`
> - `Env`（Environment）：环境变量属性，配置格式全部大写，多个单词，用`_`分隔，如`MAVEN_LOCAL_ENABLED=true`
### 各插件详细属性如下
#### iHubSettings
<details>

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `includeDirs` | 包含项目路径 | :x: | :x: | :white_check_mark: | :x: | :x: |
| `skippedDirs` | 排除项目路径 | :x: | :x: | :white_check_mark: | :x: | :x: |
</details>

#### iHub
<details>

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `mavenLocalEnabled` | 是否启用本地仓库 | `false` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `releaseRepoUrl` | 正式版本仓库 | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `snapshotRepoUrl` | 快照版本仓库 | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `repoAllowInsecureProtocol` | 是否允许不安全协议（是否允许http） | `false` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `repoIncludeGroup` | 仓库包含组（用于限制仓库范围） | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `repoIncludeGroupRegex` | 仓库包含组正则（用于限制仓库范围） | `.*` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `repoUsername` | 仓库用户名 | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `repoPassword` | 仓库密码 | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `customizeRepoUrl` | 自定义仓库 | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `javaJaxbRuntime` | Jaxb运行时配置 | `true` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `javaCompatibility` | Java兼容性配置 | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `gradleCompilationIncremental` | gradle增量编译 | `true` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `compileGroovyAllModules` | 是否添加groovy所有模块 | `false` | :white_check_mark: | :white_check_mark: | :x: | :x: |
</details>

#### iHubPublish
<details>

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `publishNeedSign` | 组件发布是否需要签名 | `false` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `signingKeyId` | 签名key | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `signingSecretKey` | 签名密钥 | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `signingPassword` | 签名密码 | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `publishDocs` | 是否发布文档 | `false` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
</details>

#### iHubTest
<details>

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `enabled` | 启用测试 | `true` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `classes` | 包含测试类（“,”分割） | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `forkEvery` | 每跑x个测试类后重启fork进程 | `100` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `maxParallelForks` | 最多启动进程数 | `1` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `runProperties` | 任务运行时属性 | `System.properties` | :white_check_mark: | :x: | :x: | :x: |
| `runIncludePropNames` | 运行时包含系统属性名称（“,”分割） | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `runSkippedPropNames` | 运行时排除系统属性名称（“,”分割） | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `enabledLocalProperties` | 启用本地属性 | `false` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `debug` | 启用测试调试 | `false` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `failFast` | 只要有一个测试失败就停止测试 | `false` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
</details>

#### iHubVerification
<details>

> 属性说明：`pmd`开头为`PMD静态检查`，`codenarc`开头为`Codenarc静态检查`，`jacoco`开头为`Jacoco覆盖率检查`

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `pmdConsoleOutput` | 控制台是否打印PMD信息 | `false` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `pmdIgnoreFailures` | PMD检查是否忽略失败 | `false` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `pmdVersion` | PMD版本 | `6.35.0` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `codenarcIgnoreFailures` | Codenarc检查是否忽略失败 | `false` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `codenarcVersion` | Codenarc版本 | `2.1.0` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `jacocoVersion` | Jacoco版本 | `0.8.7` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `jacocoBranchCoverageRuleEnabled` | 是否启用bundle分支覆盖检查 | `true` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `jacocoBranchCoveredRatio` | bundle分支覆盖率 | `0.9` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `jacocoInstructionCoverageRuleEnabled` | 是否启用bundle指令覆盖检查 | `true` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `jacocoInstructionExclusion` | bundle指令覆盖排除目录 | `**/app`<br>`**/config` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `jacocoInstructionCoveredRatio` | bundle指令覆盖率 | `0.9` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `jacocoPackageCoverageRuleEnabled` | 是否启用package指令覆盖检查 | `true` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `jacocoPackageExclusion` | package指令覆盖排除目录 | `*.app`<br>`*.config` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `jacocoPackageCoveredRatio` | package指令覆盖率 | `0.9` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `jacocoReportExclusion` | 覆盖率报告排除目录 | `**/Application.class`<br>`**/app/*.class`<br>`**/config/*.class` | :white_check_mark: | :white_check_mark: | :x: | :x: |
</details>

#### iHubBoot
<details>

> 属性说明：`run`开头为`运行时属性`，`bootJar`开头为`打包Jar时属性`，`bp`开头为`构建镜像时属性`，`bpl`开头为`启动时属性`，`docker`开头为`Docker仓库相关属性`，[参考](https://docs.spring.io/spring-boot/docs/2.5.3/gradle-plugin/reference/htmlsingle/)

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `runProperties` | bootRun属性 | :x: | :white_check_mark: | :x: | :x: | :x: |
| `runIncludePropNames` | 运行时包含系统属性名称（“,”分割） | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `runSkippedPropNames` | 运行时排除系统属性名称（“,”分割） | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `enabledLocalProperties` | 启用本地属性 | `true` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `runOptimizedLaunch` | 优化启动 | `true` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `bootJarRequiresUnpack` | 配置需要移除的库 | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bpJvmVersion` | JVM版本 | `默认当前版本` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bpCleanCache` | 是否在构建前清理缓存 | `false` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bpVerboseLogging` | 启用构建器操作的详细日志记录 | `false` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bpPublish` | 是否将生成的镜像发布到Docker仓库 | `false` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `httpProxy` | http代理 | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `httpsProxy` | https代理 | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bplJvmHeadRoom` | JVM内存 | `8G` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bplJvmLoadedClassCount` | JVM运行时已加载类的数量 | `35% of classes` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bplJvmThreadCount` | JVM运行时用户线程数 | `250` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `javaToolOptions` | JVM环境变量 | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bpeEnvironment` | JVM运行时变量，[参考](https://paketo.io/docs/reference/configuration/) | :x: | :white_check_mark: | :x: | :x: | :x: |
| `dockerHost` | Docker守护程序的主机和端口的url | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `dockerTlsVerify` | 启用安全https协议 | `false` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `dockerCertPath` | https证书和密钥文件的路径 | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `dockerUrl` | Docker私有镜像仓库地址 | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `dockerUsername` | Docker私有镜像仓库用户名 | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `dockerPassword` | Docker私有镜像仓库密码 | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| `dockerEmail` | Docker私有镜像仓库邮箱 | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `dockerToken` | Docker私有镜像仓库身份令牌 | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
</details>

#### iHubNative
<details>

> 属性说明：`bp`开头为`构建镜像时属性`，`bpl`开头为`启动时属性`，`aot`开头为`AOT插件属性`

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `bpJvmVersion` | JVM版本 | `默认当前版本` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bpNativeImage` | 是否启用原生映像构建 | `true` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bpNativeImageBuildArguments` | 传递给原生映像命令的参数 | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bplJvmHeadRoom` | JVM内存 | `8G` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bplJvmLoadedClassCount` | JVM运行时已加载类的数量 | `35% of classes` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `bplJvmThreadCount` | JVM运行时用户线程数 | `250` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `javaToolOptions` | JVM环境变量 | :x: | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `aotMode` | native镜像编译器配置 | `NATIVE` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `aotDebugVerify` | 启用验证调试 | `false` | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| `aotRemoveXmlSupport` | 移除XML支持 | `true` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `aotRemoveSpelSupport` | 移除Spel支持 | `false` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `aotRemoveYamlSupport` | 移除Yaml支持 | `false` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `aotRemoveJmxSupport` | 移除Jmx支持 | `true` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `aotVerify` | 开启自动验证 | `true` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `aotRemoveUnusedConfig` | 移除未使用的配置 | `true` | :white_check_mark: | :white_check_mark: | :x: | :x: |
| `aotFailOnMissingSelectorHint` | 如果没有为活动选择器提供提示，则抛出错误 | `true` | :white_check_mark: | :white_check_mark: | :x: | :x: |
</details>

## Contributing

Contributors are welcomed to join IHub Plugins project. Please check [CONTRIBUTING](./CONTRIBUTING.md) about how to contribute to this project.
