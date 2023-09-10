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

import com.github.gradle.node.npm.task.NpmSetupTask
import groovy.transform.CompileStatic
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import pub.ihub.plugin.IHubTask
import pub.ihub.plugin.node.IHubNodeExtension

/**
 * CNpm Setup Task
 * @author henry
 */
@IHubTask(
        value = CnpmSetupTask.NAME, group = CnpmTask.NAME,
        description = 'Setup a specific version of CNpm to be used by the build.'
)
@CompileStatic
@SuppressWarnings('AbstractClassWithoutAbstractMethod')
abstract class CnpmSetupTask extends NpmSetupTask {

    static final String NAME = 'cnpmSetup'
    static final String REGISTRY = 'https://registry.npm.taobao.org'

    @Input
    @Optional
    protected Provider<String> getVersion() {
        project.extensions.getByType(IHubNodeExtension).cnpmVersion
    }

    @Override
    @SuppressWarnings('UnnecessaryCast')
    protected List<String> computeCommand() {
        String cNpmPackage = version.with { present ? "${CnpmTask.NAME}@${get()}" : CnpmTask.NAME }
        [
                'install', '--global', '--no-save', '--prefix',
                computeCNpmDir().absolutePath, cNpmPackage, "--registry=$REGISTRY"
        ] as List<String> + args.get()
    }

    @Override
    boolean isTaskEnabled() {
        true
    }

    private File computeCNpmDir() {
        String workDir = project.extensions.getByType(IHubNodeExtension).cnpmWorkDir.get()
        String dirnameSuffix = version.with { present ? "-v${get()}" : '-latest' }
        project.layout.projectDirectory.dir('.gradle').dir(workDir).dir("cnpm$dirnameSuffix").asFile
    }

}
