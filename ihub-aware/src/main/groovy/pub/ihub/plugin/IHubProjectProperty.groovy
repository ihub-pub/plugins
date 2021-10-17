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
import java.lang.reflect.Method

import static pub.ihub.plugin.IHubProperty.Type.ENV
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM



/**
 * IHub项目属性
 * @author henry
 */
trait IHubProjectProperty {

    abstract Object findProjectProperty(String key)

    Object getProperty(String name) {
        IHubProperty iHubProperty
        Class<?> fieldType
        Field field = getClass().declaredFields.find { f -> f.name == name }
        if (field) {
            iHubProperty = field.getAnnotation IHubProperty
            fieldType = field.type
        } else {
            Method method = getClass().getMethod(getMethodName(name))
            iHubProperty = method.getAnnotation IHubProperty
            fieldType = method.returnType
        }
        String fieldName = getClass().getAnnotation(IHubExtension).value() + '.' + (iHubProperty?.value() ?: name)
        IHubProperty.Type[] propertyType = iHubProperty?.type() ?: [] as IHubProperty.Type[]
        // 优先从系统属性和项目属性获取，环境属性多用于敏感信息配置
        Object value = null
        // 获取系统属性
        if (propertyType.contains(SYSTEM)) {
            value = System.getProperty(fieldName)?.replaceAll '\\\\n', '\n'
        }
        // 获取环境属性
        if (null == value && propertyType.contains(ENV)) {
            value = System.getenv(name.replaceAll(/([A-Z])/, '_$1').toUpperCase())?.replaceAll '\\\\n', '\n'
        }
        // 获取项目属性
        if (null == value && propertyType) {
            value = findProjectProperty fieldName
        }
        // 获取扩展属性
        if (null == value) {
            value = invokeGetMethod name
        }
        if (null == value) {
            return value
        }
        'false' == value.toString() ? false : fieldType.primitive && value.toString().integer ?
            value.toString().toInteger() : value.asType(fieldType)
    }

    private Object invokeGetMethod(String name) {
        invokeMethod getMethodName(name), null
    }

    private String getMethodName(String name) {
        name.replaceFirst(/([a-z])/, '$1_').split('_')
            .with { "get${it[0].toUpperCase()}${it[1]}" }
    }

}
