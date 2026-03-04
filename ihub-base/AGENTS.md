# IHub Base - Infrastructure Guide

## OVERVIEW
Core framework providing the annotation-driven DSL and base classes for all IHub Gradle plugins.

## WHERE TO LOOK
- `IHubProjectPluginAware`: Main entry point for project plugins. Handles annotation scanning and life-cycle management.
- `IHubPlugin`: Annotation for plugin registration and dependency/task declaration.
- `IHubExtensionAware`: Base trait for plugin extensions.
- `IHubProperty`: Property resolution engine (System > Env > Project > Default).

## CONVENTIONS
- **Annotation-First**: Plugins must be declared with `@IHubPlugin` to trigger automatic configuration.
- **Interface Extensions**: Prefer interfaces for extensions to allow Gradle's lazy Property API generation.
- **Lazy Chaining**: Use `withExtension(AFTER) { ... }` in `apply()` to ensure properties are resolved before execution.

## ANTI-PATTERNS
- **Manual Registration**: Don't manually create extensions or tasks; use the `@IHubPlugin` and `@IHubTask` annotations.
- **Direct Field Access**: Avoid public fields in extensions. Use `Property<T>` getters for lazy evaluation support.
- **Eager Configuration**: Never perform heavy logic directly in the `apply()` method. Defer to `afterEvaluate` or task execution.
- **Hardcoded Priorities**: Don't bypass the property resolution logic; use `@IHubProperty` for consistent priority handling.
