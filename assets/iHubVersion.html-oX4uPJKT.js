import{_ as c}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r,o as d,c as p,d as n,e,a as o,w as t,b as u}from"./app-DR86wGH8.js";const h={},g=n("h1",{id:"ihub-version",tabindex:"-1"},[n("a",{class:"header-anchor",href:"#ihub-version","aria-hidden":"true"},"#"),e(" ihub-version")],-1),v=n("div",{class:"hint-container info"},[n("p",{class:"hint-container-title"},"plugin description"),n("p",null,[n("code",null,"ihub-version"),e("plugin is version plugin, integrated and strengthened third party version plugin to set project version.")])],-1),b=n("thead",null,[n("tr",null,[n("th",null,"Information"),n("th",null,"Description")])],-1),_=n("tr",null,[n("td",null,"Plugin ID"),n("td",null,[n("code",null,"pub.ihub.plugin.ihub-version")])],-1),m=n("tr",null,[n("td",null,"Plugin Name"),n("td",null,[n("code",null,"Version Plugin")])],-1),f=n("tr",null,[n("td",null,"Plugin Type"),n("td",null,[n("code",null,"Project"),e("[^Project]")])],-1),k=n("tr",null,[n("td",null,"Extension Name"),n("td",null,[n("code",null,"iHubVersion")])],-1),x=n("td",null,"Plugin Dependencies",-1),y={href:"https://plugins.gradle.org/plugin/io.freefair.git-version",target:"_blank",rel:"noopener noreferrer"},V={href:"https://plugins.gradle.org/plugin/com.github.ben-manes.versions",target:"_blank",rel:"noopener noreferrer"},E=u('<div class="hint-container tip"><p class="hint-container-title">plugin functionality</p><ol><li>Introducing<code>git-version</code>plugin autoconfig project version,<code>Enhanced support to infer version</code></li><li>Introducing<code>ben-manes.versions</code>plugin to check component versions,<code>enhance support for automatic replacement of the latest version</code></li></ol></div><h2 id="extended-properties" tabindex="-1"><a class="header-anchor" href="#extended-properties" aria-hidden="true">#</a> Extended Properties</h2>',2),P=n("thead",null,[n("tr",null,[n("th",null,"Extension"),n("th",null,"Description"),n("th",null,"Default"),n("th",null,"Ext[^Ext]"),n("th",null,"Prj[^Prj]"),n("th",null,"Sys[^Sys]"),n("th",null,"Env[^Env]")])],-1),A=n("td",null,[n("code",null,"autoReplaceLaterVersions")],-1),I={href:"https://plugins.gradle.org/plugin/com.github.ben-manes.versions",target:"_blank",rel:"noopener noreferrer"},j=n("td",null,[n("code",null,"false")],-1),N=n("td",null,"✔",-1),C=n("td",null,"✔",-1),D=n("td",null,"✔",-1),H=n("td",null,"❌",-1),L=n("tr",null,[n("td",null,[n("code",null,"useInferingVersion")]),n("td",null,[e("Using a extrapolated version number, extrapolating next version number from the latest"),n("code",null,"git tag"),e("supports tag format"),n("code",null,"{major}.{minor}.{patch}"),e("or"),n("code",null,"v{major}.{minor}.{patch}"),e(", extrapolation method"),n("code",null,"patch + 1")]),n("td",null,[n("code",null,"false")]),n("td",null,"✔"),n("td",null,"✔"),n("td",null,"✔"),n("td",null,"✔")],-1),G=n("h2",{id:"plugin-installation",tabindex:"-1"},[n("a",{class:"header-anchor",href:"#plugin-installation","aria-hidden":"true"},"#"),e(" Plugin Installation")],-1),K=n("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[n("pre",{class:"language-kotlin"},[n("code",null,[e("plugins "),n("span",{class:"token punctuation"},"{"),e(`
    `),n("span",{class:"token function"},"id"),n("span",{class:"token punctuation"},"("),n("span",{class:"token string-literal singleline"},[n("span",{class:"token string"},'"pub.ihub.plugin.ihub-version"')]),n("span",{class:"token punctuation"},")"),e(`
`),n("span",{class:"token punctuation"},"}"),e(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1),T=n("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[n("pre",{class:"language-groovy"},[n("code",null,[e("plugins "),n("span",{class:"token punctuation"},"{"),e(`
    id `),n("span",{class:"token string"},"'pub.ihub.plugin.ihub-version'"),e(`
`),n("span",{class:"token punctuation"},"}"),e(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1),B=u('<div class="hint-container note"><p class="hint-container-title">Note</p><p>If base plugin<code>ihub</code>is installed, there is no need to install individually<code>ihub-version</code>plugin,<code>ihub</code>plugin is already integrated<code>ihub-version</code>plugin.</p></div><h2 id="configuration-example" tabindex="-1"><a class="header-anchor" href="#configuration-example" aria-hidden="true">#</a> Configuration Example</h2>',2),R=n("div",{class:"language-kotlin line-numbers-mode","data-ext":"kt"},[n("pre",{class:"language-kotlin"},[n("code",null,[e("iHubVersion "),n("span",{class:"token punctuation"},"{"),e(`
    autoReplaceLaterVersions`),n("span",{class:"token punctuation"},"."),n("span",{class:"token function"},"set"),n("span",{class:"token punctuation"},"("),n("span",{class:"token boolean"},"true"),n("span",{class:"token punctuation"},")"),e(`
`),n("span",{class:"token punctuation"},"}"),e(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1),S=n("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy"},[n("pre",{class:"language-groovy"},[n("code",null,[e("iHubVersion "),n("span",{class:"token punctuation"},"{"),e(`
    autoReplaceLateVersions `),n("span",{class:"token operator"},"="),e(),n("span",{class:"token boolean"},"true"),e(`
`),n("span",{class:"token punctuation"},"}"),e(`
`)])]),n("div",{class:"line-numbers","aria-hidden":"true"},[n("div",{class:"line-number"}),n("div",{class:"line-number"}),n("div",{class:"line-number"})])],-1);function w(U,q){const s=r("ExternalLinkIcon"),a=r("CodeTabs");return d(),p("div",null,[g,v,n("table",null,[b,n("tbody",null,[_,m,f,k,n("tr",null,[x,n("td",null,[n("a",y,[e("io.freeair.git-version"),o(s)]),e(","),n("a",V,[e("com.github.ben-manes.versions"),o(s)])])])])]),E,n("table",null,[P,n("tbody",null,[n("tr",null,[A,n("td",null,[e("Automatically replace the latest version ("),n("a",I,[e("versions"),o(s)]),e("plugin enhancement)")]),j,N,C,D,H]),L])]),G,o(a,{id:"157",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:t(({value:l,isActive:i})=>[e("Kotlin")]),title1:t(({value:l,isActive:i})=>[e("Groovy")]),tab0:t(({value:l,isActive:i})=>[K]),tab1:t(({value:l,isActive:i})=>[T]),_:1}),B,o(a,{id:"173",data:[{id:"Kotlin"},{id:"Groovy"}],"tab-id":"build"},{title0:t(({value:l,isActive:i})=>[e("Kotlin")]),title1:t(({value:l,isActive:i})=>[e("Groovy")]),tab0:t(({value:l,isActive:i})=>[R]),tab1:t(({value:l,isActive:i})=>[S]),_:1})])}const J=c(h,[["render",w],["__file","iHubVersion.html.vue"]]);export{J as default};
