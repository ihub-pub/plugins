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
package pub.ihub.plugin.profiles

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubPluginsExtension
import pub.ihub.plugin.IHubProjectExtensionAware

import javax.inject.Inject

/**
 * IHub配置文件插件属性扩展
 * @author henry
 */
@IHubExtension('iHubProfiles')
@CompileStatic
class IHubProfilesExtension extends IHubProjectExtensionAware {

    /**
     * 配置文件tokens
     */
    MapProperty<String, String> tokens

    @Inject
    IHubProfilesExtension(ObjectFactory objectFactory) {
        tokens = objectFactory.mapProperty(String, String).convention([:])
    }

    void profile(String name, Action<Project> action) {
        def profile = project.extensions.getByType(IHubPluginsExtension).profile
        if (!profile.present) {
            return
        }
        profile.get().split(',').each {
            if (name == it || name.startsWith('!') && name.substring(1) != it) {
                action.execute project
            }
        }
    }

    void profile(Collection<String> names, Action<Project> action) {
        names.each { name ->
            profile name, action
        }
    }

    void profile(String[] names, Action<Project> action) {
        names.each { name ->
            profile name, action
        }
    }

}
