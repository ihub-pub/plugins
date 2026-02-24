/*
 * Copyright (c) 2025 the original author or authors.
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
package pub.ihub.plugin.copyright

import groovy.transform.CompileStatic
import org.gradle.api.provider.Property
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubExtensionAware
import pub.ihub.plugin.IHubProperty

import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM


/**
 * IHub版权插件属性扩展
 * @author henry
 */
@IHubExtension('iHubCopyright')
@CompileStatic
interface IHubCopyrightExtension extends IHubExtensionAware {

    /**
     * 是否启用IDEA版权配置（可选）
     * 启用后会自动配置.idea/copyright目录下的配置文件
     */
    @IHubProperty(type = [PROJECT, SYSTEM], defaultValue = 'true', genericType = Boolean)
    Property<Boolean> getEnableIdea()

}
