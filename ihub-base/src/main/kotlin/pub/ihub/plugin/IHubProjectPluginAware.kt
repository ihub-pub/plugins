/*
 * Copyright (c) 2021-2024 the original author or authors.
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

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.TaskProvider
import pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER
import pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.BEFORE
import pub.ihub.plugin.IHubProperty.Type.ENV
import pub.ihub.plugin.IHubProperty.Type.PROJECT
import pub.ihub.plugin.IHubProperty.Type.SYSTEM
import java.lang.reflect.Method
import kotlin.reflect.KClass
import org.gradle.internal.Actions

/**
 * IHub项目插件
 * @author henry
 */
@Suppress("UnnecessaryAbstractClass")
abstract class IHubProjectPluginAware<T : IHubExtensionAware> : Plugin<Project> {

    lateinit var project: Project
    lateinit var extension: T

    override fun apply(project: Project) {
        this.project = project
        val iHubPlugin = this::class.java.getAnnotation(IHubPlugin::class.java)
        applyPlugin(*iHubPlugin.beforeApplyPlugins.map { it.java as Class<Plugin<Project>> }.toTypedArray())

        val extensionClass = iHubPlugin.value.java as Class<T>
        val annotation = extensionClass.getAnnotation(IHubExtension::class.java)
        if (annotation != null) {
            extension = project.extensions.create(annotation.value, extensionClass)
            if (extensionClass.isInterface) {
                extensionClass.declaredMethods.forEach { method ->
                    method.getAnnotation(IHubProperty::class.java)?.let {
                        configExtensionProperty(
                            it, method.name.substring(3, 4).lowercase() + method.name.substring(4),
                            annotation.value, method.invoke(extension) as Property<*>
                        )
                    }
                }
            }
            if (extension is IHubProjectExtensionAware) {
                (extension as IHubProjectExtensionAware).project = project
                extensionClass.declaredFields.forEach { field ->
                    field.getAnnotation(IHubProperty::class.java)?.let {
                        field.isAccessible = true
                        configExtensionProperty(
                            it, field.name, annotation.value, field.get(extension) as Property<*>
                        )
                    }
                }
            }
        }

        iHubPlugin.tasks.forEach { taskClass ->
            val taskAnnotation = taskClass.java.getAnnotation(IHubTask::class.java)
            registerTask(taskAnnotation.value, taskClass.java) {
                it.group = taskAnnotation.group
                it.description = taskAnnotation.description
                it.dependsOn(*taskAnnotation.dependsOn)
                it.mustRunAfter(*taskAnnotation.mustRunAfter)
                it.shouldRunAfter(*taskAnnotation.shouldRunAfter)
                it.finalizedBy(*taskAnnotation.finalizedBy)
            }
        }
        apply()
    }

    /**
     * 包含插件
     * @param id 插件ID
     * @return 是否包含
     */
    protected fun hasPlugin(id: String): Boolean {
        return project.plugins.hasPlugin(id)
    }

    /**
     * 包含插件
     * @param type 插件类型
     * @return 是否包含
     */
    protected fun hasPlugin(type: Class<out Plugin<*>>): Boolean {
        return project.plugins.hasPlugin(type)
    }

    /**
     * 应用插件
     * @param classes 插件
     */
    protected fun applyPlugin(vararg classes: Class<Plugin<Project>>) {
        classes.forEach { project.pluginManager.apply(it) }
    }

    /**
     * 应用
     */
    protected abstract fun apply()

    protected val logger: Logger
        get() = project.logger

    protected val ihub: Any?
        get() = project.findProperty("ihub")

    /**
     * 配置前执行闭包
     * @param closure 执行闭包
     */
    protected fun beforeEvaluate(closure: Action<Project>) {
        project.beforeEvaluate(closure)
    }

    /**
     * 配置后执行闭包
     * @param closure 执行闭包
     */
    protected fun afterEvaluate(closure: Action<Project>) {
        project.afterEvaluate(closure)
    }

    protected fun hasTask(name: String): Boolean {
        return project.tasks.findByName(name) != null
    }

    protected fun registerTask(name: String, action: Action<Task>): TaskProvider<Task> {
        return project.tasks.register(name, action)
    }

    protected fun <U : Task> registerTask(name: String, taskClass: Class<U>, action: Action<in U>): TaskProvider<U> {
        return project.tasks.register(name, taskClass, action)
    }

    protected fun <U : Task> withTask(taskClass: Class<U>, action: Action<in U>) {
        project.tasks.withType(taskClass, action)
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <U : Task> withTask(name: String, action: Action<in U>? = null): U {
        return project.tasks.getByName(name, action ?: Actions.doNothing()) as U
    }

    protected fun withExtension(stage: EvaluateStage? = null, action: Action<T>) {
        executeAction(extension, stage, action)
    }

    protected fun <E> getExtension(extensionClass: Class<E>): E {
        return project.extensions.getByType(extensionClass)
    }

    protected fun <E> withExtension(extensionClass: Class<E>, stage: EvaluateStage? = null, action: Action<E>) {
        executeAction(getExtension(extensionClass), stage, action)
    }

    private fun <E> executeAction(param: E, stage: EvaluateStage?, action: Action<E>) {
        when (stage) {
            BEFORE -> beforeEvaluate { action.execute(param) }
            AFTER -> afterEvaluate { action.execute(param) }
            else -> action.execute(param)
        }
    }

    private fun setExtensionProperty(property: Property<*>, type: Class<*>, value: Provider<String>) {
        if (value.isPresent) {
            setExtensionProperty(property, type, value.get().replace("\\n", "\n"))
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setExtensionProperty(property: Property<*>, type: Class<*>, value: Any?) {
        if (value != null) {
            val stringValue = value.toString()
            val typedValue = when {
                type.isAssignableFrom(Boolean::class.java) && "false" == stringValue -> false
                else -> stringValue.asType(type)
            }
            (property as Property<Any>).set(typedValue)
        }
    }

    private fun Any.asType(type: Class<*>): Any {
        return when (type) {
            String::class.java -> this.toString()
            Int::class.java, Integer::class.java -> this.toString().toInt()
            Long::class.java, java.lang.Long::class.java -> this.toString().toLong()
            Double::class.java, java.lang.Double::class.java -> this.toString().toDouble()
            Float::class.java, java.lang.Float::class.java -> this.toString().toFloat()
            Boolean::class.java, java.lang.Boolean::class.java -> this.toString().toBoolean()
            // Add other type conversions as needed
            else -> this
        }
    }


    private fun configExtensionProperty(
        iHubProperty: IHubProperty,
        name: String,
        extName: String,
        property: Property<*>
    ) {
        val fieldName = iHubProperty.value.ifEmpty { name }
        val propertyName = "$extName.$fieldName"

        // 设置默认值
        if (iHubProperty.defaultValue.isNotEmpty()) {
            setExtensionProperty(property, iHubProperty.genericType.java, iHubProperty.defaultValue)
        }

        // 获取项目属性
        if (iHubProperty.type.contains(PROJECT)) {
            val projectPropertyValue = project.findProperty(propertyName)
            if (projectPropertyValue != null) {
                setExtensionProperty(property, iHubProperty.genericType.java, projectPropertyValue)
            }
        }

        // 环境配置、系统配置要在项目扩展配置后执行，确保优先级高于扩展配置
        afterEvaluate {
            val providers: ProviderFactory = project.providers
            // 获取环境属性
            if (iHubProperty.type.contains(ENV)) {
                val envVarName = fieldName.replace(Regex("([A-Z])"), "_$1").uppercase()
                setExtensionProperty(property, iHubProperty.genericType.java, providers.environmentVariable(envVarName))
            }
            // 获取系统属性
            if (iHubProperty.type.contains(SYSTEM)) {
                setExtensionProperty(property, iHubProperty.genericType.java, providers.systemProperty(propertyName))
            }
        }
    }

    enum class EvaluateStage {
        AFTER, BEFORE
    }
}
