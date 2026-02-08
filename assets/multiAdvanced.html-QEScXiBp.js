import{_ as i}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as t,c as l,d as s,e as a,b as p,a as r,o}from"./app-BNKWpxIq.js";const u={},d={href:"https://github.com/ihub-pub/multi-template",target:"_blank",rel:"noopener noreferrer"};function c(b,n){const e=t("ExternalLinkIcon");return o(),l("div",null,[n[2]||(n[2]=s("h1",{id:"一主多子项目配置",tabindex:"-1"},[s("a",{class:"header-anchor",href:"#一主多子项目配置","aria-hidden":"true"},"#"),a(" 一主多子项目配置")],-1)),s("p",null,[n[1]||(n[1]=a("一个主多子项目配置，参见",-1)),s("a",d,[n[0]||(n[0]=a("项目模板",-1)),p(e)])]),n[3]||(n[3]=r(`<h2 id="配置-wrapper" tabindex="-1"><a class="header-anchor" href="#配置-wrapper" aria-hidden="true">#</a> 配置 wrapper</h2><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token key attr-name">distributionUrl</span><span class="token punctuation">=</span><span class="token value attr-value">https\\://services.gradle.org/distributions/gradle-8.7-bin.zip</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div></div></div><h2 id="配置-setting-gradle" tabindex="-1"><a class="header-anchor" href="#配置-setting-gradle" aria-hidden="true">#</a> 配置 setting.gradle</h2><p><code>rest</code>、<code>service</code>、<code>client</code>为子项目目录，更多配置见<a href="../iHubSettings">ihub-settings</a>插件：</p><div class="language-groovy line-numbers-mode" data-ext="groovy"><pre class="language-groovy"><code>plugins <span class="token punctuation">{</span>
    id <span class="token string">&#39;pub.ihub.plugin.ihub-settings&#39;</span> version <span class="token string">&#39;1.4.1&#39;</span>
<span class="token punctuation">}</span>

iHubSettings <span class="token punctuation">{</span>
    includeProjects <span class="token string">&#39;rest&#39;</span><span class="token punctuation">,</span> <span class="token string">&#39;service&#39;</span><span class="token punctuation">,</span> <span class="token string">&#39;client&#39;</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="配置-build-gradle" tabindex="-1"><a class="header-anchor" href="#配置-build-gradle" aria-hidden="true">#</a> 配置 build.gradle</h2><p>子项目引入Java插件（<a href="../iHubJava">ihub-java</a>）、测试插件（<a href="../iHubTest">ihub-test</a>）以及验证插件（<a href="../iHubVerification">ihub-verification</a>），配置<a href="../iHubGitHooks">ihub-git-hooks</a>插件钩子命令：</p><div class="language-groovy line-numbers-mode" data-ext="groovy"><pre class="language-groovy"><code>plugins <span class="token punctuation">{</span>
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
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="配置-gradle-properties" tabindex="-1"><a class="header-anchor" href="#配置-gradle-properties" aria-hidden="true">#</a> 配置 gradle.properties</h2><p>配置项目名称以及group，其中<code>name</code>为<a href="../iHubSettings">ihub-settings</a>插件<a href="../iHubSettings#%E6%89%A9%E5%B1%95%E5%B1%9E%E6%80%A7">扩展属性</a>，<code>group</code>为原生项目属性</p><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token key attr-name">name</span><span class="token punctuation">=</span><span class="token value attr-value">demo</span>
<span class="token key attr-name">group</span><span class="token punctuation">=</span><span class="token value attr-value">pub.ihub.demo</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div></div></div>`,11))])}const m=i(u,[["render",c],["__file","multiAdvanced.html.vue"]]);export{m as default};
