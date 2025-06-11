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
package pub.ihub.plugin.githooks

import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldNotExist
import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pub.ihub.plugin.test.IHubSpecification
import java.io.File

/**
 * @author henry
 */
@DisplayName("GitHooks测试套件")
@Suppress("TooManyFunctions", "LargeClass") // For a test class with many scenarios
class IHubGitHooksPluginTest : IHubSpecification() {

    private lateinit var commitMsgFile: File

    @BeforeEach
    override fun setup() {
        super.setup() // Call super.setup() from IHubSpecification
        buildFile.appendText("""
            plugins {
                id("pub.ihub.plugin.ihub-git-hooks")
            }
        """.trimIndent())
        File(testProjectDir, ".git").mkdirs()
        commitMsgFile = File(testProjectDir, ".git/COMMIT_EDITMSG")
        commitMsgFile.createNewFile() // Ensure it exists for most tests
    }

    @Test
    fun `GitHooks插件配置测试`() {
        // when: 使用默认目录
        var result = gradleBuilder.build()

        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
        result.output shouldContain "Unset git hooks path"

        // when: 插件扩展配置
        buildFile.appendText("""
            iHubGitHooks {
                hooks.put("pre-commit", "build")
            }
        """.trimIndent())
        result = gradleBuilder.build()

        // then: 检查结果
        File(testProjectDir, ".gradle/pub.ihub.plugin.hooks/pre-commit").shouldExist()
        result.output shouldContain "BUILD SUCCESSFUL"
        result.output shouldContain "Set git hooks path: .gradle/pub.ihub.plugin.hooks"

        // when: 自定义目录
        propertiesFile.appendText("\niHubGitHooks.hooksPath=.hooks\n")
        result = gradleBuilder.build()

        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
        result.output shouldContain "Set git hooks path: .hooks"
    }

    @Test
    fun `GitHooks插件commitCheck任务测试-Not found commit msg file`() {
        // setup: 初始化项目
        commitMsgFile.delete()

        // when: 执行任务
        val result = gradleBuilder.withArguments("commitCheck").buildAndFail()

        // then: 检查结果
        result.output shouldContain "Not found file:"
        result.output shouldContain ".git/COMMIT_EDITMSG"
    }

    @Test
    fun `GitHooks插件commitCheck任务测试-Commit msg is empty!`() {
        // commitMsgFile is empty by default from setup
        // when: 执行任务
        val result = gradleBuilder.withArguments("commitCheck").buildAndFail()

        // then: 检查结果
        result.output shouldContain "Commit msg is empty!"
    }

    @Test
    fun `GitHooks插件commitCheck任务测试-type检查失败`() {
        // setup: 初始化项目
        commitMsgFile.writeText("other(other): text")

        // when: 执行任务
        val result = gradleBuilder.withArguments("commitCheck").buildAndFail()

        // then: 检查结果
        result.output shouldContain "Commit msg header check fail!"
    }

    @Test
    fun `GitHooks插件commitCheck任务测试-description检查失败`() {
        // setup: 初始化项目
        commitMsgFile.writeText("feat: ")

        // when: description长度为0
        var result = gradleBuilder.withArguments("commitCheck").buildAndFail()

        // then: 检查结果
        result.output shouldContain "Commit msg header check fail!"

        // when: description长度为超过100
        commitMsgFile.writeText("feat: " + "-".repeat(101))
        result = gradleBuilder.withArguments("commitCheck").buildAndFail()

        // then: 检查结果
        result.output shouldContain "Commit msg header check fail!"
    }

    @Test
    fun `GitHooks插件commitCheck任务测试-scope检查`() {
        // when: 开启范围检查
        buildFile.appendText("""
            iHubGitHooks {
                type("build") {
                    it.scopes("gradle")
                    it.requiredScope(true)
                }
            }
        """.trimIndent())
        commitMsgFile.writeText("build(other): text")
        var result = gradleBuilder.withArguments("commitCheck").buildAndFail()

        // then: 检查结果
        result.output shouldContain "Commit msg header scope not in [gradle]!"

        // when: 执行任务
        commitMsgFile.writeText("build(gradle): text") // Removed overwrite = true, writeText overwrites by default
        result = gradleBuilder.withArguments("commitCheck").build()

        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
    }

    @Test
    fun `GitHooks插件commitCheck任务测试-注脚必填检查`() {
        // when: Footer必填
        buildFile.appendText("""
            iHubGitHooks {
                footer("Footer").required(true)
            }
        """.trimIndent())
        commitMsgFile.writeText("feat: text")
        var result = gradleBuilder.withArguments("commitCheck").buildAndFail()

        // then: 检查结果
        result.output shouldContain "Commit msg footer missing 'Footer'!"

        // when: 执行任务
        commitMsgFile.writeText("feat: text\n\nFooter: footer")
        result = gradleBuilder.withArguments("commitCheck").build()

        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
    }

    @Test
    fun `GitHooks插件commitCheck任务测试-注脚类型必填检查`() {
        // when: Footer必填
        buildFile.appendText("""
            iHubGitHooks {
                footer("Footer").requiredWithType("feat")
            }
        """.trimIndent())
        commitMsgFile.writeText("feat: text")
        var result = gradleBuilder.withArguments("commitCheck").buildAndFail()

        // then: 检查结果
        result.output shouldContain "Commit msg footer missing 'Footer' where type is 'feat'!"

        // when: 执行任务
        commitMsgFile.writeText("feat: text\n\nFooter: footer")
        result = gradleBuilder.withArguments("commitCheck").build()

        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
    }

    @Test
    fun `GitHooks插件commitCheck任务测试-注脚值正则校验`() {
        // when: 注解值开启校验但无注解值
        buildFile.appendText("""
            iHubGitHooks {
                footer("Closes").valueRegex("\\d+")
            }
        """.trimIndent())
        commitMsgFile.writeText("feat: text")
        var result = gradleBuilder.withArguments("commitCheck").build()

        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"

        // when: 注解值开启校验注解值错误
        commitMsgFile.writeText("feat: text\n\nCloses: abc")
        result = gradleBuilder.withArguments("commitCheck").buildAndFail()

        // then: 检查结果
        result.output shouldContain "Commit msg footer 'Closes' check fail with regex: '\\d+'!"

        // when: 注解值开启校验且注解值正确
        commitMsgFile.writeText("feat: text\n\nCloses: 123")
        result = gradleBuilder.withArguments("commitCheck").build()

        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
    }

    @Test
    fun `GitHooks插件commitCheck任务测试-生成自定义配置`() {
        // when: 非IDEA环境，没有插件目录
        var result = gradleBuilder.withArguments("-Didea.plugins.path=").build()

        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
        File(testProjectDir, ".gradle/pub.ihub.plugin.cache/conventionalCommit.json").shouldNotExist()
        File(testProjectDir, ".idea/conventionalCommit.xml").shouldNotExist()

        // when: IDEA环境，没有Conventional Commit插件
        // Using a non-existent file as plugins path for this test case
        val nonExistentPluginsPath = File(testProjectDir, "nonexistentPluginsDir").path
        result = gradleBuilder.withArguments("-Didea.plugins.path=$nonExistentPluginsPath").build()


        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
        File(testProjectDir, ".gradle/pub.ihub.plugin.cache/conventionalCommit.json").shouldNotExist()
        File(testProjectDir, ".idea/conventionalCommit.xml").shouldNotExist()

        // when: IDEA环境且有Conventional Commit插件
        val pluginsDir = File(testProjectDir, ".plugins")
        pluginsDir.mkdirs()
        File(pluginsDir, "idea-conventional-commit").createNewFile() // Mocking plugin presence by file name
        val pluginsPath = pluginsDir.path
        result = gradleBuilder.withArguments("-Didea.plugins.path=$pluginsPath").build()


        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
        File(testProjectDir, ".gradle/pub.ihub.plugin.cache/conventionalCommit.json").shouldExist()
        File(testProjectDir, ".idea/conventionalCommit.xml").shouldExist()

        // when: 模拟插件配置已存在且没有自定义配置
        File(testProjectDir, ".idea/conventionalCommit.xml").writeText("""
            <?xml version="1.0" encoding="UTF-8"?>
            <project version="4">
              <component name="general">
                <option name="other" value="text" />
              </component>
            </project>
        """.trimIndent())
        result = gradleBuilder.withArguments("-Didea.plugins.path=$pluginsPath").build()

        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
        // Custom file path should now be added
        File(testProjectDir, ".idea/conventionalCommit.xml").readText() shouldContain "customFilePath"


        // when: 模拟插件配置已存在且设置了自定义配置
         File(testProjectDir, ".idea/conventionalCommit.xml").writeText("""
            <?xml version="1.0" encoding="UTF-8"?>
            <project version="4">
              <component name="general">
                <option name="customFilePath" value="path" />
              </component>
            </project>
        """.trimIndent())
        result = gradleBuilder.withArguments("-Didea.plugins.path=$pluginsPath").build()

        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
        // Ensure it doesn't get overwritten if already present
         File(testProjectDir, ".idea/conventionalCommit.xml").readText() shouldContain """<option name="customFilePath" value="path" />"""
    }

    @Test
    fun `GitHooks插件commitCheck任务测试-成功`() {
        // setup: 初始化项目
        buildFile.appendText("""
            iHubGitHooks {
                types("type1", "type2", "type3")
                type("type") {
                    it.scope("scope")
                    it.description("Scope description")
                }
                footer("Other").description("Other description")
            }
        """.trimIndent())
        commitMsgFile.writeText("feat(some): text\n\nCloses: 123")

        // when: 执行任务
        val result = gradleBuilder.withArguments("commitCheck").build()

        // then: 检查结果
        result.output shouldContain "BUILD SUCCESSFUL"
    }
}
