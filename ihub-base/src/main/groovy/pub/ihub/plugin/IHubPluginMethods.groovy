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
     * 打印Banner信息（以表格形式展示构建环境信息）
     * @param title 标题
     * @param info 信息键值对
     */
    static void printBanner(String title, Map<String, String> info) {
        printConfigContent title, info.collect { k, v -> [k, v] } as List<List<?>>, 'Property', 'Value'
    }

    /**
     * 打印Banner信息（兼容跨ClassLoader场景下GString/LinkedHashMap类型）
     * @param title 标题（接受GString）
     * @param info 信息键值对（接受LinkedHashMap）
     */
    static void printBanner(Object title, Map info) {
        printConfigContent title.toString(), info.collect { k, v -> [k, v] } as List<List<?>>, 'Property', 'Value'
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
        if (taps) {
            widths.eachWithIndex { int width, int i ->
                widths.set i, [width, taps[i].length()].max()
            }
        }
        int width = CONTENT_WIDTH - (widths.size() - 1) * 3 - (widths.sum() as int)
        if (width > 0) {
            int sub = width / widths.size() as int
            widths = widths.collect { it + sub } as List<Integer>
            int remainder = (width % widths.size()).with { it > 0 ? it : 0 }
            widths << widths.removeLast() + remainder
        }
        TableFrame frame = 'UTF-8' == System.getProperty('file.encoding') ? TableFrame.UNICODE : TableFrame.ASCII
        printBorderline widths, frame.top, frame.line
        printCenter title, CONTENT_WIDTH - (width < 0 ? width : 0), frame.frame
        printBorderline widths, frame.taps, frame.line
        if (taps) {
            printTaps widths, taps.toList(), frame.data
            printBorderline widths, frame.middle, frame.line
        }
        data.each { printTaps widths, it, frame.data }
        printBorderline widths, frame.bottom, frame.line
    }

    /**
     * 居中打印
     * @param str 字符串
     */
    private static void printCenter(String str, int contentWidth, String frame) {
        int strRightBoundary = ((contentWidth + str.length()) / 2).intValue()
        int width = contentWidth > strRightBoundary ? contentWidth - strRightBoundary : 0
        printf "$frame %${strRightBoundary}s${' ' * width} $frame\n", str
    }

    /**
     * 打印行数据
     * @param widths 栏目宽度
     * @param data 打印数据
     * @param frame 边框字符
     */
    private static void printTaps(List<Integer> widths, List data, Tuple3<String, String, String> frame) {
        printf "${frame.v1}${widths.collect { "%-${it}s" }.join(frame.v2)}${frame.v3}\n", data
    }

    /**
     * 打印分割线
     * @param widths 栏目宽度
     * @param frame 边框字符
     * @param line 分割线
     */
    private static void printBorderline(List<Integer> widths, Tuple3<String, String, String> frame, String line) {
        println "${frame.v1}${widths.collect { line * it }.join(frame.v2)}${frame.v3}"
    }

    private enum TableFrame {

        UNICODE(
            '┌─', '─┬─', '─┐',
            '├─', '─┼─', '─┤',
            '└─', '─┴─', '─┘',
            '───', '│', '─'
        ),
        ASCII(
            '+-', '-+-', '-+',
            '+-', '-+-', '-+',
            '+-', '-+-', '-+',
            '---', '|', '-'
        )

        @SuppressWarnings('ParameterCount')
        TableFrame(String topLeft, String topMiddle, String topRight,
                   String middleLeft, String center, String middleRight,
                   String bottomLeft, String bottomMiddle, String bottomRight,
                   String topLine, String frame, String line) {
            this.top = Tuple.tuple(topLeft, topLine, topRight)
            this.taps = Tuple.tuple(middleLeft, topMiddle, middleRight)
            this.data = Tuple.tuple(frame + ' ', ' ' + frame + ' ', ' ' + frame)
            this.middle = Tuple.tuple(middleLeft, center, middleRight)
            this.bottom = Tuple.tuple(bottomLeft, bottomMiddle, bottomRight)
            this.frame = frame
            this.line = line
        }

        final Tuple3<String, String, String> top
        final Tuple3<String, String, String> taps
        final Tuple3<String, String, String> data
        final Tuple3<String, String, String> middle
        final Tuple3<String, String, String> bottom
        final String frame
        final String line

    }

}
