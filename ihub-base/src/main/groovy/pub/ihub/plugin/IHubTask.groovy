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
package pub.ihub.plugin

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.TYPE
import static java.lang.annotation.RetentionPolicy.RUNTIME

/**
 * IHub任务
 * @author henry
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@interface IHubTask {

    /**
     * 任务名称
     * @return 名称
     */
    String value()

    /**
     * 任务分组
     * @return 分组
     */
    String group() default 'ihub'

    /**
     * 任务描述
     * @return 描述
     */
    String description() default ''

    /**
     * 任务依赖
     * @return 依赖
     */
    String[] dependsOn() default []

    /**
     * 必须在前置任务之后运行
     * @return 前置任务
     */
    String[] mustRunAfter() default []

    /**
     * 应该在前置任务之后运行
     * @return 前置任务
     */
    String[] shouldRunAfter() default []

    /**
     * 任务完成后运行
     * @return 后置任务
     */
    String[] finalizedBy() default []

}
