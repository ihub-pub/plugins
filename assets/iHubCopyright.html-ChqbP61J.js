import{_ as r}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as l,o as u,c as p,a,w as s,d as e,e as n,b as c}from"./app-DR86wGH8.js";const h={},b=c('<h1 id="ihub-copyright" tabindex="-1"><a class="header-anchor" href="#ihub-copyright" aria-hidden="true">#</a> ihub-copyright</h1><div class="hint-container info"><p class="hint-container-title">插件说明</p><p><code>ihub-copyright</code>插件是版权插件，用于自动配置<code>IDEA</code>版权设置，自动伪代码添加或更新版权信息。</p></div><table><thead><tr><th>插件ID</th><th>插件名称</th><th>插件类型</th><th>插件依赖</th></tr></thead><tbody><tr><td><code>pub.ihub.plugin.ihub-copyright</code></td><td><code>版权插件</code></td><td><code>Project</code><sup class="footnote-ref"><a href="#footnote1">[1]</a><a class="footnote-anchor" id="footnote-ref1"></a></sup></td><td><a href="iHub">ihub</a></td></tr></tbody></table><h2 id="插件安装" tabindex="-1"><a class="header-anchor" href="#插件安装" aria-hidden="true">#</a> 插件安装</h2>',4),v=e("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[e("pre",{class:"language-kotlin"},[e("code",null,[n("plugins "),e("span",{class:"token punctuation"},"{"),n(`
    `),e("span",{class:"token function"},"id"),e("span",{class:"token punctuation"},"("),e("span",{class:"token string-literal singleline"},[e("span",{class:"token string"},'"pub.ihub.plugin.ihub-copyright"')]),e("span",{class:"token punctuation"},")"),n(`
`),e("span",{class:"token punctuation"},"}"),n(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),g=e("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[e("pre",{class:"language-groovy"},[e("code",null,[n("plugins "),e("span",{class:"token punctuation"},"{"),n(`
    id `),e("span",{class:"token string"},"'pub.ihub.plugin.ihub-copyright'"),n(`
`),e("span",{class:"token punctuation"},"}"),n(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),m=e("p",null,"或",-1),_=e("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[e("pre",{class:"language-kotlin"},[e("code",null,[n("plugins "),e("span",{class:"token punctuation"},"{"),n(`
    `),e("span",{class:"token function"},"id"),e("span",{class:"token punctuation"},"("),e("span",{class:"token string-literal singleline"},[e("span",{class:"token string"},'"pub.ihub.plugin"')]),e("span",{class:"token punctuation"},")"),n(`
`),e("span",{class:"token punctuation"},"}"),n(`

apply `),e("span",{class:"token punctuation"},"{"),n(`
    `),e("span",{class:"token function"},"plugin"),e("span",{class:"token punctuation"},"("),e("span",{class:"token string-literal singleline"},[e("span",{class:"token string"},'"pub.ihub.plugin.ihub-copyright"')]),e("span",{class:"token punctuation"},")"),n(`
`),e("span",{class:"token punctuation"},"}"),n(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),k=e("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[e("pre",{class:"language-groovy"},[e("code",null,[n("plugins "),e("span",{class:"token punctuation"},"{"),n(`
    id `),e("span",{class:"token string"},"'pub.ihub.plugin'"),n(`
`),e("span",{class:"token punctuation"},"}"),n(`

apply `),e("span",{class:"token punctuation"},"{"),n(`
    plugin `),e("span",{class:"token string"},"'pub.ihub.plugin.ihub-copyright'"),n(`
`),e("span",{class:"token punctuation"},"}"),n(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),f=c(`<h2 id="配置示例" tabindex="-1"><a class="header-anchor" href="#配置示例" aria-hidden="true">#</a> 配置示例</h2><ol><li>通过<code>COPYRIGHT</code>文件获取版权信息，文件配置项目根目录下</li></ol><div class="language-text line-numbers-mode" data-ext="text"><pre class="language-text"><code>Copyright (c) 2023 the original author or authors.

Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0
    
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><ol start="2"><li>通过<code>LICENSE</code>文件提取版权信息，目前会识别<code>Apache License</code>、<code>MIT License</code>配置通用版权信息，其他版权会使用IDEA默认版权配置</li></ol><hr class="footnotes-sep">`,5),y={class:"footnotes"},x={class:"footnotes-list"},A={id:"footnote1",class:"footnote-item"},I=e("code",null,"Project",-1),L=e("code",null,"build.gradle",-1),C={href:"https://docs.gradle.org/current/dsl/org.gradle.api.Project.html",target:"_blank",rel:"noopener noreferrer"},N=e("a",{href:"#footnote-ref1",class:"footnote-backref"},"↩︎",-1);function E(S,w){const o=l("CodeTabs"),d=l("ExternalLinkIcon");return u(),p("div",null,[b,a(o,{id:"45",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:s(({value:i,isActive:t})=>[n("Kotlin")]),title1:s(({value:i,isActive:t})=>[n("Groovy")]),tab0:s(({value:i,isActive:t})=>[v]),tab1:s(({value:i,isActive:t})=>[g]),_:1}),m,a(o,{id:"56",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:s(({value:i,isActive:t})=>[n("Kotlin")]),title1:s(({value:i,isActive:t})=>[n("Groovy")]),tab0:s(({value:i,isActive:t})=>[_]),tab1:s(({value:i,isActive:t})=>[k]),_:1}),f,e("section",y,[e("ol",x,[e("li",A,[e("p",null,[I,n("：项目类型插件，配置于"),L,n("文件，类型说明"),e("a",C,[n("详见"),a(d)]),n(),N])])])])])}const q=r(h,[["render",E],["__file","iHubCopyright.html.vue"]]);export{q as default};
