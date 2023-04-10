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
package pub.ihub.plugin.verification

import groovy.transform.CompileStatic
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.buildinit.plugins.internal.modifiers.BuildInitTestFramework
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty

import javax.inject.Inject

import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM

/**
 * 测试插件扩展
 * @author henry
 */
@IHubExtension('iHubTest')
@CompileStatic
class IHubTestExtension extends IHubProjectExtensionAware implements IHubSystemProperties {

    /**
     * 启用测试
     */
    @IHubProperty(type = [PROJECT, SYSTEM], genericType = Boolean)
    Property<Boolean> enabled
    /**
     * 包含测试类（“,”分割）
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> classes
    /**
     * 每跑100个测试类后重启fork进程
     */
    @IHubProperty(type = [PROJECT, SYSTEM], genericType = Integer)
    Property<Integer> forkEvery
    /**
     * 最多启动进程数
     */
    @IHubProperty(type = [PROJECT, SYSTEM], genericType = Integer)
    Property<Integer> maxParallelForks
    /**
     * 任务运行时属性
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
    /**
     * 启用测试调试
     */
    @IHubProperty(type = [PROJECT, SYSTEM], genericType = Boolean)
    Property<Boolean> debug
    /**
     * 只要有一个测试失败就停止测试
     */
    @IHubProperty(type = [PROJECT, SYSTEM], genericType = Boolean)
    Property<Boolean> failFast

    /**
     * 测试框架
     * @return 测试框架
     */
    @IHubProperty(genericType = BuildInitTestFramework)
    Property<BuildInitTestFramework> testFramework

    @Inject
    IHubTestExtension(ObjectFactory objectFactory) {
        enabled = objectFactory.property(Boolean).convention(true)
        classes = objectFactory.property(String)
        forkEvery = objectFactory.property(Integer).convention(100)
        maxParallelForks = objectFactory.property(Integer).convention(1)
        runProperties = objectFactory.mapProperty(String, String).convention(System.properties as Map<String, String>)
        runIncludePropNames = objectFactory.property(String)
        runSkippedPropNames = objectFactory.property(String)
        enabledLocalProperties = objectFactory.property(Boolean).convention(true)
        debug = objectFactory.property(Boolean).convention(false)
        failFast = objectFactory.property(Boolean).convention(false)
        testFramework = objectFactory.property(BuildInitTestFramework)
    }

}
