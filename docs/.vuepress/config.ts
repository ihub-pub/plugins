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
import { searchPlugin } from '@vuepress/plugin-search'
import { googleAnalyticsPlugin } from '@vuepress/plugin-google-analytics'
import { defaultTheme } from '@vuepress/theme-default'
import { sidebar } from './configs/sidebar.js'
import { navbar } from './configs/navbar.js'

const vvv = '1.0.0'
export default defineUserConfig({
    base: '/plugins/',
    lang: 'zh-CN',
    title: 'IHub Doc',
    description: '工欲善其事，必先利其器。IHub是一套用于Java开发的神兵利器，IHub Plugins插件集封装了常用Gradle插件，可以极大的简化项目管理配置。',
    head: [
        ['link', { rel: 'icon', href: 'https://doc.ihub.pub/favicon.ico' }],
        ['meta', { name: 'keywords', content: 'ihub,java,groovy,gradle,maven,插件,组件,plugins,libs,技术,博客' }]
    ],

    theme: defaultTheme({
        logo: 'https://doc.ihub.pub/ihub.png',
        logoDark: 'https://doc.ihub.pub/ihub_white.png',
        repo: 'ihub-pub/plugins',
        docsDir: 'docs',
        editLinkText: '在 GitHub 上编辑此页',
        lastUpdatedText: '上次更新',
        contributorsText: '贡献者',
        // custom containers
        tip: '提示',
        warning: '注意',
        danger: '警告',
        // 404 page
        notFound: [
            '这里什么都没有',
            '我们怎么到这来了？',
            '这是一个 404 页面',
            '看起来我们进入了错误的链接',
        ],
        backToHome: '返回首页',
        navbar: navbar,
        sidebar: sidebar,
    }),

    plugins: [
        searchPlugin({
            locales: {
                '/': {
                    placeholder: '搜索',
                },
            },
        }),
        googleAnalyticsPlugin({
            id: 'G-G60CQD0XTC',
        }),
    ],
})
