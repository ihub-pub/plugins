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

import groovy.transform.CompileStatic



/**
 * 插件通用方法
 * @author henry
 */
@CompileStatic
@SuppressWarnings('Println')
final class IHubPluginMethods {

    private static final int PRINT_WIDTH = 100
    private static final int CONTENT_WIDTH = PRINT_WIDTH - 4

    /**
     * 打印Map配置信息
     * @param title 标题
     * @param key key描述
     * @param value 值描述
     * @param map 配置数据
     */
    static void printMapConfigContent(String title, String key, String value, Map map) {
        printConfigContent title, map.inject([]) { list, k, v ->
            if (v instanceof List) {
                v.each { list << [k, it] }
            } else {
                list << [k, v]
            }
            list
        } as List<List<String>>, key, value
    }

    /**
     * 打印配置信息
     * @param title 标题
     * @param data 配置信息
     * @param taps 配置栏目描述
     */
    static void printLineConfigContent(String title, List<String> data, String... taps) {
        printConfigContent title, data.collect { [it] }, taps
    }

    /**
     * 打印配置信息
     * @param title 标题
     * @param data 配置信息
     * @param taps 配置栏目描述
     */
    static void printConfigContent(String title, List<List<?>> data, String... taps) {
        if (!data) {
            return
        }

        List<Integer> widths = data.with {
            (0..first().size() - 1).inject([]) { s, i ->
                s << max { it[i].toString().length() }[i].toString().length()
            }
        } as List<Integer>
        int width = CONTENT_WIDTH - (widths.size() - 1) * 3 - (widths.sum() as int)
        if (width > 0) {
            int sub = width / widths.size() as int
            widths = widths.collect { it + sub } as List<Integer>
            int remainder = (width % widths.size()).with { it > 0 ? it : 0 }
            widths << widths.removeLast() + remainder
        }
        printBorderline widths, '┌─', '───', '─┐'
        printCenter title, CONTENT_WIDTH - (width < 0 ? width : 0)
        printBorderline widths, '├─', '─┬─', '─┤'
        if (taps) {
            printTaps widths, taps.toList(), '│ ', ' │ ', ' │'
            printBorderline widths, '├─', '─┼─', '─┤'
        }
        data.each { printTaps widths, it, '│ ', ' │ ', ' │' }
        printBorderline widths, '└─', '─┴─', '─┘'
    }

    /**
     * 居中打印
     * @param str 字符串
     */
    private static void printCenter(String str, int contentWidth) {
        int strRightBoundary = ((contentWidth + str.length()) / 2).intValue()
        printf "│ %${strRightBoundary}s${' ' * (contentWidth - strRightBoundary)} │\n", str
    }

    /**
     * 打印行数据
     * @param widths 栏目宽度
     * @param data 打印数据
     * @param leftFrame 左边框字符
     * @param separator 分隔符
     * @param rightFrame 右边框字符
     */
    private static void printTaps(List<Integer> widths, List data,
                                  String leftFrame, String separator, String rightFrame) {
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
