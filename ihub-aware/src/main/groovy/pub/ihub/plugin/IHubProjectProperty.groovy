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

import java.lang.reflect.Field

import static pub.ihub.plugin.IHubProperty.Type.ENV
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM



/**
 * IHub项目属性
 * @author henry
 */
trait IHubProjectProperty {

    abstract Object findProjectProperty(String key)

    Object getProperty(String name) {
        Field field = getClass().declaredFields.find { f -> f.name == name }
        if (!field) {
            return invokeGetMethod(name)
        }
        IHubProperty iHubProperty = field.getAnnotation IHubProperty
        String fieldName = getClass().getAnnotation(IHubExtension).value() + '.' + (iHubProperty?.value() ?: name)
        // 优先从系统属性和项目属性获取，环境属性多用于敏感信息配置
        Object value = null
        // 获取系统属性
        if (iHubProperty?.type()?.contains(SYSTEM)) {
            value = System.getProperty fieldName
        }
        // 获取环境属性
        if (!value && iHubProperty?.type()?.contains(ENV)) {
            value = System.getenv name.replaceAll(/([A-Z])/, '_$1').toUpperCase()
        }
        // 获取项目属性
        if (!value && iHubProperty?.type()) {
            value = findProjectProperty fieldName
        }
        // 获取扩展属性
        value = value ?: invokeGetMethod(name)
        if (!value) {
            return value
        }
        'false' == value.toString() ? false : field.type.primitive && value.toString().integer ?
            value.toString().toInteger() : value.asType(field.type)
    }

    private Object invokeGetMethod(String name) {
        invokeMethod name.replaceFirst(/([a-z])/, '$1_').split('_')
            .with { "get${it[0].toUpperCase()}${it[1]}" }, null
    }

}
