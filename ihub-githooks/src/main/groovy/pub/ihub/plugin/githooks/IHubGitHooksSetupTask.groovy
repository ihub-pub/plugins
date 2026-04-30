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
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

import javax.inject.Inject

/**
 * 在执行阶段调用 {@code git config core.hooksPath ...}，
 * 替代原本在配置阶段调用的写法，从而让 Configuration Cache 兼容。
 *
 * @author henry
 * @since 1.9.5
 */
@CompileStatic
@DisableCachingByDefault(because = '直接调用 git config，无需缓存')
abstract class IHubGitHooksSetupTask extends DefaultTask {

    @Input
    @Optional
    abstract Property<String> getHooksPath()

    @Input
    @Optional
    abstract MapProperty<String, String> getHooks()

    /**
     * 由插件注入的扩展引用，用于在执行阶段写出 hook 脚本。
     * 仅参与 task action，不影响 up-to-date。
     */
    @Internal
    abstract IHubGitHooksExtension getExtension()

    abstract void setExtension(IHubGitHooksExtension extension)

    @Inject
    abstract ProviderFactory getProviders()

    @TaskAction
    void setup() {
        String customPath = hooksPath.orNull
        Map<String, String> hookMap = hooks.getOrElse([:])

        if (customPath) {
            logger.lifecycle 'Set git hooks path: ' + customPath
            runGitConfig 'core.hooksPath', customPath
        } else if (hookMap) {
            extension.writeHooks()
            logger.lifecycle 'Set git hooks path: .gradle/pub.ihub.plugin.hooks'
            runGitConfig 'core.hooksPath', '.gradle/pub.ihub.plugin.hooks'
        } else {
            logger.lifecycle 'Unset git hooks path, learn more see https://doc.ihub.pub/plugins/iHubGitHooks'
            runGitConfigUnset 'core.hooksPath'
        }
    }

    private void runGitConfig(String key, String value) {
        try {
            providers.exec { spec ->
                spec.commandLine 'git', 'config', key, value
                spec.setIgnoreExitValue(true)
            }.result.get()
        } catch (e) {
            logger.lifecycle 'Git hooks config fail: ' + e.message
        }
    }

    private void runGitConfigUnset(String key) {
        try {
            providers.exec { spec ->
                spec.commandLine 'git', 'config', '--unset', key
                spec.setIgnoreExitValue(true)
            }.result.get()
        } catch (e) {
            logger.lifecycle 'Git hooks config fail: ' + e.message
        }
    }

}
