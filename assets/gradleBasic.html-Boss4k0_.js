import{_ as t}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as i,o as r,c as o,d as n,e as a,a as s,b as l}from"./app-DR86wGH8.js";const d={},p=n("h1",{id:"gradle-basic-start",tabindex:"-1"},[n("a",{class:"header-anchor",href:"#gradle-basic-start","aria-hidden":"true"},"#"),a(" Gradle Basic Start")],-1),c=n("code",null,"Gradle",-1),u={href:"https://ant.apache.org/",target:"_blank",rel:"noopener noreferrer"},v={href:"https://maven.apache.org/",target:"_blank",rel:"noopener noreferrer"},g={href:"https://groovy.apache.org/",target:"_blank",rel:"noopener noreferrer"},m={href:"https://gradle.org/maven-vs-gradle/",target:"_blank",rel:"noopener noreferrer"},b=n("img",{src:"https://gradle.org/images/gradle-vs-maven.gif",alt:"Gradle vs Maven",tabindex:"0",loading:"lazy"},null,-1),k=n("figcaption",null,"Gradle vs Maven",-1),f=l(`<h2 id="configure-wrapper" tabindex="-1"><a class="header-anchor" href="#configure-wrapper" aria-hidden="true">#</a> Configure wrapper</h2><blockquote><p>It is recommended to use <code>wrapper</code> to configure the Gradle environment to keep the project environment unified, add the <code>gradle/wrapper/gradle-wrapper.properties</code> configuration file in the root directory of the project, see the introductory document for detailed configuration, the configuration content is as follows:</p></blockquote><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token key attr-name">distributionUrl</span><span class="token punctuation">=</span><span class="token value attr-value">https\\://services.gradle.org/distributions/gradle-8.7-bin.zip</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div></div></div><blockquote><p>gradle-wrapper.jar, gradlew, gradlew.bat files can be automatically generated by command. Project structure below：</p></blockquote><div class="language-text line-numbers-mode" data-ext="text"><pre class="language-text"><code>.
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="configure-setting-gradle" tabindex="-1"><a class="header-anchor" href="#configure-setting-gradle" aria-hidden="true">#</a> Configure setting.gradle</h2><blockquote><p>settings.gradle and Settings Instances are used to declare instantiation and configuration Projects, which can be used for multiproject management and project plugin versions etc. Configure examples below：</p></blockquote><div class="language-groovy line-numbers-mode" data-ext="groovy"><pre class="language-groovy"><code><span class="token comment">// include two projects, &#39;Foo&#39; and &#39;Foo:bar&#39;</span>
<span class="token comment">// directories are referred by replacing &#39;:&#39; with &#39;/&#39;</span>
include <span class="token string">&#39;fo:bar&#39;</span>

<span class="token comment">// include one project whose project dir does not match the logical project path</span>
include <span class="token string">&#39;baz&#39;</span>
<span class="token function">project</span><span class="token punctuation">(</span><span class="token string">&#39;:baz&#39;</span><span class="token punctuation">)</span><span class="token punctuation">.</span> rojectDir <span class="token operator">=</span> <span class="token function">file</span><span class="token punctuation">(</span><span class="token string">&#39;foo/baz&#39;</span><span class="token punctuation">)</span>

<span class="token comment">// include many projects whose project irs do not match the logical project paths</span>
<span class="token function">file</span><span class="token punctuation">(</span><span class="token string">&#39;subjects&#39;</span><span class="token punctuation">)</span><span class="token punctuation">.</span> achDir LOR dir <span class="token operator">-&gt;</span>
    include dir<span class="token punctuation">.</span>name
    <span class="token function">project</span><span class="token punctuation">(</span><span class="token interpolation-string"><span class="token string">&quot;:</span><span class="token interpolation"><span class="token interpolation-punctuation punctuation">\${</span><span class="token expression">dir<span class="token punctuation">.</span>name</span><span class="token interpolation-punctuation punctuation">}</span></span><span class="token string">&quot;</span></span><span class="token punctuation">)</span><span class="token punctuation">.</span>projectDir <span class="token operator">=</span> dir
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><blockquote><p>Project structure below：</p></blockquote><div class="language-text line-numbers-mode" data-ext="text"><pre class="language-text"><code>.
├── settings.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="configure-build-gradle" tabindex="-1"><a class="header-anchor" href="#configure-build-gradle" aria-hidden="true">#</a> Configure build.gradle</h2><blockquote><p>Used to configure project dependencies, required plugins etc. Configure examples below：</p></blockquote><div class="language-groovy line-numbers-mode" data-ext="groovy"><pre class="language-groovy"><code><span class="token comment">// 添加插件</span>
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
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><blockquote><p>Project structure below：</p></blockquote><div class="language-text line-numbers-mode" data-ext="text"><pre class="language-text"><code>.
├── build.gradle
├── settings.gradle
├── gradle
│ └── wrapper
│ ├── gradle-wrapper.jar
│ └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="configure-gradle-properties" tabindex="-1"><a class="header-anchor" href="#configure-gradle-properties" aria-hidden="true">#</a> Configure gradle.properties</h2><blockquote><p>Configure some Gradle environment properties, e.g.：</p></blockquote><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token key attr-name">org.gradle.parallel</span><span class="token punctuation">=</span><span class="token value attr-value">true</span>
<span class="token key attr-name">org.gradle.caching</span><span class="token punctuation">=</span><span class="token value attr-value">true</span>
<span class="token key attr-name">org.gradle.configureondemand</span><span class="token punctuation">=</span><span class="token value attr-value">true</span>
<span class="token key attr-name">org.gradle.daemon</span><span class="token punctuation">=</span><span class="token value attr-value">false</span>
<span class="token key attr-name">org.gradle.daemon.performance.enable-monitoring</span><span class="token punctuation">=</span><span class="token value attr-value">false</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><blockquote><p>Project structure below：</p></blockquote><div class="language-text line-numbers-mode" data-ext="text"><pre class="language-text"><code>.
├── gradle.properties
├── build.gradle
├── settings.gradle
├── gradle
│ └── wrapper
│ ├── gradle-wrapper.jar
│ └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,20);function h(w,y){const e=i("ExternalLinkIcon");return r(),o("div",null,[p,n("blockquote",null,[n("p",null,[c,a("is the origin of "),n("a",u,[a("Apache Ant"),s(e)]),a(" and "),n("a",v,[a("Apache Maven"),s(e)]),a(" The project automates construction of open source tools using a field language (DSL) based on "),n("a",g,[a("Groovy"),s(e)]),a(" to declare project settings and discard a very burdensome XML-based configuration for Java apps.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift.Currently supported languages are Java, Groovy, Kotlin, Scala, Android, C++ and Swift. Below is a comparison between Gradle and Apache Maven：")])]),n("figure",null,[n("a",m,[b,s(e)]),k]),f])}const x=t(d,[["render",h],["__file","gradleBasic.html.vue"]]);export{x as default};
