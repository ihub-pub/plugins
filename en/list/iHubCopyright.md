# ihub copyright

::: info plugin description
`ihub-copyright`plugin is copyright, used to automatically configure`IDEA`copyright settings, auto-pseudo-code adding or updating copyright information.
:::

| Plugin ID                        | Plugin Name        | Plugin Type         | Plugin Dependencies |
| -------------------------------- | ------------------ | ------------------- | ------------------- |
| `pub.ihub.plugin.ihub-copyright` | `Copyright Plugin` | `Project`[^Project] | [ihub](iHub)        |

## Plugin Installation

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LOR
    id("pub.ihub.plugin.ihub-copyright")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin.ihub-copyright'
}
```

:::

or

:::code-tabs#build

@tab Kotlin

```kotlin
plugins LO
    id("pub.ihub.plugin")
}

applet {
    plugin("pub.ihub.plugin.ihu-copyright")
}
```

@tab Groovy

```groovy
plugins {
    id 'pub.ihub.plugin'
}

apply {
    plugin 'pub.ihub.plugin.ihub-copyright'
}
```

:::

## Configuration Example

1. Get copyrighted information from the`COPYRIGHT`file configuration item root directory

```text
Copyright (c) 2023 the original author authors. Licensed under the Apache License, Version 2. (The "License");
you may not use this file except in compliance with the License. You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2. Unless required by applicable law or agreed to in writing, software
distributed under the License is distressed on an "AS IS" BASIS,
WITHOUT WARRANES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing missions and
limitations under the License.
```

2. Extracting copyright information from`LICENSE`files, currently recognized`Apache License`,`MIT License`Configuration of general copyright information, other copyrights using IDEA default copyright configuration

@include(../snippet/footnote.md)