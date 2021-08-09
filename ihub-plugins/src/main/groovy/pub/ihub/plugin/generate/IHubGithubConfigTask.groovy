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
package pub.ihub.plugin.generate

import groovy.transform.CompileStatic
import pub.ihub.plugin.IHubProjectTask
import pub.ihub.plugin.IHubTask

import static groovy.transform.TypeCheckingMode.SKIP



/**
 * Github配置任务
 * @author henry
 */
@CompileStatic
@IHubTask('generateGithubConfig')
class IHubGithubConfigTask extends IHubProjectTask {

    @Override
    void action() {
        // Build Config
        copyFile '.github/workflows/gradle-build.yml'
        // Release Drafter Config
        copyFile '.github/release-drafter.yml'
        copyFile '.github/workflows/release-drafter.yml'
    }

    @CompileStatic(SKIP)
    private void copyFile(String path) {
        project.file(path).with {
            if (!exists()) {
                project.mkdir(path.replaceAll(/\/[a-zA-Z_-]*\..*/, ''))
                createNewFile()
            }
            it.text = IHubGeneratePlugin.getResourceAsStream('/' + path).readLines().join('\n')
        }
    }

}
