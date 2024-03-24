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

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.TaskProvider

import static groovy.transform.TypeCheckingMode.SKIP
import static org.gradle.internal.Actions.doNothing
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.BEFORE
import static pub.ihub.plugin.IHubProperty.Type.ENV
import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM

/**
 * IHub项目插件
 * @author henry
 */
@CompileStatic
@SuppressWarnings('UnnecessaryObjectReferences')
abstract class IHubProjectPluginAware<T extends IHubExtensionAware> implements Plugin<Project> {

    Project project
    T extension

    @Override
    void apply(Project project) {
        this.project = project
        IHubPlugin iHubPlugin = this.class.getAnnotation IHubPlugin
        applyPlugin iHubPlugin.beforeApplyPlugins()

        Class<T> extensionClass = iHubPlugin.value() as Class<T>
        IHubExtension annotation = extensionClass.getAnnotation IHubExtension
        if (annotation) {
            extension = project.extensions.create annotation.value(), extensionClass
            if (extensionClass.interface) {
                extensionClass.declaredMethods.each { method ->
                    method.getAnnotation(IHubProperty)?.with {
                        configExtensionProperty it, method.name.with {
                            substring(3, 4).toLowerCase() + substring(4)
                        }, annotation.value(), (Property) method.invoke(extension)
                    }
                }
            }
            if (extension instanceof IHubProjectExtensionAware) {
                extension.project = project
                extensionClass.declaredFields.each { field ->
                    field.getAnnotation(IHubProperty)?.with {
                        configExtensionProperty it, field.name, annotation.value(), (Property) extension[field.name]
                    }
                }
            }
        }

        iHubPlugin.tasks().each {
            IHubTask task = it.getAnnotation IHubTask
            registerTask task.value(), it, {
                it.group = task.group()
                it.description = task.description()
                it.dependsOn task.dependsOn()
                it.mustRunAfter task.mustRunAfter()
                it.shouldRunAfter task.shouldRunAfter()
                it.finalizedBy task.finalizedBy()
            }
        }

        apply()
    }

    /**
     * 包含插件
     * @param id 插件ID
     * @return 是否包含
     */
    protected boolean hasPlugin(String id) {
        project.plugins.hasPlugin id
    }

    /**
     * 包含插件
     * @param type 插件类型
     * @return 是否包含
     */
    protected boolean hasPlugin(Class<? extends Plugin> type) {
        project.plugins.hasPlugin type
    }

    /**
     * 应用插件
     * @param classes 插件
     */
    protected void applyPlugin(Class<Plugin<Project>>... classes) {
        classes.each {
            project.pluginManager.apply it
        }
    }

    /**
     * 应用
     */
    protected abstract void apply()

    protected Logger getLogger() {
        project.logger
    }

    protected getIhub() {
        project.findProperty 'ihub'
    }

    /**
     * 配置前执行闭包
     * @param closure 执行闭包
     */
    protected void beforeEvaluate(Closure closure) {
        project.beforeEvaluate closure
    }

    /**
     * 配置后执行闭包
     * @param closure 执行闭包
     */
    protected void afterEvaluate(Closure closure) {
        project.afterEvaluate closure
    }

    protected boolean hasTask(String name) {
        project.tasks.findByName name
    }

    @CompileStatic(SKIP)
    protected TaskProvider<Task> registerTask(String name, Action<Task> action) {
        project.tasks.register name, action
    }

    @CompileStatic(SKIP)
    protected <T extends Task> TaskProvider<T> registerTask(String name, Class<T> taskClass, Action<? super T> action) {
        project.tasks.register name, taskClass, action
    }

    @CompileStatic(SKIP)
    protected <T extends Task> void withTask(Class<T> taskClass, Action<? super T> action) {
        project.tasks.withType taskClass, action
    }

    @CompileStatic(SKIP)
    protected <T extends Task> T withTask(String name, Action<? super T> action = null) {
        project.tasks.getByName(name, action ?: doNothing()) as T
    }

    protected void withExtension(EvaluateStage stage = null, Action<T> action) {
        executeAction extension, stage, action
    }

    protected <E> E withExtension(Class<E> extensionClass) {
        project.extensions.getByType(extensionClass)
    }

    protected <E> void withExtension(Class<E> extensionClass, EvaluateStage stage = null, Action<E> action) {
        executeAction withExtension(extensionClass), stage, action
    }

    private <E> void executeAction(E param, EvaluateStage stage, Action<E> action) {
        if (BEFORE == stage) {
            beforeEvaluate { action.execute param }
        } else if (AFTER == stage) {
            afterEvaluate { action.execute param }
        } else {
            action.execute param
        }
    }

    private static void setExtensionProperty(Property property, Class type, Provider<String> value) {
        if (value.present) {
            setExtensionProperty property, type, value.get().replaceAll('\\\\n', '\n')
        }
    }

    private static void setExtensionProperty(Property property, Class type, value) {
        if (value) {
            property.set value.toString().with {
                type.isAssignableFrom(Boolean) && 'false' == it ? false : asType(type)
            }
        }
    }

    private void configExtensionProperty(IHubProperty iHubProperty, String name, String extName, Property property) {
        // 优先从系统属性和项目属性获取，环境属性多用于敏感信息配置
        iHubProperty.with {
            String fieldName = value() ?: name
            String propertyName = extName + '.' + fieldName
            // 设置默认值
            if (defaultValue()) {
                setExtensionProperty property, genericType(), defaultValue()
            }
            // 获取项目属性
            if (type().contains(PROJECT)) {
                setExtensionProperty property, genericType(), project.findProperty(propertyName)
            }
            // 环境配置、系统配置要在项目扩展配置后执行，确保优先级高于扩展配置
            afterEvaluate {
                ProviderFactory providers = project.providers
                // 获取环境属性
                if (type().contains(ENV)) {
                    setExtensionProperty property, genericType(), providers
                        .environmentVariable(fieldName.replaceAll(/([A-Z])/, '_$1').toUpperCase())
                }
                // 获取系统属性
                if (type().contains(SYSTEM)) {
                    setExtensionProperty property, genericType(), providers.systemProperty(propertyName)
                }
            }
        }
    }

    enum EvaluateStage {

        AFTER, BEFORE

    }

}
