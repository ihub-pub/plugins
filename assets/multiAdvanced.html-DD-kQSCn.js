import{_ as i}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as t,c as r,d as s,e as a,b as p,a as l,o}from"./app-BNKWpxIq.js";const u={},c={href:"https://github.com/ihub-pub/multi-template",target:"_blank",rel:"noopener noreferrer"};function d(b,n){const e=t("ExternalLinkIcon");return o(),r("div",null,[n[2]||(n[2]=s("h1",{id:"a-master-multi-subproject-configuration",tabindex:"-1"},[s("a",{class:"header-anchor",href:"#a-master-multi-subproject-configuration","aria-hidden":"true"},"#"),a(" A master multi-subproject configuration")],-1)),s("p",null,[n[1]||(n[1]=a("A primary multi-subproject configuration, see",-1)),s("a",c,[n[0]||(n[0]=a("project template",-1)),p(e)])]),n[3]||(n[3]=l(`<h2 id="configure-wrapper" tabindex="-1"><a class="header-anchor" href="#configure-wrapper" aria-hidden="true">#</a> Configure wrapper</h2><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token key attr-name">distributionUrl</span><span class="token punctuation">=</span><span class="token value attr-value">https\\://services.gradle.org/distributions/gradle-8.7-bin.zip</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div></div></div><h2 id="configure-setting-gradle" tabindex="-1"><a class="header-anchor" href="#configure-setting-gradle" aria-hidden="true">#</a> Configure setting.gradle</h2><p><code>rest</code>,<code>service</code>,<code>client</code>for subproject directories, more configuration see<a href="../iHubSettings">ihub-settings</a>plugin：</p><div class="language-groovy line-numbers-mode" data-ext="groovy"><pre class="language-groovy"><code>plugins <span class="token punctuation">{</span>
    id <span class="token string">&#39;pub.ihub.plugin.ihub-settings&#39;</span> version <span class="token string">&#39;1.4.1&#39;</span>
<span class="token punctuation">}</span>

iHubSettings <span class="token punctuation">{</span>
    includeProjects <span class="token string">&#39;rest&#39;</span><span class="token punctuation">,</span> <span class="token string">&#39;service&#39;</span><span class="token punctuation">,</span> <span class="token string">&#39;client&#39;</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="configure-build-gradle" tabindex="-1"><a class="header-anchor" href="#configure-build-gradle" aria-hidden="true">#</a> Configure build.gradle</h2><p>Subprojects introduce Java plugins (<a href="../iHubJava">ihub-java</a>), test plugin (<a href="../iHubTest">ihub-test</a>) and validation plugin (<a href="../iHubVerification">ihub-certification</a>), config<a href="../iHubGitHooks">ihub-git-hooks</a>plugin hook command：</p><div class="language-groovy line-numbers-mode" data-ext="groovy"><pre class="language-groovy"><code>plugins <span class="token punctuation">{</span>
    id <span class="token string">&#39;pub.ihub.plugin&#39;</span>
    id <span class="token string">&#39;pub.ihub.plugin.ihub-git-hooks&#39;</span>
    id <span class="token string">&#39;pub.ihub.plugin.ihub-java&#39;</span> apply <span class="token boolean">false</span>
    id <span class="token string">&#39;pub.ihub.plugin.ihub-test&#39;</span> apply <span class="token boolean">false</span>
    id <span class="token string">&#39;pub.ihub.plugin.ihub-verification&#39;</span> apply <span class="token boolean">false</span>
    id <span class="token string">&#39;pub.ihub.plugin.ihub-publish&#39;</span> apply <span class="token boolean">false</span>
    id <span class="token string">&#39;pub.ihub.plugin.ihub-boot&#39;</span> apply <span class="token boolean">false</span>
<span class="token punctuation">}</span>

subprojects <span class="token punctuation">{</span>
    apply <span class="token punctuation">{</span>
        plugin <span class="token string">&#39;pub.ihub.plugin.ihub-java&#39;</span>
        plugin <span class="token string">&#39;pub.ihub.plugin.ihub-test&#39;</span>
        plugin <span class="token string">&#39;pub.ihub.plugin.ihub-verification&#39;</span>
    <span class="token punctuation">}</span>

    dependencies <span class="token punctuation">{</span>
        testImplementation <span class="token string">&#39;org.springframework.boot:spring-boot-starter-test&#39;</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>

iHubGitHooks <span class="token punctuation">{</span>
    hooks <span class="token operator">=</span> <span class="token punctuation">[</span>
        <span class="token string">&#39;pre-commit&#39;</span><span class="token punctuation">:</span> <span class="token string">&#39;./gradlew build&#39;</span><span class="token punctuation">,</span>
        <span class="token string">&#39;commit-msg&#39;</span><span class="token punctuation">:</span> <span class="token string">&#39;./gradlew commitCheck&#39;</span>
    <span class="token punctuation">]</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="configure-gradle-properties" tabindex="-1"><a class="header-anchor" href="#configure-gradle-properties" aria-hidden="true">#</a> Configure gradle.properties</h2><p>Configure project names and groups, where<code>name</code>is<a href="../iHubSettings">ihub-settings</a>plugin<a href="../iHubSettings#%E6%89%A9%E5%B1%95%E5%B1%9E%E6%80%A7">extension properties</a>,<code>group</code>native project properties</p><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token key attr-name">name</span><span class="token punctuation">=</span><span class="token value attr-value">demo</span>
<span class="token key attr-name">group</span><span class="token punctuation">=</span><span class="token value attr-value">pub.ihub.demo</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div></div></div>`,11))])}const m=i(u,[["render",d],["__file","multiAdvanced.html.vue"]]);export{m as default};
