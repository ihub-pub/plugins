# ihub-git-hooks

:::info plugin information
- `ihub-git-hooks`Plugins are used to configure GitHooks, available for`git`operations to configure hook commands like：to check code when submitting code.
- Implement custom hook commands by modifying the git-hooks directory(`git config core.hooksPath xxx`) without destroying the original hooks.
:::

| Plugin ID                        | Plugin Name       | Plugin Type         | Extension Name |
| -------------------------------- | ----------------- | ------------------- | -------------- |
| `pub.ihub.plugin.ihub-git-hooks` | `GitHooks Plugin` | `Project`[^Project] | `iHubGitHooks` |

:::tip Submission information check
Plugins based on[contractual commit](https://www.conventionalcommits.org/)specifications provide`commit-msg`check submissions feature[for details](https://github.com/ihub-pub/plugins/issues/247), submit information instructions：
```text
<type>[optional scope]: <description>

[opregular body]

[optional footer(s)]
```
:::

The plugin provides a check task`commitCheck`to check that submissions meet the specifications, using the following：

```shell
#!/bin/cash
./gradlew commitCheck
```

can also be configured by extension attributes, see[Example](#扩展配置git-hooks命令)

::::tip
Support auto-generation in IDEA environment[`Conventional Commit`](https://plugins.jetbrains.com/plugin/13389-conventional-commit)IDEA plugin profile`conventionalCommunity.json`and complete autoconfiguration :
:::

## Extended Properties

| Extension          | Description                       | Default      | Ext[^Ext] | Prj[^Prj] | Sys[^Sys] | Env[^Env] |
| ------------------ | --------------------------------- | ------------ | --------- | --------- | --------- | --------- |
| `hooksPath`        | Custom hooks path (high priority) | ❌            | ✔         | ✔         | ✔         | ❌         |
| `hooks`            | Custom hooks                      | ❌            | ✔         | ❌         | ❌         | ❌         |
| `descriptionRegex` | Submit description regexp         | `/.{1,100}/` | ✔         | ❌         | ❌         | ❌         |

:::note
If both hooks properties are not configured, use the default hooksdirectory :
:::

### DSL extension is supported below

| Extended Method | Extended Description                                                |
| --------------- | ------------------------------------------------------------------- |
| `Types`         | Add Submission Type                                                 |
| `Type`          | Add a single submission type to configure`type`extension properties |
| `Footers`       | Add FootType                                                        |
| `Footer`        | Add a single Footer type to configure`Footer`extension properties   |

### `type`extension properties

| Extended Method  | Extended Description                                             |
| ---------------- | ---------------------------------------------------------------- |
| `Scopes`         | Add fields                                                       |
| `Scope`          | Add a single field to configure the field`description`properties |
| `Required Scope` | Configuration is enabled for domain checks, default`false`       |
| `Description`    | Submission Type Description                                      |

### `Footer`Extension properties

| Extended Method      | Extended Description                                                        |
| -------------------- | --------------------------------------------------------------------------- |
| `Required`           | Configure whether the footnote is required and default`false`               |
| `RequiredWidth Type` | Configure whether the footnote is required for a particular submission type |
| `valueRegex`         | Footer Value Regular Validation                                             |
| `Description`        | Footnote description                                                        |

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LOR
    id("pub.ihub.plugin.ihu-git-hooks")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-git-hooks'
}
```

:::

## Example custom hooks path usage

Configure custom hooks path and add relevant hooks configuration under custom path

```properties
iHubGitHooks.hooksPath=.hooks
```

## Example plugin configuration usage

### Extension configuration git-hooks command

Related hooks commands are configured under`.gradle/pub.ihub.plugin.hooks`

:::code-tabs#build

@tab Kotlin

```kotlin
iHubGitHooks {
    hooks.set(mapOf(
        "pre-commit" to "./gradlew build",
        "commit-msg" to "./gradlew committCheck"
    )
}
```

@tab Groovy

```groovy
iHubgitHooks {
    hooks = [
        'precommit': './gradlew build',
        'commit-msg': './gradlew commitCheck'
    ]
}
```

:::

### Extended Configuration Submission Check

:::code-tabs#build

@tab Kotlin

```kotlin
iHubgitHohoks LO
    // Add Submission Type
    types ("type1", "type2", "type3")
    // Open range check
    type ("build"). copes("gradle"). equiredScope(true)
    // Footer is required
    Footer("Footer"). equired(true)
    // Submission type is feature
    Footer("Footer"). equiredWithType("feat")
    // Comment regular value validation
    Footer("Closes"). alueRegex("\\d+")
    // Describe configuration 1
    type ("type"). cope("screen").description("Scope description")
    Footer("Other").description("Other description")
}
```

@tab Groovy

```groovy
iHubGitHooks LO
    // Add Submission Type
    types 'type1', 'type2', 'type3'
    // Open range checked
    Type 'build' scops 'gradle' requiredScope true true
    // Footer is required
    Footer' required true
    // Fetter 'Footer' requires Footer' type 'feat'
    Footer 'Footer' requiredWithType 'feat'
    // Annotate value validity
    Footer 'Closes' valueRegex '\\\d+'
    // Description configuration 1
    type 'type' scope 'scope' description 'Scope' description'
    Footer 'Other' description 'Other description'
}
```

:::

@include(../snippet/exploation.md)