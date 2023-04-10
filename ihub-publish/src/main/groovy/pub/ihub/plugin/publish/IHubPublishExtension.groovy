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
package pub.ihub.plugin.publish

import org.gradle.api.provider.Property
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubExtensionAware
import pub.ihub.plugin.IHubProperty

import static pub.ihub.plugin.IHubProperty.Type.ENV
import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM

/**
 * 组件发布属性扩展
 * @author henry
 */
@IHubExtension('iHubPublish')
interface IHubPublishExtension extends IHubExtensionAware {

    /**
     * 签名key
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    Property<String> getSigningKeyId()

    /**
     * 组件发布是否需要签名
     */
    @IHubProperty(type = [PROJECT, SYSTEM], defaultValue = 'false', genericType = Boolean)
    Property<Boolean> getPublishNeedSign()

    /**
     * 签名密钥
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    Property<String> getSigningSecretKey()

    /**
     * 签名密码
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    Property<String> getSigningPassword()

    /**
     * 是否发布文档
     */
    @IHubProperty(type = [PROJECT, SYSTEM], defaultValue = 'false', genericType = Boolean)
    Property<Boolean> getPublishDocs()

    /**
     * 是否应用GithubPom插件
     */
    @IHubProperty(type = [PROJECT, SYSTEM], defaultValue = 'false', genericType = Boolean)
    Property<Boolean> getApplyGithubPom()

}
