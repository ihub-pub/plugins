/*
 * Copyright (c) 2023 the original author or authors.
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
import { defineUserConfig } from 'vuepress'
import { searchProPlugin } from "vuepress-plugin-search-pro"
import { googleAnalyticsPlugin } from '@vuepress/plugin-google-analytics'

import theme from "./theme.js"

export default defineUserConfig({
    base: '/plugins/',
    lang: 'zh-CN',
    title: 'IHub Doc',
    description: '工欲善其事，必先利其器。IHub是一套用于Java开发的神兵利器，IHub Plugins插件集封装了常用Gradle插件，可以极大的简化项目管理配置。',
    head: [
        ['link', { rel: 'icon', href: 'https://doc.ihub.pub/favicon.ico' }],
        ['meta', { name: 'keywords', content: 'ihub,java,groovy,gradle,maven,插件,组件,plugins,libs,技术,博客' }]
    ],

    pagePatterns: ["**/*.md", "!snippet/*.md", "!node_modules/**"],
    theme,

    plugins: [
        searchProPlugin({
            // 索引全部内容
            indexContent: true,
        }),
        googleAnalyticsPlugin({
            id: 'G-G60CQD0XTC',
        }),
    ],
})
