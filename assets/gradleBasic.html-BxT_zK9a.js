import{_ as t}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as l,c as r,d as s,e as a,b as i,a as o,o as p}from"./app-BNKWpxIq.js";const d={},c={href:"https://ant.apache.org/",target:"_blank",rel:"noopener noreferrer"},u={href:"https://maven.apache.org/",target:"_blank",rel:"noopener noreferrer"},v={href:"https://groovy.apache.org/",target:"_blank",rel:"noopener noreferrer"},m={href:"https://gradle.org/maven-vs-gradle/",target:"_blank",rel:"noopener noreferrer"};function g(b,n){const e=l("ExternalLinkIcon");return p(),r("div",null,[n[10]||(n[10]=s("h1",{id:"gradle基础入门",tabindex:"-1"},[s("a",{class:"header-anchor",href:"#gradle基础入门","aria-hidden":"true"},"#"),a(" Gradle基础入门")],-1)),s("blockquote",null,[s("p",null,[n[3]||(n[3]=s("code",null,"Gradle",-1)),n[4]||(n[4]=a("是源于 ",-1)),s("a",c,[n[0]||(n[0]=a("Apache Ant",-1)),i(e)]),n[5]||(n[5]=a(" 和 ",-1)),s("a",u,[n[1]||(n[1]=a("Apache Maven",-1)),i(e)]),n[6]||(n[6]=a(" 概念的项目自动化构建开源工具，它使用一种基于 ",-1)),s("a",v,[n[2]||(n[2]=a("Groovy",-1)),i(e)]),n[7]||(n[7]=a(" 的特定领域语言 (DSL) 来声明项目设置，抛弃了基于 XML 的各种繁琐配置面向 Java 应用为主。当前其支持的语言暂时有 Java、Groovy、Kotlin、Scala、Android、C++ 和 Swift。 下面是 Gradle 和 Apache Maven 的一个比较：",-1))])]),s("figure",null,[s("a",m,[n[8]||(n[8]=s("img",{src:"https://gradle.org/images/gradle-vs-maven.gif",alt:"Gradle vs Maven",tabindex:"0",loading:"lazy"},null,-1)),i(e)]),n[9]||(n[9]=s("figcaption",null,"Gradle vs Maven",-1))]),n[11]||(n[11]=o(`<h2 id="配置-wrapper" tabindex="-1"><a class="header-anchor" href="#配置-wrapper" aria-hidden="true">#</a> 配置 wrapper</h2><blockquote><p>推荐使用<code>wrapper</code>的方式配置 Gradle<br> 环境，便于保持项目环境统一，在项目根目录下添加<code>gradle/wrapper/gradle-wrapper.properties</code>配置文件，详细配置见入门文档，配置内容如下：</p></blockquote><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token key attr-name">distributionUrl</span><span class="token punctuation">=</span><span class="token value attr-value">https\\://services.gradle.org/distributions/gradle-8.7-bin.zip</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div></div></div><blockquote><p>gradle-wrapper.jar、gradlew、gradlew.bat 文件可以通过命令自动生成，项目结构如下：</p></blockquote><div class="language-text line-numbers-mode" data-ext="text"><pre class="language-text"><code>.
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="配置-setting-gradle" tabindex="-1"><a class="header-anchor" href="#配置-setting-gradle" aria-hidden="true">#</a> 配置 setting.gradle</h2><blockquote><p>settings.gradle 与 Settings 实例对应，用来声明实例化和配置 Project，可以用于多项目管理以及项目插件版本等配置，配置示例如下：</p></blockquote><div class="language-groovy line-numbers-mode" data-ext="groovy"><pre class="language-groovy"><code><span class="token comment">// include two projects, &#39;foo&#39; and &#39;foo:bar&#39;</span>
<span class="token comment">// directories are inferred by replacing &#39;:&#39; with &#39;/&#39;</span>
include <span class="token string">&#39;foo:bar&#39;</span>

<span class="token comment">// include one project whose project dir does not match the logical project path</span>
include <span class="token string">&#39;baz&#39;</span>
<span class="token function">project</span><span class="token punctuation">(</span><span class="token string">&#39;:baz&#39;</span><span class="token punctuation">)</span><span class="token punctuation">.</span>projectDir <span class="token operator">=</span> <span class="token function">file</span><span class="token punctuation">(</span><span class="token string">&#39;foo/baz&#39;</span><span class="token punctuation">)</span>

<span class="token comment">// include many projects whose project dirs do not match the logical project paths</span>
<span class="token function">file</span><span class="token punctuation">(</span><span class="token string">&#39;subprojects&#39;</span><span class="token punctuation">)</span><span class="token punctuation">.</span>eachDir <span class="token punctuation">{</span> dir <span class="token operator">-&gt;</span>
    include dir<span class="token punctuation">.</span>name
    <span class="token function">project</span><span class="token punctuation">(</span><span class="token interpolation-string"><span class="token string">&quot;:</span><span class="token interpolation"><span class="token interpolation-punctuation punctuation">\${</span><span class="token expression">dir<span class="token punctuation">.</span>name</span><span class="token interpolation-punctuation punctuation">}</span></span><span class="token string">&quot;</span></span><span class="token punctuation">)</span><span class="token punctuation">.</span>projectDir <span class="token operator">=</span> dir
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><blockquote><p>项目结构如下：</p></blockquote><div class="language-text line-numbers-mode" data-ext="text"><pre class="language-text"><code>.
├── settings.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="配置-build-gradle" tabindex="-1"><a class="header-anchor" href="#配置-build-gradle" aria-hidden="true">#</a> 配置 build.gradle</h2><blockquote><p>用于配置项目依赖、所需插件等，配置示例如下：</p></blockquote><div class="language-groovy line-numbers-mode" data-ext="groovy"><pre class="language-groovy"><code><span class="token comment">// 添加插件</span>
plugins <span class="token punctuation">{</span>
    id <span class="token string">&#39;groovy-gradle-plugin&#39;</span>
    id <span class="token string">&#39;maven-publish&#39;</span>
<span class="token punctuation">}</span>

<span class="token comment">// 配置项目信息</span>
group <span class="token operator">=</span> <span class="token string">&#39;com.myorg.conventions&#39;</span>
version <span class="token operator">=</span> <span class="token string">&#39;1.0&#39;</span>

<span class="token comment">// 配置组件仓库</span>
repositories <span class="token punctuation">{</span>
    <span class="token function">gradlePluginPortal</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token comment">// so that external plugins can be resolved in dependencies section</span>
<span class="token punctuation">}</span>

<span class="token comment">// 配置依赖</span>
dependencies <span class="token punctuation">{</span>
    implementation <span class="token string">&#39;gradle.plugin.com.github.spotbugs.snom:spotbugs-gradle-plugin:4.6.2&#39;</span>
    testImplementation <span class="token function">platform</span><span class="token punctuation">(</span><span class="token interpolation-string"><span class="token string">&quot;org.spockframework:spock-bom:2.0-groovy-3.0&quot;</span></span><span class="token punctuation">)</span>
    testImplementation <span class="token string">&#39;org.spockframework:spock-core&#39;</span>
    testImplementation <span class="token string">&#39;org.spockframework:spock-junit4&#39;</span>
    testImplementation <span class="token string">&#39;junit:junit:4.13.1&#39;</span>
<span class="token punctuation">}</span>

<span class="token comment">// 配置插件</span>
publishing <span class="token punctuation">{</span>
    repositories <span class="token punctuation">{</span>
        maven <span class="token punctuation">{</span>
            <span class="token comment">// change to point to your repo, e.g. http://my.org/repo</span>
            url <span class="token operator">=</span> layout<span class="token punctuation">.</span>buildDirectory<span class="token punctuation">.</span><span class="token function">dir</span><span class="token punctuation">(</span><span class="token interpolation-string"><span class="token string">&quot;repo&quot;</span></span><span class="token punctuation">)</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>

<span class="token comment">// 配置任务</span>
tasks<span class="token punctuation">.</span><span class="token function">named</span><span class="token punctuation">(</span><span class="token string">&#39;publish&#39;</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token function">dependsOn</span><span class="token punctuation">(</span><span class="token string">&#39;check&#39;</span><span class="token punctuation">)</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><blockquote><p>项目结构如下：</p></blockquote><div class="language-text line-numbers-mode" data-ext="text"><pre class="language-text"><code>.
├── build.gradle
├── settings.gradle
├── gradle
│ └── wrapper
│ ├── gradle-wrapper.jar
│ └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="配置-gradle-properties" tabindex="-1"><a class="header-anchor" href="#配置-gradle-properties" aria-hidden="true">#</a> 配置 gradle.properties</h2><blockquote><p>用于配置一些 Gradle 环境属性，如：</p></blockquote><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token key attr-name">org.gradle.parallel</span><span class="token punctuation">=</span><span class="token value attr-value">true</span>
<span class="token key attr-name">org.gradle.caching</span><span class="token punctuation">=</span><span class="token value attr-value">true</span>
<span class="token key attr-name">org.gradle.configureondemand</span><span class="token punctuation">=</span><span class="token value attr-value">true</span>
<span class="token key attr-name">org.gradle.daemon</span><span class="token punctuation">=</span><span class="token value attr-value">false</span>
<span class="token key attr-name">org.gradle.daemon.performance.enable-monitoring</span><span class="token punctuation">=</span><span class="token value attr-value">false</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><blockquote><p>项目结构如下：</p></blockquote><div class="language-text line-numbers-mode" data-ext="text"><pre class="language-text"><code>.
├── gradle.properties
├── build.gradle
├── settings.gradle
├── gradle
│ └── wrapper
│ ├── gradle-wrapper.jar
│ └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,20))])}const f=t(d,[["render",g],["__file","gradleBasic.html.vue"]]);export{f as default};
