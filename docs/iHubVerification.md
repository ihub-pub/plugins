> 扩展名`iHubVerification`，用于配置代码静态检查以及测试用例覆盖等。

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-verification' version '1.1.1'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.1.1'
}

apply {
    plugin 'pub.ihub.plugin.ihub-verification'
}
```

## 配置示例

> PMD静态检查示例

```groovy
iHubVerification {
    pmdConsoleOutput = true
}
```

> Codenarc静态检查示例

```groovy
iHubVerification {
    codenarcIgnoreFailures = true
}
```

> Jacoco测试覆盖示例

```groovy
iHubVerification {
    jacocoBranchCoverageRuleEnabled = true
    jacocoInstructionCoverageRuleEnabled = true
    jacocoPackageCoverageRuleEnabled = true
}
```

## 扩展属性

> [属性说明](/explanation?id=属性配置说明)：`pmd`开头为`PMD静态检查`，`codenarc`开头为`Codenarc静态检查`，`jacoco`开头为`Jacoco覆盖率检查`

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `pmdConsoleOutput` | 控制台是否打印PMD信息 | `false` | ✔ | ✔ | ❌ | ❌ |
| `pmdIgnoreFailures` | PMD检查是否忽略失败 | `false` | ✔ | ✔ | ✔ | ❌ |
| `pmdVersion` | PMD版本 | `6.35.0` | ✔ | ✔ | ❌ | ❌ |
| `codenarcIgnoreFailures` | Codenarc检查是否忽略失败 | `false` | ✔ | ✔ | ✔ | ❌ |
| `codenarcVersion` | Codenarc版本 | `2.1.0` | ✔ | ✔ | ❌ | ❌ |
| `jacocoVersion` | Jacoco版本 | `0.8.7` | ✔ | ✔ | ❌ | ❌ |
| `jacocoBranchCoverageRuleEnabled` | 是否启用bundle分支覆盖检查 | `true` | ✔ | ✔ | ✔ | ❌ |
| `jacocoBranchCoveredRatio` | bundle分支覆盖率 | `0.9` | ✔ | ✔ | ✔ | ❌ |
| `jacocoInstructionCoverageRuleEnabled` | 是否启用bundle指令覆盖检查 | `true` | ✔ | ✔ | ✔ | ❌ |
| `jacocoInstructionExclusion` | bundle指令覆盖排除目录 | `**/app`<br>`**/config` | ✔ | ✔ | ❌ | ❌ |
| `jacocoInstructionCoveredRatio` | bundle指令覆盖率 | `0.9` | ✔ | ✔ | ✔ | ❌ |
| `jacocoPackageCoverageRuleEnabled` | 是否启用package指令覆盖检查 | `true` | ✔ | ✔ | ✔ | ❌ |
| `jacocoPackageExclusion` | package指令覆盖排除目录 | `*.app`<br>`*.config` | ✔ | ✔ | ❌ | ❌ |
| `jacocoPackageCoveredRatio` | package指令覆盖率 | `0.9` | ✔ | ✔ | ✔ | ❌ |
| `jacocoReportExclusion` | 覆盖率报告排除目录 | `**/Application.class`<br>`**/app/*.class`<br>`**/config/*.class` | ✔ | ✔ | ❌ | ❌ |