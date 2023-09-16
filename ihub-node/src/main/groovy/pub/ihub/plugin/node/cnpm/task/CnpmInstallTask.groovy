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
package pub.ihub.plugin.node.cnpm.task

import groovy.transform.CompileStatic
import org.gradle.api.file.Directory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import pub.ihub.plugin.IHubTask

import static org.gradle.api.tasks.PathSensitivity.RELATIVE

/**
 * CNpm Install Task
 * @author henry
 */
@IHubTask(
        value = CnpmInstallTask.NAME, group = CnpmTask.NAME, dependsOn = [CnpmSetupTask.NAME],
        description = 'Install node packages using CNpm.'
)
@CompileStatic
@SuppressWarnings(['NoWildcardImports', 'PropertyName'])
class CnpmInstallTask extends CnpmTask {

    static final String NAME = 'cnpmInstall'

    @Internal
    ListProperty<String> CNpmCommand = objects.listProperty(String).convention([nodeExtension.npmInstallCommand.get()])

    @PathSensitive(RELATIVE)
    @Optional
    @InputFile
    protected File getPackageJsonFile() {
        projectFileIfExists('package.json').orNull
    }

    private Provider<File> projectFileIfExists(String name) {
        nodeExtension.nodeProjectDir.map { it.file(name).asFile }.flatMap {
            it.exists() ? providers.provider { it } : providers.provider { null }
        } as Provider<File>
    }

    @Optional
    @OutputDirectory
    protected Provider<Directory> getNodeModulesDirectory() {
        nodeExtension.nodeProjectDir.dir 'node_modules'
    }

}
