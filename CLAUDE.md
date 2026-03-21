# PROJECT KNOWLEDGE BASE

**Generated:** 2026-03-05
**Commit:** N/A

## OVERVIEW
IHub Plugins is a Gradle plugin collection providing infrastructure for Gradle projects. Primarily developed in Groovy using Spock for testing and Gradle TestKit for plugin testing.

## STRUCTURE
```
plugins/
├── ihub-base/              # Core plugin infrastructure (Annotations, traits)
├── ihub-base-test/         # Test infrastructure (IHubSpecification)
├── ihub-bom/               # BOM dependency management
├── ihub-plugins/           # Core plugins (repository, version, profiles)
├── ihub-settings/          # Settings plugin
├── ihub-publish/           # Publishing plugin
├── ihub-verification/      # Verification plugin (CodeNarc, tests)
├── samples/                # Sample projects for verification
└── docs/                   # Documentation (VuePress)
```

## COMMANDS (BUILD / LINT / TEST)
```bash
# Build the entire project
./gradlew build

# Run all tests
./gradlew test

# Run a single test class (e.g., IHubPluginTest)
./gradlew :ihub-plugins:test --tests "pub.ihub.plugin.IHubPluginTest"

# Run a single test method in a class
./gradlew :ihub-plugins:test --tests "pub.ihub.plugin.IHubPluginTest.test plugin application"

# Run static analysis (CodeNarc & others) without running tests
./gradlew check -x test

# Check Git commit message format
./gradlew commitCheck

# Publish to local Maven repository for local testing
./gradlew publishToMavenLocal

# Clean project
./gradlew clean
```

## CODE STYLE & GUIDELINES

### 1. Imports
- Avoid wildcard imports (e.g., `import java.util.*`).
- Group imports logically: Java/Groovy standard libraries first, then Gradle APIs, then project-specific classes.
- Remove unused imports.

### 2. Formatting & Types
- **Indentation**: 4 spaces, no tabs.
- **Line endings**: CRLF.
- **Encoding**: UTF-8.
- Use `@CompileStatic` for performance-critical code or where strong typing is desired.
- Strong typing is preferred over `def` for method parameters and return types to ensure clarity and reliability.

### 3. Naming Conventions
- **Plugin classes**: `IHubXxxPlugin`
- **Extension classes**: `IHubXxxExtension`
- **Task classes**: `IHubXxxTask`
- **Packages**: `pub.ihub.plugin.<module>`
- Methods and variables: `camelCase`
- Constants: `UPPER_SNAKE_CASE`

### 4. Error Handling
- Use meaningful exception messages.
- Prefer Gradle's `GradleException` or specific exceptions over generic `RuntimeException`.
- Do not swallow exceptions (e.g., empty `catch` blocks).

### 5. Plugin Architecture
- **Lazy Configuration**: ALWAYS use Gradle's `Property<T>` and `Provider<T>` APIs. Never evaluate properties during the configuration phase using `.get()`.
- **Task Registration**: ALWAYS use `register()` instead of `create()`.
- **Annotations**: Heavily rely on `@IHubPlugin`, `@IHubExtension`, `@IHubProperty`, and `@IHubTask` to reduce boilerplate.
- Extend `IHubProjectPluginAware` for Project plugins.

### 6. Testing (Spock)
- 100% test coverage is MANDATORY for all new code.
- All plugin tests should extend `IHubSpecification`.
- Follow BDD style (`setup:`, `when:`, `then:` blocks).
- Use temporary directories (`@TempDir`) for isolation.

### 7. Documentation
- **NEVER skip documentation**. When you update or create a feature, you MUST update:
  - `README.md`
  - `docs/list/` (Plugin specific docs)
  - `docs/AGENTS.md` (if rule changes)
- Documentation and code MUST remain synchronized.

## MANDATORY DEVELOPMENT WORKFLOW
1. Implement code changes.
2. Write test cases (achieve 100% coverage).
3. Run tests locally (`./gradlew test`).
4. Run static analysis (`./gradlew check -x test`).
5. Fix any CodeNarc warnings/errors.
6. Synchronize documentation updates (`docs/list/`, `README.md`).
7. Verify build (`./gradlew build`).

## ANTI-PATTERNS (DO NOT DO THIS)
- ❌ Using deprecated Gradle APIs (e.g., `buildDir` instead of `layout.buildDirectory`).
- ❌ Skipping tests or static analysis.
- ❌ Using immediate evaluation for tasks/properties (`.get()` in the configuration phase).
- ❌ Breaking backward compatibility.
- ❌ Adding new dependencies without updating the BOM/version catalog (`gradle/libs.versions.toml`).
