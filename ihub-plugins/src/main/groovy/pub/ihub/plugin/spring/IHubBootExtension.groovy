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
package pub.ihub.plugin.spring

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty
import pub.ihub.plugin.IHubSystemProperties

import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM



/**
 * IHub Spring Boot Plugin Extension
 * @author henry
 */
@IHubExtension('iHubBoot')
@CompileStatic
@TupleConstructor(allProperties = true, includes = 'project')
class IHubBootExtension implements IHubProjectExtensionAware, IHubSystemProperties {

    //<editor-fold desc="BootRun Configuration">

    /**
     * bootRun属性
     */
    Map<String, String> runProperties = [:]
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
    boolean enabledLocalProperties = true

    //</editor-fold>

    //<editor-fold desc="BootJar Configuration">

    /**
     * 配置需要移除的库
     */
    @IHubProperty
    String bootJarRequiresUnpack = ''

    //</editor-fold>

}
