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

import org.gradle.api.JavaVersion
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @author henry
 */
object IHubLibsVersions {

    const val IHUB_PLUGINS_LOCAL_VERSION = "iHub.iHubPluginsLocalVersion"
    const val IHUB_LIBS_LOCAL_VERSION = "iHub.iHubLibsLocalVersion"

    val LIBS_VERSIONS: Map<String, String> =
        BufferedReader(InputStreamReader(IHubLibsVersions::class.java.classLoader.getResourceAsStream("META-INF/ihub/libs-versions")!!)).use { reader ->
            reader.readLines().associate {
                val parts = it.split("=")
                parts[0] to parts[1]
            }
        }

    fun getLibsVersion(name: String): String? {
        return LIBS_VERSIONS[name]
    }

    fun getCompatibleLibsVersion(name: String): String? {
        return if (JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
            getLibsVersion(name)
        } else {
            getLibsVersion(name).let {
                if (it != null) "$it-java11" else null
            }
        }
    }

    fun getIHubPluginsVersion(): String? {
        return getLibsVersion("ihub-plugins")
    }

    fun getIHubLibsVersion(): String? {
        return getCompatibleLibsVersion("ihub-libs")
    }
}
