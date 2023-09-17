# ihub-authentication

:::info plugin description
`ihub-verification`plugin configuration code static check and test case coverage etc.
:::                                  
:::  
:::  
:::  
:::  
:::  
:::  
:::  
:::  
:::  
:::  
:::  
:::  
:::  
:::  
:::  
:::  
:::
:::

| Information         | Description                                                                                                                                                                                                                                                                                                                                                        |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Plugin ID           | `pub.ihub.plugin.ihub-authentication`                                                                                                                                                                                                                                                                                                                              |
| Plugin Name         | `Verify Plugin`                                                                                                                                                                                                                                                                                                                                                    |
| Plugin Type         | `Project`[^Project]                                                                                                                                                                                                                                                                                                                                                |
| Extension Name      | `iHubVerification`                                                                                                                                                                                                                                                                                                                                                 |
| Plugin Dependencies | [ihub-bom](iHubBom),[codenarr](https://docs.gradle.org/current/userguide/codenarc_plugin.html),[pmd](https://docs.gradle.org/current/userguide/pmd_plugin.html),[jacoco](https://docs.gradle.org/current/userguide/jacoco_plugin.html),[jacoco-report-agregation](https://docs.gradle.org/current/userguide/jacoco_report_aggregation_plugin.html)(`main project`) |

:::tip plugin functionality
- The project contains`groovy`plugins automatically configured`codenarrc` plugins, default configuration[See](https://github.com/ihub-pub/plugins/blob/main/ihub-plugins/src/main/resources/META-INF/codenarc.groovy)and can be configured by config`$rootDir/conf/codenarc/codenarc.groovy`overwrite the default configuration,[examples](https://github.com/ihub-pub/plugins/tree/main/samples/sample-groovy)
- 项目包含`java`插件时会自动配置`pmd`插件，组件使用`com.alibaba.p3c:p3c-pmd`，可通过`$rootDir/conf/pmd/ruleset.xml`配置检查规则，默认规则如下：
```groovy
RuleSets = [
    'rulesets/java/ali-comment.xml',
    'rulesets/java/ali-concilient. ml',
    'rulesets/java/ali-constant.xml',
    'rulesets/java/ali-exception.xml',
    'rulesets/java/ali-flowcontrol. ml',
    'rulesets/java/ali-naming.xml',
    'rulesets/java/ali-oop.xml',
    'rulesets/java/ali-orm. ml',
    'rulesets/java/ali-other.xml',
    'rulesets/java/ali-set.xml',
    'rulesets/vm/ali-other.xml',
]
```
- `jacoco`Plugins are used to check code test coverage, primarily at：`bundle branch coverage`,`bundle command covering`and`package command covering` , The result is that the main project will add`jacoco-report-agregation`plugins for multi-project aggregating test reports, printing report[See](#测试报告) ::

## Extended Properties

`pmd`starts with`PMD-static check`,`codenarrc`starts with`Codenarc static check`,`jacoco`starts with`Jacoco coverage`

| Extension                              | Description                                      | Default                                                                       | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| -------------------------------------- | ------------------------------------------------ | ----------------------------------------------------------------------------- | --------- | --------- | --------- | --------- |
| `pmConsoleOutput`                      | Whether or not the console print PMD information | `false`                                                                       | ✔         | ✔         | ❌         | ❌         |
| `pmdIgnoreFailures`                    | PMD check failed to ignore                       | `false`                                                                       | ✔         | ✔         | ✔         | ❌         |
| `pmdversion`                           | PMD Version                                      | `6.55.0`                                                                      | ✔         | ✔         | ❌         | ❌         |
| `codenarcIgnoreFailures`               | Codenarc check if failed to ignore               | `false`                                                                       | ✔         | ✔         | ✔         | ❌         |
| `codenarcVersion`                      | Codenarc Version                                 | `3.2.0`                                                                       | ✔         | ✔         | ❌         | ❌         |
| `jacoco Version`                       | Jacoco Version                                   | `0.8.8`                                                                       | ✔         | ✔         | ❌         | ❌         |
| `jacocoBranchCoverage RuleEnabled`     | Enable Bundle branch overwrite check             | `true`                                                                        | ✔         | ✔         | ✔         | ❌         |
| `jacocoBranchCoveredRatio`             | bundle branch coverage                           | `0.9`                                                                         | ✔         | ✔         | ✔         | ❌         |
| `jacocoInstructionCoverageRuleEnabled` | Enable Bundle command overwrite check            | `true`                                                                        | ✔         | ✔         | ✔         | ❌         |
| `jacocoInstructionExclusion`           | bundle command overwrite exclusion directory     | `**/app`<br>`**/config`                                                 | ✔         | ✔         | ❌         | ❌         |
| `jacoco Instruction CoveredRatio`      | bundle command coverage                          | `0.9`                                                                         | ✔         | ✔         | ✔         | ❌         |
| `jacocoPackageCoverage RuleEnabled`    | Enable package command overwrite check           | `true`                                                                        | ✔         | ✔         | ✔         | ❌         |
| `jacocoPackageExclusion`               | package command overwrite exclusion directory    | `*.app`<br>`*.config`                                                   | ✔         | ✔         | ❌         | ❌         |
| `jacocoPackageCoveredRatio`            | package command coverage                         | `0.9`                                                                         | ✔         | ✔         | ✔         | ❌         |
| `jacocoReportExclusion`                | Coverage Report Exclude Directory                | `**/Application.class`<br>`**/app/*.class`<br>`**/config/*.class` | ✔         | ✔         | ❌         | ❌         |

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-verification")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-verification'
}
```

:::

## Configuration Example

### PMD Static Check Example

:::code-tabs#build

@tab Kotlin

```kotlin
iHubVerification {
    pmdConsoleOutput.set(true)
}
```

@tab Groovy

```groovy
iHubVerification maximum
    pmdConsoleOutput = true
}
```

:::

### Example Codenacs static check

:::code-tabs#build

@tab Kotlin

```kotlin
iHubVerification maximum
    codenarcIgnoreFailures.set(true)
}
```

@tab Groovy

```groovy
iHubVerification LO
    codenicarcIgnore Failures = true
}
```

:::

### Jacoco Test Overwrite Example

:::code-tabs#build

@tab Kotlin

```kotlin
iHubVerification $
    jacocoBranchCoverageRuleEnabled.set(true)
    jacocoInstructionCoverageRuleEnabled.set(true)
    jacocoPackageEnabled.set(true)
}
```

@tab Groovy

```groovy
iHubVerification {
    jacocoBranchCoverageRuleEnabed = true
    jacocoInstruction CoverageRuleEnabed = true
    jacocoPackageRuleEnabed =
}
```

:::

## Test Report

Test case reports will be generated and the console will print test cover.

- Sample coverage of individual project tests ![](/img/printJacocoReportCoverage.png)
- Project Summary Test Coverage Example ![](/img/printFinishedJacocoReportCoverage.png)

@include(../snippet/footnote.md)