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


import org.gradle.api.Project

import static pub.ihub.plugin.Constants.MAVEN_CENTRAL_REPO_CUSTOMIZE
import static pub.ihub.plugin.Constants.MAVEN_LOCAL_ENABLED



/**
 * IHub Plugins Gradle Plugin
 * @author liheng
 */
class IHubPluginsPlugin implements IHubPluginAware<Project> {

	private static final Closure REPOSITORIES_CONFIGURE = { Project project ->
		def dirs = "$project.rootProject.projectDir/libs"
		if ((dirs as File).directory) flatDir dirs: dirs
		if (findProperty(MAVEN_LOCAL_ENABLED, true, 'false').toBoolean()) {
			mavenLocal()
		}
		// TODO 添加私有仓库
		// 添加自定义仓库
		def mavenCentralRepo = findSystemProperty MAVEN_CENTRAL_REPO_CUSTOMIZE
		if (mavenCentralRepo && !mavenCentralRepo.blank) {
			maven { url mavenCentralRepo }
		}
		maven { url 'https://maven.aliyun.com/repository/public' }
		maven {
			url 'https://maven.aliyun.com/repository/google'
			artifactUrls 'https://maven.google.com'
		}
		maven {
			url 'https://maven.aliyun.com/repository/spring'
			artifactUrls 'https://repo.spring.io/release'
		}
		maven { url 'https://repo.spring.io/release' }
		if (!findByName("MavenRepo")) mavenCentral()
		if (!findByName("BintrayJCenter")) jcenter()
	}

	@Override
	void apply() {
		// 配置项目以及子项目组件仓库
		target.repositories REPOSITORIES_CONFIGURE.curry(target)
		target.subprojects { repositories REPOSITORIES_CONFIGURE.curry(target) }
		printConfigContent 'Gradle Project Repos', target.repositories*.displayName
	}

}
