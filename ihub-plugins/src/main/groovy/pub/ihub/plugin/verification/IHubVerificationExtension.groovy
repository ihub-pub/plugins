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

    //<editor-fold desc="PMD Configuration">

    String pmdRulesetFile
    boolean pmdConsoleOutput = false
    boolean pmdIgnoreFailures = false
    String pmdVersion = '6.35.0'

    //</editor-fold>

    //<editor-fold desc="Codenarc Configuration">

    String codenarcFile
    boolean codenarcIgnoreFailures = false
    String codenarcVersion = '2.1.0'

    //</editor-fold>

    //<editor-fold desc="Jacoco Configuration">

    String jacocoVersion = '0.8.7'
    boolean jacocoBundleBranchCoverageRuleEnabled = true
    String jacocoBundleBranchCoveredRatio = '0.9'
    boolean jacocoBundleInstructionCoverageRuleEnabled = true
    String jacocoBundleInstructionExclusion = '**/app,**/config'
    String jacocoBundleInstructionCoveredRatio = '0.9'
    boolean jacocoPackageInstructionCoverageRuleEnabled = true
    String jacocoPackageInstructionExclusion = '*.app,*.config'
    String jacocoPackageInstructionCoveredRatio = '0.9'
    String jacocoReportExclusion = '**/Application.class,**/app/*.class,**/config/*.class'

    //</editor-fold>

    boolean getPmdConsoleOutput() {
        findProperty 'pmdConsoleOutput', pmdConsoleOutput
    }

    boolean getPmdIgnoreFailures() {
        findProperty 'pmdIgnoreFailures', pmdIgnoreFailures
    }

    String getPmdVersion() {
        findProperty 'pmdVersion', pmdVersion
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

    String getJacocoBundleInstructionExclusion() {
        findSystemProperty 'jacocoBundleInstructionExclusion', jacocoBundleInstructionExclusion
    }

    String getJacocoBundleInstructionCoveredRatio() {
        findSystemProperty 'jacocoBundleInstructionCoveredRatio', jacocoBundleInstructionCoveredRatio
    }

    boolean getJacocoPackageInstructionCoverageRuleEnabled() {
        findSystemProperty 'jacocoPackageInstructionCoverageRuleEnabled', jacocoPackageInstructionCoverageRuleEnabled
    }

    String getJacocoPackageInstructionExclusion() {
        findSystemProperty 'jacocoPackageInstructionExclusion', jacocoPackageInstructionExclusion
    }

    String getJacocoPackageInstructionCoveredRatio() {
        findSystemProperty 'jacocoPackageInstructionCoveredRatio', jacocoPackageInstructionCoveredRatio
    }

    String getJacocoReportExclusion() {
        findSystemProperty 'jacocoReportExclusion', jacocoReportExclusion
    }

}
