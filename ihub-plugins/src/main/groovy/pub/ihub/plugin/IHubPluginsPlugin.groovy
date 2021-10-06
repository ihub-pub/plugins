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

import com.github.benmanes.gradle.versions.VersionsPlugin
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.freefair.gradle.plugins.git.GitVersionPlugin

import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.IHubPluginMethods.printLineConfigContent



/**
 * Gradle基础插件
 * 配置项目组件仓库
 * @author liheng
 */
@IHubPlugin(IHubPluginsExtension)
class IHubPluginsPlugin extends IHubProjectPluginAware<IHubPluginsExtension> {

    @Override
    void apply() {
        withExtension { ext ->
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
        }

        if (project == project.rootProject) {
            logger.lifecycle 'Build with IHub Plugins ' + IHubPluginsPlugin.package.implementationVersion +
                ', You can see the documentation to learn more, See https://doc.ihub.pub/plugins.'
            applyPlugin GitVersionPlugin
            printLineConfigContent 'Gradle Project Repos', project.repositories*.displayName
            // 配置组件升级任务
            applyPlugin VersionsPlugin
            withTask DependencyUpdatesTask, {
                it.configure {
                    // 自定义依赖升级输出
                    outputFormatter = dependencyUpdatesOutputFormatter
                    // 配置拒绝升级策略
                    rejectVersionIf rejectVersionFilter
                    // 其他配置
                    checkConstraints = true
                    checkForGradleUpdate = false
                }
            }
        }

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

    static final isStable(String version) {
        ['RELEASE', 'FINAL', 'GA'].any { it -> version.toUpperCase().contains(it) } || version ==~ /v?(\d+\.)+\d+/
    }

    private final rejectVersionFilter = { current ->
        isStable(current.currentVersion) && !isStable(current.candidate.version)
    }

    private replaceLastVersion(dependencies) {
        project.allprojects.each { prj ->
            List<String> lines = prj.buildFile.readLines()
            prj.buildFile.withWriter { writer ->
                lines.each { line ->
                    writer.writeLine dependencies.findResult { dep ->
                        def dependency = "${dep.group}:${dep.name}:${dep.version}"
                        def latest = "${dep.group}:${dep.name}:${dep.available.release ?: dep.available.milestone}"
                        line.contains(dependency) ? line.replace(dependency, latest) : null
                    } ?: line
                }
            }
        }
    }

    private final dependencyUpdatesOutputFormatter = { result ->
        result.current.dependencies.with {
            if (!empty) {
                String title = 'The following dependencies are using the latest version'
                printConfigContent title, it.collect { dependency ->
                    [dependency.group, dependency.name, dependency.version]
                }, 'Group', 'Module', 'Version'
            }
        }
        result.exceeded.dependencies.with {
            if (!empty) {
                String title = 'The following dependencies exceed the version found at the revision level'
                printConfigContent title, it.collect { dependency ->
                    [dependency.group, dependency.name, dependency.version, dependency.latest]
                }, 'Group', 'Module', 'Current version', 'Latest version'
            }
        }
        result.outdated.dependencies.with {
            if (!empty) {
                String title = 'The following dependencies have later versions'
                printConfigContent title, it.collect { dependency ->
                    [
                        dependency.group,
                        dependency.name,
                        dependency.version,
                        dependency.available.release ?: dependency.available.milestone
                    ]
                }, 'Group', 'Module', 'Current version', 'Latest version'
                if (this.extension.autoReplaceLaterVersions) {
                    replaceLastVersion it
                }
            }
        }
    }

}
