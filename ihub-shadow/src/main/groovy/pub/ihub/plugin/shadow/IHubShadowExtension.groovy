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
package pub.ihub.plugin.shadow

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty
import pub.ihub.plugin.verification.IHubSystemProperties

import javax.inject.Inject

import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM

/**
 * IHub Shadow Plugin Extension
 */
@IHubExtension('iHubShadow')
class IHubShadowExtension extends IHubProjectExtensionAware implements IHubSystemProperties {

    /**
     * run属性
     */
    MapProperty<String, String> runProperties
    /**
     * 运行时包含系统属性名称（“,”分割,支持通配符“*”）
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> runIncludePropNames
    /**
     * 运行时排除系统属性名称（“,”分割,支持通配符“*”）
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> runSkippedPropNames
    /**
     * 启用本地属性
     */
    @IHubProperty(genericType = Boolean)
    Property<Boolean> enabledLocalProperties

    @Inject
    IHubShadowExtension(ObjectFactory objectFactory) {
        runProperties = objectFactory.mapProperty(String, String)
        runIncludePropNames = objectFactory.property(String)
        runSkippedPropNames = objectFactory.property(String)
        enabledLocalProperties = objectFactory.property(Boolean).convention(true)
    }

}
