> `ihub-verification`插件用于配置代码静态检查以及测试用例覆盖率等。

| 插件ID | 插件名称 | 插件类型 | 扩展名称 | 插件依赖                                                                                                                                                                                                                                                                                                                                                           |
|-------|---------|--------|---------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `pub.ihub.plugin.ihub-verification` | `验证插件` | `Project` | `iHubVerification` | [ihub-bom](iHubBom)、[codenarc](https://docs.gradle.org/current/userguide/codenarc_plugin.html)、[pmd](https://docs.gradle.org/current/userguide/pmd_plugin.html)、[jacoco](https://docs.gradle.org/current/userguide/jacoco_plugin.html)、<br>[jacoco-report-aggregation](https://docs.gradle.org/current/userguide/jacoco_report_aggregation_plugin.html)（`主项目`） |

- 项目包含`groovy`插件时会自动配置`codenarc`
  插件，默认配置[详见](https://github.com/ihub-pub/plugins/blob/main/ihub-plugins/src/main/resources/META-INF/codenarc.groovy)，可以通过配置`$rootDir/conf/codenarc/codenarc.groovy`覆盖默认配置，[示例](https://github.com/ihub-pub/plugins/tree/main/samples/sample-groovy)
- 项目包含`java`插件时会自动配置`pmd`插件，组件使用`com.alibaba.p3c:p3c-pmd`，可通过`$rootDir/conf/pmd/ruleset.xml`配置检查检查规则，默认规则如下：
```groovy
ruleSets = [
    'rulesets/java/ali-comment.xml',
    'rulesets/java/ali-concurrent.xml',
    'rulesets/java/ali-constant.xml',
    'rulesets/java/ali-exception.xml',
    'rulesets/java/ali-flowcontrol.xml',
    'rulesets/java/ali-naming.xml',
    'rulesets/java/ali-oop.xml',
    'rulesets/java/ali-orm.xml',
    'rulesets/java/ali-other.xml',
    'rulesets/java/ali-set.xml',
    'rulesets/vm/ali-other.xml',
]
```
- `jacoco`插件用于检查代码测试覆盖率，主要检查维度为：`bundle分支覆盖率`、`bundle指令覆盖率`、`package指令覆盖率`
  ，如果是主项目会添加`jacoco-report-aggregation`插件，用于多项目时聚合测试报告，打印报告[详见](iHubVerification?id=测试报告)

## 扩展属性

> 属性使用说明[详见](/explanation?id=属性配置说明)，`pmd`开头为`PMD静态检查`，`codenarc`开头为`Codenarc静态检查`，`jacoco`开头为`Jacoco覆盖率检查`

| Extension | Description | Default | Ext | Prj | Sys | Env |
| --------- | ----------- | ------- | --- | ------- | ------ | --- |
| `pmdConsoleOutput` | 控制台是否打印PMD信息 | `false` | ✔ | ✔ | ❌ | ❌ |
| `pmdIgnoreFailures` | PMD检查是否忽略失败 | `false` | ✔ | ✔ | ✔ | ❌ |
| `pmdVersion` | PMD版本 | `6.36.0` | ✔ | ✔ | ❌ | ❌ |
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

## 插件安装

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-verification' version '${ihub.plugin.version}'
}
```

或

```groovy
plugins {
    id 'pub.ihub.plugin' version '${ihub.plugin.version}'
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

## 测试报告

> 测试用例执行完成会生成用例报告，控制台会打印测试覆盖率

- 单个项目测试覆盖率示例
![](img/printJacocoReportCoverage.png)
- 项目汇总测试覆盖率示例
![](img/printFinishedJacocoReportCoverage.png)