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

import static pub.ihub.plugin.IHubPluginMethods.of



/**
 * 常量
 * @author liheng
 */
class Constants {

	//<editor-fold desc="Settings组件相关">

	static final Map<String, String> PLUGINS_DEPENDENCY_VERSION_MAPPING = [
		'io.spring.dependency-management'     : '1.0.11.RELEASE',
		'org.springframework.boot'            : '2.4.4',
		'org.springframework.experimental.aot': '0.9.1',
		'com.github.ben-manes.versions'       : '0.38.0',
		'com.palantir.git-version'            : '0.12.3'
	]

	//</editor-fold>

	//<editor-fold desc="Plugins组件相关">

	static final List<Tuple3<String, String, String>> GROUP_MAVEN_BOM_VERSION_CONFIG = [
		of('org.springframework.boot'		, 'spring-boot-dependencies'				, '2.4.4'			),
		of('org.springframework.cloud'		, 'spring-cloud-dependencies'			, '2020.0.2'			),
		of('org.springframework.security'	, 'spring-security-bom'					, '5.4.5'			),
		of('com.alibaba.cloud'				, 'spring-cloud-alibaba-dependencies'	, '2.2.5.RELEASE'	),
		of('com.github.xiaoymin'				, 'knife4j-dependencies'					, '3.0.2'			),
		of('com.sun.xml.bind'				, 'jaxb-bom-ext'							, '3.0.0'			),
		of('de.codecentric'					, 'batch-web-spring-boot-dependencies'	, '2.1.0.RELEASE'	),
		of('de.codecentric'					, 'spring-boot-admin-dependencies'		, '2.4.0'			),
	]

	static final Map<String, String> GROUP_MAVEN_VERSION_CONFIG = [
		'pub.ihub.lib': 'dev-SNAPSHOT',
		'cn.hutool'   : '5.6.2',
	]

	static final List<Tuple3<String, String, List<String>>> GROUP_DEPENDENCY_VERSION_CONFIG = [
		of('org.projectlombok'	, '1.18.16'	, ['lombok']),
		of('javax.xml.bind'		, '2.3.1'	, ['jaxb-api']),
		of('com.baomidou'		, '3.4.2'	, ['mybatis-plus', 'mybatis-plus-boot-starter', 'mybatis-plus-generator']),
		of('com.github.xiaoymin'	, '2.0.8'	, ['knife4j-aggregation-spring-boot-starter']),
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
		compileOnly          : ['cn.hutool:hutool-all'],
		implementation       : ['org.slf4j:slf4j-api'],
		api                  : [],
		runtimeOnly          : ['org.slf4j:jul-to-slf4j', 'org.slf4j:jcl-over-slf4j', 'org.slf4j:log4j-over-slf4j'],
		testImplementation   : [],
		debugImplementation  : [],
		releaseImplementation: []
	]

	//</editor-fold>

}
