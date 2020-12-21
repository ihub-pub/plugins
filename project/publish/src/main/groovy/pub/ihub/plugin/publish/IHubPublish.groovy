package pub.ihub.plugin.publish

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension



/**
 * @author liheng
 */
class IHubPublish implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.version = getSystemProperty('ref').with {
            !it || it.endsWith('main') ? 'dev-SNAPSHOT' : it.replaceAll(/.*\//, '')
        }
        println project.name
        project.pluginManager.apply 'maven-publish'
        project.extensions.getByType(PublishingExtension).with {
            publications {
                mavenJava(org.gradle.api.publish.maven.MavenPublication) {
                    groupId project.group_id
                    artifactId project.artifact_id ?: project.name
//                    from components.java
                }
            }
        }
    }

    private static String getSystemProperty(String name) {
        System.getProperty(name) ?: System.getenv(name.replaceAll(/\.|-/, '_').toUpperCase())
    }

}
