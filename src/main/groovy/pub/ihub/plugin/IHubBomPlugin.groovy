package pub.ihub.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

import static pub.ihub.plugin.Constants.GROUP_MAVEN_BOM_VERSION_CONFIG
import static pub.ihub.plugin.PluginUtils.findProperty
import static pub.ihub.plugin.PluginUtils.printConfigContent



/**
 * @author henry
 */
class IHubBomPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply 'io.spring.dependency-management'
		def dependenciesVersion = GROUP_MAVEN_BOM_VERSION_CONFIG.collect { group, module, version ->
			[group, module, findProperty(project, group + '.version', version)]
		}
		project.dependencyManagement {
			imports {
				dependenciesVersion.each { group, module, version ->
					mavenBom "$group:$module:$version"
				}
			}
		}
		printConfigContent "${project.name.toUpperCase()} Group Dependencies Version", 'Group', 'Version',
			dependenciesVersion.collectEntries { group, module, version -> [(group): version] }
	}

}
