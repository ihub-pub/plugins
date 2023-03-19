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
import type { SidebarConfig } from '@vuepress/theme-default'

export const sidebar: SidebarConfig = {
    '/': [
        {
            text: '入门篇',
            children: [
                '/basics/gradleBasic.md',
                '/basics/singleBasic.md',
                '/basics/groovyBasic.md',
            ],
        },
        {
            text: '进阶篇',
            children: [
                '/advanced/multiAdvanced.md',
            ],
        },
        {
            text: '插件集',
            children: [
                '/iHubSettings.md',
                '/iHub.md',
                '/iHubVersion.md',
                '/iHubCopyright.md',
                '/iHubBom.md',
                '/iHubJava.md',
                '/iHubGroovy.md',
                '/iHubPublish.md',
                '/iHubTest.md',
                '/iHubVerification.md',
                '/iHubBoot.md',
                '/iHubNative.md',
                '/iHubGitHooks.md',
            ],
        },
        {
            text: '属性配置说明',
            link: '/explanation.md',
        },
    ],
}
