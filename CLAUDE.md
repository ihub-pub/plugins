# PROJECT KNOWLEDGE BASE

**Generated:** 2026-04-27
**Project:** IHub Plugins (`pub.ihub.plugin`)
**Repository:** https://github.com/ihub-pub/plugins

## OVERVIEW

IHub Plugins 是一个为 Gradle 项目提供基础设施的插件集合。该项目通过注解驱动的 DSL 大幅减少 Gradle 插件的样板代码，并以"开箱即用"的方式集成 Java/Groovy/Kotlin 编译、Spring Boot、依赖管理（BOM）、代码质量、发布等常见构建场景。

- **开发语言**: Groovy 4.0.x（基于 `@CompileStatic`），构建脚本使用 Kotlin DSL
- **构建系统**: Gradle 9.4.1（通过 wrapper 锁定）
- **运行时要求**: JDK 17 ~ 25
- **测试框架**: Spock 2.4-groovy-4.0 + Gradle TestKit
- **静态分析**: CodeNarc 3.7.0、PMD 7.23.0
- **覆盖率**: JaCoCo 0.8.14（通过 `pl.droidsonroids.jacoco.testkit` 适配 TestKit）

## PROJECT STRUCTURE

```
plugins/
├── ihub-base/              # 注解驱动框架（@IHubPlugin/@IHubExtension/@IHubProperty/@IHubTask）
├── ihub-base-test/         # 测试基础设施（IHubSpecification + TestKit 工具）
├── ihub-plugins/           # 核心插件：pub.ihub.plugin / ihub-version / ihub-profiles
├── ihub-settings/          # Settings 插件：pub.ihub.plugin.ihub-settings
├── ihub-bom/               # BOM 依赖管理：pub.ihub.plugin.ihub-bom
├── ihub-java/              # Java 插件：ihub-java / ihub-java-base
├── ihub-groovy/            # Groovy 插件：ihub-groovy
├── ihub-kotlin/            # Kotlin 插件：ihub-kotlin
├── ihub-spring/            # Spring Boot 插件：ihub-boot / ihub-native
├── ihub-verification/      # 验证插件：ihub-test / ihub-verification
├── ihub-publish/           # 发布插件：ihub-publish
├── ihub-shadow/            # Shadow Fat JAR：ihub-shadow
├── ihub-javaagent/         # Javaagent 插件：ihub-javaagent
├── ihub-meta/              # AI 元数据 JSON 生成：ihub-meta
├── ihub-skills/            # AI 技能文件自动安装：ihub-skills
├── ihub-copyright/         # Spotless 版权头：ihub-copyright
├── ihub-githooks/          # Git Hooks 自动化：ihub-git-hooks
├── ihub-node/              # Node.js / cnpm 支持：ihub-node
├── samples/                # 验证用样例（sample-groovy / sample-multi / sample-extensions）
├── docs/                   # VuePress 文档（docs/list/ 一插件一文档）
├── buildSrc/               # precompiled convention plugin（ihub.module-conventions），IP 兼容
├── gradle/libs.versions.toml  # 版本目录（修改依赖前必须先改这里）
├── build.gradle.kts        # 根构建脚本（仅保留 root-only 配置；公共配置已迁至 buildSrc）
└── settings.gradle.kts     # 通过 ihub-settings 自动包含子模块
```

## PLUGIN ID 速查表

| Plugin ID | 模块 | 说明 |
|-----------|------|------|
| `pub.ihub.plugin.ihub-settings` | ihub-settings | Settings 插件，自动聚合子项目 |
| `pub.ihub.plugin` | ihub-plugins | 基础插件，仓库与扩展属性 |
| `pub.ihub.plugin.ihub-version` | ihub-plugins | 依赖版本检查 |
| `pub.ihub.plugin.ihub-profiles` | ihub-plugins | Maven Profiles 兼容 |
| `pub.ihub.plugin.ihub-bom` | ihub-bom | BOM 依赖管理 |
| `pub.ihub.plugin.ihub-java` / `ihub-java-base` | ihub-java | Java 工程配置 |
| `pub.ihub.plugin.ihub-groovy` | ihub-groovy | Groovy 工程配置 |
| `pub.ihub.plugin.ihub-kotlin` | ihub-kotlin | Kotlin 工程配置 |
| `pub.ihub.plugin.ihub-boot` | ihub-spring | Spring Boot 集成 |
| `pub.ihub.plugin.ihub-native` | ihub-spring | GraalVM Native 编译 |
| `pub.ihub.plugin.ihub-test` | ihub-verification | 测试任务配置 |
| `pub.ihub.plugin.ihub-verification` | ihub-verification | CodeNarc/PMD/JaCoCo |
| `pub.ihub.plugin.ihub-publish` | ihub-publish | 发布到 Maven Central / Plugin Portal |
| `pub.ihub.plugin.ihub-shadow` | ihub-shadow | Fat JAR 打包 |
| `pub.ihub.plugin.ihub-javaagent` | ihub-javaagent | Java agent 集成 |
| `pub.ihub.plugin.ihub-copyright` | ihub-copyright | Spotless 版权头管理 |
| `pub.ihub.plugin.ihub-git-hooks` | ihub-githooks | Git hooks 配置 |
| `pub.ihub.plugin.ihub-node` | ihub-node | Node.js / cnpm 支持 |
| `pub.ihub.plugin.ihub-skills` | ihub-skills | AI 技能文件自动安装（Claude Code / Copilot / OpenCode） |
| `pub.ihub.plugin.ihub-meta` | ihub-meta | 项目结构元数据 JSON 生成，供 LLM/AI 工具使用 |

## VERSION COMPATIBILITY

| 插件版本 | JDK | Gradle |
|----------|-----|--------|
| **1.9.4+**（开发中） | 17 ~ 25 | 9.3.1+ |
| 1.9.3 | 17 ~ 25 | 9.3.1 |
| 1.9.1+ | 17 ~ 25 | 9.1.0 |
| 1.9.0 | 17 ~ 24 | 9.0.0 |
| 1.7.6+ | 17 ~ 23 | 8.13 |
| 1.7.2+ | 17 ~ 23 | 8.4 |
| 1.5.6+ | 17 ~ 21 | 8.0 |

> 当前 wrapper 锁定的 Gradle 版本以 `gradle/wrapper/gradle-wrapper.properties` 为准（当前为 **9.4.1**）。`settings.gradle.kts` 中引导版本为 `1.9.5`。

## COMMANDS（BUILD / LINT / TEST）

```bash
# 构建整个项目
./gradlew build

# 运行所有测试
./gradlew test

# 运行某个模块的单个测试类
./gradlew :ihub-plugins:test --tests "pub.ihub.plugin.IHubPluginTest"

# 运行单个测试方法
./gradlew :ihub-plugins:test --tests "pub.ihub.plugin.IHubPluginTest.test plugin application"

# 仅静态分析（跳过测试）
./gradlew check -x test

# 检查 Git commit message 格式
./gradlew commitCheck

# 发布到本地 Maven 仓库（用于 sample 联调）
./gradlew publishToMavenLocal

# Spotless（版权头/代码风格）
./gradlew spotlessCheck
./gradlew spotlessApply

# 清理
./gradlew clean
```

> 项目根的 `pre-commit` 钩子会运行：
> `./gradlew clean check -x test spotlessCheck spotlessApply`
> `commit-msg` 钩子会运行：`./gradlew commitCheck`

## CODE STYLE & GUIDELINES

### 1. Imports
- 禁止通配符导入（`import java.util.*`）。
- 顺序：JDK/Groovy 标准库 → Gradle API → 项目内部类。
- 移除未使用的 import。

### 2. Formatting & Types
- **缩进**：4 空格，禁止 Tab。
- **换行**：CRLF。
- **编码**：UTF-8。
- 优先使用 `@CompileStatic`；除非确有动态需求，否则方法签名应使用强类型而非 `def`。

### 3. Naming Conventions
- 插件类：`IHubXxxPlugin`
- 扩展类：`IHubXxxExtension`
- 任务类：`IHubXxxTask`
- 包名：`pub.ihub.plugin.<module>`
- 方法/变量：`camelCase`；常量：`UPPER_SNAKE_CASE`

### 4. Error Handling
- 异常信息必须有意义。
- 优先使用 `GradleException` 或具体异常，不要直接抛 `RuntimeException`。
- 严禁吞异常（空 `catch` 块）。

### 5. Plugin Architecture
- **延迟配置**：必须使用 `Property<T>` / `Provider<T>`；配置阶段不要 `.get()`。
- **任务注册**：必须 `tasks.register()`，禁止 `tasks.create()`。
- **任务查找**：必须 `tasks.named()`，禁止 `tasks.getByName()`。
- **扩展定义**：优先 interface + `Property<T>` getter；类形式必须用 `ObjectFactory` 注入。
- **注解驱动**：充分使用 `@IHubPlugin` / `@IHubExtension` / `@IHubProperty` / `@IHubTask` 减少样板。
- 项目级插件应继承 `IHubProjectPluginAware`。
- **避免 `afterEvaluate`**：能用 `Provider.map()` / `configureEach()` 解决就不要用 `afterEvaluate`。

### 6. Testing (Spock)
- 新代码必须 100% 覆盖。
- 测试类继承 `IHubSpecification`。
- BDD 风格：`setup:` / `when:` / `then:`。
- 使用 `@TempDir` 隔离测试环境。
- 注意：JaCoCo 不直接支持 Gradle TestKit，本项目通过 `pl.droidsonroids.jacoco.testkit` 适配（详见 `build.gradle.kts`）。

### 7. Documentation
- **永远不要跳过文档同步**。新增/修改特性时必须同步：
  - `README.md`
  - `docs/list/<plugin>.md`（每个插件对应一份）
  - `docs/CLAUDE.md`（如果 Agent 规则变了）
  - 本文件 `CLAUDE.md`（如果项目结构/命令变了）

### 8. Gradle Version Upgrades
- 升级 Gradle 必须：
  1. 阅读 [Release Notes](https://docs.gradle.org/current/release-notes.html)。
  2. 阅读对应版本的 [Upgrading Guide](https://docs.gradle.org/current/userguide/upgrading_version_*.html)。
  3. 排查并替换已弃用 API。
  4. 全量跑 `./gradlew test`。
  5. 更新 `gradle/wrapper/gradle-wrapper.properties`。
  6. 同步更新 README 的 "环境要求" 表格与本文件的 VERSION COMPATIBILITY 表。

### 9. Dependency Management
- 新增依赖必须先在 `gradle/libs.versions.toml` 中声明，再在子模块中通过 `libs.xxx` 引用。
- 修改第三方依赖版本时，优先在版本目录中调整，避免分散到各 `build.gradle.kts`。

## MANDATORY DEVELOPMENT WORKFLOW

1. 实现代码改动。
2. 编写 / 更新 Spock 测试，确保 100% 覆盖。
3. 本地运行 `./gradlew test`。
4. 运行 `./gradlew check -x test`，修复所有 CodeNarc/PMD 警告。
5. 运行 `./gradlew spotlessApply` 确保版权头与格式符合规范。
6. 同步更新 `docs/list/`、`README.md`，必要时更新 `CLAUDE.md`。
7. 最终验证 `./gradlew build` 通过。

## ANTI-PATTERNS（DO NOT DO THIS）

- ❌ 使用已弃用的 Gradle API（如 `buildDir` 应改为 `layout.buildDirectory`）。
- ❌ 跳过测试或静态分析。
- ❌ 配置阶段对 `Property` / `Provider` 调用 `.get()`。
- ❌ `tasks.create()` / `tasks.getByName()`。
- ❌ 破坏向后兼容性（必须保留旧的扩展属性别名直到下一个主版本）。
- ❌ 直接在子模块中写死依赖版本，绕开 `libs.versions.toml`。
- ❌ 用 `afterEvaluate` 做能用 lazy API 完成的事。
- ❌ 升级 Gradle 不读 release notes、不跑全量测试。
- ❌ 提交不含版权头的源文件（pre-commit 钩子会失败）。

## GRADLE API STANDARDS

### Modern API vs Legacy API

| Legacy（禁止） | Modern（必须使用） |
|----------------|-------------------|
| `tasks.create('name')` | `tasks.register('name')` |
| `tasks.getByName('name')` | `tasks.named('name')` |
| `buildDir` | `layout.buildDirectory` |
| `project.files()` | `project.layout.files()` |
| `project.file()` | `project.layout.file()` |
| `extensions.create()` | Interface 扩展 + `ObjectFactory` 注入 |
| `afterEvaluate { }` | `Provider.map()` / `configureEach()` |

### Extension Definition Pattern

```groovy
// 推荐：interface（Gradle 自动生成实现）
@IHubExtension('iHub')
interface IHubDemoExtension extends IHubExtensionAware {
    @IHubProperty(genericType = Boolean, defaultValue = 'true')
    Property<Boolean> getEnabled()
}

// 备选：class + ObjectFactory
@IHubExtension('iHub')
class IHubDemoExtension implements IHubExtensionAware {
    private final Property<Boolean> enabled

    @Inject
    IHubDemoExtension(ObjectFactory objects) {
        enabled = objects.property(Boolean).convention(true)
    }

    Property<Boolean> getEnabled() { enabled }
}
```

### Property 优先级（高 → 低）

1. `-DiHub.property=value`（系统属性）
2. `IHUB_PROPERTY=value`（环境变量，下划线分隔大写）
3. `gradle.properties` / `-Pproperty=value`
4. `@IHubProperty(defaultValue = ...)` 默认值

## 子项目公共配置（buildSrc/ihub.module-conventions）

公共配置已迁移至 `buildSrc/src/main/kotlin/ihub.module-conventions.gradle.kts`（precompiled convention plugin）。每个子模块在自己的 `plugins {}` 块顶部 `id("ihub.module-conventions")` 即可获得：

- `java-gradle-plugin`、`com.gradle.plugin-publish`、`pl.droidsonroids.jacoco.testkit`（中性插件）
- `groovy-bom 4.0.29` / `spock-bom 2.4-groovy-4.0` 强制对齐（通过 `iHubBom { importBoms }` 推迟到 `pub.ihub.plugin.ihub-bom` apply 后注入；`enforcedPlatform()` 无法压制 plugin-publish/testkit 拉入的 Groovy 5.0.4 strict 请求）
- `gradlePlugin` website / vcsUrl
- `GenerateModuleMetadata` 全部禁用（避免 Plugin Portal 上传问题）

IHub 自身链 `pub.ihub.plugin.ihub-groovy` / `ihub-test` / `ihub-verification` / `ihub-publish` 仍由各子模块在自身 `plugins {}` 块单独 apply（这些插件依赖运行时由 `pub.ihub.plugin.ihub-settings` 注册的 `ihub` 版本目录，无法在 buildSrc 拉到根 classpath，否则会与 root build 已有的 `alias(ihub.plugins.root)` 冲突）。

## ADVANCED PLUGIN AUTHORING RULES（专业 Gradle 插件作者必读）

> 本节是 2026-04-27 审计后新增的进阶规范，针对 Gradle 8.5+ / 9.x 引入的新特性与最佳实践。新增插件必须遵循；老插件改动时须顺手迁移。

### 1. Configuration Cache（CC）兼容性

- 所有插件**必须**与 `--configuration-cache` 兼容。
- `gradle.properties` 应启用 `org.gradle.configuration-cache=true`，CI 必须用 `--configuration-cache` 跑全量测试。
- **禁止在任务执行体内捕获 `Project`**：
  - ❌ `task.doLast { project.file(...) }`
  - ❌ `task.doFirst { extension.foo.get() }`（捕获了持有 project 的 extension）
  - ❌ `@TaskAction` 内通过插件实例字段访问 `project`
  - ✅ 任务用 `@Input` / `@InputFile` / `@OutputFile` / `@OutputDirectory` 等注解声明输入输出，执行体只读写这些 `Property` / `RegularFileProperty`。
- 调试 CC 失败：`./gradlew <任务> --configuration-cache --info` 查看序列化报告。

### 2. Isolated Projects（IP）准备

- Gradle 8.5+ 引入 Isolated Projects；9.x 持续收紧。
- ❌ **禁止新增** `allprojects {}` / `subprojects {}` 块（包括 root build script）。
- ❌ **禁止跨 project 读取**：`project.rootProject.subprojects.each { ... }`、`project.parent.tasks` 等。
- ✅ 公共配置抽到 `buildSrc/` 的**precompiled convention plugin**，每个子模块单独 `apply` 自己需要的约定。
- 例：把当前 root `subprojects { apply plugin: "..." }` 拆成 `buildSrc/src/main/kotlin/ihub.module-conventions.gradle.kts`，再在每个 `ihub-*/build.gradle.kts` 顶部 `plugins { id("ihub.module-conventions") }`。

### 3. Task 输入输出注解

自定义 Task 类**必须**正确声明：

| 注解 | 用途 |
|------|------|
| `@Input` | 标量输入（String/Number/Boolean/枚举） |
| `@InputFile` / `@InputFiles` / `@InputDirectory` | 文件/目录输入 |
| `@OutputFile` / `@OutputDirectory` / `@OutputFiles` | 输出 |
| `@Internal` | 不参与 up-to-date 检查（如 `Project` 引用） |
| `@Optional` | 可选输入 |
| `@PathSensitive(NAME_ONLY/RELATIVE/ABSOLUTE)` | 路径敏感度，影响构建缓存命中率 |
| `@Classpath` / `@CompileClasspath` | 类路径输入 |
| `@Nested` | 嵌套对象输入 |

未标注的属性会导致 Gradle 警告 + CC 不兼容 + 无法增量构建。

### 4. 强类型 Property 变体

❌ 禁止：`Property<List<String>>` / `Property<Map<String,String>>` / `Property<File>`
✅ 必须使用对应的类型化 Property：

| 错误 | 正确 |
|------|------|
| `Property<List<T>>` | `ListProperty<T>` |
| `Property<Set<T>>` | `SetProperty<T>` |
| `Property<Map<K,V>>` | `MapProperty<K,V>` |
| `Property<File>`（普通文件） | `RegularFileProperty` |
| `Property<File>`（目录） | `DirectoryProperty` |

类型化 Property 才能正确支持 lazy 序列化、CC 与 IP。

### 5. Provider 组合惯用法

- `Provider.map { ... }` —— 单值映射
- `Provider.flatMap { ... }` —— 返回 Provider 的映射（避免嵌套）
- `Provider.zip(other) { a, b -> ... }` —— 合并两个 Provider
- `Provider.orElse(default)` —— 默认值
- ❌ 禁止 `provider1.get() + provider2.get()` 这种立即求值组合

### 6. ValueSource（CC 安全的外部输入）

读取 git/env/外部命令/文件**必须**包装成 `ValueSource`：

```groovy
abstract class GitShaValueSource implements ValueSource<String, ValueSourceParameters.None> {
    @Override String obtain() {
        // 执行 git rev-parse HEAD ...
    }
}
// 使用：
Provider<String> sha = providers.of(GitShaValueSource) {}
```

直接在配置阶段执行 `["git","rev-parse","HEAD"].execute().text` 会破坏 CC。本项目 `IHubVersionPlugin` 的 git 操作应迁移到此模式。

### 7. BuildService（共享状态）

跨任务共享可变状态（HTTP 客户端、连接池、计数器、缓存）**必须**用 `BuildService`：

```groovy
abstract class HttpClientService implements BuildService<BuildServiceParameters.None>, AutoCloseable {
    // ...
}
// 使用：
Provider<HttpClientService> svc = gradle.sharedServices.registerIfAbsent('http', HttpClientService) {}
task.usesService(svc)
```

❌ 禁止 `static` 字段、`object`、单例。

### 8. ProblemReporter API（Gradle 8.6+）

报告插件诊断（警告、错误、配置问题）**优先**用 `ProblemReporter`：

```groovy
@Inject abstract Problems getProblems()
problems.reporter.reporting {
    it.id('ihub-bom', 'Invalid BOM coordinate')
      .severity(Severity.WARNING)
      .solution('请使用 group:artifact:version 格式')
}
```

IDE（如 IDEA）能识别并展示在 Build Output 面板。

### 9. 弃用策略

向后兼容必须遵循：

1. **保留旧 API**，标 `@Deprecated`，添加 `@since` 注释指向新 API。
2. 在 getter/setter 内部调用 `nagUserOfReplacedProperty('oldProp', 'newProp')`（来自 Gradle internal）。
3. 在 `docs/list/<plugin>.md` 中添加 **Migration** 段落。
4. 至少保留一个主版本；下一主版本 release notes 中再删除。

### 10. 测试分层

| 类型 | 用途 | 工具 |
|------|------|------|
| **单元测试** | 测 plugin 类、extension 类、辅助方法 | Spock，无 TestKit，不 apply 真实 Gradle |
| **功能测试** | 测整个插件 apply 后的行为 | Spock + `GradleRunner` (TestKit) |
| **样例测试** | 测端到端构建 | `samples/*` 通过 `:samples:build` 验证 |

Spock 类目录建议：`src/test/groovy`（单元）+ `src/functionalTest/groovy`（功能），通过 `gradlePlugin.testSourceSets(...)` 接入。

### 11. 最低 Gradle 版本声明

`gradlePlugin` 块应声明兼容范围：

```kotlin
gradlePlugin {
    // ...
    plugins {
        create("iHubXxx") { /* ... */ }
    }
}
// 通过 java-gradle-plugin 自动生成 plugin marker；MinGradleVersion 通过 README/docs 声明。
```

每次升级 Gradle wrapper 后，须复检本仓库 `VERSION COMPATIBILITY` 表 + Plugin Portal 描述里的最低版本要求。

### 12. Configuration 注册

- ✅ `configurations.register('myConf')` —— lazy
- ❌ `configurations.create('myConf')` —— eager
- ✅ `configurations.named('myConf') { ... }`

### 13. 框架级 afterEvaluate 限制

`IHubProjectPluginAware.afterEvaluate(Closure)` 是框架对 Gradle `project.afterEvaluate` 的统一封装，**仅供框架内部和 `withExtension(AFTER) { }` 链路使用**。

- ❌ 子类插件**禁止**直接调用 `afterEvaluate { }`。
- ✅ 替代方案：
  - 用 `Provider.map { }` / `Provider.zip { }` 推迟到执行阶段
  - 用 `tasks.named().configure { }` lazy 配置任务
  - 用 `extension.foo.convention(provider { ... })` 设置默认值
  - 确实需要"读最终配置"时，改用 `withExtension(AFTER) { ... }`（让框架统一管理时序）

### 14. Gradle 版本升级 Checklist（追加）

在原有清单基础上，每次升级 Gradle 必须额外检查：

- [ ] `--configuration-cache` 全量测试通过
- [ ] `-Dorg.gradle.unsafe.isolated-projects=true` 全量测试（≥9.x 应能通过）
- [ ] release notes 中的 deprecation 是否影响本项目（重点关注 `Project.getConvention()`、`buildDir`、`Configuration.dependencies` 直改 等）
- [ ] Plugin Portal 是否提升了最低 Gradle 版本要求
- [ ] `pluginUnderTestMetadata` 任务输出是否仍正确生成

## CONFIGURATION CACHE STATUS（2026-04-28 探针记录）

> 本节记录截至当前对 `--configuration-cache` 的探测结果与已知阻塞点，便于后续迭代逐项修复。

### 探针命令

```bash
./gradlew :ihub-java:test --configuration-cache
./gradlew :ihub-bom:test  --configuration-cache
./gradlew :ihub-publish:test --configuration-cache
```

### 已知 CC 不兼容点

| 模块 | 位置 | 问题 | 修复方向 |
|------|------|------|----------|
| **root build.gradle.kts** | `iHubGitHooks { ... }` 块 | 当前应用的是已发布的 `1.9.5` 插件 jar，仍含旧的配置阶段 `git config` | 等下一版（含本仓库当前修复）发布后即解除；自身源码已 CC-clean |
| Gradle 内置 | `buildDashboard` / `buildNeeded` / `groovydoc` / `codenarcMain` | 这些任务在执行时调 `Task.project` | 等待 Gradle 上游修复（≥9.5 起部分已修），或在 CI 跳过 `:buildDashboard` |
| `io.freefair.gradle.plugins.git.GitVersionPlugin` | 在配置阶段执行 `git`、读 `JENKINS_HOME` | 第三方 | 升级到上游 CC-friendly 版本，或自己包一层 `ValueSource` |

### 启用策略

- ❌ **暂不**在 `gradle.properties` 启用 `org.gradle.configuration-cache=true`（root build 的 `iHubGitHooks {}` 块仍指向旧 jar；待新版发布后解除）。
- ✅ 子模块源码自身（`ihub-githooks` / `ihub-plugins` 等）已 CC-clean，可在 CI 加 `--configuration-cache` 跑核心模块（ihub-java / ihub-bom / ihub-publish 等）。
- ✅ 同步在 CI 增加 `-Dorg.gradle.unsafe.isolated-projects=true` 探针（一旦 P0-4 root subprojects 块迁到 buildSrc 即可启用）。

### 已修复的 CC 阻塞点（追溯记录）

- ✅ `IHubVerificationPlugin` jacoco 报告：移除 `afterEvaluate { task.classDirectories.from = ... }`，改用 `setFrom(provider { ... })`；`doLast` 显式持有 `pluginRef = this`。
- ✅ `IHubJavaPlugin` lombok.config：移除 `afterEvaluate { lombokConfig.createNewFile() ... }`，改为带 `@OutputFile` 的 `IHubLombokConfigTask`。
- ✅ `IHubGitHooksPlugin`：移除 `withExtension(AFTER) { it.execute(...) }`，git config 调用迁移到 `IHubGitHooksSetupTask`（`@TaskAction` + `providers.exec`）；`commitCheck` 重构为 `IHubCommitCheckTask`（带 `@InputFile` / `@Internal`），不再捕获 `project`。
- ✅ `IHubVersionPlugin`：`'git describe --tags'.execute().text` 改为 `providers.of(GitDescribeValueSource) { }`，是 CC-safe 的外部进程调用。
- ✅ **P0-4 Phase A**（2026-04-28）：root `build.gradle.kts` 中 `subprojects {}` 块迁至 `buildSrc/src/main/kotlin/ihub.module-conventions.gradle.kts`，每个 ihub-* 模块在自身 `plugins {}` 块顶部 `id("ihub.module-conventions")` 即可获得共享公共配置。`tasks.withType(...)` 升级为 `configureEach`。
- ✅ **Phase B 增量**（2026-04-29）：
  - `ihub-plugins`：`cleanRootProject()` 移除 `afterEvaluate` 包裹与 `project.subprojects` 跨项目访问。
  - `ihub-version`（`ihub-plugins` 子模块）：`replaceLastVersion` 由 `allprojects` 循环改为只操作当前 project；`apply()` 移除 `afterEvaluate { configProjectWithGit() }` 包裹，改为直接调用。
  - `ihub-profiles`：裸 `afterEvaluate` 替换为 `withExtension(AFTER)` 链路。
  - `ihub-publish`：裸 `afterEvaluate` 替换为 `withExtension(AFTER) { ext -> ... }`；`registerJavadocsJar` / `registerGroovydocJar` 由 `tasks.getByName(...)` 升级为 `tasks.named(...)` lazy 链路。

### Phase B 待办（HARD IP 阻塞）

下列仍在源码内部（不在 root build），需逐一拆解：

- `ihub-bom`：`gradle.taskGraph.whenReady { ... }`
- `ihub-verification`：`gradle.buildFinished { ... }` + `extension.rootProject.allprojects { ... }`
- `ihub-settings`：`rootProject.allprojects { ... }` × 2 + `rootProject.subprojects { ... }`
