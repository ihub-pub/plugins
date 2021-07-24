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

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty
import pub.ihub.plugin.IHubSystemProperties

import static pub.ihub.plugin.IHubProperty.Type.ENV
import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM



/**
 * IHub Spring Boot Plugin Extension
 * @author henry
 */
@IHubExtension('iHubBoot')
@CompileStatic
@TupleConstructor(allProperties = true, includes = 'project')
class IHubBootExtension implements IHubProjectExtensionAware, IHubSystemProperties {

    //<editor-fold desc="BootRun Configuration">

    /**
     * bootRun属性
     */
    Map<String, String> runProperties = [:]
    /**
     * 运行时包含系统属性名称（“,”分割）
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    String runIncludePropNames
    /**
     * 运行时排除系统属性名称（“,”分割）
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    String runSkippedPropNames
    /**
     * 启用本地属性
     */
    @IHubProperty
    boolean enabledLocalProperties = true
    /**
     * 优化启动
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean runOptimizedLaunch = true

    //</editor-fold>

    //<editor-fold desc="BootJar Configuration">

    /**
     * 配置需要解包的库
     */
    @IHubProperty
    String bootJarRequiresUnpack = ''

    //</editor-fold>

    //<editor-fold desc="Build Configuration">

    /**
     * JVM版本
     */
    @IHubProperty
    String bpJvmVersion
    /**
     * 是否在构建前清理缓存
     */
    @IHubProperty
    boolean bpCleanCache = false
    /**
     * 启用构建器操作的详细日志记录
     */
    @IHubProperty
    boolean bpVerboseLogging = false
    /**
     * 是否将生成的镜像发布到Docker仓库
     */
    @IHubProperty
    boolean bpPublish = false
    /**
     * http代理
     */
    @IHubProperty
    String httpProxy
    /**
     * https代理
     */
    @IHubProperty
    String httpsProxy

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
    /**
     * JVM运行时变量
     * 参见：https://paketo.io/docs/reference/configuration/
     */
    Map<String, String> bpeEnvironment = [:]

    //</editor-fold>

    //<editor-fold desc="Docker Registry Configuration">

    /**
     * Docker守护程序的主机和端口的url
     */
    @IHubProperty
    String dockerHost
    /**
     * 启用安全https协议
     */
    @IHubProperty
    boolean dockerTlsVerify = false
    /**
     * https证书和密钥文件的路径
     */
    @IHubProperty
    String dockerCertPath
    /**
     * Docker私有镜像仓库地址
     */
    @IHubProperty
    String dockerUrl
    /**
     * Docker私有镜像仓库用户名
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    String dockerUsername
    /**
     * Docker私有镜像仓库密码
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    String dockerPassword
    /**
     * Docker私有镜像仓库邮箱
     */
    @IHubProperty
    String dockerEmail
    /**
     * Docker私有镜像仓库身份令牌
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    String dockerToken

    //</editor-fold>

    Map<String, String> getEnvironment() {
        [
            BP_JVM_VERSION            : bpJvmVersion,
            HTTP_PROXY                : httpProxy,
            HTTPS_PROXY               : httpsProxy,
            BPL_JVM_HEAD_ROOM         : bplJvmHeadRoom,
            BPL_JVM_LOADED_CLASS_COUNT: bplJvmLoadedClassCount,
            BPL_JVM_THREAD_COUNT      : bplJvmThreadCount,
            JAVA_TOOL_OPTIONS         : javaToolOptions,
        ].findAll { it.value } + bpeEnvironment
    }

}
