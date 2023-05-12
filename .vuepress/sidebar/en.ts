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
import {sidebar} from "vuepress-theme-hope";

export const enSidebar = sidebar({
    "/en/": [
        "",
        {
            icon: "notice",
            text: "Basics",
            prefix: "basics/",
            link: "basics/",
            children: "structure",
        },
        {
            icon: "hot",
            text: "Advanced",
            prefix: "advanced/",
            link: "advanced/",
            children: "structure",
        },
        {
            icon: "list",
            text: "Plugins",
            prefix: "list/",
            children: "structure",
        },
        "explanation",
    ],
});
