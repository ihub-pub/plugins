/*
 * Copyright (c) 2021-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pub.ihub.plugin


import pub.ihub.plugin.bom.IHubBomPlugin
import pub.ihub.plugin.version.IHubVersionPlugin

import static pub.ihub.plugin.IHubPluginMethods.printLineConfigContent
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER

/**
 * Gradle基础插件
 * 配置项目组件仓库
 * @author liheng
 */
@IHubPlugin(IHubPluginsExtension)
class IHubPluginsPlugin extends IHubProjectPluginAware<IHubPluginsExtension> {

    @Override
    void apply() {
        configProjectRepositories()

        if (project == project.rootProject) {
            logger.lifecycle 'Build with IHub Plugins ' + IHubPluginsPlugin.package.implementationVersion +
                ', You can see the documentation to learn more, See https://doc.ihub.pub/plugins.'

            applyPlugin IHubVersionPlugin

            // Github Actions环境下，自动同意Scan插件条款
            if (project.hasProperty('buildScan') && System.getenv('GITHUB_ACTIONS')) {
                project.buildScan {
                    termsOfServiceUrl = 'https://gradle.com/terms-of-service'
                    termsOfServiceAgree = 'yes'
                }
            }
        }

        // 默认应用IHubBom插件
        applyPlugin IHubBomPlugin

        project.subprojects {
            pluginManager.apply IHubPluginsPlugin
        }
    }

    private void configProjectRepositories() {
        withExtension(AFTER) { ext ->
            project.repositories {
                String dirs = "$project.rootProject.projectDir/libs"
                if ((dirs as File).directory) {
                    flatDir dirs: dirs
                }
                if (ext.mavenLocalEnabled.get()) {
                    mavenLocal()
                }
                if (ext.mavenAliYunEnabled.get()) {
                    maven mavenRepo('AliYunPublic', 'https://maven.aliyun.com/repository/public',
                        'https://repo1.maven.org/maven2')
                    maven mavenRepo('AliYunGoogle', 'https://maven.aliyun.com/repository/google',
                        'https://maven.google.com')
                    maven mavenRepo('AliYunSpring', 'https://maven.aliyun.com/repository/spring',
                        'https://repo.spring.io/release')
                }
                // 添加私有仓库
                ext.releaseRepoUrl.orNull?.with { url -> maven mavenRepo('ReleaseRepo', url, ext) { releasesOnly() } }
                ext.snapshotRepoUrl.orNull?.with { url -> maven mavenRepo('SnapshotRepo', url, ext) { snapshotsOnly() } }
                // 添加自定义仓库
                ext.customizeRepoUrl.orNull?.with { url -> maven mavenRepo('CustomizeRepo', url) }
                mavenCentral()
            }

            if (project == project.rootProject) {
                printLineConfigContent 'Gradle Project Repos', project.repositories*.displayName
            }
        }
    }

    private Closure mavenRepo(String repoName, String repoUrl, String repoArtifactUrls = null) {
        return {
            name repoName
            url repoUrl
            if (repoArtifactUrls) {
                artifactUrls repoArtifactUrls
            }
        }
    }

    private Closure mavenRepo(String repoName, String repoUrl, IHubPluginsExtension ext, Closure mavenContentConfig) {
        return {
            name repoName
            url repoUrl
            allowInsecureProtocol ext.repoAllowInsecureProtocol.get()
            mavenContent mavenContentConfig
            content {
                String repoIncludeGroup = ext.repoIncludeGroup.orNull
                if (repoIncludeGroup) {
                    includeGroup repoIncludeGroup
                } else {
                    includeGroupByRegex ext.repoIncludeGroupRegex.get()
                }
            }
            ext.repoUsername.orNull?.with { repoUsername ->
                credentials {
                    username repoUsername
                    password ext.repoPassword.orNull
                }
            }
        }
    }

}
