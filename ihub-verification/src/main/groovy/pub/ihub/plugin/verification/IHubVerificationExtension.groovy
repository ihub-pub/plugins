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

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import pub.ihub.plugin.IHubExtProperty
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty

import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM



/**
 * 代码检查插件扩展
 * @author henry
 */
@IHubExtension('iHubVerification')
@CompileStatic
@TupleConstructor(allProperties = true, includes = 'project')
class IHubVerificationExtension implements IHubProjectExtensionAware, IHubExtProperty {

    private static final String DEFAULT_PMD_VERSION = '6.55.0'
    private static final String DEFAULT_JACOCO_VERSION = '0.8.8'
    private static final String DEFAULT_CODENARC_VERSION = '3.2.0'

    //<editor-fold desc="PMD Configuration">

    /**
     * PMD检查是否打印控制台信息
     */
    @IHubProperty
    boolean pmdConsoleOutput = false
    /**
     * PMD检查是否忽略失败
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean pmdIgnoreFailures = false
    /**
     * PMD版本
     */
    @IHubProperty
    String pmdVersion = DEFAULT_PMD_VERSION

    //</editor-fold>

    //<editor-fold desc="Codenarc Configuration">

    /**
     * Codenarc检查是否忽略失败
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean codenarcIgnoreFailures = false
    /**
     * Codenarc版本
     */
    @IHubProperty
    String codenarcVersion = DEFAULT_CODENARC_VERSION

    //</editor-fold>

    //<editor-fold desc="Jacoco Configuration">

    /**
     * Jacoco版本
     */
    @IHubProperty
    String jacocoVersion = DEFAULT_JACOCO_VERSION
    /**
     * 是否启用bundle分支覆盖检查
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean jacocoBranchCoverageRuleEnabled = true
    /**
     * bundle分支覆盖率
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    String jacocoBranchCoveredRatio = '0.9'
    /**
     * 是否启用bundle指令覆盖检查
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean jacocoInstructionCoverageRuleEnabled = true
    /**
     * bundle指令覆盖排除目录
     */
    @IHubProperty
    String jacocoInstructionExclusion = '**/app,**/config'
    /**
     * bundle指令覆盖率
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    String jacocoInstructionCoveredRatio = '0.9'
    /**
     * 是否启用package指令覆盖检查
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean jacocoPackageCoverageRuleEnabled = true
    /**
     * package指令覆盖排除目录
     */
    @IHubProperty
    String jacocoPackageExclusion = '*.app,*.config'
    /**
     * package指令覆盖率
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    String jacocoPackageCoveredRatio = '0.9'
    /**
     * 覆盖率报告排除目录
     */
    @IHubProperty
    String jacocoReportExclusion = '**/Application.class,**/app/*.class,**/config/*.class'

    //</editor-fold>

}
