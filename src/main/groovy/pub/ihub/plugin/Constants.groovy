package pub.ihub.plugin

import org.gradle.api.Project

import static pub.ihub.plugin.PluginUtils.findProperty



/**
 * @author liheng
 */
class Constants {

    static final PROJECT_NAME = 'project_name'
    static final INCLUDE_DIRS = 'include_dirs'
    static final SKIPPED_DIRS = 'skipped_dirs'
    static final MAVEN_CENTRAL_REPO_CUSTOMIZE = 'maven_central_repo_customize'

    static final GRADLE_COMPILATION_INCREMENTAL = 'gradle_compilation_incremental'
    static final JAVA_COMPATIBILITY = 'java_compatibility'

    //<editor-fold desc="仓库相关相关">

    static final MAVEN_LOCAL_ENABLED = 'MAVEN_LOCAL_ENABLED'

    static final RELEASE_REPOSITORY_URL = 'RELEASE_REPOSITORY_URL'
    static final SNAPSHOT_REPOSITORY_URL = 'SNAPSHOT_REPOSITORY_URL'
    static final SONATYPE_NEXUS_USERNAME = 'SONATYPE_NEXUS_USERNAME'
    static final SONATYPE_NEXUS_PASSWORD = 'SONATYPE_NEXUS_PASSWORD'

    static final MAVEN_CENTRAL_REPO_MIRROR_ALIYUN = 'https://maven.aliyun.com/repository/public/'
    static final GRADLE_PLUGIN_REPO_MIRROR_ALIYUN = 'https://maven.aliyun.com/repository/gradle-plugin/'
    static final ALIYUN_CONTENT_REPO = 'https://maven.aliyun.com/nexus/content/groups/public/'
    static final SPRING_PLUGIN_REPO_RELEASE = 'https://repo.spring.io/plugins-release/'
    static final DEFAULT_RELEASE_REPOSITORY_URL = 'https://oss.sonatype.org/service/local/staging/deploy/maven2/'
    static final DEFAULT_SNAPSHOT_REPOSITORY_URL = 'https://oss.sonatype.org/content/repositories/snapshots/'

    //</editor-fold>

    //<editor-fold desc="Plugins组件相关">

    private static final Closure<String> FIND_VERSION_FINDNER = { String key, String defaultVersion, Project project ->
        findProperty project, key, defaultVersion
    }

    static final Map<String, Closure<String>> GROUP_DEPENDENCY_VERSION_MAPPING = [
            'pub.ihub.lib'       : FIND_VERSION_FINDNER.curry('ihub_lib_version', 'dev-SNAPSHOT'),
            'org.slf4j'          : FIND_VERSION_FINDNER.curry('slf4j_version', '1.7.25'),
            'com.alibaba.cloud'  : FIND_VERSION_FINDNER.curry('alibaba_cloud_version', '2.2.3.RELEASE'),
            'io.jsonwebtoken'    : FIND_VERSION_FINDNER.curry('jjwt_version', '0.11.2'),
            'io.springfox'       : FIND_VERSION_FINDNER.curry('springfox_version', '3.0.0'),
            'io.swagger'         : FIND_VERSION_FINDNER.curry('swagger_version', '1.6.2'),
            'com.github.xiaoymin': FIND_VERSION_FINDNER.curry('knife4j_version', '3.0.2'),
            'com.sun.xml.bind'   : FIND_VERSION_FINDNER.curry('sun_jaxb_version', '3.0.0'),
            'com.baomidou'       : FIND_VERSION_FINDNER.curry('mybatis_plus_version', '3.4.1'),
            'io.protostuff'      : FIND_VERSION_FINDNER.curry('protostuff_version', '1.7.2')
    ]

    static final Map<String, List<String>> GROUP_DEPENDENCY_EXCLUDE_MAPPING = [
            'c3p0'                    : ['c3p0'],
            'commons-logging'         : ['commons-logging'],
            'com.zaxxer'              : ['HikariCP'],
            'log4j'                   : ['log4j'],
            'org.apache.logging.log4j': ['log4j-core'],
            'org.apache.tomcat'       : ['tomcat-jdbc'],
            'org.slf4j'               : ['slf4j-jcl', 'slf4j-log4j12'],
            'org.springframework.boot': ['spring-boot-starter-tomcat'],
            'stax'                    : ['stax-api']
    ]

    static final Map<String, List<String>> GROUP_DEFAULT_DEPENDENCIES_MAPPING = [
            compileOnly          : [],
            implementation       : ['org.slf4j:slf4j-api'],
            api                  : [],
            runtimeOnly          : ['org.slf4j:jul-to-slf4j',
                                    'org.slf4j:jcl-over-slf4j',
                                    'org.slf4j:log4j-over-slf4j'],
            testImplementation   : [],
            debugImplementation  : [],
            releaseImplementation: []
    ]

    //</editor-fold>

    //<editor-fold desc="Publish组件相关">

    static final RELEASE_SIGNING_ENABLED = 'RELEASE_SIGNING_ENABLED'
    static final RELEASE_DOCS_ENABLED = 'RELEASE_DOCS_ENABLED'
    static final RELEASE_SOURCES_ENABLED = 'RELEASE_SOURCES_ENABLED'

    //</editor-fold>

}
