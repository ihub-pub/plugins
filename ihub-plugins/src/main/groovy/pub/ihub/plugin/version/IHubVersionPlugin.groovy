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
package pub.ihub.plugin.version

import com.github.benmanes.gradle.versions.VersionsPlugin
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.freefair.gradle.plugins.git.GitVersionPlugin
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware

import static pub.ihub.plugin.IHubPluginMethods.printConfigContent

/**
 * IHub版本插件
 * @author henry
 */
@IHubPlugin(value = IHubVersionExtension, beforeApplyPlugins = VersionsPlugin)
class IHubVersionPlugin extends IHubProjectPluginAware<IHubVersionExtension> {

    @Override
    protected void apply() {
        afterEvaluate {
            configProjectWithGit()
        }

        // 配置组件升级任务
        withTask DependencyUpdatesTask, { task ->
            // 自定义依赖升级输出
            task.outputFormatter = dependencyUpdatesOutputFormatter
            // 配置拒绝升级策略
            task.rejectVersionIf rejectVersionFilter
            // 其他配置
            task.checkConstraints = true
            task.checkBuildEnvironmentConstraints = true
            task.gradleReleaseChannel = 'current'
            task.checkForGradleUpdate = true
        }
    }

    private void configProjectWithGit() {
        // 推断版本号
        if (extension.useInferringVersion.get() && 'unspecified' == project.version.toString()) {
            try {
                String gitTag = 'git describe --tags'.execute().text.trim()
                logger.lifecycle 'Inferring version use git tag: {}', gitTag
                project.version = (gitTag =~ /^v?(\d+).(\d+).(\d+)(-\w+\d*)?(-\d+-g\w{7})?$/)[0][1..3]
                    .with { major, minor, patch -> "$major.$minor.${(patch as int) + 1}-SNAPSHOT" }
            } catch (e) {
                logger.lifecycle 'Failed to get current git tag', e
            }
        }

        applyPlugin GitVersionPlugin
    }

    static final isStable(String version) {
        ['RELEASE', 'FINAL', 'GA'].any { it -> version.toUpperCase().contains(it) } || version ==~ /v?(\d+\.)+\d+/
    }

    private final rejectVersionFilter = { current ->
        isStable(current.currentVersion) && !isStable(current.candidate.version)
    }

    private replaceLastVersion(dependencies) {
        project.allprojects.each { prj ->
            if (prj.buildFile.exists()) {
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
                if (this.extension.autoReplaceLaterVersions.get()) {
                    replaceLastVersion it
                }
            }
        }
        result.gradle.with {
            if (enabled) {
                printConfigContent 'Gradle later version', [['current', running.version, 'later', current.version]]
            }
        }
    }

}
