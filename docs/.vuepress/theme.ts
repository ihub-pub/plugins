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
import {hopeTheme} from "vuepress-theme-hope";
import {zhNavbar} from "./navbar";
import {zhSidebar} from "./sidebar";

export default hopeTheme({
    hostname: "https://doc.ihub.pub/plugins/",

    author: {
        name: "Henry",
        url: "https://liheng.website",
    },

    iconAssets: "iconfont",

    logo: "https://doc.ihub.pub/ihub.png",
    logoDark: "https://doc.ihub.pub/ihub_white.png",

    repo: "ihub-pub/plugins",

    docsBranch: "docs",

    breadcrumb: false,

    navbar: zhNavbar,
    sidebar: zhSidebar,
    displayFooter: true,
    metaLocales: {
        editLink: "在 GitHub 上编辑此页",
    },

    navbarLayout: {
        start: ["Brand"],
        // center: ["Links"],
        end: ["Outlook", "Repo", "Gitee", "Changelog", "Search"],
    },

    plugins: {
        comment: {
            provider: "Waline",
            serverURL: "https://waline.liheng.website",
        },

        // all features are enabled for demo, only preserve features you need here
        mdEnhance: {
            align: true,
            attrs: true,
            chart: true,
            codetabs: true,
            demo: true,
            echarts: true,
            figure: true,
            flowchart: true,
            gfm: true,
            imgLazyload: true,
            imgSize: true,
            include: true,
            katex: true,
            mark: true,
            mermaid: true,
            sub: true,
            sup: true,
            tabs: true,
            vPre: true,
            vuePlayground: true,
        },
    },
});
