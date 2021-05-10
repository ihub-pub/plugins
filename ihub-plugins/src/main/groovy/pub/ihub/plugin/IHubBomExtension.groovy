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
package pub.ihub.plugin

import static pub.ihub.plugin.IHubPluginMethods.dependenciesTap
import static pub.ihub.plugin.IHubPluginMethods.dependencyTypeTap
import static pub.ihub.plugin.IHubPluginMethods.findProperty
import static pub.ihub.plugin.IHubPluginMethods.groupTap
import static pub.ihub.plugin.IHubPluginMethods.moduleTap
import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.IHubPluginMethods.versionTap

import groovy.transform.EqualsAndHashCode
import org.gradle.api.Action
import org.gradle.api.Project

/**
 * BOM插件DSL扩展
 * @author liheng
 */
class IHubBomExtension {

	boolean enabledDefaultConfig = true
	boolean printConfig = true
	final Set<BomVersionSpec> bomVersions = []
	final List<ModulesVersionSpec> dependencyVersions = []
	final Set<GroupVersionSpec> groupVersions = []
	final Map<String, Set<String>> excludeModules = [:]
	final Map<String, Set<String>> dependencies = [:]
	final Project project

	IHubBomExtension(Project project) {
		this.project = project
	}

	/**
	 * 导入mavenBom
	 * @param action 配置
	 */
	void importBoms(Action<VersionsSpec<BomVersionSpec>> action) {
		List<BomVersionSpec> specs = new VersionsSpec<BomVersionSpec>({ String group ->
			new BomVersionSpec(group)
		}).tap { action.execute it }.specs
		assert specs, 'import boms config not empty!'
		specs*.addSet()
	}

	/**
	 * 配置依赖默认版本
	 * @param action 配置
	 */
	void dependencyVersions(Action<VersionsSpec<ModulesVersionSpec>> action) {
		List<ModulesVersionSpec> specs = new VersionsSpec<ModulesVersionSpec>({ String group ->
			new ModulesVersionSpec(group)
		}).tap { action.execute it }.specs
		assert specs, 'dependency versions config not empty!'
		specs*.addSet()
	}

	/**
	 * 配置组版本策略（建议尽量使用bom）
	 * @param action 配置
	 */
	void groupVersions(Action<VersionsSpec<GroupVersionSpec>> action) {
		List<GroupVersionSpec> groupVersions = new VersionsSpec<GroupVersionSpec>({ String group ->
			new GroupVersionSpec(group)
		}).tap { action.execute it }.specs
		assert groupVersions, 'group versions config not empty!'
		groupVersions*.addSet()
	}

	/**
	 * 排除组件依赖
	 * @param action
	 */
	void excludeModules(Action<ExcludeGroupSpec> action) {
		List<ExcludeModulesSpec> groups = new ExcludeGroupSpec().tap { action.execute it }.groups
		assert groups, 'excludeModules config not empty!'
		groups.each {
			excludeModules.compute(it.group, { key, modules -> modules = modules ?: [] }).addAll it.modules
		}
	}

	/**
	 * 配置组件依赖
	 * @param action 配置
	 */
	void dependencies(Action<DependenciesSpec> action) {
		Map<String, Set<String>> dependencies = new DependenciesSpec().tap { action.execute it }.dependencies
		assert dependencies, 'dependencies config not empty!'
		dependencies.each { type, dependenciesSet ->
			this.dependencies.compute(type, { key, set -> set = set ?: [] }).addAll dependenciesSet
		}
	}

	void printConfigContent() {
		// TODO 此处逻辑太过臃肿，目前可以排除一些子项目与主项目重复的配置，后续再做优化
		if (printConfig) {
			Set<BomVersionSpec> printBomVersions
			List<ModulesVersionSpec> printDependencyVersions
			Set<GroupVersionSpec> printGroupVersions
			Map printExcludeModules
			Map printDependencies
			if (project.name == project.rootProject.name) {
				List<IHubBomExtension> subprojectsExt = project.rootProject.subprojects.extensions*.findByType IHubBomExtension
				printBomVersions = bomVersions.tap {
					addAll subprojectsExt*.bomVersions.inject { l1, l2 ->
						l1.intersect(l2) { a, b -> a.compare(b) ? 0 : -1 }
					}.findAll { !bomVersions.contains(it) }
				}
				printDependencyVersions = dependencyVersions.tap {
					subprojectsExt*.dependencyVersions.inject { l1, l2 ->
						l1.intersect(l2) { a, b -> a.compare(b) ? 0 : -1 }
					}*.addSet()
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
				IHubBomExtension rootExt = project.rootProject.extensions.findByType IHubBomExtension
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

	private interface InstanceSpec<T extends GroupVersionSpec> {

		T instance(String group)

	}

	private class VersionsSpec<T extends GroupVersionSpec> {

		private final InstanceSpec<T> instanceSpec
		private final List<T> specs = []
		boolean allIfAbsent = false

		VersionsSpec(InstanceSpec<T> instanceSpec) {
			this.instanceSpec = instanceSpec
		}

		T group(String group) {
			instanceSpec.instance(group).tap {
				ifAbsent allIfAbsent ?: ifAbsent
				specs << it
			}
		}

	}

	@EqualsAndHashCode(includes = 'group')
	private class GroupVersionSpec {

		final String group
		String version
		/**
		 * 是否判断版本缺失（如果为true时，不覆盖原有版本）
		 */
		boolean ifAbsent = false

		GroupVersionSpec(String group) {
			this.group = group
		}

		GroupVersionSpec version(String version) {
			this.version = findProperty project, group + '.version', version
			this
		}

		void ifAbsent(boolean ifAbsent) {
			this.ifAbsent = ifAbsent
		}

		private void addSet() {
			groupVersions << (!ifAbsent ? this : (groupVersions.find { group == it.group } ?: this))
		}

		boolean compare(GroupVersionSpec o) {
			group == o.group && version == o.version
		}

	}

	@EqualsAndHashCode(callSuper = true, includes = 'module')
	private class BomVersionSpec extends GroupVersionSpec {

		String module

		BomVersionSpec(String group) {
			super(group)
		}

		BomVersionSpec module(String module) {
			this.module = module
			this
		}

		private void addSet() {
			bomVersions << (!ifAbsent ? this : (bomVersions.find { group == it.group && module == it.module } ?: this))
		}

		boolean compare(BomVersionSpec o) {
			super.compare(o) && module == o.module
		}

	}

	@EqualsAndHashCode(callSuper = true, includes = 'modules')
	private class ModulesVersionSpec extends GroupVersionSpec {

		Set<String> modules

		ModulesVersionSpec(String group) {
			super(group)
		}

		ModulesVersionSpec modules(String... modules) {
			this.modules = modules
			this
		}

		@Override
		ModulesVersionSpec version(String version) {
			super.version(version) as ModulesVersionSpec
		}

		@Override
		void ifAbsent(boolean ifAbsent) {
		}

		private void addSet() {
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

	private class ExcludeGroupSpec {

		private final List<ExcludeModulesSpec> groups = []

		ExcludeModulesSpec group(String group) {
			assert group, 'exclude group not null!'
			new ExcludeModulesSpec(group).tap {
				groups << it
			}
		}

	}

	private class ExcludeModulesSpec {

		private final String group
		private List<String> modules

		ExcludeModulesSpec(String group) {
			this.group = group
		}

		void modules(String... modules) {
			this.modules = modules
		}

	}

	private final class DependenciesSpec {

		private final Map<String, Set<String>> dependencies = [:]

		void compile(String type, String... dependencies) {
			assert type, 'dependencies type not null!'
			assert dependencies, type + ' dependencies not empty!'
			this.dependencies.compute(type, { key, set -> set = set ?: [] }).addAll dependencies
		}

		void api(String... dependencies) {
			compile 'api', dependencies
		}

		void implementation(String... dependencies) {
			compile 'implementation', dependencies
		}

		void compileOnly(String... dependencies) {
			compile 'compileOnly', dependencies
		}

		void compileOnlyApi(String... dependencies) {
			compile 'compileOnlyApi', dependencies
		}

		void runtimeOnly(String... dependencies) {
			compile 'runtimeOnly', dependencies
		}

		void testImplementation(String... dependencies) {
			compile 'testImplementation', dependencies
		}

		void testCompileOnly(String... dependencies) {
			compile 'testCompileOnly', dependencies
		}

		void testRuntimeOnly(String... dependencies) {
			compile 'testRuntimeOnly', dependencies
		}

		void annotationProcessor(String... dependencies) {
			compile 'annotationProcessor', dependencies
		}

	}

	//</editor-fold>

}
