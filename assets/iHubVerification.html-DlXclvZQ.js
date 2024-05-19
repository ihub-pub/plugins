import{_ as r,a as u}from"./printFinishedJacocoReportCoverage-CAauqfUl.js";import{_ as p}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as i,o as h,c as b,d as t,e,a as s,w as n,b as l}from"./app-DR86wGH8.js";const v={},g=t("h1",{id:"ihub-verification",tabindex:"-1"},[t("a",{class:"header-anchor",href:"#ihub-verification","aria-hidden":"true"},"#"),e(" ihub-verification")],-1),f=t("div",{class:"hint-container info"},[t("p",{class:"hint-container-title"},"插件说明"),t("p",null,[t("code",null,"ihub-verification"),e("插件用于配置代码静态检查以及测试用例覆盖率等。")])],-1),m=t("thead",null,[t("tr",null,[t("th",null,"信息"),t("th",null,"描述")])],-1),_=t("tr",null,[t("td",null,"插件ID"),t("td",null,[t("code",null,"pub.ihub.plugin.ihub-verification")])],-1),k=t("tr",null,[t("td",null,"插件名称"),t("td",null,[t("code",null,"验证插件")])],-1),E=t("tr",null,[t("td",null,"插件类型"),t("td",null,[t("code",null,"Project"),t("sup",{class:"footnote-ref"},[t("a",{href:"#footnote1"},"[1]"),t("a",{class:"footnote-anchor",id:"footnote-ref1"})])])],-1),x=t("tr",null,[t("td",null,"扩展名称"),t("td",null,[t("code",null,"iHubVerification")])],-1),j=t("td",null,"插件依赖",-1),y=t("a",{href:"iHubBom"},"ihub-bom",-1),A={href:"https://docs.gradle.org/current/userguide/codenarc_plugin.html",target:"_blank",rel:"noopener noreferrer"},C={href:"https://docs.gradle.org/current/userguide/pmd_plugin.html",target:"_blank",rel:"noopener noreferrer"},B={href:"https://docs.gradle.org/current/userguide/jacoco_plugin.html",target:"_blank",rel:"noopener noreferrer"},D={href:"https://docs.gradle.org/current/userguide/jacoco_report_aggregation_plugin.html",target:"_blank",rel:"noopener noreferrer"},P=t("code",null,"主项目",-1),V={class:"hint-container tip"},I=t("p",{class:"hint-container-title"},"插件功能",-1),R=t("code",null,"groovy",-1),F=t("code",null,"codenarc",-1),H=t("br",null,null,-1),G={href:"https://github.com/ihub-pub/plugins/blob/main/ihub-plugins/src/main/resources/META-INF/codenarc.groovy",target:"_blank",rel:"noopener noreferrer"},K=t("code",null,"$rootDir/conf/codenarc/codenarc.groovy",-1),M={href:"https://github.com/ihub-pub/plugins/tree/main/samples/sample-groovy",target:"_blank",rel:"noopener noreferrer"},N=t("li",null,[e("项目包含"),t("code",null,"java"),e("插件时会自动配置"),t("code",null,"pmd"),e("插件，组件使用"),t("code",null,"com.alibaba.p3c:p3c-pmd"),e("，可通过"),t("code",null,"$rootDir/conf/pmd/ruleset.xml"),e("配置检查规则，默认规则如下：")],-1),S=l(`<div class="language-groovy line-numbers-mode" data-ext="groovy"><pre class="language-groovy"><code>ruleSets <span class="token operator">=</span> <span class="token punctuation">[</span>
    <span class="token string">&#39;rulesets/java/ali-comment.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-concurrent.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-constant.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-exception.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-flowcontrol.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-naming.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-oop.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-orm.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-other.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-set.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/vm/ali-other.xml&#39;</span><span class="token punctuation">,</span>
<span class="token punctuation">]</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><ul><li><code>jacoco</code>插件用于检查代码测试覆盖率，主要检查维度为：<code>bundle分支覆盖率</code>、<code>bundle指令覆盖率</code>、<code>package指令覆盖率</code><br> ，如果是主项目会添加<code>jacoco-report-aggregation</code>插件，用于多项目时聚合测试报告，打印报告<a href="#%E6%B5%8B%E8%AF%95%E6%8A%A5%E5%91%8A">详见</a></li></ul>`,2),T=l('<h2 id="扩展属性" tabindex="-1"><a class="header-anchor" href="#扩展属性" aria-hidden="true">#</a> 扩展属性</h2><p><code>pmd</code>开头为<code>PMD静态检查</code>，<code>codenarc</code>开头为<code>Codenarc静态检查</code>，<code>jacoco</code>开头为<code>Jacoco覆盖率检查</code></p><table><thead><tr><th>Extension</th><th>Description</th><th>Default</th><th>Ext<sup class="footnote-ref"><a href="#footnote2">[2]</a><a class="footnote-anchor" id="footnote-ref2"></a></sup></th><th>Prj<sup class="footnote-ref"><a href="#footnote3">[3]</a><a class="footnote-anchor" id="footnote-ref3"></a></sup></th><th>Sys<sup class="footnote-ref"><a href="#footnote4">[4]</a><a class="footnote-anchor" id="footnote-ref4"></a></sup></th><th>Env<sup class="footnote-ref"><a href="#footnote5">[5]</a><a class="footnote-anchor" id="footnote-ref5"></a></sup></th></tr></thead><tbody><tr><td><code>pmdConsoleOutput</code></td><td>控制台是否打印PMD信息</td><td><code>false</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>pmdIgnoreFailures</code></td><td>PMD检查是否忽略失败</td><td><code>false</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>pmdVersion</code></td><td>PMD版本</td><td><code>6.55.0</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>codenarcIgnoreFailures</code></td><td>Codenarc检查是否忽略失败</td><td><code>false</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>codenarcVersion</code></td><td>Codenarc版本</td><td><code>3.2.0</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>jacocoVersion</code></td><td>Jacoco版本</td><td><code>0.8.8</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>jacocoBranchCoverageRuleEnabled</code></td><td>是否启用bundle分支覆盖检查</td><td><code>true</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>jacocoBranchCoveredRatio</code></td><td>bundle分支覆盖率</td><td><code>0.9</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>jacocoInstructionCoverageRuleEnabled</code></td><td>是否启用bundle指令覆盖检查</td><td><code>true</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>jacocoInstructionExclusion</code></td><td>bundle指令覆盖排除目录</td><td><code>**/app</code><br><code>**/config</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>jacocoInstructionCoveredRatio</code></td><td>bundle指令覆盖率</td><td><code>0.9</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>jacocoPackageCoverageRuleEnabled</code></td><td>是否启用package指令覆盖检查</td><td><code>true</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>jacocoPackageExclusion</code></td><td>package指令覆盖排除目录</td><td><code>*.app</code><br><code>*.config</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>jacocoPackageCoveredRatio</code></td><td>package指令覆盖率</td><td><code>0.9</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>jacocoReportExclusion</code></td><td>覆盖率报告排除目录</td><td><code>**/Application.class</code><br><code>**/app/*.class</code><br><code>**/config/*.class</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr></tbody></table><h2 id="插件安装" tabindex="-1"><a class="header-anchor" href="#插件安装" aria-hidden="true">#</a> 插件安装</h2>',4),w=t("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[t("pre",{class:"language-kotlin"},[t("code",null,[e("plugins "),t("span",{class:"token punctuation"},"{"),e(`
    `),t("span",{class:"token function"},"id"),t("span",{class:"token punctuation"},"("),t("span",{class:"token string-literal singleline"},[t("span",{class:"token string"},'"pub.ihub.plugin.ihub-verification"')]),t("span",{class:"token punctuation"},")"),e(`
`),t("span",{class:"token punctuation"},"}"),e(`
`)])]),t("div",{class:"line-numbers","aria-hidden":"true"},[t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"})])],-1),J=t("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[t("pre",{class:"language-groovy"},[t("code",null,[e("plugins "),t("span",{class:"token punctuation"},"{"),e(`
    id `),t("span",{class:"token string"},"'pub.ihub.plugin.ihub-verification'"),e(`
`),t("span",{class:"token punctuation"},"}"),e(`
`)])]),t("div",{class:"line-numbers","aria-hidden":"true"},[t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"})])],-1),O=t("h2",{id:"配置示例",tabindex:"-1"},[t("a",{class:"header-anchor",href:"#配置示例","aria-hidden":"true"},"#"),e(" 配置示例")],-1),z=t("h3",{id:"pmd静态检查示例",tabindex:"-1"},[t("a",{class:"header-anchor",href:"#pmd静态检查示例","aria-hidden":"true"},"#"),e(" PMD静态检查示例")],-1),L=t("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[t("pre",{class:"language-kotlin"},[t("code",null,[e("iHubVerification "),t("span",{class:"token punctuation"},"{"),e(`
    pmdConsoleOutput`),t("span",{class:"token punctuation"},"."),t("span",{class:"token function"},"set"),t("span",{class:"token punctuation"},"("),t("span",{class:"token boolean"},"true"),t("span",{class:"token punctuation"},")"),e(`
`),t("span",{class:"token punctuation"},"}"),e(`
`)])]),t("div",{class:"line-numbers","aria-hidden":"true"},[t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"})])],-1),$=t("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[t("pre",{class:"language-groovy"},[t("code",null,[e("iHubVerification "),t("span",{class:"token punctuation"},"{"),e(`
    pmdConsoleOutput `),t("span",{class:"token operator"},"="),e(),t("span",{class:"token boolean"},"true"),e(`
`),t("span",{class:"token punctuation"},"}"),e(`
`)])]),t("div",{class:"line-numbers","aria-hidden":"true"},[t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"})])],-1),q=t("h3",{id:"codenarc静态检查示例",tabindex:"-1"},[t("a",{class:"header-anchor",href:"#codenarc静态检查示例","aria-hidden":"true"},"#"),e(" Codenarc静态检查示例")],-1),Q=t("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[t("pre",{class:"language-kotlin"},[t("code",null,[e("iHubVerification "),t("span",{class:"token punctuation"},"{"),e(`
    codenarcIgnoreFailures`),t("span",{class:"token punctuation"},"."),t("span",{class:"token function"},"set"),t("span",{class:"token punctuation"},"("),t("span",{class:"token boolean"},"true"),t("span",{class:"token punctuation"},")"),e(`
`),t("span",{class:"token punctuation"},"}"),e(`
`)])]),t("div",{class:"line-numbers","aria-hidden":"true"},[t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"})])],-1),U=t("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[t("pre",{class:"language-groovy"},[t("code",null,[e("iHubVerification "),t("span",{class:"token punctuation"},"{"),e(`
    codenarcIgnoreFailures `),t("span",{class:"token operator"},"="),e(),t("span",{class:"token boolean"},"true"),e(`
`),t("span",{class:"token punctuation"},"}"),e(`
`)])]),t("div",{class:"line-numbers","aria-hidden":"true"},[t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"})])],-1),W=t("h3",{id:"jacoco测试覆盖示例",tabindex:"-1"},[t("a",{class:"header-anchor",href:"#jacoco测试覆盖示例","aria-hidden":"true"},"#"),e(" Jacoco测试覆盖示例")],-1),X=t("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[t("pre",{class:"language-kotlin"},[t("code",null,[e("iHubVerification "),t("span",{class:"token punctuation"},"{"),e(`
    jacocoBranchCoverageRuleEnabled`),t("span",{class:"token punctuation"},"."),t("span",{class:"token function"},"set"),t("span",{class:"token punctuation"},"("),t("span",{class:"token boolean"},"true"),t("span",{class:"token punctuation"},")"),e(`
    jacocoInstructionCoverageRuleEnabled`),t("span",{class:"token punctuation"},"."),t("span",{class:"token function"},"set"),t("span",{class:"token punctuation"},"("),t("span",{class:"token boolean"},"true"),t("span",{class:"token punctuation"},")"),e(`
    jacocoPackageCoverageRuleEnabled`),t("span",{class:"token punctuation"},"."),t("span",{class:"token function"},"set"),t("span",{class:"token punctuation"},"("),t("span",{class:"token boolean"},"true"),t("span",{class:"token punctuation"},")"),e(`
`),t("span",{class:"token punctuation"},"}"),e(`
`)])]),t("div",{class:"line-numbers","aria-hidden":"true"},[t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"})])],-1),Y=t("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[t("pre",{class:"language-groovy"},[t("code",null,[e("iHubVerification "),t("span",{class:"token punctuation"},"{"),e(`
    jacocoBranchCoverageRuleEnabled `),t("span",{class:"token operator"},"="),e(),t("span",{class:"token boolean"},"true"),e(`
    jacocoInstructionCoverageRuleEnabled `),t("span",{class:"token operator"},"="),e(),t("span",{class:"token boolean"},"true"),e(`
    jacocoPackageCoverageRuleEnabled `),t("span",{class:"token operator"},"="),e(),t("span",{class:"token boolean"},"true"),e(`
`),t("span",{class:"token punctuation"},"}"),e(`
`)])]),t("div",{class:"line-numbers","aria-hidden":"true"},[t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"}),t("div",{class:"line-number"})])],-1),Z=l('<h2 id="测试报告" tabindex="-1"><a class="header-anchor" href="#测试报告" aria-hidden="true">#</a> 测试报告</h2><p>测试用例执行完成会生成用例报告，控制台会打印测试覆盖率</p><ul><li>单个项目测试覆盖率示例<br><img src="'+r+'" alt="" loading="lazy"></li><li>项目汇总测试覆盖率示例<br><img src="'+u+'" alt="" loading="lazy"></li></ul><hr class="footnotes-sep">',4),tt={class:"footnotes"},et={class:"footnotes-list"},nt={id:"footnote1",class:"footnote-item"},ot=t("code",null,"Project",-1),at=t("code",null,"build.gradle",-1),st={href:"https://docs.gradle.org/current/dsl/org.gradle.api.Project.html",target:"_blank",rel:"noopener noreferrer"},dt=t("a",{href:"#footnote-ref1",class:"footnote-backref"},"↩︎",-1),ct=l('<li id="footnote2" class="footnote-item"><p><code>Ext</code>（Extension）：插件自定义扩展属性，配置于<code>build.gradle</code>文件，配置方式<a href="explanation#%E5%B1%9E%E6%80%A7%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E">详见</a> <a href="#footnote-ref2" class="footnote-backref">↩︎</a></p></li><li id="footnote3" class="footnote-item"><p><code>Prj</code>（Project）：项目属性，配置于<code>gradle.properties</code>文件，配置格式<code>扩展名</code>.<code>属性名</code><a href="explanation#%E5%B1%9E%E6%80%A7%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E">详见</a> <a href="#footnote-ref3" class="footnote-backref">↩︎</a></p></li><li id="footnote4" class="footnote-item"><p><code>Sys</code>（System）：系统属性，如命令行传递的信息等，配置格式<code>扩展名</code>.<code>属性名</code><a href="explanation#%E5%B1%9E%E6%80%A7%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E">详见</a> <a href="#footnote-ref4" class="footnote-backref">↩︎</a></p></li><li id="footnote5" class="footnote-item"><p><code>Env</code>（Environment）：环境变量属性，配置格式全部大写，多个单词，用<code>_</code>分隔<a href="explanation#%E5%B1%9E%E6%80%A7%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E">详见</a> <a href="#footnote-ref5" class="footnote-backref">↩︎</a></p></li>',4);function lt(it,rt){const d=i("ExternalLinkIcon"),c=i("CodeTabs");return h(),b("div",null,[g,f,t("table",null,[m,t("tbody",null,[_,k,E,x,t("tr",null,[j,t("td",null,[y,e("、"),t("a",A,[e("codenarc"),s(d)]),e("、"),t("a",C,[e("pmd"),s(d)]),e("、"),t("a",B,[e("jacoco"),s(d)]),e("、"),t("a",D,[e("jacoco-report-aggregation"),s(d)]),e("（"),P,e("）")])])])]),t("div",V,[I,t("ul",null,[t("li",null,[e("项目包含"),R,e("插件时会自动配置"),F,H,e(" 插件，默认配置"),t("a",G,[e("详见"),s(d)]),e("，可以通过配置"),K,e("覆盖默认配置，"),t("a",M,[e("示例"),s(d)])]),N]),S]),T,s(c,{id:"467",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:n(({value:o,isActive:a})=>[e("Kotlin")]),title1:n(({value:o,isActive:a})=>[e("Groovy")]),tab0:n(({value:o,isActive:a})=>[w]),tab1:n(({value:o,isActive:a})=>[J]),_:1}),O,z,s(c,{id:"481",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:n(({value:o,isActive:a})=>[e("Kotlin")]),title1:n(({value:o,isActive:a})=>[e("Groovy")]),tab0:n(({value:o,isActive:a})=>[L]),tab1:n(({value:o,isActive:a})=>[$]),_:1}),q,s(c,{id:"492",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:n(({value:o,isActive:a})=>[e("Kotlin")]),title1:n(({value:o,isActive:a})=>[e("Groovy")]),tab0:n(({value:o,isActive:a})=>[Q]),tab1:n(({value:o,isActive:a})=>[U]),_:1}),W,s(c,{id:"503",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:n(({value:o,isActive:a})=>[e("Kotlin")]),title1:n(({value:o,isActive:a})=>[e("Groovy")]),tab0:n(({value:o,isActive:a})=>[X]),tab1:n(({value:o,isActive:a})=>[Y]),_:1}),Z,t("section",tt,[t("ol",et,[t("li",nt,[t("p",null,[ot,e("：项目类型插件，配置于"),at,e("文件，类型说明"),t("a",st,[e("详见"),s(d)]),e(),dt])]),ct])])])}const bt=p(v,[["render",lt],["__file","iHubVerification.html.vue"]]);export{bt as default};
