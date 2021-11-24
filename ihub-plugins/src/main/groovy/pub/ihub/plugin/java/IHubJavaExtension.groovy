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
package pub.ihub.plugin.java

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty

import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM



/**
 * Java插件扩展
 * @author henry
 */
@IHubExtension('iHubJava')
@CompileStatic
@TupleConstructor(allProperties = true, includes = 'project')
class IHubJavaExtension implements IHubProjectExtensionAware {

    /**
     * 默认依赖（“,”分割）
     */
    @IHubProperty
    String defaultDependencies = 'log'

    /**
     * Java兼容性配置
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    String compatibility

    /**
     * gradle增量编译
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean gradleCompilationIncremental = true

}
