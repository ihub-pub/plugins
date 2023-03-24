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
import {enNavbar, zhNavbar} from "./navbar";
import {enSidebar, zhSidebar} from "./sidebar";

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

    docsDir: "docs",

    breadcrumb: false,

    locales: {
        "/": {
            navbar: zhNavbar,
            sidebar: zhSidebar,
            displayFooter: true,
            metaLocales: {
                editLink: "在 GitHub 上编辑此页",
            },
        },

        "/en/": {
            navbar: enNavbar,
            sidebar: enSidebar,
            displayFooter: true,
            metaLocales: {
                editLink: "Edit this page on GitHub",
            },
        },
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
            playground: {
                presets: ["ts", "vue"],
            },
            presentation: {
                plugins: ["highlight", "math", "search", "notes", "zoom"],
            },
            stylize: [
                {
                    matcher: "Recommended",
                    replacer: ({tag}) => {
                        if (tag === "em")
                            return {
                                tag: "Badge",
                                attrs: {type: "tip"},
                                content: "Recommended",
                            };
                    },
                },
            ],
            sub: true,
            sup: true,
            tabs: true,
            vPre: true,
            vuePlayground: true,
        },

        // uncomment these if you want a pwa
        // pwa: {
        //   favicon: "/favicon.ico",
        //   cacheHTML: true,
        //   cachePic: true,
        //   appendBase: true,
        //   apple: {
        //     icon: "/assets/icon/apple-icon-152.png",
        //     statusBarColor: "black",
        //   },
        //   msTile: {
        //     image: "/assets/icon/ms-icon-144.png",
        //     color: "#ffffff",
        //   },
        //   manifest: {
        //     icons: [
        //       {
        //         src: "/assets/icon/chrome-mask-512.png",
        //         sizes: "512x512",
        //         purpose: "maskable",
        //         type: "image/png",
        //       },
        //       {
        //         src: "/assets/icon/chrome-mask-192.png",
        //         sizes: "192x192",
        //         purpose: "maskable",
        //         type: "image/png",
        //       },
        //       {
        //         src: "/assets/icon/chrome-512.png",
        //         sizes: "512x512",
        //         type: "image/png",
        //       },
        //       {
        //         src: "/assets/icon/chrome-192.png",
        //         sizes: "192x192",
        //         type: "image/png",
        //       },
        //     ],
        //     shortcuts: [
        //       {
        //         name: "Demo",
        //         short_name: "Demo",
        //         url: "/demo/",
        //         icons: [
        //           {
        //             src: "/assets/icon/guide-maskable.png",
        //             sizes: "192x192",
        //             purpose: "maskable",
        //             type: "image/png",
        //           },
        //         ],
        //       },
        //     ],
        //   },
        // },
    },
});
