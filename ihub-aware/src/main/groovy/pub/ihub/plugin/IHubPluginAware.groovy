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

import static pub.ihub.plugin.IHubPluginAware.EvaluateStage.AFTER
import static pub.ihub.plugin.IHubPluginAware.EvaluateStage.BEFORE

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * IHub插件特征
 * @author liheng
 */
trait IHubPluginAware<T extends IHubExtension> implements Plugin<Project> {

	T createExtension(Project project, String extName, Class<T> clazz, EvaluateStage evaluate, Action<T> action) {
		project.extensions.create(extName, clazz, project).tap {
			executeAction project, it, evaluate, action
		}
	}

	T createExtension(Project project, String extName, Class<T> clazz, Action<T> action = null) {
		createExtension project, extName, clazz, null, action
	}

	def <E> E getExtension(Project project, Class<E> clazz, EvaluateStage evaluate, Action<E> action) {
		project.extensions.getByType(clazz).tap {
			executeAction project, it, evaluate, action
		}
	}

	def <E> E getExtension(Project project, Class<E> clazz, Action<E> action = null) {
		getExtension project, clazz, null, action
	}

	private <E> void executeAction(Project project, E extension, EvaluateStage evaluate, Action<E> action) {
		if (action && AFTER == evaluate) {
			project.afterEvaluate {
				action.execute extension
			}
		} else if (action && BEFORE == evaluate) {
			project.beforeEvaluate {
				action.execute extension
			}
		}
	}

	enum EvaluateStage {

		AFTER, BEFORE

	}

}
