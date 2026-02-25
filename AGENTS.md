# IHub Plugins - AI Agent Guide

## Project Overview

IHub Plugins is a Gradle plugin collection that provides infrastructure for Gradle projects, significantly simplifying project configuration. The project is developed primarily in Groovy and follows Gradle plugin development best practices.

### Technology Stack

| Technology | Version | Description |
|------------|---------|-------------|
| Language | Groovy 4.0.x | Primary development language |
| Build Tool | Gradle 9.x | Build system |
| Testing | Spock 2.4 | BDD testing framework |
| Test Kit | Gradle TestKit | Plugin testing infrastructure |
| Java | 17~25 | Target Java versions |
| Code Quality | CodeNarc 3.7 | Groovy static analysis |

### Version Compatibility

| Plugin Version | Java | Gradle |
|---------------|------|--------|
| 1.9.3+ | 17~25 | 9.3.1 |
| 1.9.1+ | 17~25 | 9.1.0 |
| 1.9.0 | 17~24 | 9.0.0 |
| 1.7.6+ | 17~23 | 8.13 |

## Project Structure

```
plugins/
├── build.gradle.kts          # Root build configuration
├── settings.gradle.kts       # Project settings
├── gradle.properties         # Gradle properties
├── gradle/
│   ├── wrapper/              # Gradle wrapper
│   └── libs.versions.toml    # Version catalog
├── ihub-base/                # Core plugin infrastructure
│   └── src/main/groovy/pub/ihub/plugin/
│       ├── IHubPlugin.groovy              # Plugin annotation
│       ├── IHubExtension.groovy           # Extension annotation
│       ├── IHubExtensionAware.groovy      # Extension trait
│       ├── IHubProjectPluginAware.groovy  # Project plugin base
│       ├── IHubProperty.groovy            # Property annotation
│       └── IHubTask.groovy                # Task annotation
├── ihub-base-test/           # Test infrastructure
│   └── src/main/groovy/pub/ihub/plugin/test/
│       └── IHubSpecification.groovy       # Spock test base class
├── ihub-plugins/             # Core plugins (repository, version, profiles)
├── ihub-settings/            # Settings plugin
├── ihub-bom/                 # BOM/dependency management plugin
├── ihub-java/                # Java plugin
├── ihub-groovy/              # Groovy plugin
├── ihub-kotlin/              # Kotlin plugin
├── ihub-spring/              # Spring Boot plugin
├── ihub-verification/        # Verification plugin (test, code quality)
├── ihub-publish/             # Publishing plugin
├── ihub-shadow/              # Shadow plugin
├── ihub-copyright/           # Copyright plugin
├── ihub-githooks/            # Git hooks plugin
├── ihub-node/                # Node.js plugin
├── ihub-javaagent/           # Javaagent plugin
├── samples/                  # Sample projects
│   ├── sample-groovy/        # Groovy sample
│   └── sample-multi/         # Multi-module sample
└── docs/                     # Documentation (VuePress)
```

## Plugin Architecture

This project follows Gradle's official plugin development best practices. Refer to:
- [Gradle Plugin Development Guide](https://docs.gradle.org/current/userguide/implementing_gradle_plugins.html)
- [Gradle Plugin Portal](https://plugins.gradle.org/)

### Core Annotations

1. **@IHubPlugin** - Marks a class as an IHub plugin
   ```groovy
   @IHubPlugin(
       value = MyExtension,           // Extension class (Gradle extension)
       beforeApplyPlugins = [],       // Plugins to apply before
       tasks = []                     // Associated tasks
   )
   ```

2. **@IHubExtension** - Defines extension name (maps to Gradle's project.extensions.create())
   ```groovy
   @IHubExtension('myExtension')
   ```

3. **@IHubProperty** - Configures extension property using Gradle's Property API
   ```groovy
   @IHubProperty(type = [PROJECT, ENV, SYSTEM], defaultValue = '...')
   ```

4. **@IHubTask** - Registers a task using Gradle's TaskContainer.register()
   ```groovy
   @IHubTask(
       value = 'taskName',
       group = 'ihub',
       description = 'Task description'
   )
   ```

### Plugin Base Classes

| Class | Purpose | Gradle Equivalent |
|-------|---------|-------------------|
| `IHubProjectPluginAware<T>` | Base class for Project plugins | `Plugin<Project>` |
| `IHubExtensionAware` | Trait for extension implementations | Extension interface |
| `IHubProjectExtensionAware` | Extension with Project reference | Extension with Project |
| `IHubTask` | Base class for custom tasks | `DefaultTask` |

### Gradle Best Practices Compliance

This project adheres to Gradle's official plugin development guidelines:

#### 1. Lazy Configuration (Provider API)

Use Gradle's `Provider` and `Property` APIs for lazy evaluation:

```groovy
interface MyExtension extends IHubProjectExtensionAware {
    @IHubProperty(defaultValue = 'true', genericType = Boolean)
    Property<Boolean> getEnabled()
    
    Property<String> getOutputDir()
}
```

#### 2. Task Registration

Use `register()` instead of `create()` for lazy task configuration:

```groovy
registerTask('myTask', MyTask) {
    it.group = 'ihub'
    it.description = 'My custom task'
}
```

#### 3. Avoid Configuration Phase Work

Expensive operations should be deferred using `afterEvaluate`:

```groovy
withExtension(AFTER) { ext ->
    // Configuration that depends on extension values
}
```

#### 4. Incremental Build Support

Tasks should declare inputs/outputs for caching:

```groovy
class MyTask extends DefaultTask {
    @InputFile
    File inputFile

    @OutputDirectory
    File outputDir

    @TaskAction
    void execute() {
        // Task implementation
    }
}
```

### Creating a New Plugin

Follow this pattern for creating new plugins:

```groovy
@IHubPlugin(MyPluginExtension)
class MyPlugin extends IHubProjectPluginAware<MyPluginExtension> {

    @Override
    void apply() {
        // Apply prerequisite plugins
        applyPlugin SomeRequiredPlugin
        
        // Configure extension with lazy evaluation
        withExtension(AFTER) { ext ->
            configureProject ext
        }
    }
    
    private void configureProject(MyPluginExtension ext) {
        // Register tasks lazily
        if (ext.enabled.get()) {
            registerTask('myTask', MyTask) {
                it.inputFile.set(ext.inputFile)
                it.outputDir.set(ext.outputDir)
            }
        }
    }
}

@IHubExtension('myPlugin')
interface MyPluginExtension extends IHubProjectExtensionAware {

    @IHubProperty(defaultValue = 'true', genericType = Boolean)
    Property<Boolean> getEnabled()
    
    @IHubProperty
    Property<String> getInputFile()
    
    @IHubProperty(defaultValue = '${project.buildDir}/output')
    Property<String> getOutputDir()

}
```

### Plugin Lifecycle

1. **Settings Phase** - `IHubSettingsPlugin` configures plugin repositories
2. **Configuration Phase** - Plugin `apply()` method executes, extensions registered
3. **After Evaluate** - Extension properties resolved, tasks configured
4. **Execution Phase** - Only requested tasks execute

## Build Commands

```bash
# Build project
./gradlew build

# Run tests
./gradlew test

# Run checks (without tests)
./gradlew check -x test

# Check commit message
./gradlew commitCheck

# Publish to local
./gradlew publishToMavenLocal

# Clean build
./gradlew clean
```

## Testing Guidelines

This project uses Gradle TestKit and Spock Framework for testing. Refer to:
- [Gradle TestKit Guide](https://docs.gradle.org/current/userguide/test_kit.html)
- [Spock Framework Documentation](https://spockframework.org/)

### Test Base Class

All tests should extend `IHubSpecification`:

```groovy
class MyPluginTest extends IHubSpecification {

    def 'test description'() {
        setup: 'Initialize project'
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin'
            }
        '''

        when: 'Build project'
        def result = gradleBuilder.build()

        then: 'Check results'
        result.output.contains 'BUILD SUCCESSFUL'
    }
}
```

### Gradle TestKit Best Practices

1. **Use GradleRunner for functional tests**
   ```groovy
   def result = gradleBuilder
       .withArguments('build', '--stacktrace')
       .build()
   ```

2. **Test both success and failure scenarios**
   ```groovy
   when: 'Build with invalid configuration'
   def result = gradleBuilder.buildAndFail()

   then: 'Verify error message'
   result.output.contains 'expected error'
   ```

3. **Use temporary directories for isolation**
   ```groovy
   @TempDir
   protected File testProjectDir
   ```

4. **Test with different Gradle versions for compatibility**
   ```groovy
   gradleBuilder.withGradleVersion('8.5')
   ```

### Test Utilities

| Method | Description |
|--------|-------------|
| `copyProject(fileName)` | Copy sample build file |
| `copyProject(name, dirs)` | Copy sample project with directories |
| `newFile(path)` | Create new file in test project |
| `newFolder(path)` | Create new folder in test project |

### Test Resources

- Test configurations: `ihub-base-test/src/main/resources/`
- Sample projects: `samples/`
- Sample extensions: `samples/sample-extensions/`

## Code Style

### Groovy Conventions

- **Indentation**: 4 spaces
- **Line endings**: CRLF
- **Encoding**: UTF-8
- **Trailing whitespace**: Trimmed
- **Final newline**: Required

### Code Style Guidelines

1. Use `@CompileStatic` for performance-critical code
2. Use strong typing where possible
3. Follow Groovy idiomatic patterns
4. Use annotation-based plugin configuration
5. Prefer composition over inheritance

### CodeNarc Configuration

CodeNarc rules are configured in:
- `ihub-verification/src/main/resources/META-INF/codenarc.groovy`

### License Header

All source files must include the Apache License header:

```groovy
/*
 * Copyright (c) 2021-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```

## Extension Properties

Properties follow Gradle's configuration avoidance API and can be configured via:

1. **gradle.properties** file (Project properties)
2. **Environment variables** (uppercase with underscores)
3. **System properties** (-D flags)
4. **Command-line properties** (-P flags)

### Property Priority (Highest to Lowest)

1. System properties (`-D`)
2. Environment variables
3. Project properties (`-P` or `gradle.properties`)
4. Default value in annotation

### Property Resolution Example

```groovy
@IHubProperty(
    type = [PROJECT, ENV, SYSTEM],
    defaultValue = 'false',
    genericType = Boolean
)
Property<Boolean> getMavenLocalEnabled()
```

This property can be configured as:

| Method | Example |
|--------|---------|
| gradle.properties | `iHub.mavenLocalEnabled=true` |
| Environment | `IHUB_MAVENLOCAL_ENABLED=true` |
| System property | `-DiHub.mavenLocalEnabled=true` |
| Command-line | `-PiHub.mavenLocalEnabled=true` |

### Gradle Property API Best Practices

1. **Always use Property<T> for mutable properties**
   ```groovy
   Property<String> getOutputDir()
   Property<Boolean> getEnabled()
   ```

2. **Use Provider<T> for read-only values**
   ```groovy
   Provider<String> getComputedValue()
   ```

3. **Chain providers for lazy evaluation**
   ```groovy
   outputDir.set(project.layout.buildDirectory.dir('output'))
   ```

4. **Use convention for default values**
   ```groovy
   Property<String> getVersion() {
       version.convention(project.provider { project.version.toString() })
   }
   ```

## Plugin IDs

| Plugin ID | Description |
|-----------|-------------|
| `pub.ihub.plugin` | Base plugin |
| `pub.ihub.plugin.ihub-settings` | Settings plugin |
| `pub.ihub.plugin.ihub-version` | Version plugin |
| `pub.ihub.plugin.ihub-profiles` | Profiles plugin |
| `pub.ihub.plugin.ihub-bom` | BOM plugin |
| `pub.ihub.plugin.ihub-java` | Java plugin |
| `pub.ihub.plugin.ihub-groovy` | Groovy plugin |
| `pub.ihub.plugin.ihub-kotlin` | Kotlin plugin |
| `pub.ihub.plugin.ihub-boot` | Spring Boot plugin |
| `pub.ihub.plugin.ihub-native` | GraalVM Native plugin |
| `pub.ihub.plugin.ihub-test` | Test plugin |
| `pub.ihub.plugin.ihub-verification` | Verification plugin |
| `pub.ihub.plugin.ihub-publish` | Publish plugin |
| `pub.ihub.plugin.ihub-shadow` | Shadow plugin |
| `pub.ihub.plugin.ihub-copyright` | Copyright plugin |
| `pub.ihub.plugin.ihub-git-hooks` | Git hooks plugin |
| `pub.ihub.plugin.ihub-node` | Node.js plugin |
| `pub.ihub.plugin.ihub-javaagent` | Javaagent plugin |

## Key Dependencies

| Dependency | Purpose | Gradle API |
|------------|---------|------------|
| `gradleApi()` | Gradle API | Core API for plugin development |
| `pub.ihub.plugin:ihub-base` | Core plugin infrastructure | IHub base classes |
| `pub.ihub.plugin:ihub-base-test` | Test infrastructure | TestKit helpers |
| `org.spockframework:spock-core` | Spock testing | BDD testing framework |
| `org.codenarc:CodeNarc` | Static analysis | Groovy code quality |

### Gradle API Key Interfaces

| Interface | Purpose |
|-----------|---------|
| `Plugin<Project>` | Plugin entry point |
| `ExtensionContainer` | Extension management |
| `TaskContainer` | Task registration and configuration |
| `Configuration` | Dependency management |
| `SourceSetContainer` | Source set management |
| `Property<T>` | Lazy property evaluation |
| `Provider<T>` | Lazy value computation |

## Common Patterns

### Repository Configuration

Using Gradle's repository DSL:

```groovy
iHubBom {
    importBoms {
        group('org.springframework.boot').module('spring-boot-dependencies').version('3.0.0')
    }
    dependencies {
        group('org.example').module('example-lib').version('1.0.0')
    }
}
```

### Task Registration (Lazy Configuration)

Using Gradle's TaskContainer.register():

```groovy
registerTask('myTask', MyTask) {
    it.group = 'ihub'
    it.description = 'My custom task'
    // Use lazy configuration
    it.inputs.property('config', extension.config)
    it.outputs.dir(extension.outputDir)
}
```

### Extension Configuration

Using Gradle's extension DSL:

```groovy
iHub {
    mavenLocalEnabled = true
    mavenAliYunEnabled = true
}
```

### Plugin Application

Using Gradle's plugins DSL (recommended):

```groovy
plugins {
    id 'pub.ihub.plugin' version '1.9.4'
}
```

Or using apply plugin (legacy):

```groovy
apply plugin: 'pub.ihub.plugin'
```

### Multi-Project Configuration

Using Gradle's subprojects/allprojects:

```groovy
// In root build.gradle.kts
subprojects {
    apply {
        plugin('pub.ihub.plugin.ihub-groovy')
        plugin('pub.ihub.plugin.ihub-test')
    }
}
```

### Version Catalog Integration

Using Gradle's version catalog:

```groovy
// settings.gradle.kts
dependencyResolutionManagement {
    versionCatalogs {
        ihub {
            from(files('gradle/libs.versions.toml'))
        }
    }
}

// build.gradle.kts
dependencies {
    implementation(libs.some.dependency)
}
```

## Documentation

- Main documentation: https://doc.ihub.pub/plugins/
- Source: `docs/` directory (VuePress)
- Plugin documentation: `docs/list/`

## Important Files

| File | Purpose |
|------|---------|
| `build.gradle.kts` | Root build configuration |
| `settings.gradle.kts` | Project settings and plugin management |
| `gradle.properties` | Project properties |
| `gradle/libs.versions.toml` | Version catalog |
| `.editorconfig` | Editor configuration |
| `.pre-commit-config.yaml` | Pre-commit hooks |

## Mandatory Development Workflow

**IMPORTANT: These rules MUST be strictly followed for all code changes.**

### 1. Test Coverage Requirements

After completing any code implementation, you MUST:

- Write comprehensive test cases using `IHubSpecification`
- Achieve **100% test coverage** for all new code
- Test all code paths including edge cases and error scenarios
- Use Spock BDD style: `setup`, `when`, `then` blocks

```groovy
def 'feature description'() {
    setup: 'Initialize test environment'
    // Setup code

    when: 'Execute action'
    def result = gradleBuilder.build()

    then: 'Verify results'
    result.output.contains 'BUILD SUCCESSFUL'
}
```

### 2. Static Analysis

After code implementation, you MUST:

- Run CodeNarc static analysis
- Fix all reported issues
- Ensure no warnings or errors

```bash
./gradlew check -x test
```

### 3. Documentation Updates

After completing code changes, you MUST update ALL relevant documentation:

| Document | Location | When to Update |
|----------|----------|----------------|
| Plugin Docs | `docs/list/` | New plugin or feature |
| README.md | Project root | New plugin, feature, or configuration |
| AGENTS.md | Project root | Architecture or workflow changes |
| API Docs | `docs/` | New API or configuration options |

### 4. Complete Workflow Checklist

For every code change, complete these steps in order:

```
[ ] 1. Implement code changes
[ ] 2. Write test cases (100% coverage)
[ ] 3. Run tests: ./gradlew test
[ ] 4. Run static analysis: ./gradlew check -x test
[ ] 5. Update docs/list/ documentation
[ ] 6. Update README.md
[ ] 7. Update AGENTS.md
[ ] 8. Verify build: ./gradlew build
```

## Notes for AI Agents

### Code Development Guidelines

1. **Always read existing code first** - Understand the patterns and conventions used in the codebase before making changes.

2. **Follow the annotation-based approach** - Use `@IHubPlugin`, `@IHubExtension`, `@IHubProperty`, and `@IHubTask` annotations.

3. **Extend appropriate base classes** - Use `IHubProjectPluginAware` for Project plugins.

4. **Write tests using IHubSpecification** - All plugin tests should extend `IHubSpecification`.

5. **Use Gradle TestKit** - Test plugins using GradleRunner from TestKit.

6. **Maintain backward compatibility** - Consider existing users when modifying plugins.

7. **Add license headers** - All new files must include the Apache License header.

8. **Follow naming conventions**:
   - Plugin classes: `IHubXxxPlugin`
   - Extension classes: `IHubXxxExtension`
   - Task classes: `IHubXxxTask`
   - Package: `pub.ihub.plugin.<module>`

### Gradle-Specific Guidelines

9. **Use lazy configuration** - Always use `Property<T>` and `Provider<T>` for extension properties.

10. **Register tasks lazily** - Use `register()` instead of `create()` for task registration.

11. **Defer expensive operations** - Use `afterEvaluate` for configuration that depends on extension values.

12. **Declare task inputs/outputs** - Enable incremental build and caching by declaring proper annotations.

13. **Avoid deprecated APIs** - Use modern Gradle APIs (e.g., `layout.buildDirectory` instead of `buildDir`).

14. **Use version catalog** - Reference dependencies through version catalog (`libs.xxx`).

### Mandatory Requirements

15. **NEVER skip documentation** - Documentation is mandatory, not optional.

16. **NEVER skip tests** - 100% test coverage is required for all new code.

17. **NEVER use deprecated Gradle APIs** - Always check for deprecation warnings.

18. **NEVER break backward compatibility** - Add new features without removing existing functionality.
