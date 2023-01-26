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

import groovy.transform.CompileStatic
import org.gradle.api.Project

import static groovy.transform.TypeCheckingMode.SKIP



/**
 * IHub项目扩展属性
 * @author henry
 */
@CompileStatic
trait IHubExtProperty {

    abstract Project getProject()

    Project getRootProject() {
        project.rootProject
    }

    @CompileStatic(SKIP)
    void setExtProperty(Project project = this.project, String key, value) {
        project.ext.setProperty key, value
    }

    @CompileStatic(SKIP)
    <V> V findExtProperty(Project project = this.project, String key, V defaultValue = null) {
        project.ext.with { has(key) ? getProperty(key) as V : defaultValue }
    }

}
