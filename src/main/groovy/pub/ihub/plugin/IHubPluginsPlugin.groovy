package pub.ihub.plugin


import org.gradle.api.Plugin
import org.gradle.api.Project

import static pub.ihub.plugin.Constants.ALIYUN_CONTENT_REPO
import static pub.ihub.plugin.Constants.GROUP_DEFAULT_DEPENDENCIES_MAPPING
import static pub.ihub.plugin.Constants.GROUP_DEPENDENCY_EXCLUDE_MAPPING
import static pub.ihub.plugin.Constants.GROUP_DEPENDENCY_VERSION_MAPPING
import static pub.ihub.plugin.Constants.MAVEN_CENTRAL_REPO_CUSTOMIZE
import static pub.ihub.plugin.Constants.MAVEN_CENTRAL_REPO_MIRROR_ALIYUN
import static pub.ihub.plugin.Constants.MAVEN_LOCAL_ENABLED
import static pub.ihub.plugin.PluginUtils.findProperty
import static pub.ihub.plugin.PluginUtils.printConfigContent



/**
 * @author liheng
 */
class IHubPluginsPlugin implements Plugin<Project> {

    private static final Closure REPOSITORIES_CONFIGURE = { Project project ->
        flatDir dirs: "$project.rootProject.projectDir/libs"
        if (findProperty(MAVEN_LOCAL_ENABLED, 'false').toBoolean()) {
            mavenLocal()
        }
        // TODO 添加私有仓库
        // 添加自定义仓库
        def mavenCentralRepo = findProperty project, MAVEN_CENTRAL_REPO_CUSTOMIZE
        if (mavenCentralRepo && !mavenCentralRepo.blank) {
            maven { url mavenCentralRepo }
        }
        maven { url MAVEN_CENTRAL_REPO_MIRROR_ALIYUN }
        maven { url ALIYUN_CONTENT_REPO }
        mavenCentral()
        jcenter()
    }

    private static final Closure CONFIGURATIONS_CONFIGURE = { Project project, Map<String, String> groupVersion ->
        all {
            resolutionStrategy {
                // TODO 使用dependencyManagement管理依赖版本
                eachDependency {
                    def version = groupVersion[it.requested.group]
                    if (version) {
                        it.useVersion version
                    }
                }
                // 不缓存动态版本
                cacheDynamicVersionsFor 0, 'seconds'
                // 不缓存快照模块
                cacheChangingModulesFor 0, 'seconds'
            }
        }
        all {
            GROUP_DEPENDENCY_EXCLUDE_MAPPING.each { group, modules ->
                modules.each { module ->
                    exclude group: group, module: module
                }
            }
        }
        GROUP_DEFAULT_DEPENDENCIES_MAPPING.each { key, dependencies ->
            maybeCreate(key).getDependencies()
                    .addAll(dependencies.collect { project.getDependencies().create(it) })
        }
    }

    @Override
    void apply(Project project) {
        project.repositories REPOSITORIES_CONFIGURE.curry(project)
        project.subprojects { repositories REPOSITORIES_CONFIGURE.curry(project) }
        printConfigContent 'Gradle Project Repos', project.repositories*.displayName

        def groupVersion = GROUP_DEPENDENCY_VERSION_MAPPING.findAll { it.key != project.group }
                .collectEntries { group, version ->
                    [(group): findProperty(project, group + '.version', version)]
                }
        project.configurations CONFIGURATIONS_CONFIGURE.curry(project, groupVersion)
        project.subprojects { configurations CONFIGURATIONS_CONFIGURE.curry(project, groupVersion) }
        printConfigContent 'Gradle Project Group Dependency Version', 'Group', 'Version', groupVersion
        printConfigContent 'Gradle Project Exclude Group Modules', 'Group', 'Modules',
                GROUP_DEPENDENCY_EXCLUDE_MAPPING
        printConfigContent 'Gradle Project Config Default Dependencies', 'DependencyType', 'Dependencies',
                GROUP_DEFAULT_DEPENDENCIES_MAPPING
    }

}
