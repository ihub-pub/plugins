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
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.BOM
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.EXCLUDE
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.GROUP
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.MODULES

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import org.gradle.api.Action
import org.gradle.api.GradleException
import pub.ihub.plugin.IHubExtension

/**
 * BOM插件DSL扩展
 * @author liheng
 */
@TupleConstructor(includeSuperFields = true)
class IHubBomExtension extends IHubExtension {

	boolean enabledDefaultConfig = true
	final Set<BomSpecImpl> bomVersions = []
	final Set<BomSpecImpl> dependencyVersions = []
	final Set<BomSpecImpl> groupVersions = []
	final Set<BomSpecImpl> excludeModules = []
	final Set<DepSpecImpl> dependencies = []

	/**
	 * 导入mavenBom
	 * @param action 配置
	 */
	void importBoms(Action<GroupSpec<ModuleSpec>> action) {
		bomVersions.addAll actionExecute(action, new GroupSpecImpl(BOM))
	}

	/**
	 * 配置依赖默认版本
	 * @param action 配置
	 */
	void dependencyVersions(Action<GroupSpec<ModulesSpec>> action) {
		actionExecute(action, new GroupSpecImpl(MODULES)).each { spec ->
			dependencyVersions << (dependencyVersions.find { spec == it }?.tap { modules.addAll spec.modules } ?: spec)
		}
	}

	/**
	 * 配置组版本策略（建议尽量使用bom）
	 * @param action 配置
	 */
	void groupVersions(Action<GroupSpec<VersionSpec>> action) {
		groupVersions.addAll actionExecute(action, new GroupSpecImpl(GROUP))
	}

	/**
	 * 排除组件依赖
	 * @param action
	 */
	void excludeModules(Action<GroupSpec<ModulesSpec>> action) {
		actionExecute(action, new GroupSpecImpl(EXCLUDE)).each { spec ->
			excludeModules << (excludeModules.find { spec == it }?.tap { modules.addAll spec.modules } ?: spec)
		}
	}

	/**
	 * 配置组件依赖
	 * @param action 配置
	 */
	void dependencies(Action<DependenciesSpec> action) {
		actionExecute(action, new DependenciesSpecImpl()).each { spec ->
			dependencies << (dependencies.find { spec.type == it.type }
				?.tap { it.dependencies.addAll spec.dependencies } ?: spec)
		}
	}

	boolean getEnabledDefaultConfig() {
		findBooleanProperty 'enabledBomDefaultConfig', enabledDefaultConfig
	}

	private <T> Collection<T> actionExecute(Action<ActionSpec<T>> action, ActionSpec<T> spec) {
		action.execute spec
		Collection<T> specs = spec.specs
		assert specs, 'config not empty!'
		specs
	}

	void printConfigContent() {
		// TODO 此处逻辑太过臃肿，目前可以排除一些子项目与主项目重复的配置，后续再做优化
		String projectName = projectName.toUpperCase()
		IHubBomExtension rootExt = rootProject.extensions.findByType IHubBomExtension
		printConfigContent "${projectName} Group Maven Bom Version", bomVersions.collect {
			root || it.inSpecs(rootExt.bomVersions) ? [it.group, it.module, it.version] : null
		}, groupTap(30), moduleTap(), versionTap(20)
		printConfigContent "${projectName} Group Maven Module Version", dependencyVersions
			.inject([]) { list, config ->
				list + (root || config.inSpecs(rootExt.dependencyVersions) ?
					config.modules.collect { [config.group, it, config.version] } : null)
			}, groupTap(35), moduleTap(), versionTap(15)
		printConfigContent "${projectName} Group Maven Default Version", groupTap(), versionTap(),
			groupVersions.collectEntries { root || it.inSpecs(rootExt.groupVersions) ? [(it.group): it.version] : [:] }
		printConfigContent "${projectName} Exclude Group Modules", groupTap(40), moduleTap(),
			excludeModules.collectEntries {
				[(it.group): (it.modules - (root ? [] : rootExt.excludeModules.find { r -> it.group == r.group }?.modules)).toList()]
			}
		printConfigContent "${projectName} Config Default Dependencies",
			dependencyTypeTap(), dependenciesTap(), dependencies.collectEntries {
			[(it.type): (it.dependencies - (root ? [] : rootExt.dependencies.find { r -> it.type == r.type }?.dependencies)).toList()]
		}
	}

	//<editor-fold desc="DSL扩展相关实体">

	interface ActionSpec<T> {

		Collection<T> getSpecs()

	}

	interface GroupSpec<T> extends ActionSpec<T> {

		T group(String group)

	}

	interface VersionSpec {

		void version(String version)

	}

	interface ModuleSpec extends VersionSpec {

		ModuleSpec module(String module)

	}

	interface ModulesSpec extends VersionSpec {

		ModulesSpec modules(String... modules)

	}

	interface DependenciesSpec extends ActionSpec<DepSpecImpl> {

		void compile(String type, String... dependencies)

		default void api(String... dependencies) {
			compile 'api', dependencies
		}

		default void implementation(String... dependencies) {
			compile 'implementation', dependencies
		}

		default void compileOnly(String... dependencies) {
			compile 'compileOnly', dependencies
		}

		default void compileOnlyApi(String... dependencies) {
			compile 'compileOnlyApi', dependencies
		}

		default void runtimeOnly(String... dependencies) {
			compile 'runtimeOnly', dependencies
		}

		default void testImplementation(String... dependencies) {
			compile 'testImplementation', dependencies
		}

		default void testCompileOnly(String... dependencies) {
			compile 'testCompileOnly', dependencies
		}

		default void testRuntimeOnly(String... dependencies) {
			compile 'testRuntimeOnly', dependencies
		}

		default void annotationProcessor(String... dependencies) {
			compile 'annotationProcessor', dependencies
		}

	}

	@TupleConstructor(includes = 'type')
	private class GroupSpecImpl implements GroupSpec<BomSpecImpl> {

		final VersionType type
		List<BomSpecImpl> specs = []

		@Override
		BomSpecImpl group(String group) {
			new BomSpecImpl(type, group).tap {
				specs << it
			}
		}

	}

	@TupleConstructor(includes = 'type,group')
	@ToString
	private class BomSpecImpl implements ModuleSpec, ModulesSpec, Comparable<BomSpecImpl> {

		final VersionType type
		final String group
		String version
		String module
		Set<String> modules

		@Override
		void version(String version) {
			if (EXCLUDE == type) throw new GradleException('Does not support \'version\' method!')
			this.version = findProperty group + '.version', version
		}

		@Override
		BomSpecImpl module(String module) {
			this.module = module
			this
		}

		@Override
		BomSpecImpl modules(String... modules) {
			this.modules = modules
			this
		}

		@Override
		int compareTo(BomSpecImpl o) {
			type == o.type && group == o.group &&
				version == o.version && module == o.module && modules == o.modules ? 0 : -1
		}

		boolean inSpecs(Set<BomSpecImpl> rootSpecs) {
			rootSpecs.every { s -> 0 != (this <=> s) }
		}

		@Override
		boolean equals(o) {
			if (this.is(o)) return true
			if (!(o instanceof BomSpecImpl)) return false
			type == o.type && group == o.group &&
				(BOM != type || module == o.module) && (MODULES != type || version == o.version)
		}

		@Override
		int hashCode() {
			int result
			result = 31 * type.hashCode() + group.hashCode()
			if (BOM == type) result = 31 * result + module.hashCode()
			if (MODULES == type) result = 31 * result + version.hashCode()
			result
		}

	}

	private final class DependenciesSpecImpl implements DependenciesSpec {

		final Set<DepSpecImpl> specs = []

		@Override
		void compile(String type, String... dependencies) {
			assert type, 'dependencies type not null!'
			assert dependencies, type + ' dependencies not empty!'
			specs << (specs.find { type == it.type }?.tap { o -> o.dependencies.addAll dependencies }
				?: new DepSpecImpl(type, dependencies as Set<String>))
		}

	}

	@TupleConstructor
	@EqualsAndHashCode(includes = 'type')
	@ToString
	private final class DepSpecImpl implements Comparable<DepSpecImpl> {

		String type
		Set<String> dependencies

		@Override
		int compareTo(DepSpecImpl o) {
			type == o.type && dependencies == o.dependencies ? 0 : -1
		}

	}

	private enum VersionType {

		BOM, MODULES, GROUP, EXCLUDE

	}

	//</editor-fold>

}
