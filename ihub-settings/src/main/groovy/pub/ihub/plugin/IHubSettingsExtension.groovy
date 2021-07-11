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
import groovy.transform.TupleConstructor
import org.gradle.api.Action
import org.gradle.api.initialization.Settings

import static groovy.transform.TypeCheckingMode.SKIP



/**
 * 子项目配置扩展
 * @author liheng
 */
@CompileStatic
@SuppressWarnings('JUnitPublicProperty')
class IHubSettingsExtension implements IHubExtensionAware, IHubProjectProperty {

    private static final List<String> EXCLUDE_DIRS = [
        'build', 'src', 'conf', 'libs', 'logs', 'docs', 'classes', 'target', 'out', 'node_modules', 'db', 'gradle',
    ]

    final Settings settings
    final Map<String, String> pluginVersionSpecs = [:]
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
     * 配置插件版本
     * @param action 配置
     */
    void pluginVersions(Action<PluginVersionsSpec> action) {
        List<PluginVersionSpec> versions = new PluginVersionsSpec().tap { action.execute it }.specs
        versions.each {
            pluginVersionSpecs.put it.id, it.pluginVersion
        }
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
    @Override
    Object findProjectProperty(String key) {
        settings.hasProperty(key) ? settings."$key" : null
    }

    @CompileStatic
    class ProjectSpec {

        private String namePrefix = settings.rootProject.name + '-'
        private String nameSuffix = ''
        ProjectSpec subprojectSpec

        ProjectSpec prefix(String namePrefix) {
            this.namePrefix = namePrefix
            this
        }

        ProjectSpec suffix(String nameSuffix) {
            this.nameSuffix = nameSuffix
            this
        }

        ProjectSpec subproject(String nameSuffix = this.nameSuffix) {
            new ProjectSpec().tap {
                this.subprojectSpec = it
                it.namePrefix = this.namePrefix
                it.nameSuffix = nameSuffix
            }
        }

        @CompileStatic(SKIP)
        String includeProject(String projectPath) {
            String gradleProjectPath = ":$projectPath"
            String projectName = projectPath.split(':').last()
            if (projectPath.startsWith('.') || projectName in EXCLUDE_DIRS || settings.findProject(gradleProjectPath)) {
                return null
            }
            settings.include gradleProjectPath
            settings.project(gradleProjectPath).tap {
                name = namePrefix + projectPath.replaceAll(':', '-') + nameSuffix
            }.name
        }

    }

    @CompileStatic
    class PluginVersionsSpec {

        private final List<PluginVersionSpec> specs = []

        PluginVersionSpec id(String id) {
            new PluginVersionSpec(id).tap {
                specs << it
            }
        }

    }

    @CompileStatic
    @TupleConstructor(includeFields = true, includes = 'id')
    class PluginVersionSpec {

        private final String id
        private String pluginVersion

        void version(String version) {
            pluginVersion = findVersion id, version
        }

    }

}
