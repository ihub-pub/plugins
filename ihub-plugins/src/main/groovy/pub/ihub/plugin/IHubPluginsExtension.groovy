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

import groovy.transform.CompileStatic
import org.gradle.api.provider.Property

import static pub.ihub.plugin.IHubProperty.Type.ENV
import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM

/**
 * IHub插件属性扩展
 * @author henry
 */
@IHubExtension('iHub')
@CompileStatic
interface IHubPluginsExtension extends IHubExtensionAware {

    //<editor-fold desc="组件仓库相关扩展属性">

    /**
     * 是否启用本地仓库
     */
    @IHubProperty(defaultValue = 'false', genericType = Boolean)
    Property<Boolean> getMavenLocalEnabled()

    /**
     * 是否启用阿里云代理仓库
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV], defaultValue = 'false', genericType = Boolean)
    Property<Boolean> getMavenAliYunEnabled()

    /**
     * 是否启用SpringMilestone仓库
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV], defaultValue = 'false', genericType = Boolean)
    Property<Boolean> getMavenSpringMilestoneEnabled()

    /**
     * 是否启用私有仓库（组件发布仓库）
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV], defaultValue = 'true', genericType = Boolean)
    Property<Boolean> getMavenPrivateEnabled()

    /**
     * 正式版本仓库
     */
    @IHubProperty
    Property<String> getReleaseRepoUrl()

    /**
     * 快照版本仓库
     */
    @IHubProperty
    Property<String> getSnapshotRepoUrl()

    /**
     * 是否允许不安全协议（是否允许http）
     */
    @IHubProperty(defaultValue = 'false', genericType = Boolean)
    Property<Boolean> getRepoAllowInsecureProtocol()

    /**
     * 仓库包含组（用于限制仓库范围）
     */
    @IHubProperty
    Property<String> getRepoIncludeGroup()

    /**
     * 仓库包含组正则（用于限制仓库范围）
     */
    @IHubProperty(defaultValue = '.*')
    Property<String> getRepoIncludeGroupRegex()

    /**
     * 仓库用户名
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    Property<String> getRepoUsername()

    /**
     * 仓库密码
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    Property<String> getRepoPassword()

    /**
     * 自定义仓库
     */
    @IHubProperty
    Property<String> getCustomizeRepoUrl()

    //</editor-fold>

    //<editor-fold desc="配置文件相关扩展属性">

    /**
     * 配置文件，多个配置用逗号分隔，优先级从右到左
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> getProfile()

    //</editor-fold>

}
