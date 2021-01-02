package pub.ihub.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

import static pub.ihub.plugin.Constants.ALIYUN_CONTENT_REPO
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
		if (findProperty(MAVEN_LOCAL_ENABLED, findProperty(project, MAVEN_LOCAL_ENABLED, 'false')).toBoolean()) {
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

	@Override
	void apply(Project project) {
		project.repositories REPOSITORIES_CONFIGURE.curry(project)
		project.subprojects { repositories REPOSITORIES_CONFIGURE.curry(project) }
		printConfigContent 'Gradle Project Repos', project.repositories*.displayName
	}

}
