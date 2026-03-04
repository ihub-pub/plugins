# IHub BOM Plugin - AI Agent Guide

## OVERVIEW
Dependency management plugin that integrates Spring's dependency management to handle BOM imports, version sets, and dependency exclusions.

## WHERE TO LOOK
- `IHubBomPlugin.groovy`: Main plugin logic, handles `DependencyManagementPlugin` integration and resolution strategies.
- `IHubBomExtension.groovy`: Extension DSL for `importBoms`, `dependencies`, `groupVersions`, and `excludeModules`.
- `specs/`: Interface definitions for the configuration DSL (e.g., `DependencySpec`, `ModuleSpec`).
- `impl/`: Internal implementation classes for the DSL specifications.

## CONVENTIONS
- Uses Spring Dependency Management under the hood.
- Skips application if `JavaPlatformPlugin` or `VersionCatalogPlugin` is present.
- Disables dependency caching for dynamic versions and snapshots by default.
- DSL follows a nested group-module-version structure for clean dependency sets.

## ANTI-PATTERNS
- **Manual Versioning**: Do not hardcode versions in `build.gradle` if they can be managed via `iHubBom`.
- **Duplicate BOMs**: Avoid importing the same BOM through both Gradle's `platform()` and `iHubBom.importBoms`.
- **Direct Configuration Access**: Avoid modifying `configurations.all` directly for resolution strategies; use the extension's `excludeModules` or `groupVersions` instead.
- **Applying to BOM Projects**: Do not apply this plugin to projects that are themselves intended to be published as BOMs (Java Platform projects).
