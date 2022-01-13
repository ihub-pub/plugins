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
 * 系统属性扩展特征
 * @author henry
 */
trait IHubSystemProperties {

    abstract Project getProject()

    /**
     * 任务运行时属性
     * @return 任务运行时属性
     */
    abstract Map<String, String> getRunProperties()

    /**
     * 包含属性名称（“,”分割）
     * @return 包含属性名称
     */
    abstract String getRunIncludePropNames()

    /**
     * 排除属性名称（“,”分割）
     * @return 排除属性名称
     */
    abstract String getRunSkippedPropNames()

    /**
     * 启用本地属性
     * @return 启用本地属性
     */
    abstract boolean getEnabledLocalProperties()

    /**
     * 获取本地Java属性配置
     * @return 本地Java属性
     */
    Map<String, Object> getLocalProperties(String propertiesName = '.java-local.properties') {
        project.rootProject.file(propertiesName).with {
            exists() ? withInputStream { is ->
                new Properties().tap { load(is) }
            } : [:]
        } as Map
    }

    void systemProperties(JavaForkOptions task, String propertiesName) {
        task.systemProperties System.properties.subMap(runIncludePropNames?.split(',') ?: []) ?: runProperties
        runSkippedPropNames?.split(',')?.each {
            task.systemProperties.remove it
        }
        if (enabledLocalProperties) {
            (localProperties + getLocalProperties(propertiesName)).each { k, v ->
                task.systemProperties.putIfAbsent k, v
            }
        }
    }

}
