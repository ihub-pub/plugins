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
package pub.ihub.plugin.node.cnpm.exec

import com.github.gradle.node.NodeExtension
import com.github.gradle.node.exec.ExecConfiguration
import com.github.gradle.node.exec.ExecRunner
import com.github.gradle.node.exec.NodeExecConfiguration
import com.github.gradle.node.util.DefaultProjectApiHelper
import com.github.gradle.node.variant.VariantComputer
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Provider
import org.gradle.process.ExecResult
import pub.ihub.plugin.node.IHubNodeExtension

/**
 * CNpm Exec Runner
 * @author henry
 */
class CnpmExecRunner {

    static ExecResult executeCNpmCommand(Project project, DefaultProjectApiHelper helper, NodeExtension nodeExtension,
                                         NodeExecConfiguration configuration, VariantComputer computer) {
        DirectoryProperty nodeDir = nodeExtension.resolvedNodeDir
        Directory binDir = computeBinDir project, nodeExtension
        List<String> additionalBinPath = computeAdditionalBinPath nodeExtension, nodeDir, binDir, computer
        ExecConfiguration execConfiguration = new ExecConfiguration(
                computeCnpmExec(nodeExtension, binDir),
                configuration.command, additionalBinPath,
                configuration.environment, configuration.workingDir,
                configuration.ignoreExitValue, configuration.execOverrides
        )
        new ExecRunner().execute(helper, nodeExtension, execConfiguration)
    }

    private static List<String> computeAdditionalBinPath(NodeExtension extension, Provider<Directory> nodeDir,
                                                         Directory cnpmBinDir, VariantComputer computer) {
        Directory nodeBinDir = computer.computeNodeBinDir(nodeDir, extension.resolvedPlatform).get()
        Directory npmBinDir = computer
                .computeNpmBinDir(computer.computeNpmDir(extension, nodeDir), extension.resolvedPlatform).get()
        [nodeBinDir, npmBinDir, cnpmBinDir].collect { it.asFile.absolutePath }
    }

    private static Directory computeBinDir(Project project, NodeExtension nodeExtension) {
        IHubNodeExtension extension = project.extensions.getByType(IHubNodeExtension)
        Directory cacheDir = project.layout.projectDirectory.dir '.gradle'
        String dirnameSuffix = extension.cnpmVersion.with { present ? "-v${get()}" : '-latest' }
        Directory dir = cacheDir.dir(extension.cnpmWorkDir.get()).dir "cnpm$dirnameSuffix"
        nodeExtension.resolvedPlatform.get().windows ? dir : dir.dir('bin')
    }

    private static String computeCnpmExec(NodeExtension nodeExtension, Directory binDir) {
        def command = nodeExtension.resolvedPlatform.get().windows ? 'cnpm.cmd' : 'cnpm'
        binDir.dir(command).asFile.absolutePath
    }

}
