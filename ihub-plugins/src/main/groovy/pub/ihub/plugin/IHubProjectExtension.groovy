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

import org.gradle.api.Project
import org.gradle.process.JavaForkOptions



/**
 * IHub项目扩展
 * @author liheng
 */
class IHubProjectExtension implements IHubExtension {

    protected Project project

    @Override
    String findProjectProperty(String key) {
        project.findProperty key
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

    /**
     * 任务运行时属性
     * @return 任务运行时属性
     */
    protected Map<String, String> getRunProperties() {
        [:]
    }

    /**
     * 包含属性名称（“,”分割）
     * @return 包含属性名称
     */
    protected String getRunIncludePropNames() {
        null
    }

    /**
     * 排除属性名称（“,”分割）
     * @return 排除属性名称
     */
    protected String getRunSkippedPropNames() {
        null
    }

    /**
     * 启用本地属性
     * @return 启用本地属性
     */
    protected boolean getEnabledLocalProperties() {
        false
    }

    /**
     * 获取本地Java属性配置
     * @return 本地Java属性
     */
    protected Map<String, Object> getLocalProperties() {
        project.rootProject.file('.java-local.properties').with {
            exists() ? withInputStream { is ->
                new Properties().tap { load(is) }
            } : [:]
        } as Map
    }

    void systemProperties(JavaForkOptions task) {
        task.systemProperties System.properties.subMap(runIncludePropNames?.split(',') ?: []) ?: runProperties
        runSkippedPropNames?.split(',')?.each {
            task.systemProperties.remove it
        }
        if (enabledLocalProperties) {
            localProperties.each { k, v ->
                task.systemProperties.putIfAbsent k, v
            }
        }
    }

}
