package pub.ihub.plugin.publish

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.javadoc.Groovydoc
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar
import org.gradle.plugins.signing.SigningPlugin

import static pub.ihub.plugin.PluginUtils.findProperty
import static pub.ihub.plugin.publish.Constants.RELEASE_DOCS_ENABLED
import static pub.ihub.plugin.publish.Constants.RELEASE_SIGNING_ENABLED
import static pub.ihub.plugin.publish.Constants.RELEASE_SOURCES_ENABLED



/**
 * @author liheng
 */
class IHubPublishPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def jarTasks = getJarTasks project

        project.pluginManager.apply 'maven-publish'
        project.extensions.getByType(PublishingExtension).with {
            publications {
                it.create('mavenJava', MavenPublication) { publication ->
                    def pom = new IHubPublishPom(project)

                    publication.groupId = pom.groupId ?: project.group
                    publication.artifactId = pom.pomArtifactId ?: project.name
                    assert publication.artifactId, 'artifactId不能为空！'
                    publication.version = project.version as String
                    assert 'unspecified' != publication.version, 'version不能为空！'

                    publication.from project.components.getByName('java')

                    jarTasks.each {
                        publication.artifact it
                    }

                    publication.pom { p ->
                        p.name.set pom.pomName ?: project.name
                        p.packaging = pom.pomPackaging
                        p.description.set pom.pomDescription
                        p.url.set pom.pomUrl
                        p.inceptionYear.set pom.pomInceptionYear

                        p.scm {
                            url.set pom.pomScmUrl
                            connection.set pom.pomScmConnection
                            developerConnection.set pom.pomScmDeveloperConnection
                            tag.set pom.pomScmTag
                        }

                        p.licenses { licenses ->
                            licenses.license {
                                name.set pom.pomLicenseName
                                url.set pom.pomLicenseUrl
                                distribution.set pom.pomLicenseDistribution
                                comments.set pom.pomLicenseComments
                            }
                        }

                        p.developers { developers ->
                            developers.developer {
                                id.set pom.pomDeveloperId
                                name.set pom.pomDeveloperName
                                email.set pom.pomDeveloperEmail
                                url.set pom.pomDeveloperUrl
                                organization.set pom.pomDeveloperOrganization
                                organizationUrl.set pom.pomDeveloperOrganizationUrl
                                roles.set pom.pomDeveloperRoles?.split(',')?.toList() ?: []
                                timezone.set pom.pomDeveloperTimezone
                            }
                        }
                    }
                }
            }
            repositories {
                // TODO 配置远端仓库
            }
        }

        def releaseSigningEnabled = findProperty(RELEASE_SIGNING_ENABLED, 'false').toBoolean()
        project.plugins.apply SigningPlugin
        project.signing.setRequired releaseSigningEnabled
        project.afterEvaluate {
            if (releaseSigningEnabled) {
                project.signing.sign project.publishing.publications
            }
        }
    }

    private static List<TaskProvider> getJarTasks(Project project) {
        assert project.plugins.hasPlugin('java') || project.plugins.hasPlugin('java-library')
        def releaseDocsEnabled = findProperty(RELEASE_DOCS_ENABLED, 'false').toBoolean()
        def releaseSourcesEnabled = findProperty(RELEASE_SOURCES_ENABLED, 'true').toBoolean()
        def tasks = []
        if (releaseSourcesEnabled) {
            tasks << project.tasks.register('sourcesJar', Jar) {
                archiveClassifier.set 'sources'
                from project.convention.getPlugin(JavaPluginConvention).sourceSets.getByName('main').allSource
            }
        }
        if (releaseDocsEnabled) {
            tasks << project.tasks.register('javadocsJar', Jar) {
                archiveClassifier.set 'javadoc'
                def javadocTask = project.tasks.getByName('javadoc') as Javadoc
                dependsOn javadocTask
                from javadocTask
            }
        }
        if (releaseDocsEnabled && project.plugins.hasPlugin('groovy')) {
            tasks << project.tasks.register('groovydocJar', Jar) {
                archiveClassifier.set 'groovydoc'
                def groovydocTask = project.tasks.getByName('groovydoc') as Groovydoc
                dependsOn groovydocTask
                from groovydocTask.destinationDir
            }
        }
        tasks
    }

}
