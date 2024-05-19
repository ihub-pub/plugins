import{_ as o}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as u,o as c,c as d,a as l,w as e,b as r,d as n,e as s}from"./app-DR86wGH8.js";const p={},g=r('<p>开始使用本插件之前先要了解<code>Gradle</code>相关的基础配置，可移步至<a href="basics/gradleBasic">入门篇-Gradle基础入门</a>，本插件主要用于简化<code>setting.gradle</code>和<code>build.gradle</code>配置</p><h2 id="setting-gradle-配置" tabindex="-1"><a class="header-anchor" href="#setting-gradle-配置" aria-hidden="true">#</a> setting.gradle 配置</h2><p>引入<code>pub.ihub.plugin.ihub-settings</code>插件，该插件提供了常用的插件仓库配置以及多项目配置，详细使用见<a href="list/iHubSettings">文档</a>：</p>',3),b=n("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[n("pre",{class:"language-kotlin"},[n("code",null,[s("plugins "),n("span",{class:"token punctuation"},"{"),s(`
    `),n("span",{class:"token function"},"id"),n("span",{class:"token punctuation"},"("),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"pub.ihub.plugin.ihub-settings"')]),n("span",{class:"token punctuation"},")"),s(" version "),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"1.6.2"')]),s(`
`),n("span",{class:"token punctuation"},"}"),s(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1),v=n("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[n("pre",{class:"language-groovy"},[n("code",null,[s("plugins "),n("span",{class:"token punctuation"},"{"),s(`
    id `),n("span",{class:"token string"},"'pub.ihub.plugin.ihub-settings'"),s(" version "),n("span",{class:"token string"},"'1.6.2'"),s(`
`),n("span",{class:"token punctuation"},"}"),s(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1),k=n("h2",{id:"build-gradle-配置",tabindex:"-1"},[n("a",{class:"header-anchor",href:"#build-gradle-配置","aria-hidden":"true"},"#"),s(" build.gradle 配置")],-1),h=n("p",null,[s("引入"),n("code",null,"pub.ihub.plugin"),s("基础插件，该插件提供了常用组件仓库配置以及集成了一些其他扩展设置，详细使用见"),n("a",{href:"list/iHub"},"文档"),s("，此外还有一些其他的插件，见"),n("code",null,"插件集"),s("各插件说明：")],-1),m=n("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[n("pre",{class:"language-kotlin"},[n("code",null,[s("plugins "),n("span",{class:"token punctuation"},"{"),s(`
    `),n("span",{class:"token function"},"id"),n("span",{class:"token punctuation"},"("),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"pub.ihub.plugin"')]),n("span",{class:"token punctuation"},")"),s(`
`),n("span",{class:"token punctuation"},"}"),s(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1),_=n("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[n("pre",{class:"language-groovy"},[n("code",null,[s("plugins "),n("span",{class:"token punctuation"},"{"),s(`
    id `),n("span",{class:"token string"},"'pub.ihub.plugin'"),s(`
`),n("span",{class:"token punctuation"},"}"),s(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1),f=n("p",null,[s("另外："),n("code",null,"ihub-settings"),s("插件不是必须的，如果你的项目不需要多项目配置，可以直接引入"),n("code",null,"pub.ihub.plugin"),s("插件，注意需要设置版本号（引入"),n("code",null,"ihub-settings"),s("插件时会"),n("a",{href:"list/iHubSettings#%E9%BB%98%E8%AE%A4%E7%89%88%E6%9C%AC"},"自动配置版本号"),s("），配置如下：")],-1),x=n("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[n("pre",{class:"language-kotlin"},[n("code",null,[s("plugins "),n("span",{class:"token punctuation"},"{"),s(`
    `),n("span",{class:"token function"},"id"),n("span",{class:"token punctuation"},"("),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"pub.ihub.plugin"')]),n("span",{class:"token punctuation"},")"),s(" version "),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"'),n("span",{class:"token interpolation"},[n("span",{class:"token interpolation-punctuation punctuation"},"${"),n("span",{class:"token expression"},[s("ihub"),n("span",{class:"token punctuation"},"."),s("plugin"),n("span",{class:"token punctuation"},"."),s("version")]),n("span",{class:"token interpolation-punctuation punctuation"},"}")]),n("span",{class:"token string"},'"')]),s(`
`),n("span",{class:"token punctuation"},"}"),s(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1),y=n("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[n("pre",{class:"language-groovy"},[n("code",null,[s("plugins "),n("span",{class:"token punctuation"},"{"),s(`
    id `),n("span",{class:"token string"},"'pub.ihub.plugin'"),s(" version "),n("span",{class:"token string"},"'${ihub.plugin.version}'"),s(`
`),n("span",{class:"token punctuation"},"}"),s(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1);function A(G,B){const t=u("CodeTabs");return c(),d("div",null,[g,l(t,{id:"10",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:e(({value:i,isActive:a})=>[s("Kotlin")]),title1:e(({value:i,isActive:a})=>[s("Groovy")]),tab0:e(({value:i,isActive:a})=>[b]),tab1:e(({value:i,isActive:a})=>[v]),_:1}),k,h,l(t,{id:"25",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:e(({value:i,isActive:a})=>[s("Kotlin")]),title1:e(({value:i,isActive:a})=>[s("Groovy")]),tab0:e(({value:i,isActive:a})=>[m]),tab1:e(({value:i,isActive:a})=>[_]),_:1}),f,l(t,{id:"36",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:e(({value:i,isActive:a})=>[s("Kotlin")]),title1:e(({value:i,isActive:a})=>[s("Groovy")]),tab0:e(({value:i,isActive:a})=>[x]),tab1:e(({value:i,isActive:a})=>[y]),_:1})])}const K=o(p,[["render",A],["__file","index.html.vue"]]);export{K as default};
