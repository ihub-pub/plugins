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
package pub.ihub.plugin.verification

import groovy.transform.TupleConstructor
import pub.ihub.plugin.IHubProjectExtension



/**
 * 代码检查插件扩展
 * @author henry
 */
@TupleConstructor(includeSuperFields = true)
class IHubVerificationExtension extends IHubProjectExtension {

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

    static final String CODENARC_DEFAULT_RULESET = '''ruleset {
	description '全局默认CodeNarc规则集'

	ruleset('rulesets/basic.xml')
	ruleset('rulesets/braces.xml')
	ruleset('rulesets/comments.xml')
	ruleset('rulesets/concurrency.xml')
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

    //<editor-fold desc="PMD Configuration">

    String pmdRulesetFile
    boolean pmdConsoleOutput = false
    boolean pmdIgnoreFailures = false
    String pmdVersion = '6.31.0'

    //</editor-fold>

    //<editor-fold desc="Codenarc Configuration">

    String codenarcFile
    boolean codenarcIgnoreFailures = false
    String codenarcVersion = '2.1.0'

    //</editor-fold>

    //<editor-fold desc="Jacoco Configuration">

    String jacocoVersion = '0.8.6'
    boolean jacocoBundleBranchCoverageRuleEnabled = true
    String jacocoBundleBranchCoveredRatio = '1.0'
    boolean jacocoBundleInstructionCoverageRuleEnabled = true
    String jacocoBundleInstructionCoveredRatio = '0.9'
    boolean jacocoPackageInstructionCoverageRuleEnabled = true
    String jacocoPackageInstructionCoveredRatio = '0.9'
    String jacocoReportExclusion = '**/app/**/*.class'

    //</editor-fold>

    File getRootDir() {
        project.rootProject.projectDir
    }

    String getPmdRulesetFile() {
        findProperty 'pmdRulesetFile', pmdRulesetFile ?: "$rootDir/conf/pmd/ruleset.xml"
    }

    boolean getPmdConsoleOutput() {
        findProperty 'pmdConsoleOutput', pmdConsoleOutput
    }

    boolean getPmdIgnoreFailures() {
        findProperty 'pmdIgnoreFailures', pmdIgnoreFailures
    }

    String getPmdVersion() {
        findProperty 'pmdVersion', pmdVersion
    }

    String getCodenarcFile() {
        findProperty 'codenarcFile', codenarcFile ?: "$rootDir/conf/codenarc/codenarc.groovy"
    }

    boolean getCodenarcIgnoreFailures() {
        findProperty 'codenarcIgnoreFailures', codenarcIgnoreFailures
    }

    String getCodenarcVersion() {
        findProperty 'codenarcVersion', codenarcVersion
    }

    String getJacocoVersion() {
        findProperty 'jacocoVersion', jacocoVersion
    }

    boolean getJacocoBundleBranchCoverageRuleEnabled() {
        findSystemProperty 'jacocoBundleBranchCoverageRuleEnabled', jacocoBundleBranchCoverageRuleEnabled
    }

    String getJacocoBundleBranchCoveredRatio() {
        findSystemProperty 'jacocoBundleBranchCoveredRatio', jacocoBundleBranchCoveredRatio
    }

    boolean getJacocoBundleInstructionCoverageRuleEnabled() {
        findSystemProperty 'jacocoBundleInstructionCoverageRuleEnabled', jacocoBundleInstructionCoverageRuleEnabled
    }

    String getJacocoBundleInstructionCoveredRatio() {
        findSystemProperty 'jacocoBundleInstructionCoveredRatio', jacocoBundleInstructionCoveredRatio
    }

    boolean getJacocoPackageInstructionCoverageRuleEnabled() {
        findSystemProperty 'jacocoPackageInstructionCoverageRuleEnabled', jacocoPackageInstructionCoverageRuleEnabled
    }

    String getJacocoPackageInstructionCoveredRatio() {
        findSystemProperty 'jacocoPackageInstructionCoveredRatio', jacocoPackageInstructionCoveredRatio
    }

    String getJacocoReportExclusion() {
        findSystemProperty 'jacocoReportExclusion', jacocoReportExclusion
    }

}
