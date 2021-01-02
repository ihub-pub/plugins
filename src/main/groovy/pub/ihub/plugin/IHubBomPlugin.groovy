package pub.ihub.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

import static pub.ihub.plugin.Constants.GROUP_DEFAULT_DEPENDENCIES_MAPPING
import static pub.ihub.plugin.Constants.GROUP_DEPENDENCY_EXCLUDE_MAPPING
import static pub.ihub.plugin.Constants.GROUP_DEPENDENCY_VERSION_CONFIG
import static pub.ihub.plugin.Constants.GROUP_MAVEN_BOM_VERSION_CONFIG
import static pub.ihub.plugin.Constants.IHUB_LIB_VERSION
import static pub.ihub.plugin.PluginUtils.findProperty
import static pub.ihub.plugin.PluginUtils.printConfigContent
import static pub.ihub.plugin.PluginUtils.tap



/**
 * @author henry
 */
class IHubBomPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply 'io.spring.dependency-management'

		def bomVersion = GROUP_MAVEN_BOM_VERSION_CONFIG.collect { group, module, version ->
			[group, module, findProperty(project, group + '.version', version)]
		}
		def dependenciesVersion = GROUP_DEPENDENCY_VERSION_CONFIG.collect { group, version, modules ->
			[group, findProperty(project, group + '.version', version), modules]
		}
		project.dependencyManagement {
			imports {
				bomVersion.each { group, module, version ->
					mavenBom "$group:$module:$version"
				}
			}
			printConfigContent "${project.name.toUpperCase()} Group Maven Bom Version", bomVersion,
				tap('Group', 30), tap('Module'), tap('Version', 20)

			dependencies {
				dependenciesVersion.each { group, version, modules ->
					dependencySet(group: group, version: version) {
						modules.each { entry it }
					}
				}
			}
			printConfigContent "${project.name.toUpperCase()} Group Maven Bom Version", dependenciesVersion
				.inject([]) { list, config ->
					def (group, version, modules) = config
					list + modules.collect { [group, it, version] }
				}, tap('Group', 30), tap('Module'), tap('Version', 20)
		}

		project.configurations {
			all {
				resolutionStrategy {
					eachDependency {
						if ('pub.ihub.lib' == it.requested.group) {
							it.useVersion findProperty(project, 'pub.ihub.lib.version', IHUB_LIB_VERSION)
						}
					}
					// 不缓存动态版本
					cacheDynamicVersionsFor 0, 'seconds'
					// 不缓存快照模块
					cacheChangingModulesFor 0, 'seconds'
				}
			}
			all {
				GROUP_DEPENDENCY_EXCLUDE_MAPPING.each { group, modules ->
					modules.each { module ->
						exclude group: group, module: module
					}
				}
			}
			printConfigContent "${project.name.toUpperCase()} Exclude Group Modules",
				tap('Group', 40), tap('Modules'), GROUP_DEPENDENCY_EXCLUDE_MAPPING

			GROUP_DEFAULT_DEPENDENCIES_MAPPING.each { key, dependencies ->
				maybeCreate(key).getDependencies()
					.addAll(dependencies.collect { project.getDependencies().create(it) })
			}
			printConfigContent "${project.name.toUpperCase()} Config Default Dependencies",
				tap('DependencyType', 30), tap('Dependencies'), GROUP_DEFAULT_DEPENDENCIES_MAPPING
		}
	}

}
