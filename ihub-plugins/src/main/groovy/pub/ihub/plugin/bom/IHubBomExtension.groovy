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

import org.gradle.api.Action
import pub.ihub.plugin.IHubExtension

/**
 * BOM插件DSL扩展
 * @author liheng
 */
interface IHubBomExtension extends IHubExtension {

	/**
	 * 导入mavenBom
	 * @param action 配置
	 */
	void importBoms(Action<GroupSpec<ModuleSpec>> action)

	/**
	 * 配置依赖默认版本
	 * @param action 配置
	 */
	void dependencyVersions(Action<GroupSpec<ModulesSpec>> action)

	/**
	 * 配置组版本策略（建议尽量使用bom）
	 * @param action 配置
	 */
	void groupVersions(Action<GroupSpec<VersionSpec>> action)

	/**
	 * 排除组件依赖
	 * @param action
	 */
	void excludeModules(Action<GroupSpec<ModulesSpec>> action)

	/**
	 * 配置组件依赖
	 * @param action 配置
	 */
	void dependencies(Action<DependenciesSpec> action)

	/**
	 * 启用默认配置
	 * @param enabledDefaultConfig 是否启用，默认true
	 */
	void enabledDefaultConfig(boolean enabledDefaultConfig)

	//<editor-fold desc="DSL扩展相关实体">

	interface GroupSpec<T> {

		T group(String group)

	}

	interface VersionSpec {

		VersionSpec version(String version)

	}

	interface ModuleSpec extends VersionSpec {

		ModuleSpec module(String module)

	}

	interface ModulesSpec extends VersionSpec {

		ModulesSpec version(String version)

		ModulesSpec modules(String... modules)

	}

	final class DependenciesSpec {

		final Map<String, Set<String>> dependencies = [:]

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
