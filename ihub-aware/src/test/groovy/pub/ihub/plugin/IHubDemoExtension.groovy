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
package pub.ihub.plugin

import groovy.transform.TupleConstructor

import static pub.ihub.plugin.IHubProperty.Type.ENV
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM



/**
 * @author henry
 */
@IHubExtension('iHubDemo')
@SuppressWarnings('GetterMethodCouldBeProperty')
@TupleConstructor(allProperties = true, includes = 'project')
class IHubDemoExtension implements IHubProjectExtensionAware, IHubExtProperty, IHubProjectProperty {

    boolean flag = false

    @IHubProperty
    String str = 'text'

    @IHubProperty(type = SYSTEM)
    String system = 'system'

    @IHubProperty(type = ENV)
    String env = 'env'

    String getGetMethod() {
        'method'
    }

}
