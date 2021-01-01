package pub.ihub.plugin

import org.gradle.api.Project



/**
 * @author liheng
 */
class PluginUtils {

	private static final DEFAULT_CONTENT_WIDTH = 80

	static String findProperty(String key, String defaultValue = null) {
		findProperty({ String k -> System.getProperty(k) ?: System.getenv(k) }, key, defaultValue)
	}

	static String findProperty(Project project, String key, String defaultValue = null) {
		findProperty({ Project p, String k -> p.findProperty k }.curry(project), key, defaultValue)
	}

	static String findProperty(obj, String key, String defaultValue = null) {
		findProperty({ o, String k -> o.hasProperty(k) ? o."$k" : null }.curry(obj), key, defaultValue)
	}

	static String findProperty(Closure closure, String key, String defaultValue = null) {
		closure(key) ?: key.replaceAll(/([A-Z])/, '_$1').toLowerCase().with {
			closure(it) ?: closure(it.toUpperCase())
				?: closure(it.replaceAll('_', '.'))
				?: closure(it.replaceAll('_', '-'))
				?: defaultValue
		}
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
