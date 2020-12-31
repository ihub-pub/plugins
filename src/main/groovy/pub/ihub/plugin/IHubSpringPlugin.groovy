package pub.ihub.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

import static pub.ihub.plugin.Constants.SPRING_BOOT_VERSION
import static pub.ihub.plugin.Constants.SPRING_CLOUD_VERSION
import static pub.ihub.plugin.PluginUtils.findProperty
import static pub.ihub.plugin.PluginUtils.printConfigContent



/**
 * @author henry
 */
class IHubSpringPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def springBootDepVersion = findProperty project, 'spring.boot.dependencies.version', SPRING_BOOT_VERSION
        def springCloudDepVersion = findProperty project, 'spring.cloud.dependencies.version', SPRING_CLOUD_VERSION

        project.pluginManager.apply 'io.spring.dependency-management'

        project.dependencyManagement {
            imports {
                mavenBom "org.springframework.boot:spring-boot-dependencies:$springBootDepVersion"
                mavenBom "org.springframework.cloud:spring-cloud-dependencies:$springCloudDepVersion"
            }
        }
        printConfigContent 'Gradle Project Spring Dependencies Version', 'Group', 'Modules', [
                'org.springframework.boot': SPRING_BOOT_VERSION, 'org.springframework.cloud': SPRING_CLOUD_VERSION
        ]
    }

}
