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
package pub.ihub.plugin.verification

import org.gradle.api.Project
import pub.ihub.plugin.IHubExtension

/**
 * 测试插件扩展
 * @author henry
 */
class IHubTestExtension implements IHubExtension {

	/**
	 * 包含属性名称（“,”分割）
	 */
	String testClasses = ''
	/**
	 * 每跑100个测试类后重启fork进程
	 */
	int testForkEvery = 100
	/**
	 * 最多启动进程数
	 */
	int testMaxParallelForks = 2

	IHubTestExtension(Project project) {
		pub_ihub_plugin_IHubExtension__project = project
	}

	String getTestClasses() {
		findEnvProperty 'test.classes', testClasses
	}

	int getTestForkEvery() {
		findEnvProperty('test.forkEvery', testForkEvery.toString()).toInteger()
	}

	int getTestMaxParallelForks() {
		findEnvProperty('test.maxParallelForks', testMaxParallelForks.toString()).toInteger()
	}

}
