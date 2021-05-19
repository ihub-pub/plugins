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

import org.gradle.api.Project
import pub.ihub.plugin.IHubPluginAware
import pub.ihub.plugin.IHubProjectExtension
import pub.ihub.plugin.bom.IHubBomExtension
import pub.ihub.plugin.bom.IHubBomPlugin
import pub.ihub.plugin.verification.IHubTestPlugin
import pub.ihub.plugin.verification.IHubVerificationPlugin

/**
 * Java插件
 * @author henry
 */
class IHubJavaPlugin implements IHubPluginAware<IHubProjectExtension> {

	@Override
	void apply(Project project) {
		project.pluginManager.apply IHubBomPlugin
		project.pluginManager.apply IHubJavaBasePlugin

		getExtension(project, IHubBomExtension).dependencies {
			// 添加lombok依赖
			String lombok = 'org.projectlombok:lombok'
			compileOnly lombok
			annotationProcessor lombok
		}

		project.pluginManager.apply IHubTestPlugin
		project.pluginManager.apply IHubVerificationPlugin
	}

}
