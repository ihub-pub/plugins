package pub.ihub.plugin

import org.gradle.api.Project



/**
 * @author liheng
 */
class PluginUtils {

    static String findProperty(String key, String defaultValue = null) {
        System.getProperty(key) ?: System.getenv(key) ?: System.getProperty(key.toUpperCase()) ?: key.toLowerCase().with {
            System.getProperty(it) ?: System.getProperty(it.split('_').inject { a, b -> a + b.capitalize() })
                    ?: System.getProperty(it.replaceAll('_', '.'), defaultValue)
        }
    }

    static String findProperty(Project project, String key, String defaultValue = null) {
        project.findProperty(key) ?: key.replaceAll(/([A-Z])/, '_$1').toLowerCase().with {
            project.findProperty(it) ?: project.findProperty(it.toUpperCase())
                    ?: project.findProperty(it.replaceAll('_', '.'))
                    ?: project.findProperty(it.replaceAll('_', '-'))
                    ?: defaultValue
        }
    }

}
