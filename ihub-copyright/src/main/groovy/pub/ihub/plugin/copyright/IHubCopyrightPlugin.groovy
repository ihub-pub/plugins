/*
 * Copyright (c) 2021-2025 the original author or authors.
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
package pub.ihub.plugin.copyright

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import org.gradle.api.Project
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubProjectPluginAware


/**
 * Gradle版权插件
 * 基于Spotless插件实现自动为源代码添加或更新版权信息
 * @author liheng
 */
@IHubPlugin(value = IHubCopyrightExtension, beforeApplyPlugins = [SpotlessPlugin])
@SuppressWarnings('JavaIoPackageAccess')
class IHubCopyrightPlugin extends IHubProjectPluginAware<IHubCopyrightExtension> {

    @Override
    protected void apply() {
        Project rootProject = project.rootProject

        String copyrightHeader = resolveCopyrightHeader(rootProject)

        if (!copyrightHeader) {
            logger.warn 'No copyright header found. Please create COPYRIGHT or LICENSE file in root project.'
            return
        }

        configSpotlessExtension copyrightHeader
    }

    private String resolveCopyrightHeader(Project rootProject) {
        String copyright = rootProject.file('COPYRIGHT').with { exists() ? text : null }
        if (copyright) {
            logger.lifecycle 'Using copyright header from COPYRIGHT file'
            return copyright.trim()
        }

        logger.lifecycle 'The COPYRIGHT file does not exist and will use the LICENSE information'

        File license = rootProject.file('LICENSE')
        if (license.exists()) {
            IHubCopyright copyrightTemplate = IHubCopyright.matchCopyright(license.text)
            if (copyrightTemplate) {
                logger.lifecycle "Using copyright header from LICENSE file (${copyrightTemplate.name})"
                return copyrightTemplate.header
            }
            logger.warn 'LICENSE file exists but no matching copyright template found'
        } else {
            logger.warn 'The LICENSE file does not exist and will use the default copyright information'
        }

        null
    }

    private void configSpotlessExtension(String copyrightHeader) {
        withExtension(SpotlessExtension) { spotless ->
            spotless.with {
                String formattedHeader = formatCopyrightHeader(copyrightHeader)

                java {
                    licenseHeader formattedHeader
                    target 'src/*/java/**/*.java'
                    targetExclude '**/generated/**'
                }

                groovy {
                    licenseHeader formattedHeader
                    target 'src/*/groovy/**/*.groovy'
                    targetExclude '**/generated/**'
                }

                kotlin {
                    licenseHeader formattedHeader
                    target 'src/*/kotlin/**/*.kt'
                    targetExclude '**/generated/**'
                }

                if (extension.enableIdea.get()) {
                    configIdeaCopyright copyrightHeader
                }
            }
        }
    }

    private String formatCopyrightHeader(String copyrightHeader) {
        int currentYear = Calendar.instance.get(Calendar.YEAR)
        String header = copyrightHeader
            .replace('$today.year', String.valueOf(currentYear))
            .trim()
        String copyrightLine = "Copyright (c) $currentYear the original author or authors."
        String content = header.replaceAll(/(?m)^Copyright \(c\).*\n?/, '').trim()
        content ? "$copyrightLine\n\n$content" : copyrightLine
    }

    private void configIdeaCopyright(String copyrightHeader) {
        Project rootProject = project.rootProject
        if (!rootProject.file('.idea').exists()) {
            return
        }

        File copyrightDir = rootProject.file('.idea/copyright')
        if (!copyrightDir.exists()) {
            copyrightDir.mkdirs()
        }

        String copyrightName = rootProject.name
        File copyrightFile = new File(copyrightDir, "${copyrightName}.xml")

        int currentYear = Calendar.instance.get(Calendar.YEAR)
        String formattedNotice = copyrightHeader
            .replace('$today.year', String.valueOf(currentYear))
            .trim()

        copyrightFile.text = """<component name="CopyrightManager">
  <copyright>
    <option name="myName" value="${copyrightName}" />
    <option name="notice" value="${escapeXml(formattedNotice)}" />
  </copyright>
</component>"""

        File profilesSettings = rootProject.file('.idea/copyright/profiles_settings.xml')
        if (!profilesSettings.exists()) {
            profilesSettings.text = """<component name="CopyrightManager">
  <settings>
    <module2copyright>
      <element module="Project Files" copyright="${copyrightName}" />
    </module2copyright>
    <LanguageOptions name="__TEMPLATE__">
      <option name="addBlankAfter" value="false" />
    </LanguageOptions>
  </settings>
</component>"""
        }
    }

    private static String escapeXml(String text) {
        text?.replaceAll(/&/, '&amp;')
            ?.replaceAll(/</, '&lt;')
            ?.replaceAll(/>/, '&gt;')
            ?.replaceAll(/"/, '&quot;')
            ?.replaceAll(/'/, '&apos;')
            ?: ''
    }

}
