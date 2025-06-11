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
import org.gradle.api.Project
import javax.inject.Inject
import pub.ihub.plugin.IHubProperty.Type.ENV
import pub.ihub.plugin.IHubProperty.Type.PROJECT
import pub.ihub.plugin.IHubProperty.Type.SYSTEM

/**
 * @author henry
 */
@IHubExtension("iHubDemo")
open class IHubDemoExtension @Inject constructor(project: Project, objectFactory: ObjectFactory) :
    IHubProjectExtensionAware(project), IHubExtProperty {

    val flag: Property<Boolean> = objectFactory.property(Boolean::class.java).convention(false)

    @get:IHubProperty
    val str: Property<String> = objectFactory.property(String::class.java).convention("text")

    @get:IHubProperty(type = [SYSTEM])
    val system: Property<String> = objectFactory.property(String::class.java).convention("system")

    @get:IHubProperty(type = [ENV])
    val env: Property<String> = objectFactory.property(String::class.java).convention("env")

    @get:IHubProperty(type = [PROJECT, SYSTEM], defaultValue = "true", genericType = Boolean::class)
    val trueFlag: Property<Boolean> = objectFactory.property(Boolean::class.java)

    @get:IHubProperty(type = [PROJECT, ENV], defaultValue = "false", genericType = Boolean::class)
    val falseFlag: Property<Boolean> = objectFactory.property(Boolean::class.java)

    @get:IHubProperty(defaultValue = "true")
    val trueStrFlag: Property<String> = objectFactory.property(String::class.java)

    @get:IHubProperty(defaultValue = "false")
    val falseStrFlag: Property<String> = objectFactory.property(String::class.java)

    @get:IHubProperty("demoProperty")
    val customizationProperty: Property<String> = objectFactory.property(String::class.java).convention("str")

    @get:IHubProperty(type = [ENV])
    val javaHome: Property<String> = objectFactory.property(String::class.java)
}
