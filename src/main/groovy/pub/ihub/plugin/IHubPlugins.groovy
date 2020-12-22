package pub.ihub.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

import static pub.ihub.plugin.Constants.MAVEN_CENTRAL_REPOSITORY
import static pub.ihub.plugin.Constants.MAVEN_LOCAL_ENABLED
import static pub.ihub.plugin.PluginUtils.findProperty



/**
 * @author liheng
 */
class IHubPlugins implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.subprojects {
            repositories {
                flatDir dirs: "$project.rootProject.projectDir/libs"
                if (findProperty(MAVEN_LOCAL_ENABLED, 'false').toBoolean()) {
                    mavenLocal()
                }
                // TODO 添加私有仓库
                // 添加自定义仓库
                def mavenCentralRepo = findProperty MAVEN_CENTRAL_REPOSITORY
                if (mavenCentralRepo && !mavenCentralRepo.blank) {
                    maven { url mavenCentralRepo }
                }
                maven { url 'https://maven.aliyun.com/repository/public/' }
                maven { url 'https://maven.aliyun.com/nexus/content/groups/public/' }
                mavenCentral()
                jcenter()
            }
        }
        println "${project.name}已加载IHubPlugins插件"
    }

}
