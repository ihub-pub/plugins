/*
 * Copyright (c) 2022-2026 the original author or authors.
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
package pub.ihub.plugin.version

import groovy.transform.CompileStatic
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.process.ExecOperations

import javax.inject.Inject

/**
 * Configuration-Cache 兼容地获取 {@code git describe --tags} 输出。
 *
 * <p>Gradle 自 7.5+ 起将 {@link ValueSource} 视为 CC 安全的外部进程入口，
 * 避免在配置阶段直接调用 {@code "...".execute()}。</p>
 *
 * @author henry
 * @since 1.9.5
 */
@CompileStatic
abstract class GitDescribeValueSource implements ValueSource<String, ValueSourceParameters.None> {

    @Inject
    abstract ExecOperations getExecOperations()

    @Override
    String obtain() {
        try {
            def stdout = new ByteArrayOutputStream()
            execOperations.exec { spec ->
                spec.commandLine 'git', 'describe', '--tags'
                spec.standardOutput = stdout
                spec.errorOutput = new ByteArrayOutputStream()
                spec.setIgnoreExitValue(true)
            }
            String tag = stdout.toString().trim()
            tag ?: null
        } catch (e) {
            null
        }
    }

}
