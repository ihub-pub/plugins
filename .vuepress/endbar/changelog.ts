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

import type {FunctionalComponent} from "vue";
import {h} from "vue";

const Changelog: FunctionalComponent = () =>
    h(
        "div",
        {class: "nav-item vp-repo"},
        h("a", {
            class: "vp-repo-link",
            href: "https://github.com/ihub-pub/plugins/releases/",
            target: "_blank",
            rel: "noopener noreferrer",
            "aria-label": "changelog",
            innerHTML:
                '<svg t="1689238420113" class="icon" viewBox="0 0 1024 1024" style="width:1.25rem;height:1.25rem;vertical-align:middle" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="9083" width="200" height="200"><path d="M512 1024C229.248 1024 0 794.752 0 512S229.248 0 512 0s512 229.248 512 512-229.248 512-512 512z m42.666667-486.869333V298.538667C554.666667 275.328 535.552 256 512 256c-23.722667 0-42.666667 19.029333-42.666667 42.538667v256.256a41.984 41.984 0 0 0 12.202667 29.866666l121.258667 121.258667a42.368 42.368 0 0 0 60.032-0.298667 42.666667 42.666667 0 0 0 0.298666-60.032L554.666667 537.130667z" fill="currentColor" p-id="9084"></path></svg>',

        }),
    );

Changelog.displayName = "Changelog";

export default Changelog;
