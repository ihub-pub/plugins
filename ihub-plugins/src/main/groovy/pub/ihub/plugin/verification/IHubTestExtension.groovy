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

import groovy.transform.TupleConstructor
import pub.ihub.plugin.IHubProjectExtension

/**
 * 测试插件扩展
 * @author henry
 */
@TupleConstructor(includeSuperFields = true)
class IHubTestExtension extends IHubProjectExtension {

	/**
	 * 启用测试
	 */
	boolean enabled = true
	/**
	 * 包含属性名称（“,”分割）
	 */
	String classes = ''
	/**
	 * 每跑100个测试类后重启fork进程
	 */
	int forkEvery = 100
	/**
	 * 最多启动进程数
	 */
	int maxParallelForks = 1
	/**
	 * 包含属性名称（“,”分割）
	 */
	String runIncludePropNames = ''
	/**
	 * 启用本地属性
	 */
	boolean enabledLocalProperties = false
	/**
	 * 启用测试调试
	 */
	boolean debug = false
	/**
	 * 只要有一个测试失败就停止测试
	 */
	boolean failFast = false

	boolean getEnabled() {
		findSystemProperty 'iHubTestEnabled', enabled
	}

	String getClasses() {
		findSystemProperty 'iHubTestClasses', classes
	}

	int getForkEvery() {
		findSystemProperty 'iHubTestForkEvery', forkEvery
	}

	int getMaxParallelForks() {
		findSystemProperty 'iHubTestMaxParallelForks', maxParallelForks
	}

	@Override
	protected String getRunIncludePropNames() {
		findSystemProperty 'iHubTestRunIncludePropNames', runIncludePropNames
	}

	boolean getDebug() {
		findSystemProperty 'iHubTestDebug', debug
	}

	boolean getFailFast() {
		findSystemProperty 'iHubTestFailFast', failFast
	}

}
