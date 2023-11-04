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
package pub.ihub.plugin.profiles

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title

/**
 * @author henry
 */
@Title('Profiles测试套件')
class IHubProfilesPluginTest extends IHubSpecification {

    @Override
    def setup() {
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-profiles'
            }
        '''
    }

    def '配置文件基础测试'() {
        when: '配置文件测试'
        buildFile << '''
            iHubProfiles {
                profile('dev') {
                    println 'dev'
                }
                profile('test') {
                    println 'test'
                }
                profile('prod') {
                    println 'prod'
                }
            }
        '''
        propertiesFile << 'iHub.profile=' + profile
        def result = gradleBuilder.build()

        then: '检查结果'
        expected.every {
            result.output.contains it
        }
        noExpected.every {
            !result.output.contains(it)
        }

        where:
        profile | expected | noExpected
        ''      | ['']     | ['dev', 'test', 'prod']
        'dev'   | ['dev']  | ['test', 'prod']
        'test'  | ['test'] | ['dev', 'prod']
        'prod'  | ['prod'] | ['dev', 'test']
    }

    def '配置文件多环境配置测试'() {
        when: '配置文件测试'
        buildFile << '''
            iHubProfiles {
                profile(['dev', 'test']) {
                    println 'collection'
                }
                profile(['test', 'prod'] as String[]) {
                    println 'array'
                }
            }
        '''
        propertiesFile << 'iHub.profile=' + profile
        def result = gradleBuilder.build()

        then: '检查结果'
        expected.every {
            result.output.contains it
        }
        noExpected.every {
            !result.output.contains(it)
        }

        where:
        profile    | expected                | noExpected
        ''         | ['']                    | ['collection', 'array']
        'dev'      | ['collection']          | ['array']
        'test'     | ['collection', 'array'] | ['dev']
        'prod'     | ['array']               | ['collection']
        'dev,test' | ['collection', 'array'] | ['dev', 'test', 'prod']
    }

    def '配置文件取反测试'() {
        when: '配置文件测试'
        buildFile << '''
            iHubProfiles {
                profile('!dev') {
                    println 'dev'
                }
                profile('!test') {
                    println 'test'
                }
                profile('!prod') {
                    println 'prod'
                }
            }
        '''
        propertiesFile << 'iHub.profile=' + profile
        def result = gradleBuilder.build()

        then: '检查结果'
        expected.every {
            result.output.contains it
        }
        noExpected.every {
            !result.output.contains(it)
        }

        where:
        profile | expected         | noExpected
        'dev'   | ['test', 'prod'] | ['dev']
        'test'  | ['dev', 'prod']  | ['test']
        'prod'  | ['dev', 'test']  | ['prod']
    }

    def '配置文件属性替换测试'() {
        when: '配置文件测试'
        buildFile << '''
            apply plugin: 'java'
        '''
        propertiesFile << 'iHub.profile=dev'
        testProjectDir.newFolder 'src', 'main', 'resources'
        testProjectDir.newFile('src/main/resources/application.properties') << 'spring.profiles.active=@profile@'
        gradleBuilder.withArguments('processResources').build()

        then: '检查结果'
        buildFile.parentFile.toPath().resolve('build/resources/main/application.properties').toFile()
            .text == 'spring.profiles.active=dev'

        when: '配置文件测试'
        buildFile << '''
            iHubProfiles {
                tokens = [
                    profile: 'test'
                ]
            }
        '''
        gradleBuilder.withArguments('clean', 'processResources').build()

        then: '检查结果'
        buildFile.parentFile.toPath().resolve('build/resources/main/application.properties').toFile()
            .text == 'spring.profiles.active=test'
    }

}
