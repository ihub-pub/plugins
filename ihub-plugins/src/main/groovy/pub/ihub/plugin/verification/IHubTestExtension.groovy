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
package pub.ihub.plugin.verification

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty
import pub.ihub.plugin.IHubSystemProperties

import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM



/**
 * 测试插件扩展
 * @author henry
 */
@IHubExtension('iHubTest')
@CompileStatic
@TupleConstructor(allProperties = true, includes = 'project')
class IHubTestExtension implements IHubProjectExtensionAware, IHubSystemProperties {

    /**
     * 启用测试
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean enabled = true
    /**
     * 包含测试类（“,”分割）
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    String classes = ''
    /**
     * 每跑100个测试类后重启fork进程
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    int forkEvery = 100
    /**
     * 最多启动进程数
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    int maxParallelForks = 1
    /**
     * 任务运行时属性
     */
    Map<String, String> runProperties = System.properties as Map<String, String>
    /**
     * 运行时包含系统属性名称（“,”分割）
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    String runIncludePropNames
    /**
     * 运行时排除系统属性名称（“,”分割）
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    String runSkippedPropNames
    /**
     * 启用本地属性
     */
    @IHubProperty
    boolean enabledLocalProperties = false
    /**
     * 启用测试调试
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean debug = false
    /**
     * 只要有一个测试失败就停止测试
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean failFast = false

}
