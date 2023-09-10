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

import com.github.gradle.node.NodeExtension
import com.github.gradle.node.exec.NodeExecConfiguration
import com.github.gradle.node.task.BaseTask
import com.github.gradle.node.util.DefaultProjectApiHelper
import groovy.transform.CompileStatic
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult
import pub.ihub.plugin.IHubTask
import pub.ihub.plugin.node.cnpm.exec.CnpmExecRunner

/**
 * CNpm Task
 * @author henry
 */
@IHubTask(
        value = CnpmTask.NAME, group = CnpmTask.NAME, dependsOn = [CnpmSetupTask.NAME],
        description = 'Install node packages using CNpm.'
)
@CompileStatic
@SuppressWarnings('PropertyName')
abstract class CnpmTask extends BaseTask {

    static final String NAME = 'cnpm'

    @Internal
    protected ObjectFactory getObjects() {
        project.objects
    }

    @Internal
    protected ProviderFactory getProviders() {
        project.providers
    }

    @Internal
    protected RegularFileProperty getWorkingDir() {
        objects.fileProperty()
    }

    @Internal
    protected DefaultProjectApiHelper getProjectHelper() {
        objects.newInstance DefaultProjectApiHelper
    }

    @Internal
    protected NodeExtension getNodeExtension() {
        project.extensions.getByType NodeExtension
    }

    @Optional
    @Input
    ListProperty<String> CNpmCommand = objects.listProperty String

    @Optional
    @Input
    ListProperty<String> args = objects.listProperty String

    @Optional
    @Input
    MapProperty<String, String> environment = objects.mapProperty String, String

    @TaskAction
    ExecResult exec() {
        List<String> command = CNpmCommand.get() + args.get()
        logger.lifecycle 'exec command: cnpm {}', command.join(' ')
        NodeExecConfiguration nodeExecConfiguration = new NodeExecConfiguration(
                command, environment.get(), workingDir.asFile.orNull, true, null
        )
        result = CnpmExecRunner.executeCNpmCommand project, projectHelper, nodeExtension,
                nodeExecConfiguration, variantComputer$gradle_node_plugin
    }

}
