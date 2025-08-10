/*
 * Copyright (c) 2021-2023 the original author or authors.
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

import pub.ihub.plugin.test.IHubSpecification
import spock.lang.Title



/**
 * @author henry
 */
@Title('Copyright测试套件')
class IHubCopyrightPluginTest extends IHubSpecification {

    @Override
    def setup() {
        buildFile << '''
            plugins {
                id 'pub.ihub.plugin.ihub-copyright'
            }
        '''
        newFolder '.idea'
    }

    def 'Copyright插件测试：没有配置配置信息'() {
        when: '没有配置配置信息'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'The COPYRIGHT file does not exist and will use the LICENSE information'
        result.output.contains 'The LICENSE file does not exist and will use the default copyright information'

        when: '模拟非idea环境'
        testProjectDir.eachDir {
            if (it.name == '.idea') {
                it.deleteDir()
            }
        }
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Copyright插件测试：存在COPYRIGHT配置'() {
        when: '使用COPYRIGHT配置'
        newFile('COPYRIGHT') << 'COPYRIGHT'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        !result.output.contains('The COPYRIGHT file does not exist and will use the LICENSE information')
        !result.output.contains('The LICENSE file does not exist and will use the default copyright information')
    }

    def 'Copyright插件测试：存在LICENSE配置'() {
        when: '使用LICENSE配置'
        def license = newFile('LICENSE')
        license << 'LICENSE'
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'The COPYRIGHT file does not exist and will use the LICENSE information'
        !result.output.contains('The LICENSE file does not exist and will use the default copyright information')

        when: '使用LICENSE配置'
        license << '                                 Apache License'
        result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
    }

    def 'Copyright插件测试：模拟修改配置'() {
        when: '使用COPYRIGHT配置'
        newFile('COPYRIGHT') << 'COPYRIGHT'
        newFolder '.idea/copyright'
        newFile('.idea/copyright/profiles_settings.xml') << settings
        def result = gradleBuilder.build()

        then: '检查结果'
        result.output.contains except

        where:
        settings                                                     | except
        '''
<component name="CopyrightManager">
  <settings>
    <module2copyright>
      <element module="Project Files" copyright="ihub" />
      <element module="All" copyright="ihub" />
    </module2copyright>
    <LanguageOptions name="__TEMPLATE__">
      <option name="addBlankAfter" value="false" />
    </LanguageOptions>
  </settings>
</component>'''                                           | 'BUILD SUCCESSFUL'
        '''
<component name="CopyrightManager">
  <settings>
    <LanguageOptions name="__TEMPLATE__">
      <option name="addBlankAfter" value="false" />
    </LanguageOptions>
  </settings>
</component>'''                                           | 'BUILD SUCCESSFUL'
    }

}
