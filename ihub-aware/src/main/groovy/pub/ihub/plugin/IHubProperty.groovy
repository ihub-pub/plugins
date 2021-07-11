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

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static groovy.transform.TypeCheckingMode.SKIP
import static java.lang.annotation.ElementType.FIELD
import static java.lang.annotation.RetentionPolicy.RUNTIME
import static pub.ihub.plugin.IHubProperty.Type.PROJECT



/**
 * IHub属性
 * @author henry
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@CompileStatic
@interface IHubProperty {

    /**
     * 属性名称（默认原属性名称）
     * @return 名称
     */
    String value() default ''

    /**
     * 属性类型
     * @return 类型
     */
    @CompileStatic(SKIP)
    Type[] type() default [PROJECT]

    enum Type {

        PROJECT, SYSTEM, ENV

    }

}
