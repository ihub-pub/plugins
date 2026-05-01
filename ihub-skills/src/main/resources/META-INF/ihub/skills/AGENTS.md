# IHub Plugins - Agent Instructions

This project uses the `pub.ihub.plugin` suite of Gradle plugins. Follow these guidelines when helping with this codebase.

## Quick Reference

### Plugin Selection Guide

| Need | Plugin to Use |
|------|--------------|
| Multi-project setup | `pub.ihub.plugin.ihub-settings` |
| Java project | `pub.ihub.plugin.ihub-java` |
| Groovy project | `pub.ihub.plugin.ihub-groovy` |
| Kotlin project | `pub.ihub.plugin.ihub-kotlin` |
| Spring Boot | `pub.ihub.plugin.ihub-boot` |
| Code quality + coverage | `pub.ihub.plugin.ihub-verification` |
| Maven Central publish | `pub.ihub.plugin.ihub-publish` |
| Git commit conventions | `pub.ihub.plugin.ihub-git-hooks` |
| AI tool integration | `pub.ihub.plugin.ihub-skills` |

### Minimal settings.gradle.kts

```kotlin
plugins {
    id("pub.ihub.plugin.ihub-settings") version "1.9.5"
}
```

### Typical build.gradle.kts (Java + Spring Boot)

```kotlin
plugins {
    id("pub.ihub.plugin")
    id("pub.ihub.plugin.ihub-java")
    id("pub.ihub.plugin.ihub-boot")
    id("pub.ihub.plugin.ihub-test")
    id("pub.ihub.plugin.ihub-verification")
}
```

## Gradle API Rules

**Never use deprecated APIs. Always prefer:**

- `tasks.register()` over `tasks.create()`
- `tasks.named()` over `tasks.getByName()`
- `layout.buildDirectory` over `buildDir`
- `ListProperty<T>` over `Property<List<T>>`
- `DirectoryProperty` over `Property<File>` for directories
- `RegularFileProperty` over `Property<File>` for files
- `Provider.map()` over `afterEvaluate {}`

## Dependency Management

All versions **must** be in `gradle/libs.versions.toml`. Never hardcode versions in build scripts.

## Property Configuration

IHub properties follow this priority (highest to lowest):
1. System property: `-DiHub.xxx=value`
2. Environment variable: `IHUB_XXX=value`
3. `gradle.properties`
4. Plugin default value

## Common Anti-Patterns to Avoid

```groovy
// ❌ Wrong
tasks.create('myTask') { ... }
def dir = buildDir
Property<List<String>> items

// ✅ Correct
tasks.register('myTask') { ... }
def dir = layout.buildDirectory
ListProperty<String> items
```

## Useful Commands

```bash
./gradlew build              # Full build
./gradlew test               # Run tests
./gradlew check -x test      # Static analysis only
./gradlew spotlessApply      # Fix code style
./gradlew commitCheck        # Validate commit message
```

## Reference

- Documentation: https://doc.ihub.pub/plugins/
- Repository: https://github.com/ihub-pub/plugins
