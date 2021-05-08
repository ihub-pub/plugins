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

import static pub.ihub.plugin.IHubPluginMethods.findVersion

import org.gradle.api.Action
import org.gradle.api.Project

/**
 * BOM插件DSL扩展
 * @author liheng
 */
class IHubBomExtension {

	boolean enabledDefaultConfig = true
	boolean printConfig = true
	final List<DependencyVersionSpec> bomVersions = []
	final List<DependencyVersionSpec> dependencyVersions = []
	final Map<String, String> groupVersions = [:]
	final Map<String, Set<String>> excludeModules = [:]
	final Map<String, Set<String>> dependencies = [:]

	/**
	 * 导入mavenBom
	 * @param action 配置
	 */
	void importBoms(Action<DependencyVersionsSpec> action) {
		List<DependencyVersionSpec> specs = new DependencyVersionsSpec().tap { action.execute it }.specs
		assert specs, 'import boms config not empty!'
		specs.each { spec ->
			DependencyVersionSpec bom = bomVersions.find { spec.group == it.group && spec.module == it.module }
			if (bom) {
				if (!spec.ifAbsent) {
					bom.version spec.version
				}
			} else {
				bomVersions << spec
			}
		}
	}

	/**
	 * 配置依赖默认版本
	 * @param action 配置
	 */
	void dependencyVersions(Action<DependencyVersionsSpec> action) {
		List<DependencyVersionSpec> specs = new DependencyVersionsSpec().tap { action.execute it }.specs
		assert specs, 'dependency versions config not empty!'
		dependencyVersions.addAll specs
	}

	/**
	 * 配置组版本策略（建议尽量使用bom）
	 * @param action 配置
	 */
	void groupVersions(Action<DependencyVersionsSpec> action) {
		List<DependencyVersionSpec> groupVersions = new DependencyVersionsSpec().tap { action.execute it }.specs
		assert groupVersions, 'group versions config not empty!'
		groupVersions.each {
			if (it.ifAbsent) {
				this.groupVersions.putIfAbsent it.group, it.version
			} else {
				this.groupVersions.put it.group, it.version
			}
		}
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

	String findVersion(Project project, String group) {
		findVersion project, group, groupVersions[group]
	}

	private class DependencyVersionsSpec {

		private final List<DependencyVersionSpec> specs = []
		boolean allIfAbsent = false

		DependencyVersionSpec group(String group) {
			new DependencyVersionSpec(group).tap {
				ifAbsent allIfAbsent ?: ifAbsent
				specs << it
			}
		}

	}

	private class DependencyVersionSpec {

		final String group
		String module
		String version
		Set<String> modules
		boolean ifAbsent = false

		DependencyVersionSpec(String group) {
			this.group = group
		}

		DependencyVersionSpec module(String module) {
			this.module = module
			this
		}

		DependencyVersionSpec version(String version) {
			this.version = version
			this
		}

		DependencyVersionSpec modules(String... modules) {
			this.modules = modules
			this
		}

		DependencyVersionSpec ifAbsent(boolean ifAbsent) {
			this.ifAbsent = ifAbsent
			this
		}

		String getVersion(Project project) {
			findVersion project, group, version
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

	private class DependenciesSpec {

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

}
