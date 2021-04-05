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
