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


import org.gradle.api.Plugin
import org.gradle.api.Project

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.TYPE
import static java.lang.annotation.RetentionPolicy.RUNTIME



/**
 * IHub插件
 * @author henry
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@interface IHubPlugin {

    /**
     * 插件扩展类型
     * @return 扩展类型
     */
    Class<? extends IHubProjectExtensionAware> value() default {
    }

    /**
     * 前置应用插件
     * @return 插件
     */
    Class<Plugin<Project>>[] beforeApplyPlugins() default []

}
