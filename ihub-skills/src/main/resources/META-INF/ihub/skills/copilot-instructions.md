# IHub Plugins - GitHub Copilot Instructions

This project uses the `pub.ihub.plugin` suite of Gradle plugins. When helping with this codebase, please follow these guidelines:

## Plugin Overview

| Plugin ID | Module | Purpose |
|-----------|--------|---------|
| `pub.ihub.plugin.ihub-settings` | ihub-settings | Settings plugin, auto-aggregates subprojects |
| `pub.ihub.plugin` | ihub-plugins | Base plugin, repositories and extensions |
| `pub.ihub.plugin.ihub-bom` | ihub-bom | BOM dependency management |
| `pub.ihub.plugin.ihub-java` | ihub-java | Java project configuration |
| `pub.ihub.plugin.ihub-groovy` | ihub-groovy | Groovy project configuration |
| `pub.ihub.plugin.ihub-kotlin` | ihub-kotlin | Kotlin project configuration |
| `pub.ihub.plugin.ihub-boot` | ihub-spring | Spring Boot integration |
| `pub.ihub.plugin.ihub-test` | ihub-verification | Test task configuration |
| `pub.ihub.plugin.ihub-verification` | ihub-verification | CodeNarc/PMD/JaCoCo |
| `pub.ihub.plugin.ihub-publish` | ihub-publish | Publish to Maven Central |
| `pub.ihub.plugin.ihub-git-hooks` | ihub-githooks | Git hooks automation |
| `pub.ihub.plugin.ihub-skills` | ihub-skills | AI skills integration |

## Gradle API Rules (IMPORTANT)

**Always use modern APIs:**

| ❌ Legacy (Forbidden) | ✅ Modern (Required) |
|----------------------|---------------------|
| `tasks.create('name')` | `tasks.register('name')` |
| `tasks.getByName('name')` | `tasks.named('name')` |
| `buildDir` | `layout.buildDirectory` |
| `Property<List<T>>` | `ListProperty<T>` |
| `Property<Map<K,V>>` | `MapProperty<K,V>` |
| `Property<File>` (file) | `RegularFileProperty` |
| `Property<File>` (dir) | `DirectoryProperty` |
| `afterEvaluate {}` | `Provider.map()` / `configureEach()` |
| `allprojects {}` / `subprojects {}` | buildSrc convention plugins |

## Dependency Management

- All dependency versions **must** be declared in `gradle/libs.versions.toml`
- Reference them via `libs.xxx` in submodule build scripts
- Never hardcode versions directly in `build.gradle.kts` files

## Property Priority (High to Low)

1. `-DiHub.property=value` (system property)
2. `IHUB_PROPERTY=value` (environment variable)
3. `gradle.properties` / `-Pproperty=value`
4. `@IHubProperty(defaultValue = ...)` default value

## Configuration Cache Compatibility

- Task action bodies must NOT capture `Project` references
- Use `@Input` / `@InputFile` / `@OutputFile` annotations for task properties
- External commands must use `ValueSource` pattern
- Test with `--configuration-cache` flag

## Common Fixes

| Symptom | Cause | Fix |
|---------|-------|-----|
| `Cannot serialize object of type Project` | Task captured Project | Use `@Input` annotations instead |
| `buildDir is deprecated` | Old API | Use `layout.buildDirectory` |
| BOM dependency conflict | Version misalignment | Manage in `gradle/libs.versions.toml` |

## Reference

- IHub Plugins Documentation: https://doc.ihub.pub/plugins/
- Version Compatibility: https://github.com/ihub-pub/plugins#readme
