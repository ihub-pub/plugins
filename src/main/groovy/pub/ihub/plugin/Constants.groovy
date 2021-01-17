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

/**
 * @author liheng
 */
class Constants {

	static final PROJECT_NAME = 'projectName'
	static final INCLUDE_DIRS = 'includeDirs'
	static final SKIPPED_DIRS = 'skippedDirs'
	static final MAVEN_CENTRAL_REPO_CUSTOMIZE = 'mavenCentralRepoCustomize'

	static final GRADLE_COMPILATION_INCREMENTAL = 'gradleCompilationIncremental'
	static final JAVA_COMPATIBILITY = 'javaCompatibility'

	//<editor-fold desc="仓库相关相关">

	static final MAVEN_LOCAL_ENABLED = 'mavenLocalEnabled'

	static final RELEASE_REPOSITORY_URL = 'releaseRepositoryUrl'
	static final SNAPSHOT_REPOSITORY_URL = 'snapshotRepositoryUrl'
	static final SONATYPE_NEXUS_USERNAME = 'sonatypeNexusUsername'
	static final SONATYPE_NEXUS_PASSWORD = 'sonatypeNexusPassword'

	static final MAVEN_CENTRAL_REPO_MIRROR_ALIYUN = 'https://maven.aliyun.com/repository/public/'
	static final GRADLE_PLUGIN_REPO_MIRROR_ALIYUN = 'https://maven.aliyun.com/repository/gradle-plugin/'
	static final ALIYUN_CONTENT_REPO = 'https://maven.aliyun.com/nexus/content/groups/public/'
	static final SPRING_PLUGIN_REPO_RELEASE = 'https://repo.spring.io/plugins-release/'
	static final DEFAULT_RELEASE_REPOSITORY_URL = 'https://oss.sonatype.org/service/local/staging/deploy/maven2/'
	static final DEFAULT_SNAPSHOT_REPOSITORY_URL = 'https://oss.sonatype.org/content/repositories/snapshots/'

	//</editor-fold>

	//<editor-fold desc="Plugins组件相关">

	static final SPRING_BOOT_VERSION = '2.3.8.RELEASE'
	static final IHUB_LIB_VERSION = 'dev-SNAPSHOT'
	static final HUTOOL_VERSION = '5.5.7'

	static final List<List<String>> GROUP_MAVEN_BOM_VERSION_CONFIG = [
		['org.springframework.boot'		, 'spring-boot-dependencies'			, SPRING_BOOT_VERSION	],
		['org.springframework.cloud'	, 'spring-cloud-dependencies'			, 'Hoxton.SR9'			],
		['org.springframework.security'	, 'spring-security-bom'					, '5.3.6.RELEASE'		],
		['com.alibaba.cloud'			, 'spring-cloud-alibaba-dependencies'	, '2.2.4.RELEASE'		],
		['com.github.xiaoymin'			, 'knife4j-dependencies'				, '3.0.2'				],
		['com.sun.xml.bind'				, 'jaxb-release-documentation'			, '3.0.0'				],
		['de.codecentric'				, 'batch-web-spring-boot-dependencies'	, '2.1.0.RELEASE'		],
		['de.codecentric'				, 'spring-boot-admin-dependencies'		, '2.3.1'				],
		['cn.hutool'					, 'hutool-bom'							, HUTOOL_VERSION		],
	]

	static final List GROUP_DEPENDENCY_VERSION_CONFIG = [
		['com.baomidou'			, '3.4.1'			, ['mybatis-plus', 'mybatis-plus-boot-starter',
														   'mybatis-plus-generator']],
		['com.github.xiaoymin'	, '2.0.8'			, ['knife4j-aggregation-spring-boot-starter']],
		['cn.hutool'			, HUTOOL_VERSION	, ['hutool-all']],
	]

	static final Map<String, List<String>> GROUP_DEPENDENCY_EXCLUDE_MAPPING = [
		'c3p0'                    : ['c3p0'],
		'commons-logging'         : ['commons-logging'],
		'com.zaxxer'              : ['HikariCP'],
		'log4j'                   : ['log4j'],
		'org.apache.logging.log4j': ['log4j-core'],
		'org.apache.tomcat'       : ['tomcat-jdbc'],
		'org.slf4j'               : ['slf4j-jcl', 'slf4j-log4j12'],
		'stax'                    : ['stax-api']
	]

	static final Map<String, List<String>> GROUP_DEFAULT_DEPENDENCIES_MAPPING = [
		compileOnly          : [],
		implementation       : ['org.slf4j:slf4j-api'],
		api                  : [],
		runtimeOnly          : ['org.slf4j:jul-to-slf4j', 'org.slf4j:jcl-over-slf4j', 'org.slf4j:log4j-over-slf4j'],
		testImplementation   : [],
		debugImplementation  : [],
		releaseImplementation: []
	]

	static final Map<String, String> PLUGINS_DEPENDENCY_VERSION_MAPPING = [
		'com.github.ben-manes.versions'  : '0.36.0',
		'com.palantir.git-version'       : '0.12.3',
		'io.spring.dependency-management': '1.0.10.RELEASE',
		'org.springframework.boot'       : SPRING_BOOT_VERSION
	]

	//</editor-fold>

	//<editor-fold desc="Publish组件相关">

	static final RELEASE_SIGNING_ENABLED = 'releaseSigningEnabled'
	static final RELEASE_DOCS_ENABLED = 'releaseDocsEnabled'
	static final RELEASE_SOURCES_ENABLED = 'releaseSourcesEnabled'

	//</editor-fold>

}
