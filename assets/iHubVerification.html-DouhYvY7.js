import{_ as r,a as u}from"./printFinishedJacocoReportCoverage-CAauqfUl.js";import{_ as p}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as i,o as h,c as v,d as e,e as t,a as s,w as n,b as l}from"./app-DR86wGH8.js";const b={},g=l('<h1 id="ihub-authentication" tabindex="-1"><a class="header-anchor" href="#ihub-authentication" aria-hidden="true">#</a> ihub-authentication</h1><div class="hint-container info"><p class="hint-container-title">plugin description</p><p><code>ihub-verification</code>plugin configuration code static check and test case coverage etc.</p></div><p>:::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::<br> :::</p>',3),m=e("thead",null,[e("tr",null,[e("th",null,"Information"),e("th",null,"Description")])],-1),k=e("tr",null,[e("td",null,"Plugin ID"),e("td",null,[e("code",null,"pub.ihub.plugin.ihub-authentication")])],-1),_=e("tr",null,[e("td",null,"Plugin Name"),e("td",null,[e("code",null,"Verify Plugin")])],-1),f=e("tr",null,[e("td",null,"Plugin Type"),e("td",null,[e("code",null,"Project"),t("[^Project]")])],-1),x=e("tr",null,[e("td",null,"Extension Name"),e("td",null,[e("code",null,"iHubVerification")])],-1),j=e("td",null,"Plugin Dependencies",-1),y=e("a",{href:"iHubBom"},"ihub-bom",-1),E={href:"https://docs.gradle.org/current/userguide/codenarc_plugin.html",target:"_blank",rel:"noopener noreferrer"},C={href:"https://docs.gradle.org/current/userguide/pmd_plugin.html",target:"_blank",rel:"noopener noreferrer"},P={href:"https://docs.gradle.org/current/userguide/jacoco_plugin.html",target:"_blank",rel:"noopener noreferrer"},A={href:"https://docs.gradle.org/current/userguide/jacoco_report_aggregation_plugin.html",target:"_blank",rel:"noopener noreferrer"},V=e("code",null,"main project",-1),w={class:"hint-container tip"},I=e("p",{class:"hint-container-title"},"plugin functionality",-1),R=e("code",null,"groovy",-1),D=e("code",null,"codenarrc",-1),B={href:"https://github.com/ihub-pub/plugins/blob/main/ihub-plugins/src/main/resources/META-INF/codenarc.groovy",target:"_blank",rel:"noopener noreferrer"},T=e("code",null,"$rootDir/conf/codenarc/codenarc.groovy",-1),H={href:"https://github.com/ihub-pub/plugins/tree/main/samples/sample-groovy",target:"_blank",rel:"noopener noreferrer"},S=e("li",null,[t("项目包含"),e("code",null,"java"),t("插件时会自动配置"),e("code",null,"pmd"),t("插件，组件使用"),e("code",null,"com.alibaba.p3c:p3c-pmd"),t("，可通过"),e("code",null,"$rootDir/conf/pmd/ruleset.xml"),t("配置检查规则，默认规则如下：")],-1),G=l(`<div class="language-groovy line-numbers-mode" data-ext="groovy"><pre class="language-groovy"><code>RuleSets <span class="token operator">=</span> <span class="token punctuation">[</span>
    <span class="token string">&#39;rulesets/java/ali-comment.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-concilient. ml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-constant.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-exception.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-flowcontrol. ml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-naming.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-oop.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-orm. ml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-other.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/java/ali-set.xml&#39;</span><span class="token punctuation">,</span>
    <span class="token string">&#39;rulesets/vm/ali-other.xml&#39;</span><span class="token punctuation">,</span>
<span class="token punctuation">]</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><ul><li><code>jacoco</code>Plugins are used to check code test coverage, primarily at：<code>bundle branch coverage</code>,<code>bundle command covering</code>and<code>package command covering</code> , The result is that the main project will add<code>jacoco-report-agregation</code>plugins for multi-project aggregating test reports, printing report<a href="#%E6%B5%8B%E8%AF%95%E6%8A%A5%E5%91%8A">See</a> ::</li></ul><h2 id="extended-properties" tabindex="-1"><a class="header-anchor" href="#extended-properties" aria-hidden="true">#</a> Extended Properties</h2><p><code>pmd</code>starts with<code>PMD-static check</code>,<code>codenarrc</code>starts with<code>Codenarc static check</code>,<code>jacoco</code>starts with<code>Jacoco coverage</code></p><table><thead><tr><th>Extension</th><th>Description</th><th>Default</th><th>Ext[^Ext]</th><th>Prj[^Prj]</th><th>Sys[^Sys]</th><th>Env[^Env]</th></tr></thead><tbody><tr><td><code>pmConsoleOutput</code></td><td>Whether or not the console print PMD information</td><td><code>false</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>pmdIgnoreFailures</code></td><td>PMD check failed to ignore</td><td><code>false</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>pmdversion</code></td><td>PMD Version</td><td><code>6.55.0</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>codenarcIgnoreFailures</code></td><td>Codenarc check if failed to ignore</td><td><code>false</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>codenarcVersion</code></td><td>Codenarc Version</td><td><code>3.2.0</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>jacoco Version</code></td><td>Jacoco Version</td><td><code>0.8.8</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>jacocoBranchCoverage RuleEnabled</code></td><td>Enable Bundle branch overwrite check</td><td><code>true</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>jacocoBranchCoveredRatio</code></td><td>bundle branch coverage</td><td><code>0.9</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>jacocoInstructionCoverageRuleEnabled</code></td><td>Enable Bundle command overwrite check</td><td><code>true</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>jacocoInstructionExclusion</code></td><td>bundle command overwrite exclusion directory</td><td><code>**/app</code><br><code>**/config</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>jacoco Instruction CoveredRatio</code></td><td>bundle command coverage</td><td><code>0.9</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>jacocoPackageCoverage RuleEnabled</code></td><td>Enable package command overwrite check</td><td><code>true</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>jacocoPackageExclusion</code></td><td>package command overwrite exclusion directory</td><td><code>*.app</code><br><code>*.config</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr><tr><td><code>jacocoPackageCoveredRatio</code></td><td>package command coverage</td><td><code>0.9</code></td><td>✔</td><td>✔</td><td>✔</td><td>❌</td></tr><tr><td><code>jacocoReportExclusion</code></td><td>Coverage Report Exclude Directory</td><td><code>**/Application.class</code><br><code>**/app/*.class</code><br><code>**/config/*.class</code></td><td>✔</td><td>✔</td><td>❌</td><td>❌</td></tr></tbody></table><h2 id="plugin-installation" tabindex="-1"><a class="header-anchor" href="#plugin-installation" aria-hidden="true">#</a> Plugin Installation</h2>`,6),K=e("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[e("pre",{class:"language-kotlin"},[e("code",null,[t("plugins "),e("span",{class:"token punctuation"},"{"),t(`
    `),e("span",{class:"token function"},"id"),e("span",{class:"token punctuation"},"("),e("span",{class:"token string-literal singleline"},[e("span",{class:"token string"},'"pub.ihub.plugin.ihub-verification"')]),e("span",{class:"token punctuation"},")"),t(`
`),e("span",{class:"token punctuation"},"}"),t(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),N=e("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[e("pre",{class:"language-groovy"},[e("code",null,[t("plugins "),e("span",{class:"token punctuation"},"{"),t(`
    id `),e("span",{class:"token string"},"'pub.ihub.plugin.ihub-verification'"),t(`
`),e("span",{class:"token punctuation"},"}"),t(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),F=e("h2",{id:"configuration-example",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#configuration-example","aria-hidden":"true"},"#"),t(" Configuration Example")],-1),M=e("h3",{id:"pmd-static-check-example",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#pmd-static-check-example","aria-hidden":"true"},"#"),t(" PMD Static Check Example")],-1),O=e("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[e("pre",{class:"language-kotlin"},[e("code",null,[t("iHubVerification "),e("span",{class:"token punctuation"},"{"),t(`
    pmdConsoleOutput`),e("span",{class:"token punctuation"},"."),e("span",{class:"token function"},"set"),e("span",{class:"token punctuation"},"("),e("span",{class:"token boolean"},"true"),e("span",{class:"token punctuation"},")"),t(`
`),e("span",{class:"token punctuation"},"}"),t(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),J=e("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[e("pre",{class:"language-groovy"},[e("code",null,[t(`iHubVerification maximum
    pmdConsoleOutput `),e("span",{class:"token operator"},"="),t(),e("span",{class:"token boolean"},"true"),t(`
`),e("span",{class:"token punctuation"},"}"),t(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),L=e("h3",{id:"example-codenacs-static-check",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#example-codenacs-static-check","aria-hidden":"true"},"#"),t(" Example Codenacs static check")],-1),$=e("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[e("pre",{class:"language-kotlin"},[e("code",null,[t(`iHubVerification maximum
    codenarcIgnoreFailures`),e("span",{class:"token punctuation"},"."),e("span",{class:"token function"},"set"),e("span",{class:"token punctuation"},"("),e("span",{class:"token boolean"},"true"),e("span",{class:"token punctuation"},")"),t(`
`),e("span",{class:"token punctuation"},"}"),t(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),z=e("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[e("pre",{class:"language-groovy"},[e("code",null,[t(`iHubVerification LO
    codenicarcIgnore Failures `),e("span",{class:"token operator"},"="),t(),e("span",{class:"token boolean"},"true"),t(`
`),e("span",{class:"token punctuation"},"}"),t(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),W=e("h3",{id:"jacoco-test-overwrite-example",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#jacoco-test-overwrite-example","aria-hidden":"true"},"#"),t(" Jacoco Test Overwrite Example")],-1),q=e("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[e("pre",{class:"language-kotlin"},[e("code",null,[t(`iHubVerification $
    jacocoBranchCoverageRuleEnabled`),e("span",{class:"token punctuation"},"."),e("span",{class:"token function"},"set"),e("span",{class:"token punctuation"},"("),e("span",{class:"token boolean"},"true"),e("span",{class:"token punctuation"},")"),t(`
    jacocoInstructionCoverageRuleEnabled`),e("span",{class:"token punctuation"},"."),e("span",{class:"token function"},"set"),e("span",{class:"token punctuation"},"("),e("span",{class:"token boolean"},"true"),e("span",{class:"token punctuation"},")"),t(`
    jacocoPackageEnabled`),e("span",{class:"token punctuation"},"."),e("span",{class:"token function"},"set"),e("span",{class:"token punctuation"},"("),e("span",{class:"token boolean"},"true"),e("span",{class:"token punctuation"},")"),t(`
`),e("span",{class:"token punctuation"},"}"),t(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),Q=e("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[e("pre",{class:"language-groovy"},[e("code",null,[t("iHubVerification "),e("span",{class:"token punctuation"},"{"),t(`
    jacocoBranchCoverageRuleEnabed `),e("span",{class:"token operator"},"="),t(),e("span",{class:"token boolean"},"true"),t(`
    jacocoInstruction CoverageRuleEnabed `),e("span",{class:"token operator"},"="),t(),e("span",{class:"token boolean"},"true"),t(`
    jacocoPackageRuleEnabed `),e("span",{class:"token operator"},"="),t(`
`),e("span",{class:"token punctuation"},"}"),t(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),U=e("h2",{id:"test-report",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#test-report","aria-hidden":"true"},"#"),t(" Test Report")],-1),X=e("p",null,"Test case reports will be generated and the console will print test cover.",-1),Y=e("ul",null,[e("li",null,[t("Sample coverage of individual project tests "),e("img",{src:r,alt:"",loading:"lazy"})]),e("li",null,[t("Project Summary Test Coverage Example "),e("img",{src:u,alt:"",loading:"lazy"})])],-1);function Z(ee,te){const d=i("ExternalLinkIcon"),c=i("CodeTabs");return h(),v("div",null,[g,e("table",null,[m,e("tbody",null,[k,_,f,x,e("tr",null,[j,e("td",null,[y,t(","),e("a",E,[t("codenarr"),s(d)]),t(","),e("a",C,[t("pmd"),s(d)]),t(","),e("a",P,[t("jacoco"),s(d)]),t(","),e("a",A,[t("jacoco-report-agregation"),s(d)]),t("("),V,t(")")])])])]),e("div",w,[I,e("ul",null,[e("li",null,[t("The project contains"),R,t("plugins automatically configured"),D,t(" plugins, default configuration"),e("a",B,[t("See"),s(d)]),t("and can be configured by config"),T,t("overwrite the default configuration,"),e("a",H,[t("examples"),s(d)])]),S]),G,s(c,{id:"469",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:n(({value:a,isActive:o})=>[t("Kotlin")]),title1:n(({value:a,isActive:o})=>[t("Groovy")]),tab0:n(({value:a,isActive:o})=>[K]),tab1:n(({value:a,isActive:o})=>[N]),_:1})]),F,M,s(c,{id:"484",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:n(({value:a,isActive:o})=>[t("Kotlin")]),title1:n(({value:a,isActive:o})=>[t("Groovy")]),tab0:n(({value:a,isActive:o})=>[O]),tab1:n(({value:a,isActive:o})=>[J]),_:1}),L,s(c,{id:"495",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:n(({value:a,isActive:o})=>[t("Kotlin")]),title1:n(({value:a,isActive:o})=>[t("Groovy")]),tab0:n(({value:a,isActive:o})=>[$]),tab1:n(({value:a,isActive:o})=>[z]),_:1}),W,s(c,{id:"506",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:n(({value:a,isActive:o})=>[t("Kotlin")]),title1:n(({value:a,isActive:o})=>[t("Groovy")]),tab0:n(({value:a,isActive:o})=>[q]),tab1:n(({value:a,isActive:o})=>[Q]),_:1}),U,X,Y])}const se=p(b,[["render",Z],["__file","iHubVerification.html.vue"]]);export{se as default};
