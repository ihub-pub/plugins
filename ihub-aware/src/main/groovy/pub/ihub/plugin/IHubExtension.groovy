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

/**
 * IHub扩展特征
 * @author henry
 */
trait IHubExtension {

	abstract String findProjectProperty(String key)

	/**
	 * 查找属性
	 *
	 * 按如下优先级变换key查询：
	 * demoKey(原值) -> demo_key -> demo.key -> demo-key
	 *
	 * @param key key
	 * @param defaultValue 默认值
	 * @param closure 查询闭包
	 * @return 属性值
	 */
	String findProperty(String key, String defaultValue = null, Closure closure) {
		closure(key) ?: key.replaceAll(/([A-Z])/, '_$1').toLowerCase().with {
			closure(it) ?: closure(it.replaceAll('_', '.'))
				?: closure(it.replaceAll('_', '-'))
				?: defaultValue
		}
	}

	String findProperty(String key, String defaultValue = null) {
		findProperty(key, defaultValue) { String k -> findProjectProperty k }
	}

	boolean findProperty(String key, boolean defaultValue) {
		findProperty(key, String.valueOf(defaultValue)).toBoolean()
	}

	String findSystemProperty(String key, String defaultValue = null) {
		findProperty(key, defaultValue) { String k -> System.getProperty(k) ?: findProjectProperty(k) }
	}

	boolean findSystemProperty(String key, boolean defaultValue) {
		findSystemProperty(key, String.valueOf(defaultValue)).toBoolean()
	}

	int findSystemProperty(String key, int defaultValue) {
		findSystemProperty(key, String.valueOf(defaultValue)).toInteger()
	}

}
