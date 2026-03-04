# IHub Plugins - AI Agent Rules

**Date:** 2026-03-05
**Context:** Rules and instructions for AI Agent developers working in `IHub Plugins`.

## 1. Overview
IHub Plugins is an infrastructure plugin collection for Gradle projects, primarily written in Groovy. Testing is done via Spock, and static analysis uses CodeNarc.

## 2. Mandatory Development Workflow
Any time you write or modify code, you **MUST** follow these steps:
1. **Implementation**: Write the feature or bug fix.
2. **Code Quality**: Run static analysis and fix all errors.
   - `./gradlew check -x test`
3. **Testing**: Write Spock tests achieving **100% test coverage**.
   - Run a single test: `./gradlew :ihub-plugins:test --tests "pub.ihub.plugin.IHubPluginTest"`
   - Run all tests: `./gradlew test`
4. **Documentation**: Code and documentation **MUST** correspond. Always synchronize doc updates!
   - Update `docs/list/<plugin_name>.md`
   - Update `README.md`
   - Update this file `docs/AGENTS.md` if agent rules change.
5. **Final Verification**: `./gradlew build`

## 3. Code Style & Guidelines

### Imports
- Avoid wildcard imports (e.g., `import java.util.*`).
- Group imports logically: Java/Groovy standard libraries first, Gradle APIs next, then project classes.
- Remove unused imports.

### Formatting & Types
- **Indentation**: 4 spaces, no tabs.
- **Line endings**: CRLF.
- **Encoding**: UTF-8.
- Use `@CompileStatic` for performance-critical code.
- Prefer strong typing over `def` for method parameters and return types.

### Naming Conventions
- **Plugin classes**: `IHubXxxPlugin`
- **Extension classes**: `IHubXxxExtension`
- **Task classes**: `IHubXxxTask`
- **Packages**: `pub.ihub.plugin.<module>`
- Methods and variables: `camelCase`
- Constants: `UPPER_SNAKE_CASE`

### Error Handling
- Meaningful exception messages are mandatory.
- Prefer `GradleException` over generic `RuntimeException`.
- Do not swallow exceptions (no empty catch blocks).

### Plugin Architecture
- **Lazy Configuration**: ALWAYS use `Property<T>` and `Provider<T>`. Never evaluate (`.get()`) during the configuration phase.
- **Task Registration**: ALWAYS use `register()` rather than `create()`.
- **Annotations**: Extensively use `@IHubPlugin`, `@IHubExtension`, `@IHubProperty`, and `@IHubTask`.

## 4. Build & Test Commands Summary
- Build project: `./gradlew build`
- Run all tests: `./gradlew test`
- Run single test class: `./gradlew :<module>:test --tests "<package.ClassTest>"`
- Run single test method: `./gradlew :<module>:test --tests "<package.ClassTest>.<method name>"`
- Static analysis: `./gradlew check -x test`
- Local publish: `./gradlew publishToMavenLocal`

## 5. Anti-Patterns
- ❌ Using deprecated Gradle APIs (e.g., `buildDir`).
- ❌ Skipping static analysis or tests.
- ❌ Discrepancies between code and docs.
