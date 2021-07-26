/*
 * Copyright (c) 2021 Henry 李恒 (henry.box@outlook.com).
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
package pub.ihub.plugin.bom

import groovy.transform.CompileStatic



/**
 * 组件版本属性
 * @author henry
 */
@CompileStatic
final class IHubVersionProperties {

    static final String IHUB_LIBS = 'ihub-libs'
    static final String SPRING_BOOT = 'spring-boot'
    static final String SPRING_CLOUD = 'spring-cloud'
    static final String SPRING_STATEMACHINE = 'spring-statemachine'
    static final String SPRING_CLOUD_ALIBABA = 'spring-cloud-alibaba'
    static final String SPRING_BOOT_ADMIN = 'spring-boot-admin'
    static final String KNIFE4J = 'knife4j'
    static final String KNIFE4J_AGGREGATION = 'knife4j-aggregation'
    static final String JAXB = 'jaxb'
    static final String HUTOOL = 'hutool'
    static final String ALIBABA_FASTJSON = 'alibaba-fastjson'
    static final String ALIBABA_DRUID = 'alibaba-druid'
    static final String ALIBABA_P3C = 'alibaba-p3c'
    static final String MYBATIS_PLUS = 'mybatis-plus'

    static final Map<String, Tuple3<String, String, String>> GROUP_MAVEN_BOM_VERSION_CONFIG = [
        // TODO 由于GitHub仓库token只能个人使用，组件发布到中央仓库方可使用
//        (IHUB_LIBS)           : bom('pub.ihub.lib', 'ihub-libs', '1.0.0-SNAPSHOT'),
        (SPRING_BOOT)         : bom('org.springframework.boot', 'spring-boot-dependencies', '2.5.1'),
        (SPRING_CLOUD)        : bom('org.springframework.cloud', 'spring-cloud-dependencies', '2020.0.3'),
        (SPRING_STATEMACHINE) : bom('org.springframework.statemachine', 'spring-statemachine-bom', '3.0.1'),
        (SPRING_CLOUD_ALIBABA): bom('com.alibaba.cloud', 'spring-cloud-alibaba-dependencies', '2021.1'),
        (SPRING_BOOT_ADMIN)   : bom('de.codecentric', 'spring-boot-admin-dependencies', '2.4.2'),
        (KNIFE4J)             : bom('com.github.xiaoymin', 'knife4j-dependencies', '3.0.3'),
        (JAXB)                : bom('com.sun.xml.bind', 'jaxb-bom-ext', '3.0.1'),
    ]

    static final Map<String, Tuple3<String, String[], String>> GROUP_DEPENDENCY_VERSION_CONFIG = [
        (ALIBABA_FASTJSON)   : dependency('com.alibaba', '1.2.76', 'fastjson'),
        (ALIBABA_DRUID)      : dependency('com.alibaba', '1.2.6', 'druid', 'druid-spring-boot-starter'),
        (ALIBABA_P3C)        : dependency('com.alibaba.p3c', '2.1.1', 'p3c-pmd'),
        (MYBATIS_PLUS)       : dependency('com.baomidou', '3.4.3.1',
            'mybatis-plus', 'mybatis-plus-boot-starter', 'mybatis-plus-generator'),
        (KNIFE4J_AGGREGATION): dependency('com.github.xiaoymin', '2.0.9', 'knife4j-aggregation-spring-boot-starter'),
    ]

    static final Map<String, Tuple2<String, String>> GROUP_VERSION_CONFIG = [
        (HUTOOL): group('cn.hutool', '5.7.2'),
    ]

    private static Tuple3<String, String, String> bom(String group, String module, String version) {
        new Tuple3<>(group, module, version)
    }

    private static Tuple3<String, String[], String> dependency(String group, String version, String... modules) {
        new Tuple3<>(group, modules, version)
    }

    private static Tuple2<String, String> group(String group, String version) {
        new Tuple2<>(group, version)
    }

}
