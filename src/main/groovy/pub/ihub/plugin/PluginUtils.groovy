package pub.ihub.plugin

import org.gradle.api.Project



/**
 * @author liheng
 */
class PluginUtils {

	private static final DEFAULT_CONTENT_WIDTH = 100

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

	static void printConfigContent(String title, Tuple2<String, Integer> key, Tuple2<String, Integer> value, Map map) {
		printConfigContent title, map.inject([]) { list, k, v ->
			if (v instanceof List)
				v.each { list << [k, it] }
			else
				list << [k, v]
			list
		}, key, value
	}

	static void printConfigContent(String title, List data, Tuple2<String, Integer>... taps) {
		def contentWidth = DEFAULT_CONTENT_WIDTH - 4
		def size = taps.count { !it.second }
		def tapWidth = size ? ((contentWidth - taps.sum { it.second ?: 0 } - 3 * (taps.size() - 1)) / size).intValue() : null
		def tapsList = taps ? taps.collect { it.second ? it : tap(it.first, tapWidth) } : [tap(null, contentWidth)]
		printBorderline tapsList*.second, '┌─', '───', '─┐'
		printCenter title
		printBorderline tapsList*.second, '├─', '─┬─', '─┤'
		if (taps) {
			printTaps tapsList*.second, tapsList*.first, '│ ', ' │ ', ' │'
			printBorderline tapsList*.second, '├─', '─┼─', '─┤'
		}
		data.each { printTaps tapsList*.second, it instanceof List ? it : [it], '│ ', ' │ ', ' │' }
		printBorderline tapsList*.second, '└─', '─┴─', '─┘'
	}

	static Tuple2<String, Integer> tap(String tap, Integer width = null) {
		new Tuple2<>(tap, width)
	}

	private static void printCenter(String str) {
		int width = DEFAULT_CONTENT_WIDTH - 4
		def strRightBoundary = ((width + str.length()) / 2).intValue()
		printf "│ %${strRightBoundary}s${' ' * (width - strRightBoundary)} │\n", str
	}

	private static void printTaps(List<Integer> widths, List data, String leftFrame, String separator, String rightFrame) {
		printf "$leftFrame${widths.collect { "%-${it}s" }.join(separator)}$rightFrame\n", data
	}

	private static void printBorderline(List<Integer> widths, String leftFrame, String separator, String rightFrame) {
		println "$leftFrame${widths.collect { '─' * it }.join(separator)}$rightFrame"
	}

}
