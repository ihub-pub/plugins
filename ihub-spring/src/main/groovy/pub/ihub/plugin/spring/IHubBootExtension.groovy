/*
 * Copyright (c) 2022-2023 the original author or authors.
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
package pub.ihub.plugin.spring

import groovy.transform.CompileStatic
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty
import pub.ihub.plugin.verification.IHubSystemProperties

import javax.inject.Inject

import static pub.ihub.plugin.IHubProperty.Type.ENV
import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM

/**
 * IHub Spring Boot Plugin Extension
 * @author henry
 */
@IHubExtension('iHubBoot')
@CompileStatic
class IHubBootExtension extends IHubProjectExtensionAware implements IHubSystemProperties {

    //<editor-fold desc="BootRun Configuration">

    /**
     * bootRun属性
     */
    MapProperty<String, String> runProperties
    /**
     * 运行时包含系统属性名称（“,”分割,支持通配符“*”）
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> runIncludePropNames
    /**
     * 运行时排除系统属性名称（“,”分割,支持通配符“*”）
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    Property<String> runSkippedPropNames
    /**
     * 启用本地属性
     */
    @IHubProperty(genericType = Boolean)
    Property<Boolean> enabledLocalProperties
    /**
     * 优化启动
     */
    @IHubProperty(type = [PROJECT, SYSTEM], genericType = Boolean)
    Property<Boolean> runOptimizedLaunch

    //</editor-fold>

    //<editor-fold desc="BootJar Configuration">

    /**
     * 配置需要解包的库
     */
    @IHubProperty
    Property<String> bootJarRequiresUnpack

    //</editor-fold>

    //<editor-fold desc="Build Configuration">

    /**
     * JVM版本
     */
    @IHubProperty
    Property<String> bpJvmVersion
    /**
     * 是否在构建前清理缓存
     */
    @IHubProperty(genericType = Boolean)
    Property<Boolean> bpCleanCache
    /**
     * 启用构建器操作的详细日志记录
     */
    @IHubProperty(genericType = Boolean)
    Property<Boolean> bpVerboseLogging
    /**
     * 是否将生成的镜像发布到Docker仓库
     */
    @IHubProperty(genericType = Boolean)
    Property<Boolean> bpPublish
    /**
     * http代理
     */
    @IHubProperty
    Property<String> httpProxy
    /**
     * https代理
     */
    @IHubProperty
    Property<String> httpsProxy

    //</editor-fold>

    //<editor-fold desc="Launch Configuration">

    /**
     * JVM内存
     */
    @IHubProperty
    Property<String> bplJvmHeadRoom
    /**
     * JVM运行时已加载类的数量，默认“35% of classes"
     */
    @IHubProperty
    Property<String> bplJvmLoadedClassCount
    /**
     * JVM运行时用户线程数，默认“250”
     */
    @IHubProperty
    Property<String> bplJvmThreadCount
    /**
     * JVM环境变量
     */
    @IHubProperty
    Property<String> javaToolOptions
    /**
     * JVM运行时变量
     * 参考：https://paketo.io/docs/reference/configuration/
     */
    MapProperty<String, String> bpeEnvironment

    //</editor-fold>

    //<editor-fold desc="Docker Registry Configuration">

//    /**
//     * Docker守护程序的主机和端口的url
//     */
//    @IHubProperty
//    Property<String> dockerHost
//    /**
//     * 启用安全https协议
//     */
//    @IHubProperty(genericType = Boolean)
//    Property<Boolean> dockerTlsVerify
//    /**
//     * https证书和密钥文件的路径
//     */
//    @IHubProperty
//    Property<String> dockerCertPath
//    /**
//     * Docker私有镜像仓库地址
//     */
//    @IHubProperty
//    Property<String> dockerUrl
//    /**
//     * Docker私有镜像仓库用户名
//     */
//    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
//    Property<String> dockerUsername
//    /**
//     * Docker私有镜像仓库密码
//     */
//    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
//    Property<String> dockerPassword
//    /**
//     * Docker私有镜像仓库邮箱
//     */
//    @IHubProperty
//    Property<String> dockerEmail
//    /**
//     * Docker私有镜像仓库身份令牌
//     */
//    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
//    Property<String> dockerToken

    //</editor-fold>

    @Inject
    IHubBootExtension(ObjectFactory objectFactory) {
        runProperties = objectFactory.mapProperty(String, String)
        runIncludePropNames = objectFactory.property(String)
        runSkippedPropNames = objectFactory.property(String)
        enabledLocalProperties = objectFactory.property(Boolean).convention(true)
        runOptimizedLaunch = objectFactory.property(Boolean).convention(true)
        bootJarRequiresUnpack = objectFactory.property(String).convention('')

        bpJvmVersion = objectFactory.property(String)
        bpCleanCache = objectFactory.property(Boolean).convention(false)
        bpVerboseLogging = objectFactory.property(Boolean).convention(false)
        bpPublish = objectFactory.property(Boolean).convention(false)
        httpProxy = objectFactory.property(String)
        httpsProxy = objectFactory.property(String)

        bplJvmHeadRoom = objectFactory.property(String).convention('8G')
        bplJvmLoadedClassCount = objectFactory.property(String)
        bplJvmThreadCount = objectFactory.property(String)
        javaToolOptions = objectFactory.property(String)
        bpeEnvironment = objectFactory.mapProperty(String, String).convention([:])
    }

    Map<String, String> getEnvironment() {
        [
            BP_JVM_VERSION            : bpJvmVersion.orNull,
            HTTP_PROXY                : httpProxy.orNull,
            HTTPS_PROXY               : httpsProxy.orNull,
            BPL_JVM_HEAD_ROOM         : bplJvmHeadRoom.get(),
            BPL_JVM_LOADED_CLASS_COUNT: bplJvmLoadedClassCount.orNull,
            BPL_JVM_THREAD_COUNT      : bplJvmThreadCount.orNull,
            JAVA_TOOL_OPTIONS         : javaToolOptions.orNull,
        ].findAll { it.value } + bpeEnvironment.get()
    }

}
