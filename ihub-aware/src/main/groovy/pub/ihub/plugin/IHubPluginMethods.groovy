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


import org.gradle.api.Project
import org.gradle.api.plugins.PluginAware



/**
 * 插件通用方法
 * @author henry
 */
final class IHubPluginMethods {

	private static final int DEFAULT_CONTENT_WIDTH = 100

	/**
	 * 优先从环境变量查找属性
	 * @param key key
	 * @param plugin 插件
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	static String findProperty(String key, PluginAware plugin, String defaultValue = null) {
		findProperty(key) ?: findProperty(plugin, key, defaultValue)
	}

	/**
	 * 从任务插件查找属性
	 * @param project 插件
	 * @param key key
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	static String findProperty(Project project, String key, String defaultValue = null) {
		findProperty(key, defaultValue) { String k -> project.findProperty k }
	}

	/**
	 * 从插件查找属性
	 * @param plugin 插件
	 * @param key key
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	static String findProperty(PluginAware plugin, String key, String defaultValue = null) {
		findProperty(key, defaultValue) { String k -> plugin.hasProperty(k) ? plugin."$k" : null }
	}

	/**
	 * 从环境变量查找属性
	 * @param key key
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	static String findProperty(String key, String defaultValue = null) {
		findProperty(key, defaultValue) { String k -> System.getProperty(k) ?: System.getenv(k) }
	}

	/**
	 * 查找属性
	 *
	 * 按如下优先级变换key查询：
	 * demoKey(原值) -> demo_key -> DEMO_KEY -> demo.key -> demo-key
	 *
	 * @param key key
	 * @param defaultValue 默认值
	 * @param closure 查询闭包
	 * @return 属性值
	 */
	static String findProperty(String key, String defaultValue = null, Closure closure) {
		closure(key) ?: key.replaceAll(/([A-Z])/, '_$1').toLowerCase().with {
			closure(it) ?: closure(it.toUpperCase())
				?: closure(it.replaceAll('_', '.'))
				?: closure(it.replaceAll('_', '-'))
				?: defaultValue
		}
	}

	static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 v1, T2 v2, T3 v3) {
		new Tuple3<>(v1, v2, v3)
	}

	static Tuple2<String, Integer> tap(String tap, Integer width = null) {
		new Tuple2<>(tap, width)
	}

	/**
	 * 打印Map配置信息
	 * @param title 标题
	 * @param key key描述
	 * @param value 值描述
	 * @param map 配置数据
	 */
	static void printConfigContent(String title, Tuple2<String, Integer> key, Tuple2<String, Integer> value, Map map) {
		printConfigContent title, map.inject([]) { list, k, v ->
			if (v instanceof List)
				v.each { list << [k, it] }
			else
				list << [k, v]
			list
		}, key, value
	}

	/**
	 * 打印Map配置信息
	 * @param title 标题
	 * @param data 配置信息
	 * @param taps 配置栏目描述
	 */
	static void printConfigContent(String title, List data, Tuple2<String, Integer>... taps) {
		def contentWidth = DEFAULT_CONTENT_WIDTH - 4
		def size = taps.count { !it.v2 }
		def tapWidth = size ? ((contentWidth - (taps.sum { it.v2 ?: 0 } as Integer) - 3 * (taps.size() - 1)) / size).intValue() : null
		def tapsList = taps ? taps.collect { it.v2 ? it : tap(it.v1, tapWidth) } : [tap(null, contentWidth)]
		printBorderline tapsList*.v2, '┌─', '───', '─┐'
		printCenter title
		printBorderline tapsList*.v2, '├─', '─┬─', '─┤'
		if (taps) {
			printTaps tapsList*.v2, tapsList*.v1, '│ ', ' │ ', ' │'
			printBorderline tapsList*.v2, '├─', '─┼─', '─┤'
		}
		data.each { printTaps tapsList*.v2, it instanceof List ? it : [it], '│ ', ' │ ', ' │' }
		printBorderline tapsList*.v2, '└─', '─┴─', '─┘'
	}

	/**
	 * 居中打印
	 * @param str 字符串
	 */
	private static void printCenter(String str) {
		int width = DEFAULT_CONTENT_WIDTH - 4
		def strRightBoundary = ((width + str.length()) / 2).intValue()
		printf "│ %${strRightBoundary}s${' ' * (width - strRightBoundary)} │\n", str
	}

	/**
	 * 打印行数据
	 * @param widths 栏目宽度
	 * @param data 打印数据
	 * @param leftFrame 左边框字符
	 * @param separator 分隔符
	 * @param rightFrame 右边框字符
	 */
	private static void printTaps(List<Integer> widths, List data, String leftFrame, String separator, String rightFrame) {
		printf "$leftFrame${widths.collect { "%-${it}s" }.join(separator)}$rightFrame\n", data
	}

	/**
	 * 打印分割线
	 * @param widths 栏目宽度
	 * @param leftFrame 左边框字符
	 * @param separator 分隔符
	 * @param rightFrame 右边框字符
	 */
	private static void printBorderline(List<Integer> widths, String leftFrame, String separator, String rightFrame) {
		println "$leftFrame${widths.collect { '─' * it }.join(separator)}$rightFrame"
	}

}
