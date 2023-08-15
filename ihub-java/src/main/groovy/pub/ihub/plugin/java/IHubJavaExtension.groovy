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
package pub.ihub.plugin.java

import groovy.transform.CompileStatic
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty

import javax.inject.Inject

import static groovy.transform.TypeCheckingMode.SKIP
import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM

/**
 * Java插件扩展
 * @author henry
 */
@IHubExtension('iHubJava')
@CompileStatic
class IHubJavaExtension extends IHubProjectExtensionAware {

    /**
     * 默认依赖（“,”分割）
     */
    @IHubProperty
    Property<String> defaultDependencies

    /**
     * Java编译编码
     */
    @IHubProperty
    Property<String> compileEncoding

    /**
     * Java兼容性配置
     */
    @IHubProperty
    Property<String> compatibility

    /**
     * gradle增量编译
     */
    @IHubProperty(genericType = Boolean)
    Property<Boolean> gradleCompilationIncremental

    /**
     * 编译扩展属性，多个参数用空格分隔，如：-parameters -Xlint:unchecked
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> compilerArgs

    /**
     * JVM扩展属性，多个参数用空格分隔，如：-XX:+UseG1GC -Xms128m -Xmx512m
     */
    @IHubProperty
    Property<String> jvmArgs

    /**
     * JMolecules架构，可选类型：cqrs、layered、onion
     */
    @IHubProperty
    Property<String> jmoleculesArchitecture

    /**
     * 启用org.springdoc.openapi-gradle-plugin插件
     */
    @IHubProperty(type = [PROJECT, SYSTEM], genericType = Boolean)
    Property<Boolean> applyOpenapiPlugin

    @Inject
    IHubJavaExtension(ObjectFactory objectFactory) {
        defaultDependencies = objectFactory.property(String).convention('log')
        compileEncoding = objectFactory.property(String).convention('UTF-8')
        compatibility = objectFactory.property(String)
        gradleCompilationIncremental = objectFactory.property(Boolean).convention(true)
        compilerArgs = objectFactory.property(String)
        jvmArgs = objectFactory.property(String)
        jmoleculesArchitecture = objectFactory.property(String).convention('onion')
        applyOpenapiPlugin = objectFactory.property(Boolean).convention(false)
    }

    /**
     * 可选功能配置
     * @param feature 功能
     * @param capabilities 能力
     */
    @CompileStatic(SKIP)
    void registerFeature(String feature, String... capabilities) {
        project.extensions.getByType(JavaPluginExtension).with {
            registerFeature(feature) {
                it.usingSourceSet sourceSets.main
                capabilities.each { capability ->
                    it.capability project.group.toString(), capability, project.version.toString()
                }
            }
        }
    }

}
