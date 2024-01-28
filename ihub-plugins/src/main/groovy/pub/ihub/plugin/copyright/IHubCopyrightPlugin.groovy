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
package pub.ihub.plugin.copyright

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
        String copyrightName = rootProject.name
        Document component = XmlUtil.createXml 'component'
        Node copyright = XmlUtil.appendChild component.documentElement.tap {
            setAttribute 'name', 'CopyrightManager'
        }, 'copyright'
        appendOption copyright, 'myName', copyrightName
        notice = notice?.trim()
        if (notice) {
            appendOption copyright, 'notice', notice
        }
        XmlUtil.toFile component, rootProject.file(".idea/copyright/${copyrightName}.xml").path
        configCopyrightManager rootProject, copyrightName
    }

    private static void configCopyrightManager(Project rootProject, String copyrightName) {
        File settingsFile = rootProject.file '.idea/copyright/profiles_settings.xml'
        List modules = ['Project Files', 'All Changed Files']
        Document component = settingsFile.exists() ? XmlUtil.readXML(settingsFile.path) :
            XmlUtil.createXml('component').tap {
                documentElement.setAttribute 'name', 'CopyrightManager'
            }
        Node settings = appendChild component.documentElement, 'settings'
        Node module2copyright = appendChild settings, 'module2copyright'
        if (XmlUtil.getNodeByXPath("element[@copyright=\"$copyrightName\"]", module2copyright)) {
            return
        }
        modules.each { module ->
            appendChild module2copyright, 'element', module, [module: module, copyright: copyrightName]
        }
        appendOption appendChild(settings, 'LanguageOptions', [name: '__TEMPLATE__']), 'addBlankAfter', 'false'
        XmlUtil.toFile component, settingsFile.path
    }

    private static Node appendChild(Node node, String name) {
        XmlUtil.getNodeByXPath(name, node) ?: XmlUtil.appendChild(node, name)
    }

    private static Node appendChild(Node node, String name, String module, Map<String, String> attributes) {
        XmlUtil.getNodeByXPath("$name[@module=\"$module\"]", node) ?: appendChild(node, name, attributes)
    }

    private static Node appendChild(Node node, String name, Map<String, String> attributes) {
        XmlUtil.appendChild(node, name).tap {
            attributes.each { k, v ->
                setAttribute k, v
            }
        }
    }

    private static void appendOption(Node node, String name, String value) {
        if (!XmlUtil.getNodeByXPath("option[@name=\"$name\"]", node)) {
            appendChild node, 'option', [name: name, value: value]
        }
    }

}
