package pub.ihub.plugin.publish

import groovy.transform.TupleConstructor
import org.gradle.api.Project

import static pub.ihub.plugin.PluginUtils.findProperty



/**
 * @author henry
 */
@TupleConstructor
class IHubPublishPom {

    String groupId
    String pomArtifactId

    String pomName
    String pomPackaging
    String pomDescription
    String pomUrl = 'https://ihub.pub'
    String pomInceptionYear

    String pomScmUrl
    String pomScmConnection
    String pomScmDeveloperConnection
    String pomScmTag

    String pomLicenseName
    String pomLicenseUrl
    String pomLicenseDistribution
    String pomLicenseComments

    String pomDeveloperId
    String pomDeveloperName = 'henry'
    String pomDeveloperEmail = 'henry.box@outlook.com'
    String pomDeveloperUrl = 'https://henry-hub.github.io'
    String pomDeveloperOrganization = 'Dock'
    String pomDeveloperOrganizationUrl = 'https://ihub.pub'
    String pomDeveloperRoles
    String pomDeveloperTimezone

    IHubPublishPom(Project project) {
        properties.each { k, v ->
            if ('class' != k) {
                this."$k" = findProperty project, k, v
            }
        }
    }

}
