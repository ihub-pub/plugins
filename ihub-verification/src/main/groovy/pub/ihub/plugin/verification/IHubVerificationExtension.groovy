/*
 * Copyright (c) 2021-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pub.ihub.plugin.verification

import groovy.transform.CompileStatic
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import pub.ihub.plugin.IHubExtProperty
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty

import javax.inject.Inject

import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM

/**
 * 代码检查插件扩展
 * @author henry
 */
@IHubExtension('iHubVerification')
@CompileStatic
class IHubVerificationExtension extends IHubProjectExtensionAware implements IHubExtProperty {

    private static final String DEFAULT_PMD_VERSION = '6.55.0'
    private static final String DEFAULT_JACOCO_VERSION = '0.8.10'
    private static final String DEFAULT_CODENARC_VERSION = '3.2.0'

    //<editor-fold desc="PMD Configuration">

    /**
     * PMD检查是否打印控制台信息
     */
    @IHubProperty(genericType = Boolean)
    Property<Boolean> pmdConsoleOutput
    /**
     * PMD检查是否忽略失败
     */
    @IHubProperty(type = [PROJECT, SYSTEM], genericType = Boolean)
    Property<Boolean> pmdIgnoreFailures
    /**
     * PMD版本
     */
    @IHubProperty
    Property<String> pmdVersion

    //</editor-fold>

    //<editor-fold desc="Codenarc Configuration">

    /**
     * Codenarc检查是否忽略失败
     */
    @IHubProperty(type = [PROJECT, SYSTEM], genericType = Boolean)
    Property<Boolean> codenarcIgnoreFailures
    /**
     * Codenarc版本
     */
    @IHubProperty
    Property<String> codenarcVersion

    //</editor-fold>

    //<editor-fold desc="Jacoco Configuration">

    /**
     * Jacoco版本
     */
    @IHubProperty
    Property<String> jacocoVersion
    /**
     * 是否启用bundle分支覆盖检查
     */
    @IHubProperty(type = [PROJECT, SYSTEM], genericType = Boolean)
    Property<Boolean> jacocoBranchCoverageRuleEnabled
    /**
     * bundle分支覆盖率
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> jacocoBranchCoveredRatio
    /**
     * 是否启用bundle指令覆盖检查
     */
    @IHubProperty(type = [PROJECT, SYSTEM], genericType = Boolean)
    Property<Boolean> jacocoInstructionCoverageRuleEnabled
    /**
     * bundle指令覆盖排除目录
     */
    @IHubProperty
    Property<String> jacocoInstructionExclusion
    /**
     * bundle指令覆盖率
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> jacocoInstructionCoveredRatio
    /**
     * 是否启用package指令覆盖检查
     */
    @IHubProperty(type = [PROJECT, SYSTEM], genericType = Boolean)
    Property<Boolean> jacocoPackageCoverageRuleEnabled
    /**
     * package指令覆盖排除目录
     */
    @IHubProperty
    Property<String> jacocoPackageExclusion
    /**
     * package指令覆盖率
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> jacocoPackageCoveredRatio
    /**
     * 覆盖率报告排除目录
     */
    @IHubProperty
    Property<String> jacocoReportExclusion

    //</editor-fold>

    @Inject
    IHubVerificationExtension(ObjectFactory objectFactory) {
        pmdConsoleOutput = objectFactory.property(Boolean).convention(false)
        pmdIgnoreFailures = objectFactory.property(Boolean).convention(false)
        pmdVersion = objectFactory.property(String).convention(DEFAULT_PMD_VERSION)

        codenarcIgnoreFailures = objectFactory.property(Boolean).convention(false)
        codenarcVersion = objectFactory.property(String).convention(DEFAULT_CODENARC_VERSION)

        jacocoVersion = objectFactory.property(String).convention(DEFAULT_JACOCO_VERSION)
        jacocoBranchCoverageRuleEnabled = objectFactory.property(Boolean).convention(true)
        jacocoBranchCoveredRatio = objectFactory.property(String).convention('0.9')
        jacocoInstructionCoverageRuleEnabled = objectFactory.property(Boolean).convention(true)
        jacocoInstructionExclusion = objectFactory.property(String).convention('**/app,**/config')
        jacocoInstructionCoveredRatio = objectFactory.property(String).convention('0.9')
        jacocoPackageCoverageRuleEnabled = objectFactory.property(Boolean).convention(true)
        jacocoPackageExclusion = objectFactory.property(String).convention('*.app,*.config')
        jacocoPackageCoveredRatio = objectFactory.property(String).convention('0.9')
        jacocoReportExclusion = objectFactory.property(String).convention('**/Application.class,**/app/*.class,**/config/*.class')
    }

}
