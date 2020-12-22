package pub.ihub.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project



/**
 * @author liheng
 */
class IHubPlugins implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "${project.name}已加载IHubPlugins插件"
    }

}
