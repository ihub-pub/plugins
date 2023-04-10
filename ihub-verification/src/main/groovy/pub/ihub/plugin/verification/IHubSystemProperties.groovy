/*
 * Copyright (c) 2021-2023 the original author or authors.
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
package pub.ihub.plugin.verification

import org.gradle.api.Project
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
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
    abstract MapProperty<String, String> getRunProperties()

    /**
     * 包含属性名称（“,”分割,支持通配符“*”）
     * @return 包含属性名称
     */
    abstract Property<String> getRunIncludePropNames()

    /**
     * 排除属性名称（“,”分割,支持通配符“*”）
     * @return 排除属性名称
     */
    abstract Property<String> getRunSkippedPropNames()

    /**
     * 启用本地属性
     * @return 启用本地属性
     */
    abstract Property<Boolean> getEnabledLocalProperties()

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
        if (runIncludePropNames.present) {
            runIncludePropNames.get().replaceAll(',', '|').replaceAll('\\*', '.*').with { regex ->
                task.systemProperties System.properties.findAll { it.key ==~ regex }
            }
        } else {
            task.systemProperties runProperties.getOrElse([:])
        }
        if (runSkippedPropNames.present) {
            runSkippedPropNames.get().replaceAll(',', '|').replaceAll('\\*', '.*').with { regex ->
                task.systemProperties.removeAll { it.key ==~ regex }
            }
        }
        if (enabledLocalProperties.get()) {
            (localProperties + getLocalProperties(propertiesName)).each { k, v ->
                task.systemProperties.putIfAbsent k, v
            }
        }
    }

}
