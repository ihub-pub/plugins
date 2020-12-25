package pub.ihub.plugin

import org.gradle.api.Project
import org.gradle.api.initialization.Settings



/**
 * @author liheng
 */
class PluginUtils {

    private static final DEFAULT_CONTENT_WIDTH = 80

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

    static String findProperty(obj, String key, String defaultValue = null) {
        getObjectProperty(obj, key) ?: key.toLowerCase().with {
            getObjectProperty(obj, it) ?: getObjectProperty(obj, it.toUpperCase())
                    ?: getObjectProperty(obj, it.replaceAll('_', '.'))
                    ?: getObjectProperty(obj, it.replaceAll('_', '-'))
                    ?: defaultValue
        }
    }

    private static String getObjectProperty(obj, String key) {
        obj.hasProperty(key) ? obj."$key" : null
    }

    static void printConfigContent(String title, List<String> list) {
        def width = DEFAULT_CONTENT_WIDTH
        println '┌─' + '─' * width + '─┐'
        printCenter title, width
        println '├─' + '─' * width + '─┤'
        list.each { printContent it, width }
        println '└─' + '─' * width + '─┘'
    }

    static void printConfigContent(String title, String keyName, String valueName, Map map) {
        def width = DEFAULT_CONTENT_WIDTH
        def leftWidth = (width / 2).intValue()
        def rightWidth = width - leftWidth - 1
        println '┌─' + '─' * width + '─┐'
        printCenter title, width
        println '├─' + '─' * leftWidth + '┬' + '─' * rightWidth + '─┤'
        printMap leftWidth, rightWidth, keyName, valueName
        println '├─' + '─' * leftWidth + '┼' + '─' * rightWidth + '─┤'
        map.each { k, v ->
            if (v instanceof List) {
                v.each {
                    printMap leftWidth, rightWidth, k, it
                }
            } else {
                printMap leftWidth, rightWidth, k, v
            }
        }
        println '└─' + '─' * leftWidth + '┴' + '─' * rightWidth + '─┘'
    }

    private static void printContent(String str, int width) {
        printf "│ %-${width}s │\n", str
    }

    private static void printCenter(String str, int width) {
        def strRightBoundary = ((width + str.length()) / 2).intValue()
        printf "│ %${strRightBoundary}s${' ' * (width - strRightBoundary)} │\n", str
    }

    private static void printMap(int leftWidth, int rightWidth, key, value) {
        printf "│ %-${leftWidth}s│ %-${rightWidth}s│\n", [key, value]
    }

}
