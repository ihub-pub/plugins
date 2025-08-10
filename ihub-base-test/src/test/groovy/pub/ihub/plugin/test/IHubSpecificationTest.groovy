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
package pub.ihub.plugin.test

import spock.lang.Title

import static org.gradle.api.Project.DEFAULT_BUILD_FILE

/**
 * @author henry
 */
@Title('IHubSpecification测试套件')
class IHubSpecificationTest extends IHubSpecification {

    def "test"() {
        given:
        copyProject 'basic.gradle'
        copyProject 'sample-groovy', 'src', 'conf'
        // 示例配置脚本无法在本模块构建，以上代码仅作方法测试
        buildFile.delete()
        buildFile = newFile DEFAULT_BUILD_FILE

        when:
        def result = gradleBuilder.withArguments('tasks').build()

        then:
        result.output.contains('tasks - Displays the tasks runnable from')
    }

    def "test newFolder"() {
        when:
        newFolder 'a', 'b', 'c', 'd', 'e'
        def result = gradleBuilder.build()

        then:
        result.output.contains('BUILD SUCCESSFUL')
    }

}
