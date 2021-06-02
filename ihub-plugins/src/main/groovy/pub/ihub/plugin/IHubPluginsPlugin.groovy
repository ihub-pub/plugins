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


import org.gradle.api.Project
import pub.ihub.plugin.bom.IHubBomPlugin

import static pub.ihub.plugin.IHubPluginMethods.printConfigContent



/**
 * Gradle基础插件
 * 配置项目组件仓库
 * @author liheng
 */
class IHubPluginsPlugin implements IHubPluginAware<IHubPluginsExtension> {

    @Override
    void apply(Project project) {
        IHubPluginsExtension ext = createExtension project, 'iHub', IHubPluginsExtension

        // TODO
//        project.pluginManager.apply 'com.palantir.git-version'
//        project.version = ext.version.with {
//            'unspecified' == it ? project.versionDetails().lastTag : it
//        }
        project.version = ext.version

        project.repositories {
            String dirs = "$project.rootProject.projectDir/libs"
            if ((dirs as File).directory) {
                flatDir dirs: dirs
            }
            if (ext.mavenLocalEnabled) {
                mavenLocal()
            }
            maven mavenRepo('AliYunPublic', 'https://maven.aliyun.com/repository/public')
            maven mavenRepo('AliYunGoogle', 'https://maven.aliyun.com/repository/google',
                'https://maven.google.com')
            maven mavenRepo('AliYunSpring', 'https://maven.aliyun.com/repository/spring',
                'https://repo.spring.io/release')
            maven mavenRepo('SpringRelease', 'https://repo.spring.io/release')
            // 添加私有仓库
            ext.releaseRepoUrl?.with { url -> maven mavenRepo('ReleaseRepo', url, ext) { releasesOnly() } }
            ext.snapshotRepoUrl?.with { url -> maven mavenRepo('SnapshotRepo', url, ext) { snapshotsOnly() } }
            // 添加自定义仓库
            ext.customizeRepoUrl?.with { url -> maven mavenRepo('CustomizeRepo', url) }
            mavenCentral()
        }

        if (project.name == project.rootProject.name) {
            printConfigContent 'Gradle Project Repos', project.repositories*.displayName
        }

        project.pluginManager.apply IHubBomPlugin

        project.subprojects {
            pluginManager.apply IHubPluginsPlugin
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
            allowInsecureProtocol ext.repoAllowInsecureProtocol
            mavenContent mavenContentConfig
            content {
                String repoIncludeGroup = ext.repoIncludeGroup
                if (repoIncludeGroup) {
                    includeGroup repoIncludeGroup
                } else {
                    includeGroupByRegex ext.repoIncludeGroupRegex
                }
            }
            ext.repoUsername?.with { repoUsername ->
                credentials {
                    username repoUsername
                    password ext.repoPassword
                }
            }
        }
    }

}
