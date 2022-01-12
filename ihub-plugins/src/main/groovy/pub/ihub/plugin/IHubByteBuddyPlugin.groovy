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
package pub.ihub.plugin

import groovy.transform.CompileStatic
import net.bytebuddy.build.Plugin
import net.bytebuddy.build.Plugin.WithPreprocessor
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.DynamicType.Builder

import static groovy.transform.TypeCheckingMode.SKIP



/**
 * 增强编译插件
 * @author henry
 */
@CompileStatic
@SuppressWarnings(['UnnecessaryGetter', 'CloseWithoutCloseable'])
class IHubByteBuddyPlugin implements IHubByteBuddyPluginSupport, WithPreprocessor {

    private final List<? extends Plugin> plugins = []
    private final List<? extends WithPreprocessor> pluginsWithPreprocessor = []
    private final Map<TypeDescription, List<? extends Plugin>> delegates = [:]

    IHubByteBuddyPlugin(Map<? extends Class<? extends Plugin>, Map> plugins) {
        plugins.each { clazz, config ->
            clazz.getDeclaredConstructor().newInstance().with { plugin ->
                if (plugin instanceof IHubByteBuddyPluginSupport) {
                    plugin.config = config
                }
                if (plugin instanceof WithPreprocessor) {
                    this.pluginsWithPreprocessor.add plugin
                } else {
                    this.plugins.add plugin
                }
            }
        }
    }

    @Override
    void onPreprocess(TypeDescription typeDescription, ClassFileLocator classFileLocator) {
        pluginsWithPreprocessor*.onPreprocess typeDescription, classFileLocator
    }

    @CompileStatic(SKIP)
    @Override
    Builder<?> apply(Builder<?> builder, TypeDescription description, ClassFileLocator locator) {
        delegates.get(description)?.inject(builder) { b, p -> p.apply b, description, locator } ?: builder
    }

    @CompileStatic(SKIP)
    @Override
    boolean matches(TypeDescription description) {
        delegates.putIfAbsent description, (plugins + pluginsWithPreprocessor).findAll { it.matches description }
        delegates.get description
    }

    @Override
    void close() throws IOException {
        plugins*.close()
        pluginsWithPreprocessor*.close()
    }

}
