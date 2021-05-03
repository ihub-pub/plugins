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

import org.gradle.api.Action
import org.gradle.api.Project

/**
 * BOM插件DSL扩展
 * @author liheng
 */
class IHubBomExtension {

	final Project project

	IHubBomExtension(Project project) {
		this.project = project
	}

	/**
	 * 导入mavenBom
	 * @param action 配置
	 */
	void importBoms(Action<DependencyVersionsSpec> action) {
		List<DependencyVersionSpec> boms = new DependencyVersionsSpec().tap { action.execute it }.specs
		assert boms, 'import boms config not empty!'
		project.dependencyManagement {
			imports {
				boms.each { spec ->
					mavenBom spec.coordinates
					println "$project.name import bom -> $spec.coordinates"
				}
			}
		}
	}

	/**
	 * 配置依赖默认版本
	 * @param action 配置
	 */
	void dependencyVersions(Action<DependencyVersionsSpec> action) {
		List<DependencyVersionSpec> boms = new DependencyVersionsSpec().tap { action.execute it }.specs
		assert boms, 'dependency versions config not empty!'
		project.dependencyManagement {
			dependencies {
				boms.each { spec ->
					dependencySet(group: spec.group, version: spec.version) {
						spec.modules.each { entry it }
					}
					println "$project.name dependencies -> $spec.group:$spec.modules:$spec.version"
				}
			}
		}
	}

	/**
	 * 配置组版本策略（建议尽量使用bom）
	 * @param action 配置
	 */
	void groupVersions(Action<DependencyVersionsSpec> action) {
		List<DependencyVersionSpec> groupVersions = new DependencyVersionsSpec().tap { action.execute it }.specs
		assert groupVersions, 'group versions config not empty!'
		project.configurations {
			all {
				resolutionStrategy {
					eachDependency {
						groupVersions.find { spec -> spec.group == it.requested.group }?.with { spec ->
							it.useVersion spec.version
							println "$project.name strategy -> $spec.group use version $spec.version"
						}
					}
				}
			}
		}
	}

	private class DependencyVersionsSpec {

		private final List<DependencyVersionSpec> specs = []

		DependencyVersionSpec group(String group) {
			new DependencyVersionSpec(group).tap {
				specs << it
			}
		}

	}

	private class DependencyVersionSpec {

		private final String group
		private String module
		private String version
		private List<String> modules

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

		String getCoordinates() {
			"$group:$module:$version"
		}

	}

}
