# ihub-test

:::info plugin description
`ihub-test`plugin configuration test task.
:::

| Information         | Description                                                                                                                              |
| ------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- |
| Plugin ID           | `pub.ihub.plugin.ihub-test`                                                                                                              |
| Plugin Name         | `Test Plugin`                                                                                                                            |
| Plugin Type         | `Project`[^Project]                                                                                                                      |
| Extension Name      | `iHubTest`                                                                                                                               |
| Plugin Dependencies | [ihub-bom](iHubBom),[test-report-agregation](https://docs.gradle.org/current/userguide/test_report_aggregation_plugin.html)(`main item`) |

## Extended Properties

| Extension                | Description                                                                                                         | Default                     | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| ------------------------ | ------------------------------------------------------------------------------------------------------------------- | --------------------------- | --------- | --------- | --------- | --------- |
| `enabled`                | Enable Test                                                                                                         | `true`                      | ✔         | ✔         | ✔         | ❌         |
| `classes`                | Include test classes (`,`separated, supported wildcard`*`)                                                          | `**/*Test*,**/*FT*,**/*UT*` | ✔         | ✔         | ✔         | ❌         |
| `forkEvery`              | Restart fork process per runx test class                                                                            | `100`                       | ✔         | ✔         | ✔         | ❌         |
| `maxParallelForks`       | Max startup processes                                                                                               | `1`                         | ✔         | ✔         | ✔         | ❌         |
| `runProperties`          | Attribute[See](explanation#runproperties) on task runtime                                                           | `{System.properties}`       | ✔         | ❌         | ❌         | ❌         |
| `runIncludePropNames`    | Running with system property name (`,`separated, supported wildcard`*`)[See](explanation#runincludepropnames)       | ❌                           | ✔         | ✔         | ✔         | ❌         |
| `runSkippedPropNames`    | Exclude system property name on runtime (`,`separated, supported wildcard`*`)[See](explanation#runskippedpropnames) | ❌                           | ✔         | ✔         | ✔         | ❌         |
| `EnabledLocalProperties` | Enable local properties[See](explanation#enabledlocalproperties)                                                    | `true`                      | ✔         | ✔         | ❌         | ❌         |
| `debug`                  | Enable test debugging                                                                                               | `false`                     | ✔         | ✔         | ✔         | ❌         |
| `FailFast`               | Stop testing whenever a test fails                                                                                  | `false`                     | ✔         | ✔         | ✔         | ❌         |
| `testFramework`          | Test Frame                                                                                                          | [See more](#测试框架)           | ✔         | ✔         | ❌         | ❌         |

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LO
    id("pub.ihub.plugin.ihu-test")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-test'
}
```

:::

## Configuration Example

:::code-tabs#build

@tab Kotlin

```kotlin
iHubTest {
    enabled.set(true)
    FailFast.set(true)
}
```

@tab Groovy

```groovy
iHubTest LO
    enabled = true
    ailFast = true
}
```

:::

## Test Frame

:::tip
- Testframe dependency configuration currently supports：`SPOCK`,`JUNITA_JUPITER`,`NONE`
- `Groovy`environmental default`SPOCK`,`Java`environmental default`JUNITA_JUPITER` ::

### SPOCK

```groovy
dependencies are
    testImplementation 'org.spockframe: spock-spring'
    testRuntimesOnly 'com.athaydes:spock-reports'
}
```

### JUNIT_JUPITER

```groovy
Dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter'
}
```

@include(../snippet/exploation.md)