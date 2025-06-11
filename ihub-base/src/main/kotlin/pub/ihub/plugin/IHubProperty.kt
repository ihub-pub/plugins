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

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.Target
import kotlin.reflect.KClass

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FUNCTION

/**
 * IHub属性
 * @author henry
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD, FUNCTION)
annotation class IHubProperty(
    /**
     * 属性名称（默认原属性名称）
     * @return 名称
     */
    val value: String = "",

    /**
     * 属性默认值
     * @return 默认值
     */
    val defaultValue: String = "",

    /**
     * 泛型类型
     * @return 泛型类型
     */
    val genericType: KClass<*> = String::class,

    /**
     * 属性类型
     * @return 类型
     */
    val type: Array<Type> = [Type.PROJECT]
) {
    enum class Type {
        PROJECT, SYSTEM, ENV
    }
}
