/*
 * Copyright (c) 2021 Henry 李恒 (henry.box@outlook.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pub.ihub.plugin

import groovy.transform.TupleConstructor



/**
 * IHub插件属性扩展
 * @author henry
 */
@TupleConstructor(includeSuperFields = true)
class IHubPluginsExtension extends IHubProjectExtension {

    /**
     * 项目版本
     * @return 项目版本
     */
    String getVersion() {
        findSystemProperty 'version', project.version.toString()
    }

    //<editor-fold desc="组件仓库相关扩展属性">

    /**
     * 是否启用本地仓库
     * @return 是否启用本地仓库
     */
    boolean getMavenLocalEnabled() {
        findProperty 'mavenLocalEnabled', false
    }

    /**
     * 发布版本仓库
     * @return 发布版本仓库
     */
    String getReleaseRepoUrl() {
        findProperty 'releaseRepoUrl'
    }

    /**
     * 快照版本仓库
     * @return 快照版本仓库
     */
    String getSnapshotRepoUrl() {
        findProperty 'snapshotRepoUrl'
    }

    /**
     * 是否允许不安全协议（是否允许http）
     * @return 是否允许不安全协议
     */
    boolean getRepoAllowInsecureProtocol() {
        findProperty 'repoAllowInsecureProtocol', false
    }

    /**
     * 仓库包含组（用于限制仓库范围）
     * @return 仓库包含组
     */
    String getRepoIncludeGroup() {
        findProperty 'repoIncludeGroup'
    }

    /**
     * 仓库包含组正则（用于限制仓库范围）
     * @return 仓库包含组正则
     */
    String getRepoIncludeGroupRegex() {
        findProperty 'repoIncludeGroupRegex', '.*'
    }

    /**
     * 仓库用户名
     * @return 仓库用户名
     */
    String getRepoUsername() {
        findEnvProperty 'repoUsername'
    }

    /**
     * 仓库密码
     * @return 仓库密码
     */
    String getRepoPassword() {
        findEnvProperty 'repoPassword'
    }

    /**
     * 自定义仓库
     * @return 自定义仓库
     */
    String getCustomizeRepoUrl() {
        findProperty 'customizeRepoUrl'
    }

    //</editor-fold>

    //<editor-fold desc="Java相关扩展属性">

    /**
     * Java兼容性配置
     * @return 兼容版本
     */
    String getJavaCompatibility() {
        findSystemProperty 'javaCompatibility'
    }

    /**
     * gradle增量编译
     * @return gradle增量编译
     */
    boolean getGradleCompilationIncremental() {
        findSystemProperty 'gradleCompilationIncremental', true
    }

    //</editor-fold>

    //<editor-fold desc="组件发布相关扩展属性">

    /**
     * 组件发布是否需要签名
     * @return 是否需要签名
     */
    boolean getPublishNeedSign() {
        findSystemProperty 'publishNeedSign', false
    }

    /**
     * 签名key
     * @return 签名key
     */
    String getSigningKeyId() {
        findEnvProperty 'signingKeyId'
    }

    /**
     * 签名密钥
     * @return 签名密钥
     */
    String getSigningSecretKey() {
        findEnvProperty 'signingSecretKey'
    }

    /**
     * 签名密码
     * @return 签名密码
     */
    String getSigningPassword() {
        findEnvProperty 'signingPassword'
    }

    /**
     * 是否发布文档
     * @return 是否发布文档
     */
    boolean getPublishDocs() {
        findSystemProperty 'publishDocs', false
    }

    //</editor-fold>

    /**
     * 获取环境属性（优先从系统属性和项目属性获取，环境属性多用于敏感信息配置）
     * @param key key
     * @return 属性值
     */
    String findEnvProperty(String key) {
        findProperty(key) { String k ->
            System.getProperty(k) ?: findProjectProperty(key)
        } ?: System.getenv(key.replaceAll(/([A-Z])/, '_$1').toUpperCase())
    }

}
