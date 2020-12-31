package pub.ihub.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.tasks.compile.AbstractCompile

import static pub.ihub.plugin.Constants.GRADLE_COMPILATION_INCREMENTAL
import static pub.ihub.plugin.Constants.JAVA_COMPATIBILITY
import static pub.ihub.plugin.PluginUtils.findProperty



/**
 * @author henry
 */
class IHubJavaPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.pluginManager.apply JavaLibraryPlugin
//        project.pluginManager.apply 'build-dashboard' // TODO 待确认
//        project.pluginManager.apply 'project-report' // TODO 待确认
        project.pluginManager.apply 'io.freefair.lombok'

        if (System.getProperty('java.version').startsWith('11')) {
            project.configurations {
                maybeCreate('runtimeOnly').getDependencies().addAll([
                        'javax.xml.bind:jaxb-api:2.3.1',
                        'com.sun.xml.bind:jaxb-core:2.3.0.1',
                        'com.sun.xml.bind:jaxb-impl:2.3.1'
                ].collect { project.getDependencies().create(it) })
            }
        }

        def javaCompatibility = findProperty project, JAVA_COMPATIBILITY
        if (javaCompatibility) {
            def gradleCompilationIncremental = findProperty(project, GRADLE_COMPILATION_INCREMENTAL, 'true').toBoolean()
            project.tasks.withType(AbstractCompile) {
                sourceCompatibility = javaCompatibility
                targetCompatibility = javaCompatibility
                options.encoding = 'UTF-8'
                options.incremental = gradleCompilationIncremental
            }
        }
    }

}
