package pub.ihub.plugin

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

import static pub.ihub.plugin.Constants.ALIYUN_CONTENT_REPO
import static pub.ihub.plugin.Constants.GRADLE_PLUGIN_REPO_MIRROR_ALIYUN
import static pub.ihub.plugin.Constants.MAVEN_CENTRAL_REPO_MIRROR_ALIYUN
import static pub.ihub.plugin.Constants.PROJECT_NAME
import static pub.ihub.plugin.Constants.SPRING_PLUGIN_REPO_RELEASE
import static pub.ihub.plugin.PluginUtils.findProperty



/**
 * @author henry
 */
class IHubSettingsPlugin implements Plugin<Settings> {

    @Override
    void apply(Settings settings) {
        settings.pluginManagement {
            repositories {
                mavenLocal()
                flatDir dirs: "$settings.rootProject.projectDir/gradle/plugins"
                maven { url GRADLE_PLUGIN_REPO_MIRROR_ALIYUN }
                maven { url MAVEN_CENTRAL_REPO_MIRROR_ALIYUN }
                maven { url ALIYUN_CONTENT_REPO }
                gradlePluginPortal()
                mavenCentral()
                jcenter()
                maven { url SPRING_PLUGIN_REPO_RELEASE }
            }
        }

        settings.rootProject.name = findProperty PROJECT_NAME, settings.rootProject.name
    }

}
