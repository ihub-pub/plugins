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
package pub.ihub.plugin.githooks

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

/**
 * 校验当前 .git/COMMIT_EDITMSG 是否符合约定式提交规范。
 *
 * <p>设计要点：</p>
 * <ul>
 *     <li>commitMsgFile 通过 {@link RegularFileProperty} + {@code @InputFile} 声明，
 *         彻底避免 Configuration Cache 阶段对 {@code Project} 的引用。</li>
 *     <li>校验逻辑委派给 {@link IHubGitHooksExtension}（运行时通过 {@code @Internal} 注入），
 *         保留原有的 DSL 与默认规则不变。</li>
 * </ul>
 *
 * @author henry
 * @since 1.9.5
 */
@CompileStatic
@DisableCachingByDefault(because = '依赖运行时 .git/COMMIT_EDITMSG，不适合构建缓存')
abstract class IHubCommitCheckTask extends DefaultTask {

    /**
     * COMMIT_EDITMSG 文件位置；典型为 {@code .git/COMMIT_EDITMSG}。
     */
    @InputFile
    @PathSensitive(PathSensitivity.NONE)
    @Optional
    abstract RegularFileProperty getCommitMsgFile()

    /**
     * 校验规则的扩展实例。{@code @Internal} 因为它是 plugin 的内部 DSL 容器，
     * 校验规则在 task action 中显式取出 footers/types 列表使用，不参与 up-to-date 计算。
     */
    @Internal
    abstract IHubGitHooksExtension getExtension()

    abstract void setExtension(IHubGitHooksExtension extension)

    /**
     * 当 {@code .git/COMMIT_EDITMSG} 不存在时回退到的展示路径。
     * 仅用于错误信息，不影响 up-to-date 检查（{@code @Internal}）。
     */
    @Internal
    abstract org.gradle.api.provider.Property<String> getMissingFileHint()

    @TaskAction
    void check() {
        File file = commitMsgFile.orNull?.asFile
        if (file == null || !file.exists()) {
            String hint = file?.toURI()?.toString() ?: missingFileHint.getOrElse('<unset>')
            logger.warn 'Not found file: {}', hint
            throw new org.gradle.api.GradleException('Not found file: ' + hint)
        }

        List<String> lines = file.readLines()
        IHubGitHooksExtension.assertRule !lines.empty, 'Commit msg is empty!'

        logger.lifecycle 'Extract commit msg:'
        logger.lifecycle '---------------------------------------------'
        lines.each { logger.lifecycle it }
        logger.lifecycle '---------------------------------------------'

        String header = lines.first()
        Map footers = lines.findAll {
            it ==~ /^(${extension.footers*.name.join('|')}): .+/
        }.collectEntries { it.split ': ' }

        // 信息头整体格式检查
        List<String> headerParts = extension.checkHeader(header)
        String type = headerParts[0]
        String scope = headerParts[1]
        // 范围检查
        extension.types.find { it.name == type }.checkScope scope
        extension.footers.each {
            String footerValue = footers[it.name]
            it.checkRequired footerValue
            it.checkRequiredWithType type, footerValue
            it.checkValue footerValue
        }
    }

}
