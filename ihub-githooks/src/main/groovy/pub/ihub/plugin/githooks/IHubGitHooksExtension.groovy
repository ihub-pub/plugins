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

import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.XmlUtil
import groovy.json.JsonBuilder
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.w3c.dom.Document
import org.w3c.dom.Node
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty

import javax.inject.Inject
import java.nio.file.Path

import static groovy.transform.TypeCheckingMode.SKIP
import static java.nio.file.Files.createDirectories
import static java.nio.file.Files.write
import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM

/**
 * IHub Git Hooks插件扩展
 * @author henry
 */
@IHubExtension('iHubGitHooks')
@CompileStatic
@SuppressWarnings(['ConfusingMethodName', 'JUnitPublicProperty'])
class IHubGitHooksExtension extends IHubProjectExtensionAware {

    private static final Map<String, List<String>> DEFAULT_TYPES = [
        refactor: ['代码重构', 'Changes which neither fix a bug nor add a feature'],
        fix     : ['修复错误', 'Changes which patch a bug'],
        feat    : ['引入新功能', 'Changes which introduce a new feature'],
        build   : ['架构改动', 'Changes which affect the build system or external dependencies'],
        chore   : ['与业务无关的改动', 'Changes which aren\'t user-facing'],
        style   : ['结构改进/代码格式化', 'Changes which don\'t affect code logic, such as white-spaces, formatting, missing semi-colons'],
        test    : ['更新测试', 'Changes which add missing tests or correct existing tests'],
        docs    : ['更新文档', 'Changes which affect documentation'],
        perf    : ['性能改善', 'Changes which improve performance'],
        ci      : ['CI/CD', 'Changes which affect CI configuration files and scripts'],
        revert  : ['代码回滚', 'Changes which revert a previous commit'],
    ]

    private static final Map<String, List<String>> DEFAULT_SCOPES = [
        build: ['gradle', 'maven', 'ant'],
        ci   : ['github', 'gitlab', 'jenkins', 'travis'],
    ]

    private static final Map<String, String> DEFAULT_FOOTERS = [
        'BREAKING CHANGE': 'The commit introduces breaking API changes',
        'Closes'         : 'The commit closes issues or pull requests',
        'Implements'     : 'The commit implements features',
        'Co-authored-by' : 'The commit is co-authored by another person.<br/>For multiple people use one line each',
        'Refs'           : 'The commit references other commits by their hash ID.<br/>For multiple hash IDs use a comma as separator',
    ]

    /**
     * 自定义hooks路径
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> hooksPath

    /**
     * 自定义hooks
     */
    MapProperty<String, String> hooks

    /**
     * 提交描述正则表达式
     */
    Property<String> descriptionRegex

    /**
     * 提交类型
     */
    Set<TypeSpec> types = []

    /**
     * 注脚信息
     */
    Set<FooterSpec> footers = []

    @Inject
    IHubGitHooksExtension(ObjectFactory objectFactory) {
        hooksPath = objectFactory.property(String)
        hooks = objectFactory.mapProperty(String, String).convention([:])
        descriptionRegex = objectFactory.property(String).convention(/.{1,100}/)
    }

    void types(String... types) {
        this.types.addAll types.collect {
            new TypeSpec(it).description(DEFAULT_TYPES[it] as String[]).scopes((DEFAULT_SCOPES[it] ?: []) as String[])
        }
    }

    TypeSpec type(String name) {
        new TypeSpec(name).tap {
            types << it
        }
    }

    void footers(String... footers) {
        this.footers.addAll footers.collect { new FooterSpec(it).description DEFAULT_FOOTERS[it] }
    }

    FooterSpec footer(String name) {
        new FooterSpec(name).tap {
            footers << it
        }
    }

    @CompileStatic(SKIP)
    List<String> checkHeader(String header) {
        (header =~ /^(${types*.name.join('|')})(\((.+)\))?!?: ${descriptionRegex.get()}/).with {
            assertRule matches(), 'Commit msg header check fail!'
            it[0][1, 3] as List<String>
        }
    }

    void writeHook(String hookName, String command) {
        Path scriptPath = hooksDir.toPath().resolve hookName
        createDirectories scriptPath.parent
        write scriptPath, [
            '#!/bin/bash', '# This file is generated by the \'pub.ihub.plugin.ihub-git-hooks\' Gradle plugin',
            "echo 'ihub $hookName hook: $command'", command
        ]
        scriptPath.toFile().setExecutable true, false
    }

    void writeHooks() {
        hooksDir.deleteDir()
        hooks.get().forEach this::writeHook
    }

    void execute(String hooksPath, Map<String, String> hooks) {
        Logger logger = project.logger
        try {
            if (hooksPath) {
                "git config core.hooksPath $hooksPath".execute()
                logger.lifecycle 'Set git hooks path: ' + hooksPath
            } else if (hooks) {
                writeHooks()
                'git config core.hooksPath .gradle/pub.ihub.plugin.hooks'.execute()
                logger.lifecycle 'Set git hooks path: .gradle/pub.ihub.plugin.hooks'
            } else {
                'git config --unset core.hooksPath'.execute()
                logger.lifecycle 'Unset git hooks path, learn more see https://doc.ihub.pub/plugins/iHubGitHooks'
            }
        } catch (e) {
            logger.lifecycle 'Git hooks config fail: ' + e.message
        }
    }

    void configDefaultGitCommitCheck() {
        types DEFAULT_TYPES.keySet() as String[]
        footers DEFAULT_FOOTERS.keySet() as String[]
    }

    void configConventionalCommit() {
        Project rootProject = project.rootProject
        String pluginConfigPath = rootProject.file('.idea/conventionalCommit.xml').path
        String configPath = generateConventionalCommitConfig rootProject
        Document document
        Node component
        if (FileUtil.exist(pluginConfigPath)) {
            document = XmlUtil.readXML pluginConfigPath
            component = XmlUtil.getNodeByXPath 'component[@name="general"]', document.documentElement
            if (XmlUtil.getNodeByXPath('option[@name="customFilePath"]', component)) {
                return
            }
        } else {
            document = XmlUtil.createXml 'project'
            component = XmlUtil.appendChild(document.documentElement.tap {
                setAttribute 'version', '4'
            }, 'component').tap {
                setAttribute 'name', 'general'
            }
        }
        XmlUtil.appendChild(component, 'option').with {
            setAttribute 'name', 'customFilePath'
            setAttribute 'value', configPath
        }
        XmlUtil.toFile document, pluginConfigPath
    }

    @CompileStatic(SKIP)
    private String generateConventionalCommitConfig(Project rootProject) {
        String tmpPath = "$rootProject.projectDir/.gradle/pub.ihub.plugin.cache"
        rootProject.mkdir tmpPath
        rootProject.file("$tmpPath/conventionalCommit.json").tap {
            createNewFile()
            write new JsonBuilder([
                types      : types.collectEntries { it.generateConfig() },
                footerTypes: footers*.generateConfig()
            ]).toPrettyString()
        }.path
    }

    private File getHooksDir() {
        project.layout.projectDirectory.dir('.gradle').dir('pub.ihub.plugin.hooks').asFile
    }

    @CompileStatic
    @TupleConstructor(includes = 'name')
    @EqualsAndHashCode(includes = 'name')
    class ConfigSpec<T> {

        String name
        String description = ''

        T description(String... description) {
            this.description = description?.join '<br/>'
            this as T
        }

        Map generateConfig() {
            [(name): [description: description]]
        }

    }

    @CompileStatic
    @EqualsAndHashCode(callSuper = true)
    class TypeSpec extends ConfigSpec<TypeSpec> {

        private final Set<ConfigSpec> scopes = []
        private boolean requiredScope = false

        TypeSpec(String name) {
            super(name)
        }

        TypeSpec scopes(String... scopes) {
            this.scopes.addAll scopes.collect { new ConfigSpec(it) }
            this
        }

        ConfigSpec scope(String name) {
            new ConfigSpec(name).tap {
                scopes << it
            }
        }

        TypeSpec requiredScope(boolean requiredScope) {
            this.requiredScope = requiredScope
            this
        }

        void checkScope(String scope) {
            assertRule !requiredScope || scopes*.name.contains(scope),
                "Commit msg header scope not in [${scopes*.name.join(', ')}]!"
        }

        @Override
        Map generateConfig() {
            [
                (name): [
                    description: description + (scopes ? '<br/>Example scopes: ' + scopes*.name.join(', ') : ''),
                    scopes     : scopes.collectEntries { it.generateConfig() }
                ]
            ]
        }

    }

    @CompileStatic
    @EqualsAndHashCode(callSuper = true)
    class FooterSpec extends ConfigSpec<FooterSpec> {

        /**
         * 是否必填
         */
        private boolean required = false
        /**
         * 需要必填的类型
         */
        private String[] types = []
        /**
         * 注脚值正则表达式
         */
        private String valueRegex

        FooterSpec(String name) {
            super(name)
        }

        FooterSpec required(boolean required) {
            this.required = required
            this
        }

        FooterSpec valueRegex(String valueRegex) {
            this.valueRegex = valueRegex
            this
        }

        FooterSpec requiredWithType(String... types) {
            this.types = types
            this
        }

        void checkRequired(String footerValue) {
            assertRule !required || footerValue, "Commit msg footer missing '$name'!"
        }

        void checkRequiredWithType(String type, String footerValue) {
            assertRule !types.contains(type) || footerValue,
                "Commit msg footer missing '$name' where type is '$type'!"
        }

        void checkValue(String footerValue) {
            assertRule !valueRegex || !footerValue || footerValue ==~ valueRegex,
                "Commit msg footer '$name' check fail with regex: '$valueRegex'!"
        }

        @Override
        Map generateConfig() {
            [name: name, description: description]
        }

    }

    static void assertRule(boolean condition, String message) {
        if (!condition) {
            throw new GradleException(message + ' See https://doc.ihub.pub/plugins/iHubGitHooks')
        }
    }

}
