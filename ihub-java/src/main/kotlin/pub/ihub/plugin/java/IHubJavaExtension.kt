/*
 * Copyright (c) 2021-2024 the original author or authors.
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
package pub.ihub.plugin.java

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty
import javax.inject.Inject

/**
 * Java插件扩展
 * @author henry
 */
@IHubExtension("iHubJava")
open class IHubJavaExtension @Inject constructor(project: Project, objectFactory: ObjectFactory) :
    IHubProjectExtensionAware(project) {

    /**
     * 默认依赖（“,”分割）
     */
    @get:IHubProperty
    val defaultDependencies: Property<String> = objectFactory.property(String::class.java).convention("false")

    /**
     * Java编译编码
     */
    @get:IHubProperty
    val compileEncoding: Property<String> = objectFactory.property(String::class.java).convention("UTF-8")

    /**
     * Java Source 兼容性配置
     */
    @get:IHubProperty
    val sourceCompatibility: Property<String> = objectFactory.property(String::class.java)

    /**
     * Java Target 兼容性配置
     */
    @get:IHubProperty
    val targetCompatibility: Property<String> = objectFactory.property(String::class.java)

    /**
     * gradle增量编译
     */
    @get:IHubProperty(genericType = Boolean::class)
    val gradleCompilationIncremental: Property<Boolean> = objectFactory.property(Boolean::class.javaObjectType).convention(true)


    /**
     * 编译扩展属性，多个参数用空格分隔，如：-parameters -Xlint:unchecked
     */
    @get:IHubProperty(type = [IHubProperty.Type.PROJECT, IHubProperty.Type.SYSTEM])
    val compilerArgs: Property<String> = objectFactory.property(String::class.java)

    /**
     * JVM扩展属性，多个参数用空格分隔，如：-XX:+UseG1GC -Xms128m -Xmx512m
     */
    @get:IHubProperty
    val jvmArgs: Property<String> = objectFactory.property(String::class.java)

    /**
     * JMolecules架构，可选类型：cqrs、layered、onion
     */
    @get:IHubProperty
    val jmoleculesArchitecture: Property<String> = objectFactory.property(String::class.java).convention("onion")

    /**
     * 可选功能配置
     * @param feature 功能
     * @param capabilities 能力
     */
    fun registerFeature(feature: String, vararg capabilities: String) {
        project.extensions.findByType(JavaPluginExtension::class.java)?.apply {
            registerFeature(feature) {
                it.usingSourceSet(sourceSets.getByName("main"))
                capabilities.forEach { capability ->
                    it.capability(project.group.toString(), capability, project.version.toString())
                }
            }
        }
    }
}
