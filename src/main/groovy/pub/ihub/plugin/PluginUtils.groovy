package pub.ihub.plugin

import org.gradle.api.Project



/**
 * @author liheng
 */
class PluginUtils {

    static String findProperty(String key, String defaultValue = null) {
        System.getProperty(key.toUpperCase()) ?: System.getenv(key.toLowerCase()) ?: defaultValue
    }

    static String findProperty(Project project, String key, String defaultValue = null) {
        project.findProperty(key) ?: key.replaceAll(/([A-Z])/, '_$1').with {
            project.findProperty(it.toLowerCase()) ?: project.findProperty(it.toUpperCase()) ?:
                    project.findProperty(it.toLowerCase().replaceAll('_', '-')) ?: defaultValue
        }
    }

}
