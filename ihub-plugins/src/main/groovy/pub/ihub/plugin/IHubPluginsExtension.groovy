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
package pub.ihub.plugin

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

import static pub.ihub.plugin.IHubProperty.Type.ENV
import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM



/**
 * IHub插件属性扩展
 * @author henry
 */
@IHubExtension('iHub')
@CompileStatic
@TupleConstructor(allProperties = true, includes = 'project')
class IHubPluginsExtension implements IHubProjectExtensionAware {

    //<editor-fold desc="组件仓库相关扩展属性">

    /**
     * 是否启用本地仓库
     */
    @IHubProperty
    boolean mavenLocalEnabled = false

    /**
     * 正式版本仓库
     */
    @IHubProperty
    String releaseRepoUrl

    /**
     * 快照版本仓库
     */
    @IHubProperty
    String snapshotRepoUrl

    /**
     * 是否允许不安全协议（是否允许http）
     */
    @IHubProperty
    boolean repoAllowInsecureProtocol = false

    /**
     * 仓库包含组（用于限制仓库范围）
     */
    @IHubProperty
    String repoIncludeGroup

    /**
     * 仓库包含组正则（用于限制仓库范围）
     */
    @IHubProperty
    String repoIncludeGroupRegex = '.*'

    /**
     * 仓库用户名
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    String repoUsername

    /**
     * 仓库密码
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    String repoPassword

    /**
     * 自定义仓库
     */
    @IHubProperty
    String customizeRepoUrl

    //</editor-fold>

    //<editor-fold desc="Groovy相关扩展属性">

    /**
     * 是否添加groovy所有模块
     */
    @IHubProperty
    boolean compileGroovyAllModules = false

    //</editor-fold>

    //<editor-fold desc="其他扩展属性">

    /**
     * 自动替换最新版本
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean autoReplaceLaterVersions = false

    /**
     * 使用推断版本号
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    boolean useInferringVersion = false

    //</editor-fold>

}
