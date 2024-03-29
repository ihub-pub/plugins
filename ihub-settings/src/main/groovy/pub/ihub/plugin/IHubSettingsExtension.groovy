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

import groovy.transform.CompileStatic
import org.gradle.api.initialization.Settings

import static groovy.transform.TypeCheckingMode.SKIP


/**
 * 子项目配置扩展
 * @author liheng
 */
@CompileStatic
@SuppressWarnings('JUnitPublicProperty')
class IHubSettingsExtension implements IHubExtensionAware {

    private static final List<String> EXCLUDE_DIRS = [
        'build', 'src', 'conf', 'libs', 'logs', 'docs', 'classes', 'target', 'out', 'node_modules', 'db', 'gradle',
    ]

    final Settings settings
    final Map<String, ProjectSpec> projectSpecs = [:]

    private final String[] skippedDirs

    /**
     * 包括物料清单
     */
    String includeBom
    /**
     * 包括依赖组件
     */
    String includeDependencies
    /**
     * 包括兼容性库
     */
    String includeLibs

    IHubSettingsExtension(Settings settings) {
        this.settings = settings

        // 配置主项目名称
        settings.rootProject.name = findProperty 'name', settings.rootProject.name

        // 通过项目属性配置子项目
        includeProjects findProperty('iHubSettings.includeDirs')?.split(',')
        skippedDirs = findProperty('iHubSettings.skippedDirs')?.split ','
        includeBom = findProperty 'iHubSettings.includeBom'
        includeDependencies = findProperty 'iHubSettings.includeDependencies'
        includeLibs = findProperty 'iHubSettings.includeLibs', 'false'
    }

    /**
     * 添加多个项目
     * @param projectPaths 项目路径
     */
    @CompileStatic(SKIP)
    ProjectSpec includeProjects(String... projectPaths) {
        new ProjectSpec().tap {
            projectPaths.each { projectPath ->
                projectSpecs.put projectPath, it
            }
        }
    }

    @CompileStatic(SKIP)
    ProjectSpec getProjectSpec(String path) {
        skippedDirs ? path in skippedDirs ? null : new ProjectSpec() : projectSpecs[path]
    }

    String findProperty(String key, String defaultValue = null) {
        findProperty settings, key, defaultValue
    }

    @CompileStatic(SKIP)
    static String findProperty(Settings settings, String key, String defaultValue = null) {
        settings.hasProperty(key) ? settings."$key" : defaultValue
    }

    @CompileStatic
    class ProjectSpec {

        private String namePrefix = ''
        private String nameSuffix = ''
        boolean include = true
        ProjectSpec subprojectSpec
        private String[] skippedSubDirs = []

        ProjectSpec prefix(String namePrefix) {
            this.namePrefix = namePrefix
            this
        }

        ProjectSpec getNoPrefix() {
            prefix ''
        }

        ProjectSpec suffix(String nameSuffix) {
            this.nameSuffix = nameSuffix
            this
        }

        ProjectSpec subproject(String nameSuffix) {
            new ProjectSpec().tap {
                this.subprojectSpec = it
                it.namePrefix = this.namePrefix
                it.nameSuffix = nameSuffix
                it.skippedSubDirs = this.skippedSubDirs
            }
        }

        ProjectSpec getSubproject() {
            subproject nameSuffix
        }

        ProjectSpec getOnlySubproject() {
            subprojectSpec = this
            include = false
            this
        }

        ProjectSpec skippedDirs(String... dirs) {
            skippedSubDirs = dirs
            this
        }

        @CompileStatic(SKIP)
        private String includeProject(String projectPath, File projectDir = null) {
            String gradleProjectPath = ":$projectPath"
            String projectName = projectPath.split(':').last()
            if (projectPath.startsWith('.') || projectName in EXCLUDE_DIRS || settings.findProject(gradleProjectPath)) {
                return null
            }
            settings.include gradleProjectPath
            settings.project(gradleProjectPath).tap {
                if (projectDir) {
                    it.projectDir = projectDir
                }
                name = namePrefix + projectPath.replaceAll(':', '-') + nameSuffix
            }.name
        }

        Map<String, List<String>> includeSubProject(File dir) {
            String path = dir.name
            List<String> names = include ? [includeProject(path)] : []
            subprojectSpec?.with { subSpec ->
                dir.eachDir { subDir ->
                    if (!subSpec.skippedSubDirs.contains(subDir.name)) {
                        names << (include ? subSpec.includeProject("$path:$subDir.name") :
                            subSpec.includeProject(subDir.name, subDir))
                    }
                }
            }
            names.removeIf { !it }
            [(path): names]
        }

    }

}
