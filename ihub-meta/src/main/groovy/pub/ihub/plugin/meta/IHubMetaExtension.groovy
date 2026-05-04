/*
 * Copyright (c) 2021-2024 the original author or authors.
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
package pub.ihub.plugin.meta

import groovy.transform.CompileStatic
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

import javax.inject.Inject

/**
 * IHub AI Metadata 插件扩展，控制项目元数据 JSON 的生成行为。
 *
 * @author henry
 */
@CompileStatic
class IHubMetaExtension {

    /**
     * 是否启用元数据生成（默认开启）
     */
    final Property<Boolean> enabled

    /**
     * JSON 输出文件（默认：{buildDir}/ihub/project-meta.json）
     */
    final RegularFileProperty outputFile

    /**
     * 是否包含依赖信息（默认开启）
     */
    final Property<Boolean> includeDependencies

    /**
     * 是否包含 SourceSet 信息（默认开启）
     */
    final Property<Boolean> includeSourceSets

    /**
     * 是否将项目已解析依赖与 IHub 能力目录进行语义匹配，并在输出中附加组件语义信息（默认关闭）。
     * 启用后，输出 JSON 中每条依赖将附加 catalog 中对应组件的 description、use_case、domain 等字段。
     */
    final Property<Boolean> includeCatalogContext

    /**
     * IHub 能力目录 catalog.json 的路径（默认：项目根目录下 gradle/ihub-catalog/catalog.json）。
     * 仅在 {@code includeCatalogContext = true} 时生效。
     */
    final RegularFileProperty catalogFile

    @Inject
    IHubMetaExtension(ObjectFactory objects) {
        enabled = objects.property(Boolean).convention(true)
        outputFile = objects.fileProperty()
        includeDependencies = objects.property(Boolean).convention(true)
        includeSourceSets = objects.property(Boolean).convention(true)
        includeCatalogContext = objects.property(Boolean).convention(false)
        catalogFile = objects.fileProperty()
    }

}

