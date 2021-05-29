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
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.DEPENDENCY
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.EXCLUDE
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.GROUP
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.MODULES

import groovy.transform.TupleConstructor
import org.gradle.api.Action
import org.gradle.api.GradleException
import pub.ihub.plugin.IHubProjectExtension

import java.util.function.BiConsumer

/**
 * BOM插件DSL扩展
 * @author liheng
 */
@TupleConstructor(includeSuperFields = true)
class IHubBomExtension extends IHubProjectExtension {

	boolean enabledDefaultConfig = true
	final Set<BomSpecImpl> bomVersions = []
	final Set<BomSpecImpl> dependencyVersions = []
	final Set<BomSpecImpl> groupVersions = []
	final Set<BomSpecImpl> excludeModules = []
	final Set<BomSpecImpl> dependencies = []
	final Map<VersionType, Set<BomSpecImpl>> commonSpecs = [:]

	/**
	 * 导入mavenBom
	 * @param action 配置
	 */
	void importBoms(Action<GroupSpec<ModuleSpec>> action) {
		actionExecute BOM, action, bomVersions
	}

	/**
	 * 配置依赖默认版本
	 * @param action 配置
	 */
	void dependencyVersions(Action<GroupSpec<ModulesSpec>> action) {
		actionExecute MODULES, action, dependencyVersions
	}

	/**
	 * 配置组版本策略（建议尽量使用bom）
	 * @param action 配置
	 */
	void groupVersions(Action<GroupSpec<VersionSpec>> action) {
		actionExecute GROUP, action, groupVersions
	}

	/**
	 * 排除组件依赖
	 * @param action
	 */
	void excludeModules(Action<GroupSpec<ModulesSpec>> action) {
		actionExecute EXCLUDE, action, excludeModules
	}

	/**
	 * 配置组件依赖
	 * @param action 配置
	 */
	void dependencies(Action<DependenciesSpec> action) {
		actionExecute DEPENDENCY, action, dependencies
	}

	boolean getEnabledDefaultConfig() {
		findProperty 'enabledBomDefaultConfig', enabledDefaultConfig
	}

	private void actionExecute(VersionType type, Action<ActionSpec<BomSpecImpl>> action, Set<BomSpecImpl> specs) {
		ActionSpec<BomSpecImpl> actionSpec = DEPENDENCY == type ? new DependenciesSpecImpl() : new GroupSpecImpl(type)
		action.execute actionSpec
		actionSpec.specs.with {
			assert it, 'config not empty!'
			it*.rightShift specs
		}
	}

	private List getSpecsPrintData(VersionType type, Set<BomSpecImpl> specs, BiConsumer<List, BomSpecImpl> consumer) {
		IHubBomExtension rootExt = rootProject.extensions.findByType IHubBomExtension
		Set<BomSpecImpl> commonSpecs = rootExt.commonSpecs[type]
		(root ? commonSpecs?.tap { specs*.rightShift it } :
			specs.findAll { commonSpecs.every { s -> !it.compare(s) } }).inject([]) { set, spec ->
			consumer.accept set, type in [EXCLUDE, DEPENDENCY] ? new BomSpecImpl(type, spec.id).modules(spec.modules -
				(root ? [] : commonSpecs.find { r -> spec.id == r.id }?.modules) as String[]) : spec
			set
		}
	}

	private void setCommonSpecs(VersionType type, Set<BomSpecImpl> specs) {
		if (!root) {
			IHubBomExtension rootExt = rootProject.extensions.findByType IHubBomExtension
			rootExt.commonSpecs.put type, rootExt.commonSpecs[type].with {
				null == it ? specs : it.findAll { specs.any { s -> it.compare s } }
			}
		}
	}

	void refreshCommonSpecs() {
		setCommonSpecs BOM, bomVersions
		setCommonSpecs MODULES, dependencyVersions
		setCommonSpecs GROUP, groupVersions
		setCommonSpecs EXCLUDE, excludeModules
		setCommonSpecs DEPENDENCY, dependencies
	}

	void printConfigContent() {
		String projectName = projectName.toUpperCase()
		printConfigContent "${projectName} Group Maven Bom Version", getSpecsPrintData(BOM, bomVersions,
			{ specs, spec -> specs << [spec.id, spec.module, spec.version] }),
			groupTap(30), moduleTap(), versionTap(20)
		printConfigContent "${projectName} Group Maven Module Version", getSpecsPrintData(MODULES, dependencyVersions,
			{ specs, spec -> specs.addAll spec.modules.collect { [spec.id, it, spec.version] } }),
			groupTap(35), moduleTap(), versionTap(15)
		printConfigContent "${projectName} Group Maven Default Version", getSpecsPrintData(GROUP, groupVersions,
			{ specs, spec -> specs << [spec.id, spec.version] }), groupTap(), versionTap()
		printConfigContent "${projectName} Exclude Group Modules", getSpecsPrintData(EXCLUDE, excludeModules,
			{ specs, spec -> specs.addAll(spec.modules.collect { [spec.id, it] }) }),
			groupTap(40), moduleTap()
		printConfigContent "${projectName} Config Default Dependencies", getSpecsPrintData(DEPENDENCY, dependencies,
			{ specs, spec -> specs.addAll(spec.modules.collect { [spec.id, it] }) }),
			dependencyTypeTap(), dependenciesTap()
	}

	//<editor-fold desc="DSL扩展相关实体">

	interface ActionSpec<T> {

		List<T> getSpecs()

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

	interface DependenciesSpec extends ActionSpec<BomSpecImpl> {

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
	private final class GroupSpecImpl implements GroupSpec<BomSpecImpl> {

		final VersionType type
		List<BomSpecImpl> specs = []

		@Override
		BomSpecImpl group(String group) {
			new BomSpecImpl(type, group).tap {
				specs << it
			}
		}

	}

	private final class DependenciesSpecImpl implements DependenciesSpec {

		final List<BomSpecImpl> specs = []

		@Override
		void compile(String type, String... dependencies) {
			assert type, 'dependencies type not null!'
			assert dependencies, type + ' dependencies not empty!'
			specs << (specs.find { type == it.id }?.tap { o -> o.modules.addAll dependencies }
				?: new BomSpecImpl(DEPENDENCY, type)).tap { it.modules = dependencies }
		}

	}

	@TupleConstructor(includes = 'type,id')
	private class BomSpecImpl implements ModuleSpec, ModulesSpec {

		final VersionType type
		final String id
		String version
		String module
		Set<String> modules

		@Override
		void version(String version) {
			if (EXCLUDE == type) throw new GradleException('Does not support \'version\' method!')
			this.version = findVersion id, version
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

		boolean compare(BomSpecImpl o) {
			type == o.type && id == o.id && version == o.version && module == o.module && modules == o.modules
		}

		void rightShift(Set<BomSpecImpl> specs) {
			if (EXCLUDE == type && !modules) {
				modules = ['all']
			}
			specs << (type in [BOM, GROUP] ? this : specs.find { this == it }
				?.tap { it.modules.addAll this.modules } ?: this)
		}

		@Override
		boolean equals(o) {
			if (this.is(o)) return true
			if (!(o instanceof BomSpecImpl)) return false
			type == o.type && id == o.id &&
				(BOM != type || module == o.module) && (MODULES != type || version == o.version)
		}

		@Override
		int hashCode() {
			int result
			result = 31 * type.hashCode() + id.hashCode()
			if (BOM == type) result = 31 * result + module.hashCode()
			if (MODULES == type) result = 31 * result + version.hashCode()
			result
		}

	}

	@TupleConstructor
	private enum VersionType {

		BOM, MODULES, GROUP, EXCLUDE, DEPENDENCY

	}

	//</editor-fold>

}
