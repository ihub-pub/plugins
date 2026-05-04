/*
 * Copyright (c) 2021-2024 the original author or authors.
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
package pub.ihub.plugin.meta

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

/**
 * IHub AI Metadata 生成任务
 * 收集当前 Gradle 项目及全部子项目（modules）的结构信息，
 * 输出为 build/ihub/project-meta.json，供 LLM / AI 工具读取。
 *
 * @author henry
 */
@DisableCachingByDefault(because = '元数据内容依赖项目源代码结构变化，不适合构建缓存')
@SuppressWarnings('AbstractClassWithoutAbstractMethod')
abstract class IHubMetaTask extends DefaultTask {

    @OutputFile
    abstract RegularFileProperty getOutputFile()

    @Input
    abstract Property<Boolean> getMetaEnabled()

    @Input
    abstract Property<Boolean> getIncludeDependencies()

    @Input
    abstract Property<Boolean> getIncludeSourceSets()

    @Input
    abstract Property<Boolean> getIncludeCatalogContext()

    @InputFile
    @Optional
    @PathSensitive(PathSensitivity.NONE)
    abstract RegularFileProperty getCatalogFile()

    @TaskAction
    void generate() {
        if (!metaEnabled.get()) {
            logger.lifecycle('IHub Meta: metadata generation is disabled, skipping.')
            return
        }

        Project rootProject = project.rootProject

        // 预加载 catalog（仅当启用语义注入时）
        Map<String, Map<String, Object>> catalogIndex = [:]
        if (includeCatalogContext.get()) {
            catalogIndex = loadCatalogIndex()
            logger.lifecycle('IHub Meta: catalog loaded, {} components indexed', catalogIndex.size())
        }

        Map<String, Object> meta = [:]

        meta.project = collectProjectInfo(rootProject)
        meta.gradle = collectGradleInfo(rootProject)

        if (includeSourceSets.get()) {
            meta.sourceSets = collectSourceSets(rootProject)
        }

        if (includeDependencies.get()) {
            meta.dependencies = collectDependencies(rootProject, catalogIndex)
        }

        meta.modules = collectModules(rootProject)

        File outFile = outputFile.get().asFile
        outFile.parentFile.mkdirs()
        outFile.write new JsonBuilder(meta).toPrettyString()

        logger.lifecycle('IHub Meta: project metadata written to {}', outFile)
    }

    /**
     * 加载 catalog.json 并按 gradle_ref 建立索引，便于依赖匹配。
     * 返回 Map<gradleRef, componentEntry>。
     */
    private Map<String, Map<String, Object>> loadCatalogIndex() {
        File cf = catalogFile.orNull?.asFile
        if (!cf?.exists()) {
            // 尝试默认位置：rootProject/gradle/ihub-catalog/catalog.json
            cf = new File(project.rootProject.projectDir, 'gradle/ihub-catalog/catalog.json')
        }
        if (!cf?.exists()) {
            logger.warn('IHub Meta: catalog file not found, skipping catalog context injection')
            return [:]
        }
        try {
            def parsed = new JsonSlurper().parse(cf)
            Map<String, Map<String, Object>> index = [:]
            (parsed.components as List<Map<String, Object>>).each { entry ->
                def ref = entry.gradle_ref
                List<String> refs = ref instanceof List ? ref as List<String> : [ref as String]
                refs.each { r ->
                    if (r) index[r] = entry
                }
            }
            return index
        } catch (Exception e) {
            logger.warn('IHub Meta: failed to load catalog: {}', e.message)
            return [:]
        }
    }

    private Map<String, Object> collectProjectInfo(Project project) {
        [
            name       : project.name,
            group      : project.group.toString(),
            version    : project.version.toString(),
            description: project.description ?: null,
            rootDir    : project.projectDir.absolutePath,
        ]
    }

    private Map<String, Object> collectGradleInfo(Project project) {
        [
            version: project.gradle.gradleVersion,
            plugins: collectAppliedPlugins(project),
        ]
    }

    private List<Map<String, String>> collectAppliedPlugins(Project project) {
        List<String> candidateIds = [
            'java', 'groovy', 'kotlin', 'java-library', 'application', 'war',
            'maven-publish', 'ivy-publish', 'signing',
            'checkstyle', 'pmd', 'idea', 'eclipse',
            'distribution', 'platform-base', 'reporting-base',
            'pub.ihub.plugin.ihub-java', 'pub.ihub.plugin.ihub-groovy',
            'pub.ihub.plugin.ihub-kotlin', 'pub.ihub.plugin.ihub-boot',
            'pub.ihub.plugin.ihub-native', 'pub.ihub.plugin.ihub-node',
            'pub.ihub.plugin.ihub-publish', 'pub.ihub.plugin.ihub-javaagent',
            'pub.ihub.plugin.ihub-shadow', 'pub.ihub.plugin.ihub-copyright',
            'pub.ihub.plugin.ihub-git-hooks', 'pub.ihub.plugin.ihub-settings',
            'pub.ihub.plugin.ihub-bom', 'pub.ihub.plugin.ihub-verification',
            'pub.ihub.plugin.ihub-test', 'pub.ihub.plugin.ihub-skills',
            'pub.ihub.plugin.ihub-meta', 'pub.ihub.plugin.ihub-base',
            'pub.ihub.plugin.ihub-java-base',
            'pub.ihub.plugin', 'pub.ihub.plugin.ihub-profiles',
            'pub.ihub.plugin.ihub-version',
            'org.springframework.boot',
            'io.spring.dependency-management',
            'org.springframework.cloud.contract',
            'com.gradle.plugin-publish',
            'com.diffplug.spotless',
            'com.github.ben-manes.versions',
            'org.jetbrains.kotlin.jvm',
            'org.jetbrains.kotlin.plugin.spring',
            'com.github.johnrengelman.shadow',
        ]

        List<Map<String, String>> result = []
        for (String id : candidateIds) {
            if (project.pluginManager.hasPlugin(id)) {
                result << [id: id]
            }
        }
        result
    }

    private Map<String, Object> collectSourceSets(Project project) {
        Map<String, Object> result = [:]
        for (Project p : project.allprojects) {
            collectSourceSetsForProject(p, project, result)
        }
        if (result.size() == 1 && result.containsKey(project.name)) {
            return result.get(project.name) as Map<String, Object>
        }
        result
    }

    private void collectSourceSetsForProject(Project p, Project rootProject, Map<String, Object> result) {
        if (!p.pluginManager.hasPlugin('java')) {
            return
        }
        def sourceSets = p.extensions.findByName('sourceSets')
        if (!sourceSets) {
            return
        }
        Map<String, Object> projectSources = [:]
        for (def srcSet : sourceSets) {
            if (srcSet.name in ['main', 'test']) {
                Map<String, List<String>> dirs = collectSourceDirsForSourceSet(srcSet)
                if (dirs) {
                    projectSources[srcSet.name] = dirs
                }
            }
        }
        if (projectSources) {
            result[p == rootProject ? rootProject.name : p.path] = projectSources
        }
    }

    private Map<String, List<String>> collectSourceDirsForSourceSet(srcSet) {
        Map<String, List<String>> dirs = [:]
        if (srcSet.java?.srcDirs) {
            dirs.java = srcSet.java.srcDirs*.absolutePath as List<String>
        }
        if (srcSet.hasProperty('groovy') && srcSet.groovy?.srcDirs) {
            dirs.groovy = srcSet.groovy.srcDirs*.absolutePath as List<String>
        }
        if (srcSet.hasProperty('kotlin') && srcSet.kotlin?.srcDirs) {
            dirs.kotlin = srcSet.kotlin.srcDirs*.absolutePath as List<String>
        }
        if (srcSet.resources?.srcDirs) {
            dirs.resources = srcSet.resources.srcDirs*.absolutePath as List<String>
        }
        dirs
    }

    private Map<String, Object> collectDependencies(Project project, Map<String, Map<String, Object>> catalogIndex) {
        Map<String, Object> result = [:]
        List<String> relevantConfigs = [
            'implementation', 'testImplementation', 'compileOnly',
            'testCompileOnly', 'runtimeOnly', 'testRuntimeOnly',
            'annotationProcessor', 'testAnnotationProcessor',
            'api', 'testFixturesImplementation',
        ]
        for (String configName : relevantConfigs) {
            def config = project.configurations.findByName(configName)
            if (config) {
                def deps = config.dependencies.findAll { it.group }
                    .collect { dep ->
                        Map<String, Object> entry = [gav: "${dep.group}:${dep.name}:${dep.version}"]
                        if (catalogIndex) {
                            def match = catalogIndex[dep.name]
                            if (match) {
                                entry.catalog = [
                                    id         : match.id,
                                    domain     : match.domain,
                                    description: match.description,
                                    status     : match.status,
                                ]
                            }
                        }
                        entry
                    }
                if (deps) {
                    result[configName] = deps
                }
            }
        }
        result
    }

    private List<Map<String, Object>> collectModules(Project rootProject) {
        List<Map<String, Object>> modules = []
        rootProject.subprojects.each { Project sub ->
            modules << [
                name       : sub.name,
                path       : sub.path,
                group      : sub.group.toString(),
                version    : sub.version.toString(),
                description: sub.description ?: null,
            ]
        }
        modules
    }

}
