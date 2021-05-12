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
package pub.ihub.plugin.java

import static pub.ihub.plugin.IHubPluginMethods.findProperty

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubPluginAware
import pub.ihub.plugin.verification.IHubVerificationPlugin
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.bom.IHubBomPlugin

/**
 * Java插件
 * @author henry
 */
class IHubJavaPlugin implements IHubPluginAware<IHubExtension> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply IHubBomPlugin
		project.pluginManager.apply IHubJavaBasePlugin

		// 兼容性配置
		findProperty('javaCompatibility', project)?.with { version ->
			project.tasks.withType(AbstractCompile) {
				sourceCompatibility = version
				targetCompatibility = version
				options.encoding = 'UTF-8'
				options.incremental = findProperty('gradleCompilationIncremental', project, true.toString()).toBoolean()
			}
		}

		getExtension(project, IHubBomExtension).dependencies {
			// Java11添加jaxb运行时依赖
			if (JavaVersion.current().java11) {
				runtimeOnly 'javax.xml.bind:jaxb-api', 'com.sun.xml.bind:jaxb-core', 'com.sun.xml.bind:jaxb-impl'
			}

			// 添加lombok依赖
			String lombok = 'org.projectlombok:lombok'
			compileOnly lombok
			annotationProcessor lombok
		}

		project.pluginManager.apply IHubVerificationPlugin
	}

}
