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

import static pub.ihub.plugin.Constants.VALUE_FALSE
import static pub.ihub.plugin.Constants.VALUE_TRUE
import static pub.ihub.plugin.Constants.getGROOVY_VERSION
import static pub.ihub.plugin.IHubPluginMethods.findProperty

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.quality.CodeNarcExtension
import org.gradle.api.plugins.quality.CodeNarcPlugin
import org.gradle.api.plugins.quality.PmdExtension
import org.gradle.api.plugins.quality.PmdPlugin
import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension

/**
 * 代码检查插件
 * @author liheng
 */
class IHubVerificationPlugin implements Plugin<Project> {

	//<editor-fold desc="默认检查规则">

	static final List<String> PMD_DEFAULT_RULESET = [
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

	static final String CODENARC_DEFAULT_RULESET = '''// 全局默认CodeNarc规则集
ruleset {
	description '全局默认CodeNarc规则集'

	ruleset('rulesets/basic.xml')
	ruleset('rulesets/braces.xml')
	ruleset('rulesets/comments.xml')
	ruleset('rulesets/concurrency.xml')
	ruleset('rulesets/convention.xml')
	ruleset('rulesets/design.xml') {
		'Instanceof' priority: 4
	}
	ruleset('rulesets/dry.xml') {
		'DuplicateMapLiteral' priority: 4, doNotApplyToFilesMatching: /.*(FT|IT|UT|Test)_?\\d*\\.groovy/
		'DuplicateNumberLiteral' priority: 4, doNotApplyToFilesMatching: /.*(FT|IT|UT|Test)_?\\d*\\.groovy/
		'DuplicateStringLiteral' priority: 4, doNotApplyToFilesMatching: /.*(FT|IT|UT|Test)_?\\d*\\.groovy/
	}
	ruleset('rulesets/enhanced.xml')
	ruleset('rulesets/exceptions.xml')
	ruleset('rulesets/formatting.xml') {
		'LineLength' ignoreLineRegex: /.*'.*'.*|.*".*".*|.*测试.*|class .*/
		'ConsecutiveBlankLines' enabled: false
		'SpaceAroundMapEntryColon' characterBeforeColonRegex: /\\s|\\w|\\)|'|"|[\\u4e00-\\u9fa5]/, characterAfterColonRegex: /\\s/
	}
	ruleset('rulesets/generic.xml')
	ruleset('rulesets/grails.xml')
	ruleset('rulesets/groovyism.xml')
	ruleset('rulesets/imports.xml') {
		'MisorderedStaticImports' comesBefore: false
	}
	ruleset('rulesets/jdbc.xml')
	ruleset('rulesets/junit.xml')
	ruleset('rulesets/logging.xml')
	ruleset('rulesets/naming.xml') {
		'FieldName' staticFinalRegex: '[A-Z][A-Z0-9_]*', staticRegex: '[a-z][a-zA-Z0-9_]*', ignoreFieldNames: 'serialVersionUID'
		'MethodName' ignoreMethodNames: '*测试*,*test*'
		'PropertyName' staticFinalRegex: '[A-Z][A-Z0-9_]*', staticRegex: '[a-z][a-zA-Z0-9_]*'
	}
	ruleset('rulesets/security.xml')
	ruleset('rulesets/serialization.xml')
	ruleset('rulesets/size.xml')
	ruleset('rulesets/unnecessary.xml')
	ruleset('rulesets/unused.xml')
}
'''

	//</editor-fold>

	@Override
	void apply(Project project) {
		project.pluginManager.apply IHubPluginsPlugin
		project.pluginManager.apply IHubBomPlugin
		if (project.plugins.hasPlugin(JavaPlugin)) {
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
			String ruleset = "$project.rootProject.projectDir/conf/pmd/ruleset.xml"
			if (project.file(ruleset).exists()) {
				ruleSetFiles = project.files ruleset
			} else {
				ruleSets = PMD_DEFAULT_RULESET
			}
			consoleOutput = findProperty(project, 'pmdConsoleOutput', VALUE_FALSE).toBoolean()
			ignoreFailures = findProperty(project, 'pmdIgnoreFailures', VALUE_FALSE).toBoolean()
			toolVersion = findProperty project, 'pmdVersion', '6.31.0'
		}
		project.extensions.getByType(IHubBomExtension).dependencies {
			compile 'pmd', 'com.alibaba.p3c:p3c-pmd'
		}
	}

	private static void configCodenarc(Project project) {
		project.pluginManager.apply CodeNarcPlugin
		project.extensions.getByType(CodeNarcExtension).identity {
			configFile = project.rootProject.with {
				file("$projectDir/conf/codenarc/codenarc.groovy").with {
					String tmpPath = "$projectDir/build/tmp"
					exists() ? it : file("$tmpPath/codenarc.groovy").tap {
						mkdir tmpPath
						createNewFile()
						write CODENARC_DEFAULT_RULESET
					}
				}
			}
			ignoreFailures = findProperty(project, 'codenarcIgnoreFailures', VALUE_FALSE).toBoolean()
			toolVersion = findProperty project, 'codenarcVersion', '2.1.0'
		}
		String groovyVersion = findProperty project, 'org.codehaus.groovy.version', GROOVY_VERSION
		// 由于codenarc插件内强制指定了groovy版本，groovy3.0需要强制指定版本
		if (groovyVersion.startsWith('3.')) {
			project.extensions.getByType(IHubBomExtension).groupVersions {
				group 'org.codehaus.groovy' version groovyVersion
			}
		}
	}

	private static void configJacoco(Project project) {
		project.pluginManager.apply JacocoPlugin
		project.extensions.getByType(JacocoPluginExtension).identity {
			toolVersion = findProperty project, 'jacoco.version', '0.8.6'
		}

		/**
		 * 分支覆盖率达到100%
		 * 由于groovy在编译时会生成无效字节码，所以指令覆盖率无法达到100%，等待官方修复，详见
		 * https://github.com/jacoco/jacoco/issues/884
		 * http://groovy.329449.n5.nabble.com/Groovy-2-5-4-generates-dead-code-td5755188.html
		 */
		Task jacocoTestCoverageVerification = project.tasks.getByName('jacocoTestCoverageVerification').tap {
			violationRules {
				// rule #1：bundle分支覆盖率
				rule {
					enabled = findProperty('jacocoBundleBranchCoverageRuleEnabled', VALUE_TRUE).toBoolean()
					limit {
						counter = 'BRANCH'
						value = 'COVEREDRATIO'
						minimum = findProperty('jacocoBundleBranchCoveredRatio', '1.0') as BigDecimal
					}
				}
				// rule #2：bundle指令覆盖率
				rule {
					enabled = findProperty('jacocoBundleInstructionCoverageRuleEnabled', VALUE_TRUE).toBoolean()
					limit {
						minimum = findProperty('jacocoBundleInstructionCoveredRatio', '0.9') as BigDecimal
					}
				}
				// rule #3：package指令覆盖率
				rule {
					enabled = findProperty('jacocoPackageInstructionCoverageRuleEnabled', VALUE_TRUE).toBoolean()
					element = 'PACKAGE'
					limit {
						minimum = findProperty('jacocoPackageInstructionCoveredRatio', '0.9') as BigDecimal
					}
				}
			}
		}

		// 覆盖率报告排除main class
		Task jacocoTestReport = project.tasks.getByName('jacocoTestReport').tap {
			project.afterEvaluate {
				classDirectories.from = project.files(classDirectories.files.collect { dir ->
					project.fileTree dir: dir, exclude: findProperty('jacocoReportExclusion', '**/app/**/*.class')
				})
			}
		}

		// 一些任务依赖和属性设置
		project.check.dependsOn jacocoTestCoverageVerification
		project.test.finalizedBy jacocoTestReport, jacocoTestCoverageVerification
		project.tasks.withType(Test) {
			// 这是为了解决在项目根目录上执行test时Jacoco找不到依赖的类的问题
			systemProperties.'user.dir' = workingDir
		}
	}

}
