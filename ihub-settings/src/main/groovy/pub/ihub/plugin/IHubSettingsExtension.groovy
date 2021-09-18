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

    IHubSettingsExtension(Settings settings) {
        this.settings = settings

        // 配置主项目名称
        settings.rootProject.name = findProperty 'name', settings.rootProject.name

        // 通过项目属性配置子项目
        includeProjects findProperty('iHubSettings.includeDirs')?.split(',')
        skippedDirs = findProperty('iHubSettings.skippedDirs')?.split ','
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

    @CompileStatic(SKIP)
    private String findProperty(String key, String defaultValue = null) {
        settings.hasProperty(key) ? settings."$key" : defaultValue
    }

    @CompileStatic
    class ProjectSpec {

        private String namePrefix = settings.rootProject.name + '-'
        private String nameSuffix = ''
        boolean include = true
        ProjectSpec subprojectSpec

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

        @CompileStatic(SKIP)
        String includeProject(String projectPath, File projectDir = null) {
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

    }

}
