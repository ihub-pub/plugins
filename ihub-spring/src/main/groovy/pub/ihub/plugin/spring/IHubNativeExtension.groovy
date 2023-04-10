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

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty

import javax.inject.Inject

/**
 * 原生镜像插件扩展
 * @author henry
 */
@IHubExtension('iHubNative')
class IHubNativeExtension extends IHubProjectExtensionAware {

    //<editor-fold desc="Build Configuration">

    /**
     * 是否启用原生映像构建
     */
    @IHubProperty(genericType = Boolean)
    Property<Boolean> bpNativeImage
    /**
     * 传递给原生映像命令的参数
     */
    @IHubProperty
    Property<String> bpNativeImageBuildArguments

    //</editor-fold>

    @Inject
    IHubNativeExtension(ObjectFactory objectFactory) {
        bpNativeImage = objectFactory.property(Boolean).convention(true)
        bpNativeImageBuildArguments = objectFactory.property(String)
    }

    Map<String, String> getEnvironment() {
        [
            BP_NATIVE_IMAGE                : bpNativeImage.get().toString(),
            BP_NATIVE_IMAGE_BUILD_ARGUMENTS: bpNativeImageBuildArguments.orNull,
        ].findAll { it.value }
    }

}
