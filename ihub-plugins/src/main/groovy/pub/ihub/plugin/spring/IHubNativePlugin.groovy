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

import org.springframework.aot.gradle.SpringAotGradlePlugin
import org.springframework.aot.gradle.dsl.SpringAotExtension
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware

import static pub.ihub.plugin.IHubProjectPluginAware.EvaluateStage.AFTER



/**
 * 原生镜像插件
 * 参考官方入门文档：https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/
 * @author henry
 */
@IHubPlugin(value = IHubNativeExtension, beforeApplyPlugins = [IHubBootPlugin, SpringAotGradlePlugin])
class IHubNativePlugin extends IHubProjectPluginAware<IHubNativeExtension> {

    @Override
    void apply() {
        withExtension(AFTER) { ext ->
            withTask(BootBuildImage) {
                it.builder = 'paketobuildpacks/builder:tiny'
                it.environment = ext.environment
            }
            withExtension(SpringAotExtension) {
                it.mode.set ext.aotMode
                it.debugVerify.set ext.aotDebugVerify
                it.removeXmlSupport.set ext.aotRemoveXmlSupport
                it.removeSpelSupport.set ext.aotRemoveSpelSupport
                it.removeYamlSupport.set ext.aotRemoveYamlSupport
                it.removeJmxSupport.set ext.aotRemoveJmxSupport
                it.verify.set ext.aotVerify
            }
        }
    }

}
