/*
 * Copyright (c) 2021 Henry 李恒 (henry.box@outlook.com).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pub.ihub.plugin.copyright

import cn.hutool.core.util.EscapeUtil
import cn.hutool.core.util.XmlUtil
import org.gradle.api.Project
import org.w3c.dom.Document
import org.w3c.dom.Node
import pub.ihub.plugin.IHubPlugin
import pub.ihub.plugin.IHubPluginsPlugin
import pub.ihub.plugin.IHubProjectPluginAware



/**
 * Gradle版权插件
 * @author liheng
 */
@IHubPlugin(beforeApplyPlugins = IHubPluginsPlugin)
class IHubCopyrightPlugin extends IHubProjectPluginAware {

    private static final String COPYRIGHT_NAME = 'ihub'

    @Override
    protected void apply() {
        Project rootProject = project.rootProject
        if (!rootProject.file('.idea').exists()) {
            return
        }
        // 通过COPYRIGHT文件获取版权信息
        String copyright = rootProject.file('COPYRIGHT').with { exists() ? text : null }
        if (copyright) {
            configCopyright rootProject, copyright
            return
        }
        logger.lifecycle 'The COPYRIGHT file does not exist and will use the LICENSE information'
        // 通过LICENSE文件提取版权信息
        File license = rootProject.file 'LICENSE'
        if (license.exists()) {
            configCopyright rootProject, IHubCopyright.matchCopyright(license.text)?.copyright
        } else {
            logger.warn 'The LICENSE file does not exist and will use the default copyright information'
        }
    }

    private static void configCopyright(Project rootProject, String notice) {
        rootProject.mkdir '.idea/copyright'
        Document component = XmlUtil.createXml 'component'
        Node copyright = XmlUtil.appendChild component.documentElement.tap {
            setAttribute 'name', 'CopyrightManager'
        }, 'copyright'
        appendOption copyright, 'myName', COPYRIGHT_NAME
        notice = notice?.trim()
        if (notice) {
            appendOption copyright, 'notice', EscapeUtil.escapeXml(notice).replaceAll('\n', '&#10;')
        }
        XmlUtil.toFile component, rootProject.file(".idea/copyright/${COPYRIGHT_NAME}.xml").path
        configCopyrightManager rootProject
    }

    private static void configCopyrightManager(Project rootProject) {
        File settings = rootProject.file '.idea/copyright/profiles_settings.xml'
        List modules = ['Project Files', 'All Changed Files']
        Document component
        if (settings.exists()) {
            component = XmlUtil.readXML settings.path
            Node module2copyright = XmlUtil.getNodeByXPath 'module2copyright',
                XmlUtil.getNodeByXPath('settings', component.documentElement)
            modules.each { module ->
                if (!XmlUtil.getNodeByXPath("element[@module=\"$module\"]", module2copyright)) {
                    appendChild module2copyright, 'element', [module: module, copyright: COPYRIGHT_NAME]
                }
            }
        } else {
            component = XmlUtil.createXml 'component'
            Node module2copyright = appendChild appendChild(component.documentElement.tap {
                setAttribute 'name', 'CopyrightManager'
            }, 'settings'), 'module2copyright'
            modules.each { module ->
                appendChild module2copyright, 'element', [module: module, copyright: COPYRIGHT_NAME]
            }
        }
        XmlUtil.toFile component, settings.path
    }

    private static Node appendChild(Node node, String name, Map<String, String> attributes = [:]) {
        XmlUtil.appendChild(node, name).tap {
            attributes.each { k, v ->
                setAttribute k, v
            }
        }
    }

    private static Node appendOption(Node node, String name, String value) {
        appendChild node, 'option', [name: name, value: value]
    }

}
