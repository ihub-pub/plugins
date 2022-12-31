/*
 * Copyright (c) 2022 Henry 李恒 (henry.box@outlook.com).
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

import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware
import pub.ihub.plugin.java.IHubJavaPlugin

import static org.springframework.boot.buildpack.platform.build.PullPolicy.IF_NOT_PRESENT
import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER



/**
 * IHub Spring Boot Plugin
 * 参考官方入门文档：https://docs.spring.io/spring-boot/docs/3.0.0/gradle-plugin/reference/htmlsingle/
 * @author henry
 */
@IHubPlugin(value = IHubBootExtension, beforeApplyPlugins = [IHubJavaPlugin, SpringBootPlugin])
@SuppressWarnings('UnnecessaryObjectReferences')
class IHubBootPlugin extends IHubProjectPluginAware<IHubBootExtension> {

    @Override
    void apply() {
        withExtension(AFTER) { ext ->
            withTask(BootRun) {
                ext.systemProperties it, '.boot-java-local.properties'
                it.optimizedLaunch = ext.runOptimizedLaunch
            }

            withTask(BootJar) {
                it.requiresUnpack ext.bootJarRequiresUnpack
            }
            withTask('jar') {
                it.enabled = false
            }

            // 注：使用bootBuildImage时须禁用启动脚本
            withTask(BootBuildImage) {
                it.pullPolicy = IF_NOT_PRESENT
                it.environment = ext.environment
                it.cleanCache = ext.bpCleanCache
                it.verboseLogging = ext.bpVerboseLogging
                it.publish = ext.bpPublish
                it.docker {
                    host = ext.dockerHost
                    tlsVerify = ext.dockerTlsVerify
                    certPath = ext.dockerCertPath
                    builderRegistry {
                        url = ext.dockerUrl
                        username = ext.dockerUsername
                        password = ext.dockerPassword
                        email = ext.dockerEmail
                        token = ext.dockerToken
                    }
                    publishRegistry {
                        url = ext.dockerUrl
                        username = ext.dockerUsername
                        password = ext.dockerPassword
                        email = ext.dockerEmail
                        token = ext.dockerToken
                    }
                }
            }
        }
    }

}
