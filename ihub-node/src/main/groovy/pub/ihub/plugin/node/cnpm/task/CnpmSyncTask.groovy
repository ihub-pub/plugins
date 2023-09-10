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
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Internal
import pub.ihub.plugin.IHubTask

/**
 * CNpm Sync Task
 * @author henry
 */
@IHubTask(
        value = CnpmSyncTask.NAME, group = CnpmTask.NAME, dependsOn = [CnpmSetupTask.NAME],
        description = 'Sync node packages using CNpm.'
)
@CompileStatic
@SuppressWarnings('PropertyName')
class CnpmSyncTask extends CnpmTask {

    static final String NAME = 'cnpmSync'

    @Internal
    ListProperty<String> CNpmCommand = objects.listProperty(String).convention(['sync', 'cnpmcore'])

}
