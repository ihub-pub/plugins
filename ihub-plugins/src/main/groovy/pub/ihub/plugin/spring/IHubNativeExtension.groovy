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
package pub.ihub.plugin.spring

import groovy.transform.TupleConstructor
import org.springframework.aot.gradle.dsl.AotMode
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty

import static org.springframework.aot.gradle.dsl.AotMode.NATIVE
import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM



/**
 * 原生镜像插件扩展
 * @author henry
 */
@IHubExtension('iHubNative')
@TupleConstructor(allProperties = true, includes = 'project')
class IHubNativeExtension implements IHubProjectExtensionAware {

    //<editor-fold desc="Build Configuration">

    /**
     * JVM版本
     */
    @IHubProperty
    String bpJvmVersion
    /**
     * 是否启用原生映像构建
     */
    @IHubProperty
    boolean bpNativeImage = true
    /**
     * 传递给原生映像命令的参数
     */
    @IHubProperty
    String bpNativeImageBuildArguments

    //</editor-fold>

    //<editor-fold desc="AOT Configuration">

    /**
     * native镜像编译器配置
     *
     * NATIVE （默认）为本机图像以及替换提供资源、初始化、代理和反射（使用自动配置提示）配置
     * NATIVE-INIT 如果只希望提供初始化配置和替换，则应使用
     * NATIVE-AGENT 正在使用跟踪代理生成的配置作为基础，并为控制器等组件提供额外的提示
     */
    @IHubProperty
    AotMode aotMode = NATIVE
    /**
     * 启用验证调试
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean aotDebugVerify = false
    /**
     * 移除XML支持
     */
    @IHubProperty
    boolean aotRemoveXmlSupport = true
    /**
     * 移除Spel支持
     */
    @IHubProperty
    boolean aotRemoveSpelSupport = false
    /**
     * 移除Yaml支持
     */
    @IHubProperty
    boolean aotRemoveYamlSupport = false
    /**
     * 移除Jmx支持
     */
    @IHubProperty
    boolean aotRemoveJmxSupport = true
    /**
     * 开启自动验证
     */
    @IHubProperty
    boolean aotVerify = true
    /**
     * 移除未使用的配置
     */
    @IHubProperty
    boolean aotRemoveUnusedConfig = true
    /**
     * 如果没有为活动选择器提供提示，则抛出错误
     */
    @IHubProperty
    boolean aotFailOnMissingSelectorHint = true

    //</editor-fold>

    //<editor-fold desc="Launch Configuration">

    /**
     * JVM内存
     */
    @IHubProperty
    String bplJvmHeadRoom = '8G'
    /**
     * JVM运行时已加载类的数量，默认“35% of classes"
     */
    @IHubProperty
    String bplJvmLoadedClassCount
    /**
     * JVM运行时用户线程数，默认“250”
     */
    @IHubProperty
    String bplJvmThreadCount
    /**
     * JVM环境变量
     */
    @IHubProperty
    String javaToolOptions

    //</editor-fold>

    Map<String, String> getEnvironment() {
        [
            BP_JVM_VERSION                 : bpJvmVersion,
            BP_NATIVE_IMAGE                : bpNativeImage.toString(),
            BP_NATIVE_IMAGE_BUILD_ARGUMENTS: bpNativeImageBuildArguments,
            BPL_JVM_HEAD_ROOM              : bplJvmHeadRoom,
            BPL_JVM_LOADED_CLASS_COUNT     : bplJvmLoadedClassCount,
            BPL_JVM_THREAD_COUNT           : bplJvmThreadCount,
            JAVA_TOOL_OPTIONS              : javaToolOptions,
        ].findAll { it.value }
    }

}
