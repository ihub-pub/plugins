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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.quality.CodeNarcExtension
import org.gradle.api.plugins.quality.CodeNarcPlugin
import org.gradle.api.plugins.quality.PmdExtension
import org.gradle.api.plugins.quality.PmdPlugin
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension

import static pub.ihub.plugin.IHubPluginMethods.findProperty



/**
 * 代码检查插件
 * @author liheng
 */
class IHubVerificationPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		if (project.plugins.hasPlugin(IHubVerificationPlugin)) {
			return
		}
		project.pluginManager.apply IHubPluginsPlugin
		if (project.plugins.hasPlugin(JavaPlugin) || project.plugins.hasPlugin(JavaLibraryPlugin)) {
			configPmd project
		}
		if (project.plugins.hasPlugin(GroovyPlugin)) {
			configCodenarc project
		}
		configJacoco project
	}

	private static void configPmd(Project project) {
		project.pluginManager.apply PmdPlugin
		project.extensions.getByType(PmdExtension).identity {
			def ruleset = "$project.rootProject.projectDir/conf/pmd/ruleset.xml"
			if (project.file(ruleset).exists()) {
				ruleSetFiles = project.files ruleset
			} else {
				ruleSets = [
					'rulesets/java/ali-comment.xml',
					'rulesets/java/ali-concurrent.xml',
					'rulesets/java/ali-constant.xml',
					'rulesets/java/ali-exception.xml',
					'rulesets/java/ali-flowcontrol.xml',
					'rulesets/java/ali-naming.xml',
					'rulesets/java/ali-oop.xml',
					'rulesets/java/ali-orm.xml',
					'rulesets/java/ali-other.xml',
					'rulesets/java/ali-set.xml',
					'rulesets/vm/ali-other.xml',
				]
			}
			consoleOutput = findProperty(project, 'pmdConsoleOutput', 'false').toBoolean()
			ignoreFailures = findProperty(project, 'pmdIgnoreFailures', 'false').toBoolean()
			toolVersion = findProperty project, 'pmdVersion', '6.31.0'
		}
		project.configurations {
			maybeCreate('pmd').dependencies << project.dependencies.create('com.alibaba.p3c:p3c-pmd')
		}
	}

	private static void configCodenarc(Project project) {
		project.pluginManager.apply CodeNarcPlugin
		project.extensions.getByType(CodeNarcExtension).identity {
			def codenarc = "$project.rootProject.projectDir/conf/codenarc/codenarc.groovy"
			if (project.file(codenarc).exists()) {
				configFile = project.rootProject.file codenarc
			} else {
				// TODO 处理默认配置
				config = project.resources.text.fromString ''''''
			}
			ignoreFailures = findProperty(project, 'codenarcIgnoreFailures', 'false').toBoolean()
			toolVersion = findProperty project, 'codenarcVersion', '2.1.0'
		}
	}

	private static void configJacoco(def target) {
		target.pluginManager.apply JacocoPlugin
		target.extensions.getByType(JacocoPluginExtension).identity {
			toolVersion = findProperty target, 'jacoco.version', '0.8.6'
		}
		// TODO 配置检查规则
	}

}
