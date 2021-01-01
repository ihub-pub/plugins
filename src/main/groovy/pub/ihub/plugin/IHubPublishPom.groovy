package pub.ihub.plugin


import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication

import static pub.ihub.plugin.PluginUtils.findProperty



/**
 * @author henry
 */
class IHubPublishPom {

	String groupId
	String pomArtifactId

	String pomName
	String pomPackaging
	String pomDescription
	String pomUrl = 'https://ihub.pub'
	String pomInceptionYear = '2020'

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
		pomName = pomName ?: project.name
	}

	void configPom(MavenPublication publication) {
		publication.pom { p ->
			p.name.set pomName
			p.packaging = pomPackaging
			p.description.set pomDescription
			p.url.set pomUrl
			p.inceptionYear.set pomInceptionYear

			p.scm {
				url.set pomScmUrl
				connection.set pomScmConnection
				developerConnection.set pomScmDeveloperConnection
				tag.set pomScmTag
			}

			p.licenses { licenses ->
				licenses.license {
					name.set pomLicenseName
					url.set pomLicenseUrl
					distribution.set pomLicenseDistribution
					comments.set pomLicenseComments
				}
			}

			p.developers { developers ->
				developers.developer {
					id.set pomDeveloperId
					name.set pomDeveloperName
					email.set pomDeveloperEmail
					url.set pomDeveloperUrl
					organization.set pomDeveloperOrganization
					organizationUrl.set pomDeveloperOrganizationUrl
					roles.set pomDeveloperRoles?.split(',')?.toList() ?: []
					timezone.set pomDeveloperTimezone
				}
			}
		}
	}

}
