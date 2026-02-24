/*
 * Copyright (c) 2023-2025 the original author or authors.
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
package pub.ihub.plugin.copyright

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor


/**
 * 版权模板
 * @author henry
 */
@CompileStatic
@TupleConstructor(includeFields = true)
enum IHubCopyright {

    /**
     * Apache License, Version 2.0
     */
    APACHE_2_0('Apache License',
        '''Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.'''
    ),

    /**
     * MIT License
     */
    MIT('MIT License',
        '''Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.'''
    )

    final String name
    final String header

    boolean matchLicense(String license) {
        license.contains name
    }

    static IHubCopyright matchCopyright(String license) {
        values().find { it.matchLicense license }
    }

}
