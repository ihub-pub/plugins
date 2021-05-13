/*
 * Copyright (c) 2021 Henry 李恒 (henry.box@outlook.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pub.ihub.plugin.bom

import static pub.ihub.plugin.IHubPluginMethods.dependenciesTap
import static pub.ihub.plugin.IHubPluginMethods.dependencyTypeTap
import static pub.ihub.plugin.IHubPluginMethods.groupTap
import static pub.ihub.plugin.IHubPluginMethods.moduleTap
import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.IHubPluginMethods.versionTap

import groovy.transform.EqualsAndHashCode
import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Project

import java.util.function.Function

/**
 * BOM插件DSL扩展实现
 * @author liheng
 */
class IHubBomExtensionImpl implements IHubBomExtension {

	boolean enabledDefaultConfig = true
	boolean printConfig = true
	final Set<BomVersionSpec> bomVersions = []
	final List<ModulesVersionSpec> dependencyVersions = []
	final Set<GroupVersionSpec> groupVersions = []
	final Map<String, Set<String>> excludeModules = [:]
	final Map<String, Set<String>> dependencies = [:]

	IHubBomExtensionImpl(Project project) {
		pub_ihub_plugin_IHubExtension__project = project
	}

	@Override
	void importBoms(Action<GroupSpec<ModuleSpec>> action) {
		bomVersions.addAll actionExecute(action) { String group ->
			new BomVersionSpec(group)
		}
	}

	@Override
	void dependencyVersions(Action<GroupSpec<ModulesSpec>> action) {
		actionExecute(action) { String group ->
			new ModulesVersionSpec(group)
		}*.addSet dependencyVersions
	}

	@Override
	void groupVersions(Action<GroupSpec<VersionSpec>> action) {
		groupVersions.addAll actionExecute(action) { String group ->
			new GroupVersionSpec(group)
		}
	}

	@Override
	void excludeModules(Action<GroupSpec<ModulesSpec>> action) {
		actionExecute(action) { String group ->
			new ExcludeModulesSpec(group)
		}.each {
			excludeModules.compute(it.group, { key, modules -> modules = modules ?: [] }).addAll it.modules
		}
	}

	@Override
	void dependencies(Action<DependenciesSpec> action) {
		if (!getEnabledDefaultConfig()) {
			return
		}
		Map<String, Set<String>> dependencies = new DependenciesSpec().tap { action.execute it }.dependencies
		assert dependencies, 'dependencies config not empty!'
		dependencies.each { type, dependenciesSet ->
			this.dependencies.compute(type, { key, set -> set = set ?: [] }).addAll dependenciesSet
		}
	}

	@Override
	void enabledDefaultConfig(boolean enabledDefaultConfig) {
		this.enabledDefaultConfig = enabledDefaultConfig
	}

	private boolean getEnabledDefaultConfig() {
		findProperty('enabledBomDefaultConfig', enabledDefaultConfig.toString()).toBoolean()
	}

	private <T> List<T> actionExecute(Action<GroupSpec<T>> action, Function<String, T> instanceSpec) {
		if (!getEnabledDefaultConfig()) {
			return []
		}
		GroupSpecImpl<T> groupSpec = new GroupSpecImpl<>(instanceSpec)
		action.execute groupSpec
		List<T> specs = groupSpec.specs
		assert specs, 'config not empty!'
		specs
	}

	void printConfigContent() {
		// TODO 此处逻辑太过臃肿，目前可以排除一些子项目与主项目重复的配置，后续再做优化
		if (printConfig) {
			Set<BomVersionSpec> printBomVersions
			List<ModulesVersionSpec> printDependencyVersions
			Set<GroupVersionSpec> printGroupVersions
			Map printExcludeModules
			Map printDependencies
			if (!project.rootProject.childProjects) {
				printBomVersions = bomVersions
				printDependencyVersions = dependencyVersions
				printGroupVersions = groupVersions
				printExcludeModules = excludeModules
				printDependencies = dependencies
			} else if (project.name == project.rootProject.name) {
				List<IHubBomExtensionImpl> subprojectsExt = project.rootProject.subprojects.extensions*.findByType IHubBomExtensionImpl
				printBomVersions = bomVersions.tap {
					addAll subprojectsExt*.bomVersions.inject { l1, l2 ->
						l1.intersect(l2) { a, b -> a.compare(b) ? 0 : -1 }
					}.findAll { !bomVersions.contains(it) }
				}
				printDependencyVersions = dependencyVersions.tap {
					subprojectsExt*.dependencyVersions.inject { l1, l2 ->
						l1.intersect(l2) { a, b -> a.compare(b) ? 0 : -1 }
					}*.addSet it
				}
				printGroupVersions = groupVersions.tap {
					addAll subprojectsExt*.groupVersions.inject { l1, l2 ->
						l1.intersect(l2) { a, b -> a.compare(b) ? 0 : -1 }
					}.findAll { !groupVersions.contains(it) }
				}
				printExcludeModules = excludeModules
				printDependencies = dependencies.tap {
					subprojectsExt*.dependencies*.keySet().inject { k1, k2 -> k1.intersect k2 }.each { k ->
						compute(k, { key, values -> values = values ?: [] }).addAll subprojectsExt*.dependencies*.get(k)
							.inject { v1, v2 -> v1.intersect v2 }
					}
				}
			} else {
				IHubBomExtensionImpl rootExt = project.rootProject.extensions.findByType IHubBomExtensionImpl
				printBomVersions = bomVersions.findAll { s ->
					rootExt.bomVersions.every { r -> !s.compare(r) }
				}
				printDependencyVersions = dependencyVersions.findAll { s ->
					rootExt.dependencyVersions.every { r -> !s.compare(r) }
				}
				printGroupVersions = groupVersions.findAll { s ->
					rootExt.groupVersions.every { r -> !s.compare(r) }
				}
				printExcludeModules = excludeModules.collectEntries { k, v -> [(k): v - rootExt.excludeModules[k]] }
				printDependencies = dependencies.collectEntries { k, v -> [(k): v - rootExt.dependencies[k]] }
			}
			printConfigContent "${project.name.toUpperCase()} Group Maven Bom Version", printBomVersions.collect {
				[it.group, it.module, it.version]
			}, groupTap(30), moduleTap(), versionTap(20)
			printConfigContent "${project.name.toUpperCase()} Group Maven Module Version",
				printDependencyVersions.inject([]) { list, config ->
					list + config.modules.collect { [config.group, it, config.version] }
				}, groupTap(35), moduleTap(), versionTap(15)
			printConfigContent "${project.name.toUpperCase()} Group Maven Default Version",
				groupTap(), versionTap(), printGroupVersions.collectEntries { [(it.group): it.version] }
			printConfigContent "${project.name.toUpperCase()} Exclude Group Modules",
				groupTap(40), moduleTap(), printExcludeModules
			printConfigContent "${project.name.toUpperCase()} Config Default Dependencies",
				dependencyTypeTap(), dependenciesTap(), printDependencies
		}
	}

	//<editor-fold desc="DSL扩展相关实体">

	private class GroupSpecImpl<T> implements GroupSpec<T> {

		private Function<String, T> instanceSpec
		final List<T> specs = []

		GroupSpecImpl(Function<String, T> instanceSpec) {
			this.instanceSpec = instanceSpec
		}

		@Override
		T group(String group) {
			instanceSpec.apply(group).tap {
				specs << it
			}
		}

	}

	@EqualsAndHashCode(includes = 'group')
	private class GroupVersionSpec implements VersionSpec {

		final String group
		String version

		GroupVersionSpec(String group) {
			this.group = group
		}

		@Override
		GroupVersionSpec version(String version) {
			this.version = findProperty group + '.version', version
			this
		}

		boolean compare(GroupVersionSpec o) {
			group == o.group && version == o.version
		}

	}

	@EqualsAndHashCode(callSuper = true, includes = 'module')
	private class BomVersionSpec extends GroupVersionSpec implements ModuleSpec {

		String module

		BomVersionSpec(String group) {
			super(group)
		}

		@Override
		BomVersionSpec module(String module) {
			this.module = module
			this
		}

		boolean compare(BomVersionSpec o) {
			super.compare(o) && module == o.module
		}

	}

	@EqualsAndHashCode(callSuper = true, includes = 'modules')
	private class ModulesVersionSpec extends GroupVersionSpec implements ModulesSpec {

		Set<String> modules

		ModulesVersionSpec(String group) {
			super(group)
		}

		@Override
		ModulesVersionSpec modules(String... modules) {
			this.modules = modules
			this
		}

		@Override
		ModulesVersionSpec version(String version) {
			super.version(version) as ModulesVersionSpec
		}

		private void addSet(List<ModulesVersionSpec> dependencyVersions) {
			ModulesVersionSpec spec = dependencyVersions.find { group == it.group && version == it.version }
			if (spec) {
				spec.modules.addAll modules
			} else {
				dependencyVersions << this
			}
		}

		boolean compare(ModulesVersionSpec o) {
			super.compare(o) && modules == o.modules
		}

	}

	private class ExcludeModulesSpec implements ModulesSpec {

		final String group
		List<String> modules

		ExcludeModulesSpec(String group) {
			this.group = group
		}

		@Override
		ModulesSpec version(String version) {
			throw new GradleException('Does not support \'version\' method!')
		}

		@Override
		ExcludeModulesSpec modules(String... modules) {
			this.modules = modules
			this
		}

	}

	//</editor-fold>

}
