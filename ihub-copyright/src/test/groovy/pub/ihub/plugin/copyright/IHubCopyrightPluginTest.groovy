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
                id 'java'
            }
        '''
    }

    def 'Copyright插件测试：没有配置配置信息'() {
        when: '没有配置配置信息'
        def result = gradleBuilder.withArguments('help').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'The COPYRIGHT file does not exist and will use the LICENSE information'
        result.output.contains 'The LICENSE file does not exist and will use the default copyright information'
    }

    def 'Copyright插件测试：存在COPYRIGHT配置'() {
        when: '使用COPYRIGHT配置'
        newFile('COPYRIGHT') << 'Copyright (c) 2025 the original author or authors.'
        def result = gradleBuilder.withArguments('help').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Using copyright header from COPYRIGHT file'
        !result.output.contains('The COPYRIGHT file does not exist and will use the LICENSE information')
    }

    def 'Copyright插件测试：存在LICENSE配置-Apache'() {
        when: '使用Apache LICENSE配置'
        def license = newFile('LICENSE')
        license << '''
                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/
        '''
        def result = gradleBuilder.withArguments('help').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Using copyright header from LICENSE file (Apache License)'
    }

    def 'Copyright插件测试：存在LICENSE配置-MIT'() {
        when: '使用MIT LICENSE配置'
        def license = newFile('LICENSE')
        license << '''
MIT License

Copyright (c) 2025 the original author or authors.
        '''
        def result = gradleBuilder.withArguments('help').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Using copyright header from LICENSE file (MIT License)'
    }

    def 'Copyright插件测试：LICENSE存在但无匹配模板'() {
        when: '使用未知LICENSE配置'
        def license = newFile('LICENSE')
        license << '''
Some Unknown License

This is a custom license.
        '''
        def result = gradleBuilder.withArguments('help').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'LICENSE file exists but no matching copyright template found'
    }

    def 'Copyright插件测试：spotlessApply任务可用'() {
        when: '使用COPYRIGHT配置并创建Java源文件'
        newFile('COPYRIGHT') << '''
Copyright (c) 2025 the original author or authors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0
'''
        newFolder 'src/main/java/pub/ihub/test'
        def javaFile = newFile('src/main/java/pub/ihub/test/Test.java')
        javaFile << '''
package pub.ihub.test;

public class Test {
}
'''
        def result = gradleBuilder.withArguments('spotlessApply').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Using copyright header from COPYRIGHT file'

        and: 'Java文件应该被添加版权头'
        javaFile.text.contains 'Copyright (c)'
        javaFile.text.contains 'Licensed under the Apache License'
    }

    def 'Copyright插件测试：spotlessApply任务可用-仅版权行'() {
        when: '使用仅版权行的COPYRIGHT配置'
        newFile('COPYRIGHT') << 'Copyright (c) 2025 the original author or authors.'
        newFolder 'src/main/java/pub/ihub/test'
        def javaFile = newFile('src/main/java/pub/ihub/test/Test.java')
        javaFile << '''
package pub.ihub.test;

public class Test {
}
'''
        def result = gradleBuilder.withArguments('spotlessApply').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Using copyright header from COPYRIGHT file'

        and: 'Java文件应该被添加版权头'
        javaFile.text.contains 'Copyright (c)'
    }

    def 'Copyright插件测试：IDEA版权配置'() {
        when: '启用IDEA版权配置'
        newFolder '.idea'
        newFile('COPYRIGHT') << 'Copyright (c) 2025 the original author or authors.'
        buildFile << '''
            iHubCopyright {
                enableIdea = true
            }
        '''
        def result = gradleBuilder.withArguments('help').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Using copyright header from COPYRIGHT file'

        and: '应该生成IDEA版权配置目录'
        testProjectDir.toPath().resolve('.idea').resolve('copyright').toFile().exists()
    }

    def 'Copyright插件测试：IDEA版权配置-无.idea目录'() {
        when: '启用IDEA版权配置但没有.idea目录'
        newFile('COPYRIGHT') << 'Copyright (c) 2025 the original author or authors.'
        buildFile << '''
            iHubCopyright {
                enableIdea = true
            }
        '''
        def result = gradleBuilder.withArguments('help').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Using copyright header from COPYRIGHT file'

        and: '不应该生成IDEA版权配置目录'
        !testProjectDir.toPath().resolve('.idea').resolve('copyright').toFile().exists()
    }

    def 'Copyright插件测试：禁用IDEA版权配置'() {
        when: '禁用IDEA版权配置'
        newFile('COPYRIGHT') << 'Copyright (c) 2025 the original author or authors.'
        buildFile << '''
            iHubCopyright {
                enableIdea = false
            }
        '''
        def result = gradleBuilder.withArguments('help').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Using copyright header from COPYRIGHT file'
    }

    def 'Copyright插件测试：IDEA版权配置-特殊字符转义'() {
        when: '启用IDEA版权配置并使用包含特殊字符的版权信息'
        newFolder '.idea'
        newFile('COPYRIGHT') << '''Copyright (c) 2025 the original author or authors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.

Special chars: < > & " '
'''
        buildFile << '''
            iHubCopyright {
                enableIdea = true
            }
        '''
        def result = gradleBuilder.withArguments('help').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Using copyright header from COPYRIGHT file'

        and: '应该生成IDEA版权配置文件'
        def copyrightDir = testProjectDir.toPath().resolve('.idea').resolve('copyright').toFile()
        copyrightDir.exists()
        def xmlFiles = copyrightDir.listFiles().findAll { it.name.endsWith('.xml') }
        xmlFiles.size() >= 1
        def copyrightFile = xmlFiles.find { it.name != 'profiles_settings.xml' }
        copyrightFile != null
        copyrightFile.text.contains('&lt;')
        copyrightFile.text.contains('&gt;')
        copyrightFile.text.contains('&amp;')
        copyrightFile.text.contains('&quot;')
        copyrightFile.text.contains('&apos;')
    }

    def 'Copyright插件测试：IDEA版权配置-已存在profiles_settings'() {
        when: '启用IDEA版权配置且profiles_settings.xml已存在'
        newFolder '.idea/copyright'
        newFile('.idea/copyright/profiles_settings.xml') << '''<component name="CopyrightManager">
  <settings>
    <module2copyright>
      <element module="Project Files" copyright="existing" />
    </module2copyright>
  </settings>
</component>'''
        newFile('COPYRIGHT') << 'Copyright (c) 2025 the original author or authors.'
        buildFile << '''
            iHubCopyright {
                enableIdea = true
            }
        '''
        def result = gradleBuilder.withArguments('help').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'
        result.output.contains 'Using copyright header from COPYRIGHT file'

        and: '应该只生成版权配置文件，不覆盖profiles_settings.xml'
        def profilesSettings = testProjectDir.toPath().resolve('.idea').resolve('copyright').resolve('profiles_settings.xml').toFile()
        profilesSettings.exists()
        profilesSettings.text.contains('existing')
    }

    def 'Copyright插件测试：COPYRIGHT包含$today.year变量'() {
        when: 'COPYRIGHT文件包含$today.year变量'
        newFolder 'src/main/java/pub/ihub/test'
        newFile('COPYRIGHT') << '''Copyright (c) $today.year the original author or authors.

Some license text.
'''
        def javaFile = newFile('src/main/java/pub/ihub/test/Test.java')
        javaFile << '''
package pub.ihub.test;

public class Test {
}
'''
        def result = gradleBuilder.withArguments('spotlessApply').build()

        then: '检查结果'
        result.output.contains 'BUILD SUCCESSFUL'

        and: 'Java文件应该被添加版权头，且$today.year被替换为当前年份'
        int currentYear = Calendar.instance.get(Calendar.YEAR)
        javaFile.text.contains("Copyright (c) $currentYear")
        javaFile.text.contains('Some license text')
    }

}
