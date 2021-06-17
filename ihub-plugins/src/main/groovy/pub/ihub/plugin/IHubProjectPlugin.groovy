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
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskProvider

import java.lang.reflect.ParameterizedType

import static groovy.transform.TypeCheckingMode.SKIP
import static java.lang.Class.forName
import static org.gradle.internal.Actions.doNothing
import static pub.ihub.plugin.IHubProjectPlugin.EvaluateStage.AFTER
import static pub.ihub.plugin.IHubProjectPlugin.EvaluateStage.BEFORE



/**
 * IHub项目插件
 * @author henry
 */
@CompileStatic
abstract class IHubProjectPlugin<T extends IHubProjectExtension> implements Plugin<Project> {

    // TODO protected
    Project project
    protected T extension
    private final List<Closure> beforeEvaluateClosure = []
    private final List<Closure> afterEvaluateClosure = []

    @Override
    void apply(Project project) {
        this.project = project
        applyPlugin beforeApplyPlugins
        extension = extensionName?.with { project.extensions.create it, extensionClass, project }
        apply()
        beforeEvaluateClosure.each {
            project.beforeEvaluate it
        }
        afterEvaluateClosure.each {
            project.afterEvaluate it
        }
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
     * 前置应用插件
     * @return 应用插件
     */
    protected Class<Plugin<Project>>[] getBeforeApplyPlugins() {
        [] as Class<Plugin<Project>>[]
    }

    /**
     * 扩展名
     * @return 扩展名
     */
    protected String getExtensionName() {
    }

    /**
     * 应用
     */
    protected abstract void apply()

    protected Project getRootProject() {
        project.rootProject
    }

    protected Gradle getGradle() {
        project.gradle
    }

    /**
     * 配置前执行闭包
     * @param closure 执行闭包
     */
    protected void beforeEvaluate(Closure closure) {
        beforeEvaluateClosure << closure
    }

    /**
     * 配置后执行闭包
     * @param closure 执行闭包
     */
    protected void afterEvaluate(Closure closure) {
        afterEvaluateClosure << closure
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

    private Class<T> getExtensionClass() {
        forName((this.class.genericSuperclass as ParameterizedType).actualTypeArguments.first().typeName) as Class<T>
    }

    enum EvaluateStage {

        AFTER, BEFORE

    }

}
