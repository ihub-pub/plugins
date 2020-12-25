package pub.ihub.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

import static pub.ihub.plugin.PluginUtils.findProperty



/**
 * @author henry
 */
class IHubSpringPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def springBootDepVersion = findProperty project, 'spring_boot_dep_version', '2.3.5.RELEASE'
        def springCloudDepVersion = findProperty project, 'spring_cloud_dep_version', 'Hoxton.SR9'

        project.pluginManager.apply 'io.spring.dependency-management'

        project.dependencyManagement {
            imports {
                mavenBom "org.springframework.boot:spring-boot-dependencies:$springBootDepVersion"
                mavenBom "org.springframework.cloud:spring-cloud-dependencies:$springCloudDepVersion"
            }
        }
    }

}
