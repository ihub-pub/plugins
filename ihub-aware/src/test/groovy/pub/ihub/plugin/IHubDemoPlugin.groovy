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

import org.gradle.api.Plugin
import org.gradle.api.Project

import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.IHubPluginMethods.tap



/**
 * @author henry
 */
class IHubDemoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        IHubDemoExtension ext = project.extensions.create 'iHubDemo', IHubDemoExtension

        System.setProperty 'demo.a.version', '1.0.0'
        System.setProperty 'demo_b_version', '1.0.0'
        System.setProperty 'demo-c-version', '1.0.0'
        System.setProperty 'demoDVersion', '1.0.0'

        printConfigContent 'demo test print', tap('demo'), tap('version', 30), [
            'demoA', 'demoB', 'demoC', 'demoD', 'demoE'
        ].collectEntries { [(it): ext.findVersion(it, '1.0.1')] }
    }

}
