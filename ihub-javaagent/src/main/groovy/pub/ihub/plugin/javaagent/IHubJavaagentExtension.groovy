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
package pub.ihub.plugin.javaagent

import groovy.transform.CompileStatic
import org.gradle.api.provider.Property
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubExtensionAware
import pub.ihub.plugin.IHubProperty

/**
 * javaagent插件扩展配置接口
 */
@IHubExtension('iHubJavaagent')
@CompileStatic
interface IHubJavaagentExtension extends IHubExtensionAware {

    /**
     * javaagent
     */
    @IHubProperty
    Property<String> getJavaagent()

    /**
     * 分类
     */
    @IHubProperty(defaultValue = 'all')
    Property<String> getClassifier()

}
