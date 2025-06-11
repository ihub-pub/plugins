/*
 * Copyright (c) 2021-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pub.ihub.plugin

import java.util.Locale
import kotlin.math.max

/**
 * 插件通用方法
 * @author henry
 */
object IHubPluginMethods {

    private const val PRINT_WIDTH = 100
    private const val CONTENT_WIDTH = PRINT_WIDTH - 4

    /**
     * 打印Map配置信息
     * @param title 标题
     * @param key key描述
     * @param value 值描述
     * @param map 配置数据
     */
    fun printMapConfigContent(title: String, key: String, value: String, map: Map<*, *>) {
        val data = map.flatMap { (k, v) ->
            if (v is List<*>) {
                v.map { listOf(k.toString(), it.toString()) }
            } else {
                listOf(listOf(k.toString(), v.toString()))
            }
        }
        printConfigContent(title, data, key, value)
    }

    /**
     * 打印配置信息
     * @param title 标题
     * @param data 配置信息
     * @param taps 配置栏目描述
     */
    fun printLineConfigContent(title: String, data: List<String>, vararg taps: String) {
        printConfigContent(title, data.map { listOf(it) }, *taps)
    }

    /**
     * 打印配置信息
     * @param title 标题
     * @param data 配置信息
     * @param taps 配置栏目描述
     */
    fun printConfigContent(title: String, data: List<List<*>>, vararg taps: String) {
        if (data.isEmpty()) {
            return
        }

        var widths = data.first().indices.map { i ->
            data.maxOf { it[i].toString().length }
        }.toMutableList()

        if (taps.isNotEmpty()) {
            widths.forEachIndexed { i, width ->
                widths[i] = max(width, taps[i].length)
            }
        }

        val totalWidths = widths.sum()
        var width = CONTENT_WIDTH - (widths.size - 1) * 3 - totalWidths
        if (width > 0) {
            val sub = width / widths.size
            widths = widths.map { it + sub }.toMutableList()
            val remainder = width % widths.size
            if (remainder > 0 && widths.isNotEmpty()) {
                widths[widths.size -1] = widths.last() + remainder
            }
        }

        val frame = if ("UTF-8" == System.getProperty("file.encoding")) TableFrame.UNICODE else TableFrame.ASCII
        printBorderline(widths, frame.top, frame.line)
        printCenter(title, CONTENT_WIDTH - if (width < 0) width else 0, frame.frame)
        printBorderline(widths, frame.taps, frame.line)
        if (taps.isNotEmpty()) {
            printTaps(widths, taps.toList(), frame.data)
            printBorderline(widths, frame.middle, frame.line)
        }
        data.forEach { printTaps(widths, it, frame.data) }
        printBorderline(widths, frame.bottom, frame.line)
    }

    /**
     * 居中打印
     * @param str 字符串
     */
    private fun printCenter(str: String, contentWidth: Int, frameChar: String) {
        val strRightBoundary = (contentWidth + str.length) / 2
        println(String.format(Locale.getDefault(), "$frameChar %${strRightBoundary}s${" ".repeat(contentWidth - strRightBoundary)} $frameChar", str))
    }

    /**
     * 打印行数据
     * @param widths 栏目宽度
     * @param data 打印数据
     * @param frame 边框字符
     */
    private fun printTaps(widths: List<Int>, data: List<*>, frame: Triple<String, String, String>) {
        val formatString = widths.joinToString(frame.second) { "%-${it}s" }
        println(String.format(Locale.getDefault(), "${frame.first}$formatString${frame.third}", *data.toTypedArray()))
    }

    /**
     * 打印分割线
     * @param widths 栏目宽度
     * @param frame 边框字符
     * @param line 分割线
     */
    private fun printBorderline(widths: List<Int>, frame: Triple<String, String, String>, line: String) {
        println("${frame.first}${widths.joinToString(frame.second) { line.repeat(it) }}${frame.third}")
    }

    private enum class TableFrame(
        val top: Triple<String, String, String>,
        val taps: Triple<String, String, String>,
        val data: Triple<String, String, String>,
        val middle: Triple<String, String, String>,
        val bottom: Triple<String, String, String>,
        val frame: String,
        val line: String
    ) {
        UNICODE(
            Triple("┌─", "─┬─", "─┐"),
            Triple("├─", "─┬─", "─┤"),
            Triple("│ ", " │ ", " │"),
            Triple("├─", "─┼─", "─┤"),
            Triple("└─", "─┴─", "─┘"),
            "│", "─"
        ),
        ASCII(
            Triple("+-", "-+-", "-+"),
            Triple("+-", "-+-", "-+"),
            Triple("| ", " | ", " |"),
            Triple("+-", "-+-", "-+"),
            Triple("+-", "-+-", "-+"),
            "|", "-"
        );
    }
}
