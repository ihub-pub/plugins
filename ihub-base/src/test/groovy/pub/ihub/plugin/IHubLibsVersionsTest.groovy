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
import spock.lang.Specification
import spock.lang.Title

/**
 * @author henry
 */
@Title('IHubLibsVersions测试套件')
class IHubLibsVersionsTest extends Specification {

    def '测试组件版本包含ihub'() {
        setup:
        System.setProperty 'java.version', javaVersion

        expect:
        IHubLibsVersions.getCompatibleLibsVersion('ihub-libs') ==~ expected

        cleanup:
        JavaVersion.resetCurrent()

        where:
        javaVersion | expected
        '5'         | ''
        '8'         | /^\d+.\d+.\d+-java8/
        '11'        | /^\d+.\d+.\d+-java11/
        '17'        | /^\d+.\d+.\d+$/
        '19'        | /^\d+.\d+.\d+$/
    }

}
