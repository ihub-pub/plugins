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

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project

import static pub.ihub.plugin.IHubPluginAware.EvaluateStage.AFTER
import static pub.ihub.plugin.IHubPluginAware.EvaluateStage.BEFORE



/**
 * IHub项目插件特征
 * @author henry
 */
@SuppressWarnings('FactoryMethodName')
trait IHubPluginAware<T extends IHubProjectExtension> implements Plugin<Project> {

    T createExtension(Project project, String extName, Class<T> clazz, Action<T> action = null) {
        project.extensions.create(extName, clazz, project).tap {
            action?.execute it
        }
    }

    def <E> E getExtension(Project project, Class<E> clazz, Action<E> action = null) {
        project.extensions.getByType(clazz).tap {
            action?.execute it
        }
    }

    T createExtension(Project project, String extName, Class<T> clazz, EvaluateStage evaluate, Action<T> action) {
        createExtension project, extName, clazz, getAction(project, evaluate, action)
    }

    def <E> E getExtension(Project project, Class<E> clazz, EvaluateStage evaluate, Action<E> action) {
        getExtension project, clazz, getAction(project, evaluate, action)
    }

    private <E> Action<E> getAction(Project project, EvaluateStage evaluate, Action<E> action) {
        { E extension ->
            if (action && AFTER == evaluate) {
                project.afterEvaluate {
                    action.execute extension
                }
            } else if (action && BEFORE == evaluate) {
                project.beforeEvaluate {
                    action.execute extension
                }
            } else {
                action.execute extension
            }
        }
    }

    enum EvaluateStage {

        AFTER, BEFORE

    }

}
