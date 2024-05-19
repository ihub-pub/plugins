import{_ as i}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as l,o as u,c as h,d as t,e,a as o,w as n,b as a}from"./app-DR86wGH8.js";const p={},f=a('<h1 id="ihub" tabindex="-1"><a class="header-anchor" href="#ihub" aria-hidden="true">#</a> ihub</h1><div class="hint-container info"><p class="hint-container-title">插件说明</p><p><code>ihub</code>插件是基础插件，用于配置<a href="#%E7%BB%84%E4%BB%B6%E4%BB%93%E5%BA%93">组件仓库</a>以及一些其他<a href="#%E6%89%A9%E5%B1%95%E5%B1%9E%E6%80%A7">扩展属性</a>，配置与<code>build.gradle</code>。</p></div><table><thead><tr><th>信息</th><th>描述</th></tr></thead><tbody><tr><td>插件ID</td><td><code>pub.ihub.plugin</code></td></tr><tr><td>插件名称</td><td><code>基础插件</code></td></tr><tr><td>插件类型</td><td><code>Project</code><sup class="footnote-ref"><a href="#footnote1">[1]</a><a class="footnote-anchor" id="footnote-ref1"></a></sup></td></tr><tr><td>扩展名称</td><td><code>iHub</code></td></tr><tr><td>插件依赖</td><td><a href="iHubGitHooks">ihub-git-hooks</a>、<a href="iHubBom">ihub-bom</a>、<a href="iHubVersion">ihub-version</a></td></tr></tbody></table>',3),_={class:"hint-container tip"},b=t("p",{class:"hint-container-title"},"插件功能",-1),m=t("li",null,[e("配置组件仓库，仓库明细"),t("a",{href:"#%E7%BB%84%E4%BB%B6%E4%BB%93%E5%BA%93"},"见")],-1),E={href:"https://docs.gradle.org/current/userguide/java_platform_plugin.html",target:"_blank",rel:"noopener noreferrer"},v={href:"https://docs.gradle.org/current/userguide/platforms.html",target:"_blank",rel:"noopener noreferrer"},g=t("code",null,"ihub-bom",-1),x=t("li",null,"如果项目包含子项目，子项目也会引入本插件",-1),k=a('<h2 id="扩展属性" tabindex="-1"><a class="header-anchor" href="#扩展属性" aria-hidden="true">#</a> 扩展属性</h2><table><thead><tr><th>Extension</th><th>Description</th><th>Default</th><th>Ext<sup class="footnote-ref"><a href="#footnote2">[2]</a><a class="footnote-anchor" id="footnote-ref2"></a></sup></th><th>Prj<sup class="footnote-ref"><a href="#footnote3">[3]</a><a class="footnote-anchor" id="footnote-ref3"></a></sup></th><th>Sys<sup class="footnote-ref"><a href="#footnote4">[4]</a><a class="footnote-anchor" id="footnote-ref4"></a></sup></th><th>Env<sup class="footnote-ref"><a href="#footnote5">[5]</a><a class="footnote-anchor" id="footnote-ref5"></a></sup></th></tr></thead><tbody><tr><td><code>mavenLocalEnabled</code></td><td>是否启用本地仓库</td><td><code>false</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>mavenAliYunEnabled</code></td><td>是否启用阿里云代理仓库</td><td><code>false</code></td><td>✔</td><td>✔</td><td>✔</td><td>✔</td></tr><tr><td><code>mavenPrivateEnabled</code></td><td>是否启用私有仓库（组件发布仓库）</td><td><code>true</code></td><td>✔</td><td>✔</td><td>✔</td><td>✔</td></tr><tr><td><code>releaseRepoUrl</code></td><td>正式版本仓库</td><td>❌</td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>snapshotRepoUrl</code></td><td>快照版本仓库</td><td>❌</td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>repoAllowInsecureProtocol</code></td><td>是否允许不安全协议（是否允许http）</td><td><code>false</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>repoIncludeGroup</code></td><td>仓库包含组（用于限制仓库范围）</td><td>❌</td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>repoIncludeGroupRegex</code></td><td>仓库包含组正则（用于限制仓库范围）</td><td><code>.*</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>repoUsername</code></td><td>仓库用户名</td><td>❌</td><td>✔</td><td>✔</td><td>✔</td><td>✔</td></tr><tr><td><code>repoPassword</code></td><td>仓库密码</td><td>❌</td><td>✔</td><td>✔</td><td>✔</td><td>✔</td></tr><tr><td><code>customizeRepoUrl</code></td><td>自定义仓库</td><td>❌</td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>profile</code></td><td>配置文件，多个配置用逗号分隔，优先级从右到左</td><td>❌</td><td>❌</td><td>✔</td><td>✔</td><td>❌</td></tr></tbody></table><h2 id="插件安装" tabindex="-1"><a class="header-anchor" href="#插件安装" aria-hidden="true">#</a> 插件安装</h2>',3),B=t("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[t("pre",{class:"language-kotlin"},[t("code",null,[e("plugins "),t("span",{class:"token punctuation"},"{"),e(`
    `),t("span",{class:"token function"},"id"),t("span",{class:"token punctuation"},"("),t("span",{class:"token string-literal singleline"},[t("span",{class:"token string"},'"pub.ihub.plugin"')]),t("span",{class:"token punctuation"},")"),e(`
`),t("span",{class:"token punctuation"},"}"),e(`
`)])]),t("div",{class:"line-numbers","aria-hidden":"true"},[t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"})])],-1),A=t("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[t("pre",{class:"language-groovy"},[t("code",null,[e("plugins "),t("span",{class:"token punctuation"},"{"),e(`
    id `),t("span",{class:"token string"},"'pub.ihub.plugin'"),e(`
`),t("span",{class:"token punctuation"},"}"),e(`
`)])]),t("div",{class:"line-numbers","aria-hidden":"true"},[t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"})])],-1),y=a(`<h2 id="配置示例" tabindex="-1"><a class="header-anchor" href="#配置示例" aria-hidden="true">#</a> 配置示例</h2><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token key attr-name">iHub.mavenLocalEnabled</span><span class="token punctuation">=</span><span class="token value attr-value">true</span>
<span class="token key attr-name">iHub.mavenAliYunEnabled</span><span class="token punctuation">=</span><span class="token value attr-value">true</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="组件仓库" tabindex="-1"><a class="header-anchor" href="#组件仓库" aria-hidden="true">#</a> 组件仓库</h2><p>为适应国内网络环境，配置组件仓库如下</p>`,4),D=t("thead",null,[t("tr",null,[t("th",null,"Name"),t("th",null,"Description"),t("th",null,"Url")])],-1),P=t("tr",null,[t("td",null,[t("code",null,"ProjectDirs")]),t("td",null,"项目本地组件"),t("td",null,[t("code",null,"{rootProject.projectDir}/libs")])],-1),j=t("tr",null,[t("td",null,[t("code",null,"MavenLocal")]),t("td",null,"本地仓库"),t("td",null,[t("code",null,"{local}/.m2/repository")])],-1),R=t("td",null,[t("code",null,"AliYunPublic")],-1),H=t("td",null,"阿里云聚合公有仓库",-1),I={href:"https://maven.aliyun.com/repository/public",target:"_blank",rel:"noopener noreferrer"},S=t("br",null,null,-1),U={href:"https://repo1.maven.org/maven2",target:"_blank",rel:"noopener noreferrer"},C=t("td",null,[t("code",null,"ReleaseRepo")],-1),G=t("td",null,"私有Release仓库",-1),L={href:"https://repo.xxx.com/release",target:"_blank",rel:"noopener noreferrer"},N=t("td",null,[t("code",null,"SnapshotRepo")],-1),V=t("td",null,"私有Snapshot仓库",-1),w={href:"https://repo.xxx.com/snapshot",target:"_blank",rel:"noopener noreferrer"},F=t("td",null,[t("code",null,"CustomizeRepo")],-1),M=t("td",null,"自定义仓库仓库",-1),T={href:"https://repo.xxx.com/repo",target:"_blank",rel:"noopener noreferrer"},Y=t("tr",null,[t("td",null,[t("code",null,"MavenRepo")]),t("td",null,"Maven中央仓库"),t("td")],-1),z=t("hr",{class:"footnotes-sep"},null,-1),K={class:"footnotes"},q={class:"footnotes-list"},J={id:"footnote1",class:"footnote-item"},O=t("code",null,"Project",-1),Q=t("code",null,"build.gradle",-1),W={href:"https://docs.gradle.org/current/dsl/org.gradle.api.Project.html",target:"_blank",rel:"noopener noreferrer"},X=t("a",{href:"#footnote-ref1",class:"footnote-backref"},"↩︎",-1),Z=a('<li id="footnote2" class="footnote-item"><p><code>Ext</code>（Extension）：插件自定义扩展属性，配置于<code>build.gradle</code>文件，配置方式<a href="explanation#%E5%B1%9E%E6%80%A7%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E">详见</a> <a href="#footnote-ref2" class="footnote-backref">↩︎</a></p></li><li id="footnote3" class="footnote-item"><p><code>Prj</code>（Project）：项目属性，配置于<code>gradle.properties</code>文件，配置格式<code>扩展名</code>.<code>属性名</code><a href="explanation#%E5%B1%9E%E6%80%A7%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E">详见</a> <a href="#footnote-ref3" class="footnote-backref">↩︎</a></p></li><li id="footnote4" class="footnote-item"><p><code>Sys</code>（System）：系统属性，如命令行传递的信息等，配置格式<code>扩展名</code>.<code>属性名</code><a href="explanation#%E5%B1%9E%E6%80%A7%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E">详见</a> <a href="#footnote-ref4" class="footnote-backref">↩︎</a></p></li><li id="footnote5" class="footnote-item"><p><code>Env</code>（Environment）：环境变量属性，配置格式全部大写，多个单词，用<code>_</code>分隔<a href="explanation#%E5%B1%9E%E6%80%A7%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E">详见</a> <a href="#footnote-ref5" class="footnote-backref">↩︎</a></p></li>',4);function $(tt,et){const d=l("ExternalLinkIcon"),c=l("CodeTabs");return u(),h("div",null,[f,t("div",_,[b,t("ol",null,[m,t("li",null,[e("当主项目不含"),t("a",E,[e("java-platform"),o(d)]),e("和"),t("a",v,[e("version-catalog"),o(d)]),e("插件时，会默认导入"),g,e("插件")]),x])]),k,o(c,{id:"392",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:n(({value:s,isActive:r})=>[e("Kotlin")]),title1:n(({value:s,isActive:r})=>[e("Groovy")]),tab0:n(({value:s,isActive:r})=>[B]),tab1:n(({value:s,isActive:r})=>[A]),_:1}),y,t("table",null,[D,t("tbody",null,[P,j,t("tr",null,[R,H,t("td",null,[t("a",I,[e("https://maven.aliyun.com/repository/public"),o(d)]),e(),S,e(" artifactUrls: "),t("a",U,[e("https://repo1.maven.org/maven2"),o(d)])])]),t("tr",null,[C,G,t("td",null,[t("a",L,[e("https://repo.xxx.com/release"),o(d)])])]),t("tr",null,[N,V,t("td",null,[t("a",w,[e("https://repo.xxx.com/snapshot"),o(d)])])]),t("tr",null,[F,M,t("td",null,[t("a",T,[e("https://repo.xxx.com/repo"),o(d)])])]),Y])]),z,t("section",K,[t("ol",q,[t("li",J,[t("p",null,[O,e("：项目类型插件，配置于"),Q,e("文件，类型说明"),t("a",W,[e("详见"),o(d)]),e(),X])]),Z])])])}const nt=i(p,[["render",$],["__file","iHub.html.vue"]]);export{nt as default};
