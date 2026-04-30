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
package pub.ihub.plugin.verification

import groovy.transform.CompileStatic
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters

import java.util.concurrent.ConcurrentLinkedQueue

import static pub.ihub.plugin.IHubPluginMethods.printConfigContent

/**
 * 跨项目 JaCoCo 覆盖率汇总 BuildService。
 * <p>
 * 各子项目的 jacocoTestReport 任务执行完毕后，通过 {@link #addProjectCoverage} 将每行覆盖率数据注册到此服务；
 * 在构建结束时（{@link #close}）一次性打印汇总表格。
 * <p>
 * 使用 BuildService 替代已弃用的 {@code gradle.buildFinished} 钩子，同时避免跨项目
 * {@code rootProject.allprojects} 访问，与 Configuration Cache / Isolated Projects 兼容。
 *
 * @author henry
 */
@CompileStatic
@SuppressWarnings('AbstractClassWithoutAbstractMethod')
abstract class JacocoCoverageReportService implements BuildService<BuildServiceParameters.None>, AutoCloseable {

    /** 每个项目贡献一行：[projectName, inst%, branch%, line%, cxty%, method%, class%] */
    private final Queue<List<String>> rows = new ConcurrentLinkedQueue<>()

    /**
     * 由各项目的 doLast 回调调用，注册该项目的覆盖率数据。
     *
     * @param projectName 项目名称
     * @param reportData  六种覆盖类型的 coverage 字符串（顺序与 RULE_TYPE 一致）
     */
    void addProjectCoverage(String projectName, List<String> reportData) {
        rows.add([projectName] + reportData)
    }

    @Override
    void close() {
        if (rows.empty) {
            return
        }
        List<List<String>> report = rows.toList().sort { it[0] }
        printConfigContent 'Jacoco Report Coverage', report,
            'Project', 'Instruct', 'Branch', 'Line', 'Cxty', 'Method', 'Class'
    }

}
