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
package pub.ihub.plugin.node

import com.github.gradle.node.NodeExtension
import groovy.transform.CompileStatic
import org.gradle.api.provider.Property
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubExtensionAware
import pub.ihub.plugin.IHubProperty

import static pub.ihub.plugin.IHubProperty.Type.ENV
import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM

/**
 * IHub Node Extension
 * @author henry
 */
@IHubExtension('iHubNode')
@CompileStatic
interface IHubNodeExtension extends IHubExtensionAware {

    /**
     * Node.js版本
     */
    @IHubProperty(defaultValue = NodeExtension.DEFAULT_NODE_VERSION, type = [PROJECT, SYSTEM])
    Property<String> getVersion()

    /**
     * NPM版本，如果不指定则使用Node.js绑定版本
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> getNpmVersion()

    /**
     * PNPM版本，如果不指定则使用最新版本
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> getPnpmVersion()

    /**
     * Yarn版本，如果不指定则使用最新版本
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> getYarnVersion()

    /**
     * cNpm版本，如果不指定则使用最新版本
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> getCnpmVersion()

    /**
     * Node.js下载地址
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> getDistBaseUrl()

    /**
     * 是否允许不安全的协议
     */
    @IHubProperty(defaultValue = 'false', type = [PROJECT, SYSTEM], genericType = Boolean)
    Property<Boolean> getAllowInsecureProtocol()

    /**
     * 是否下载并安装特定的 Node.js 版本
     * 如果为 false，它将使用全局安装的 Node.js
     * 如果为 true，它将使用上述参数下载 Node.js，请注意，npm 与 Node.js 捆绑在一起
     */
    @IHubProperty(defaultValue = 'false', type = [PROJECT, SYSTEM], genericType = Boolean)
    Property<Boolean> getDownload()

    /**
     * Node.js安装目录
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    Property<String> getWorkDir()

    /**
     * NPM安装目录
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    Property<String> getNpmWorkDir()

    /**
     * PNPM安装目录
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    Property<String> getPnpmWorkDir()

    /**
     * Yarn安装目录
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    Property<String> getYarnWorkDir()

    /**
     * cNpm安装目录
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    Property<String> getCnpmWorkDir()

}
