import{_ as r}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as c,o as p,c as d,d as n,e as s,a as t,w as a,b as l}from"./app-DR86wGH8.js";const b={},g=n("h1",{id:"kotlin项目基础配置",tabindex:"-1"},[n("a",{class:"header-anchor",href:"#kotlin项目基础配置","aria-hidden":"true"},"#"),s(" Kotlin项目基础配置")],-1),k={href:"https://github.com/ihub-pub/kotlin-template",target:"_blank",rel:"noopener noreferrer"},v=l(`<h2 id="配置-wrapper" tabindex="-1"><a class="header-anchor" href="#配置-wrapper" aria-hidden="true">#</a> 配置 wrapper</h2><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token key attr-name">distributionUrl</span><span class="token punctuation">=</span><span class="token value attr-value">https\\://services.gradle.org/distributions/gradle-8.7-bin.zip</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div></div></div><h2 id="配置-setting-gradle" tabindex="-1"><a class="header-anchor" href="#配置-setting-gradle" aria-hidden="true">#</a> 配置 setting.gradle</h2>`,3),h=n("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[n("pre",{class:"language-kotlin"},[n("code",null,[s("plugins "),n("span",{class:"token punctuation"},"{"),s(`
    `),n("span",{class:"token function"},"id"),n("span",{class:"token punctuation"},"("),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"pub.ihub.plugin.ihub-settings"')]),n("span",{class:"token punctuation"},")"),s(" version "),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"1.6.2"')]),s(`
`),n("span",{class:"token punctuation"},"}"),s(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1),m=n("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[n("pre",{class:"language-groovy"},[n("code",null,[s("plugins "),n("span",{class:"token punctuation"},"{"),s(`
    id `),n("span",{class:"token string"},"'pub.ihub.plugin.ihub-settings'"),s(" version "),n("span",{class:"token string"},"'1.6.2'"),s(`
`),n("span",{class:"token punctuation"},"}"),s(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1),_=l('<h2 id="配置-build-gradle" tabindex="-1"><a class="header-anchor" href="#配置-build-gradle" aria-hidden="true">#</a> 配置 build.gradle</h2><p>引入Kotlin插件（<a href="../iHubKotlin">ihub-kotlin</a>）、测试插件（<a href="../iHubTest">ihub-test</a>）以及验证插件（<a href="../iHubVerification">ihub-verification</a>），配置<a href="../iHubGitHooks">ihub-git-hooks</a>插件钩子命令：</p>',2),f=n("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[n("pre",{class:"language-kotlin"},[n("code",null,[s("plugins "),n("span",{class:"token punctuation"},"{"),s(`
    `),n("span",{class:"token function"},"id"),n("span",{class:"token punctuation"},"("),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"pub.ihub.plugin.ihub-kotlin"')]),n("span",{class:"token punctuation"},")"),s(`
    `),n("span",{class:"token function"},"id"),n("span",{class:"token punctuation"},"("),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"pub.ihub.plugin.ihub-test"')]),n("span",{class:"token punctuation"},")"),s(`
    `),n("span",{class:"token function"},"id"),n("span",{class:"token punctuation"},"("),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"pub.ihub.plugin.ihub-verification"')]),n("span",{class:"token punctuation"},")"),s(`
    `),n("span",{class:"token function"},"id"),n("span",{class:"token punctuation"},"("),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"pub.ihub.plugin.ihub-git-hooks"')]),n("span",{class:"token punctuation"},")"),s(`
`),n("span",{class:"token punctuation"},"}"),s(`

iHubGitHooks `),n("span",{class:"token punctuation"},"{"),s(`
    hooks`),n("span",{class:"token punctuation"},"."),n("span",{class:"token function"},"set"),n("span",{class:"token punctuation"},"("),n("span",{class:"token function"},"mapOf"),n("span",{class:"token punctuation"},"("),s(`
        `),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"pre-commit"')]),s(),n("span",{class:"token keyword"},"to"),s(),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"./gradlew build"')]),n("span",{class:"token punctuation"},","),s(`
        `),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"commit-msg"')]),s(),n("span",{class:"token keyword"},"to"),s(),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"./gradlew commitCheck"')]),s(`
    `),n("span",{class:"token punctuation"},")"),n("span",{class:"token punctuation"},")"),s(`
`),n("span",{class:"token punctuation"},"}"),s(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1),x=n("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[n("pre",{class:"language-groovy"},[n("code",null,[s("plugins "),n("span",{class:"token punctuation"},"{"),s(`
    id `),n("span",{class:"token string"},"'pub.ihub.plugin.ihub-kotlin'"),s(`
    id `),n("span",{class:"token string"},"'pub.ihub.plugin.ihub-test'"),s(`
    id `),n("span",{class:"token string"},"'pub.ihub.plugin.ihub-verification'"),s(`
    id `),n("span",{class:"token string"},"'pub.ihub.plugin.ihub-git-hooks'"),s(`
`),n("span",{class:"token punctuation"},"}"),s(`

iHubGitHooks `),n("span",{class:"token punctuation"},"{"),s(`
    hooks `),n("span",{class:"token operator"},"="),s(),n("span",{class:"token punctuation"},"["),s(`
        `),n("span",{class:"token string"},"'pre-commit'"),n("span",{class:"token punctuation"},":"),s(),n("span",{class:"token string"},"'./gradlew build'"),n("span",{class:"token punctuation"},","),s(`
        `),n("span",{class:"token string"},"'commit-msg'"),n("span",{class:"token punctuation"},":"),s(),n("span",{class:"token string"},"'./gradlew commitCheck'"),s(`
    `),n("span",{class:"token punctuation"},"]"),s(`
`),n("span",{class:"token punctuation"},"}"),s(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1),y=l(`<h2 id="配置-gradle-properties" tabindex="-1"><a class="header-anchor" href="#配置-gradle-properties" aria-hidden="true">#</a> 配置 gradle.properties</h2><p>配置项目名称以及group，其中<code>name</code>为<a href="../iHubSettings">ihub-settings</a>插件<a href="../iHubSettings#%E6%89%A9%E5%B1%95%E5%B1%9E%E6%80%A7">扩展属性</a>，<code>group</code>为原生项目属性</p><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token key attr-name">name</span><span class="token punctuation">=</span><span class="token value attr-value">demo</span>
<span class="token key attr-name">group</span><span class="token punctuation">=</span><span class="token value attr-value">pub.ihub.demo</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div></div></div>`,3);function w(H,A){const u=c("ExternalLinkIcon"),o=c("CodeTabs");return p(),d("div",null,[g,n("p",null,[s("Kotlin项目配置，参见"),n("a",k,[s("项目模板"),t(u)])]),v,t(o,{id:"16",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:a(({value:e,isActive:i})=>[s("Kotlin")]),title1:a(({value:e,isActive:i})=>[s("Groovy")]),tab0:a(({value:e,isActive:i})=>[h]),tab1:a(({value:e,isActive:i})=>[m]),_:1}),_,t(o,{id:"31",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:a(({value:e,isActive:i})=>[s("Kotlin")]),title1:a(({value:e,isActive:i})=>[s("Groovy")]),tab0:a(({value:e,isActive:i})=>[f]),tab1:a(({value:e,isActive:i})=>[x]),_:1}),y])}const B=r(b,[["render",w],["__file","kotlinBasic.html.vue"]]);export{B as default};
