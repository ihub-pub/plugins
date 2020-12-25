package pub.ihub.plugin

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

import static pub.ihub.plugin.Constants.ALIYUN_CONTENT_REPO
import static pub.ihub.plugin.Constants.GRADLE_PLUGIN_REPO_MIRROR_ALIYUN
import static pub.ihub.plugin.Constants.MAVEN_CENTRAL_REPO_MIRROR_ALIYUN
import static pub.ihub.plugin.Constants.PROJECT_NAME
import static pub.ihub.plugin.Constants.SPRING_PLUGIN_REPO_RELEASE
import static pub.ihub.plugin.PluginUtils.findProperty
import static pub.ihub.plugin.PluginUtils.printConfigContent



/**
 * @author henry
 */
class IHubSettingsPlugin implements Plugin<Settings> {

    @Override
    void apply(Settings settings) {
        settings.pluginManagement {
            repositories {
                flatDir dirs: "$settings.rootProject.projectDir/gradle/plugins"
                [
                        GRADLE_PLUGIN_REPO_MIRROR_ALIYUN,
                        MAVEN_CENTRAL_REPO_MIRROR_ALIYUN,
                        ALIYUN_CONTENT_REPO,
                        SPRING_PLUGIN_REPO_RELEASE
                ].each { repo -> if (!it*.displayName.any { it.contains repo }) maven { url repo } }
                if (!findByName("Gradle Central Plugin Repository")) gradlePluginPortal()
                if (!findByName("MavenRepo")) mavenCentral()
                if (!findByName("BintrayJCenter")) jcenter()
            }
        }
        printConfigContent 'Gradle Plugin Repos', settings.pluginManagement.repositories*.displayName

        settings.rootProject.name = findProperty settings, PROJECT_NAME, settings.rootProject.name

        settings.extensions.add('includeSubprojects', {
            String projectPath, String namePrefix = settings.rootProject.name, String nameSuffix = '' ->
                new File(settings.rootDir, projectPath).with {
                    settings.include ":$projectPath"
                    it.eachDir { dir ->
                        if ('build' != dir.name) {
                            def subprojectName = ":$it.name:$dir.name"
                            settings.include subprojectName
                            settings.project(subprojectName).projectDir = new File(it.absolutePath, dir.name)
                            settings.project(subprojectName).name = namePrefix + '-' + dir.name + nameSuffix
                        }
                    }
                }
        })
    }

}
