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

import groovy.transform.TupleConstructor
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty



/**
 * 原生镜像插件扩展
 * @author henry
 */
@IHubExtension('iHubNative')
@TupleConstructor(allProperties = true, includes = 'project')
class IHubNativeExtension implements IHubProjectExtensionAware {

    //<editor-fold desc="Build Configuration">

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

    Map<String, String> getEnvironment() {
        [
            BP_NATIVE_IMAGE                : bpNativeImage.toString(),
            BP_NATIVE_IMAGE_BUILD_ARGUMENTS: bpNativeImageBuildArguments,
        ].findAll { it.value }
    }

}
