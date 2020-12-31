package pub.ihub.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.quality.CodeNarcExtension
import org.gradle.api.plugins.quality.CodeNarcPlugin
import org.gradle.api.plugins.quality.PmdExtension
import org.gradle.api.plugins.quality.PmdPlugin
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension

import static pub.ihub.plugin.PluginUtils.findProperty



/**
 * @author liheng
 */
class IHubVerificationPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        if (project.plugins.hasPlugin(JavaPlugin) || project.plugins.hasPlugin(JavaLibraryPlugin)) {
            configPmd project
        }
        if (project.plugins.hasPlugin(GroovyPlugin)) {
            configCodenarc project
        }
        configJacoco project
    }

    private static void configPmd(Project project) {
        project.pluginManager.apply PmdPlugin
        project.extensions.getByType(PmdExtension).identity {
            toolVersion = findProperty project, 'pmd.version', '6.30.0'
            ruleSetFiles()
            ruleSets = [
                    'rulesets/java/ali-comment.xml',
                    'rulesets/java/ali-concurrent.xml',
                    'rulesets/java/ali-constant.xml',
                    'rulesets/java/ali-exception.xml',
                    'rulesets/java/ali-flowcontrol.xml',
                    'rulesets/java/ali-naming.xml',
                    'rulesets/java/ali-oop.xml',
                    'rulesets/java/ali-orm.xml',
                    'rulesets/java/ali-other.xml',
                    'rulesets/java/ali-set.xml',
                    'rulesets/vm/ali-other.xml',
//                    'rulesets/java/basic.xml',
//                    'rulesets/java/braces.xml',
//                    'rulesets/java/clone.xml',
//                    'rulesets/java/codesize.xml',
//                    'rulesets/java/comments.xml',
//                    'rulesets/java/controversial.xml',
//                    'rulesets/java/coupling.xml',
//                    'rulesets/java/design.xml',
//                    'rulesets/java/empty.xml',
//                    'rulesets/java/finalizers.xml',
                    'rulesets/java/imports.xml',
//                    'rulesets/java/javabeans.xml',
//                    'rulesets/java/logging-jakarta-commons.xml',
//                    'rulesets/java/logging-java.xml',
//                    'rulesets/java/metrics.xml',
//                    'rulesets/java/naming.xml',
//                    'rulesets/java/optimizations.xml',
//                    'rulesets/java/quickstart.xml',
//                    'rulesets/java/strictexception.xml',
//                    'rulesets/java/strings.xml',
//                    'rulesets/java/sunsecure.xml',
//                    'rulesets/java/typeresolution.xml',
//                    'rulesets/java/unnecessary.xml',
//                    'rulesets/java/unusedcode.xml',
            ]
        }
    }

    private static void configCodenarc(Project project) {
        project.pluginManager.apply CodeNarcPlugin
        project.extensions.getByType(CodeNarcExtension).identity {
            toolVersion = findProperty project, 'codenarc.version', '1.6.1'
            // TODO 处理配置文件
            configFile = project.rootProject.file 'conf/engineering-process/static-checking/groovy/codenarc.gcfg'
            ignoreFailures = false
        }
    }

    private static void configJacoco(Project project) {
        project.pluginManager.apply JacocoPlugin
        project.extensions.getByType(JacocoPluginExtension).identity {
            toolVersion = findProperty project, 'jacoco.version', '0.8.6'
        }
        // TODO 配置检查规则
    }

}
