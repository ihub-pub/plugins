/*
 * Copyright (c) 2023 the original author or authors.
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
package pub.ihub.plugin.node

import com.github.gradle.node.NodeExtension
import com.github.gradle.node.NodePlugin
import org.gradle.api.file.Directory
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.node.cnpm.task.CnpmInstallTask
import pub.ihub.plugin.node.cnpm.task.CnpmSetupTask
import pub.ihub.plugin.node.cnpm.task.CnpmSyncTask
import pub.ihub.plugin.node.cnpm.task.CnpmTask

import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER

/**
 * IHub Node Plugin
 * @author henry
 */
@IHubPlugin(
        value = IHubNodeExtension, beforeApplyPlugins = NodePlugin,
        tasks = [CnpmTask, CnpmInstallTask, CnpmSyncTask, CnpmSetupTask]
)
@SuppressWarnings('AbcMetric')
class IHubNodePlugin extends IHubProjectPluginAware<IHubNodeExtension> {

    @Override
    protected void apply() {
        logger.lifecycle 'Build with IHub Node Plugin ' + IHubNodePlugin.package.implementationVersion +
                ', You can see the documentation to learn more see https://doc.ihub.pub/plugins/iHubNode'

        Directory cacheDir = project.layout.projectDirectory.dir '.gradle'
        extension.workDir.set extension.workDir.getOrElse(cacheDir.dir('nodejs').asFile.path)
        extension.npmWorkDir.set extension.npmWorkDir.getOrElse(cacheDir.dir('npm').asFile.path)
        extension.pnpmWorkDir.set extension.pnpmWorkDir.getOrElse(cacheDir.dir('pnpm').asFile.path)
        extension.yarnWorkDir.set extension.yarnWorkDir.getOrElse(cacheDir.dir('yarn').asFile.path)
        extension.cnpmWorkDir.set extension.cnpmWorkDir.getOrElse(cacheDir.dir('cnpm').asFile.path)

        withExtension(NodeExtension, AFTER) {
            it.version.set extension.version.get()
            it.npmVersion.set extension.npmVersion.orNull
            it.pnpmVersion.set extension.pnpmVersion.orNull
            it.yarnVersion.set extension.yarnVersion.orNull
            it.distBaseUrl.set extension.distBaseUrl.orNull
            it.allowInsecureProtocol.set extension.allowInsecureProtocol.get()
            it.download.set extension.download.get()
            it.workDir.set cacheDir.dir(extension.workDir.get())
            it.npmWorkDir.set cacheDir.dir(extension.npmWorkDir.get())
            it.pnpmWorkDir.set cacheDir.dir(extension.pnpmWorkDir.get())
            it.yarnWorkDir.set cacheDir.dir(extension.yarnWorkDir.get())

            withTask(CnpmTask) { CnpmTask t ->
                if (t.name.contains('run')) {
                    t.dependsOn CnpmInstallTask.NAME
                }
                t.group = CnpmTask.NAME
            }
        }
    }

}
