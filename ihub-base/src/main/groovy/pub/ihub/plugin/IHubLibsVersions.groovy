/*
 * Copyright (c) 2023 the original author or authors.
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
package pub.ihub.plugin

import groovy.transform.CompileStatic
import org.gradle.api.JavaVersion

import static org.codehaus.groovy.runtime.ResourceGroovyMethods.readLines
import static org.gradle.api.JavaVersion.current

/**
 * @author henry
 */
@CompileStatic
class IHubLibsVersions {

    static final String IHUB_PLUGINS_LOCAL_VERSION = 'iHub.iHubPluginsLocalVersion'
    static final String IHUB_LIBS_LOCAL_VERSION = 'iHub.iHubLibsLocalVersion'

    static final Map<String, String> LIBS_VERSIONS =
        readLines(IHubLibsVersions.classLoader.getResource('META-INF/ihub/libs-versions'))
            .collectEntries { it.split '=' }

    static String getLibsVersion(String name) {
        LIBS_VERSIONS[name]
    }

    static String getCompatibleLibsVersion(String name) {
        current().isCompatibleWith(JavaVersion.VERSION_17) ? getLibsVersion(name) : getLibsVersion(name) + '-java11'
    }

    static String getIHubPluginsVersion() {
        getLibsVersion 'ihub-plugins'
    }

    static String getIHubLibsVersion() {
        getCompatibleLibsVersion 'ihub-libs'
    }

}
