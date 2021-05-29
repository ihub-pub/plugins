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

/**
 * 插件通用方法
 * @author henry
 */
@SuppressWarnings('Println')
final class IHubPluginMethods {

    static Tuple2<String, Integer> tap(String tap, Integer width = null) {
        new Tuple2<>(tap, width)
    }

    static Tuple2<String, Integer> idTap() {
        tap 'ID'
    }

    static Tuple2<String, Integer> groupTap(Integer width = null) {
        tap 'Group', width
    }

    static Tuple2<String, Integer> versionTap(Integer width = 30) {
        tap 'Version', width
    }

    static Tuple2<String, Integer> moduleTap(Integer width = null) {
        tap 'Module', width
    }

    static Tuple2<String, Integer> dependencyTypeTap() {
        tap 'DependencyType', 30
    }

    static Tuple2<String, Integer> dependenciesTap() {
        tap 'Dependencies'
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
            if (v instanceof List) {
                v.each { list << [k, it] }
            } else {
                list << [k, v]
            }
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
        printConfigContent title, data - null, 100, taps
    }

    /**
     * 打印Map配置信息
     * @param title 标题
     * @param data 配置信息
     * @param printWidth 打印宽度
     * @param taps 配置栏目描述
     */
    static void printConfigContent(String title, List data, int printWidth, Tuple2<String, Integer>... taps) {
        if (!data) {
            return
        }
        int contentWidth = printWidth - 4
        Number size = taps.count { !it.v2 }
        int tapWidth = size ?
            ((contentWidth - (taps.sum { it.v2 ?: 0 } as Integer) - 3 * (taps.size() - 1)) / size).intValue() : null
        List tapsList = taps ? taps.collect { it.v2 ? it : tap(it.v1, tapWidth) } : [tap(null, contentWidth)]
        printBorderline tapsList*.v2, '┌─', '───', '─┐'
        printCenter title, contentWidth
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
