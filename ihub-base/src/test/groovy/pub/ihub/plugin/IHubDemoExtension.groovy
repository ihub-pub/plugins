/*
 * Copyright (c) 2021-2023 the original author or authors.
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
package pub.ihub.plugin

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

import static pub.ihub.plugin.IHubProperty.Type.ENV
import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM

/**
 * @author henry
 */
@IHubExtension('iHubDemo')
@SuppressWarnings('GetterMethodCouldBeProperty')
class IHubDemoExtension extends IHubProjectExtensionAware implements IHubExtProperty {

    Property<Boolean> flag

    @IHubProperty
    Property<String> str

    @IHubProperty(type = SYSTEM)
    Property<String> system

    @IHubProperty(type = ENV)
    Property<String> env

    @IHubProperty(type = [PROJECT, SYSTEM], defaultValue = 'true', genericType = Boolean)
    Property<Boolean> trueFlag

    @IHubProperty(type = [PROJECT, ENV], defaultValue = 'false', genericType = Boolean)
    Property<Boolean> falseFlag

    @IHubProperty(defaultValue = 'true')
    Property<String> trueStrFlag

    @IHubProperty(defaultValue = 'false')
    Property<String> falseStrFlag

    @IHubProperty('demoProperty')
    Property<String> customizationProperty

    IHubDemoExtension(ObjectFactory objectFactory) {
        flag = objectFactory.property(Boolean).convention(false)
        str = objectFactory.property(String).convention('text')
        system = objectFactory.property(String).convention('system')
        env = objectFactory.property(String).convention('env')
        trueFlag = objectFactory.property(Boolean)
        falseFlag = objectFactory.property(Boolean)
        trueStrFlag = objectFactory.property(String)
        falseStrFlag = objectFactory.property(String)
        customizationProperty = objectFactory.property(String).convention('str')
    }

}
