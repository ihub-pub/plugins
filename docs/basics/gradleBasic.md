# Gradle基础入门

> `Gradle`是源于 [Apache Ant](https://ant.apache.org/) 和 [Apache Maven](https://maven.apache.org/) 概念的项目自动化构建开源工具，它使用一种基于 [Groovy](https://groovy.apache.org/) 的特定领域语言 (DSL) 来声明项目设置，抛弃了基于 XML 的各种繁琐配置面向 Java 应用为主。当前其支持的语言暂时有 Java、Groovy、Kotlin、Scala、Android、C++ 和 Swift。 下面是 Gradle 和 Apache Maven 的一个比较：

[![Gradle vs Maven](https://gradle.org/images/gradle-vs-maven.gif)](https://gradle.org/maven-vs-gradle/)

## 配置 wrapper

> 推荐使用`wrapper`的方式配置 Gradle
> 环境，便于保持项目环境统一，在项目根目录下添加`gradle/wrapper/gradle-wrapper.properties`配置文件，详细配置见入门文档，配置内容如下：

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-${ihub.plugin.gradleVersion}-bin.zip
```

> gradle-wrapper.jar、gradlew、gradlew.bat 文件可以通过命令自动生成，项目结构如下：

```
.
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
```

## 配置 setting.gradle

> settings.gradle 与 Settings 实例对应，用来声明实例化和配置 Project，可以用于多项目管理以及项目插件版本等配置，配置示例如下：

```groovy
// include two projects, 'foo' and 'foo:bar'
// directories are inferred by replacing ':' with '/'
include 'foo:bar'

// include one project whose project dir does not match the logical project path
include 'baz'
project(':baz').projectDir = file('foo/baz')

// include many projects whose project dirs do not match the logical project paths
file('subprojects').eachDir { dir ->
    include dir.name
    project(":${dir.name}").projectDir = dir
}
```

> 项目结构如下：

```
.
├── settings.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
```

## 配置 build.gradle

> 用于配置项目依赖、所需插件等，配置示例如下：

```groovy
// 添加插件
plugins {
    id 'groovy-gradle-plugin'
    id 'maven-publish'
}

// 配置项目信息
group = 'com.myorg.conventions'
version = '1.0'

// 配置组件仓库
repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

// 配置依赖
dependencies {
    implementation 'gradle.plugin.com.github.spotbugs.snom:spotbugs-gradle-plugin:4.6.2'
    testImplementation platform("org.spockframework:spock-bom:2.0-groovy-3.0")
    testImplementation 'org.spockframework:spock-core'
    testImplementation 'org.spockframework:spock-junit4'
    testImplementation 'junit:junit:4.13.1'
}

// 配置插件
publishing {
    repositories {
        maven {
            // change to point to your repo, e.g. http://my.org/repo
            url = layout.buildDirectory.dir("repo")
        }
    }
}

// 配置任务
tasks.named('publish') {
    dependsOn('check')
}
```

> 项目结构如下：

```
.
├── build.gradle
├── settings.gradle
├── gradle
│ └── wrapper
│ ├── gradle-wrapper.jar
│ └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
```

## 配置 gradle.properties

> 用于配置一些 Gradle 环境属性，如：

```properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
org.gradle.daemon=false
org.gradle.daemon.performance.enable-monitoring=false
```

> 项目结构如下：

```
.
├── gradle.properties
├── build.gradle
├── settings.gradle
├── gradle
│ └── wrapper
│ ├── gradle-wrapper.jar
│ └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
```