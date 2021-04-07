/*
 * Copyright (c) 2021 Henry 李恒 (henry.box@outlook.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pub.ihub.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginAware



/**
 * @author henry
 */
trait IHubPluginAware<T extends PluginAware> implements Plugin<T> {

	private static final ThreadLocal<T> TARGET_PLUGIN = new ThreadLocal<>()
	private static final DEFAULT_CONTENT_WIDTH = 100

	String findProperty(String key, boolean findSystem, String defaultValue = null) {
		findProperty(key) ?: findSystem ? findSystemProperty(key, defaultValue) : defaultValue
	}

	String findProperty(String key, String defaultValue = null) {
		findProperty target instanceof Project ? { String k -> target.findProperty k } :
			{ String k -> target.hasProperty(k) ? target."$k" : null }, key, defaultValue
	}

	static String findSystemProperty(String key, String defaultValue = null) {
		findProperty({ String k -> System.getProperty(k) ?: System.getenv(k) }, key, defaultValue)
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

	@Override
	void apply(T target) {
		TARGET_PLUGIN.set target
		apply()
		TARGET_PLUGIN.remove()
	}

	T getTarget() {
		TARGET_PLUGIN.get()
	}

	abstract void apply()

}
