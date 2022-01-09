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
import net.bytebuddy.description.type.TypeDescription



/**
 * 增强编译插件特征
 * @author henry
 */
@CompileStatic
@SuppressWarnings('CloseWithoutCloseable')
trait IHubByteBuddyPluginSupport implements Plugin {

    Map config

    @Override
    void close() throws IOException {
    }

    static boolean isAnnotatedWith(TypeDescription type, Class<?> annotationType) {
        type.declaredAnnotations.asTypeList().any { it.isAssignableTo annotationType }
    }

}
