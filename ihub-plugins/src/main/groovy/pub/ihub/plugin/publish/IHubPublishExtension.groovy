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
package pub.ihub.plugin.publish

import groovy.transform.TupleConstructor
import org.gradle.api.publish.maven.MavenPublication
import pub.ihub.plugin.IHubProjectExtension

import static java.time.Year.now



/**
 * 组件发布属性扩展
 * @author henry
 */
@TupleConstructor(includeSuperFields = true)
class IHubPublishExtension extends IHubProjectExtension {

    String pomName
    String pomPackaging
    String pomDescription
    String pomUrl = 'https://ihub.pub'
    String pomInceptionYear = now().value

    String pomScmUrl
    String pomScmConnection
    String pomScmDeveloperConnection
    String pomScmTag

    String pomLicenseName
    String pomLicenseUrl
    String pomLicenseDistribution
    String pomLicenseComments

    String pomOrganizationName = 'Dock'
    String pomOrganizationUrl = 'https://ihub.pub'

    String pomDeveloperId
    String pomDeveloperName = 'henry'
    String pomDeveloperEmail = 'henry.box@outlook.com'
    String pomDeveloperUrl = 'https://henry-hub.github.io'
    String pomDeveloperOrganization = pomOrganizationName
    String pomDeveloperOrganizationUrl = pomOrganizationUrl
    String pomDeveloperRoles
    String pomDeveloperTimezone

    void configPom(MavenPublication publication, versionDetails) {
        publication.pom {
            name.set pomName ?: publication.artifactId
            packaging = pomPackaging
            description.set pomDescription
            url.set pomUrl
            inceptionYear.set pomInceptionYear

            scm {
                url.set pomScmUrl
                connection.set pomScmConnection
                developerConnection.set pomScmDeveloperConnection
                tag.set pomScmTag ?: versionDetails.lastTag
            }

            licenses {
                license {
                    name.set pomLicenseName
                    url.set pomLicenseUrl
                    distribution.set pomLicenseDistribution
                    comments.set pomLicenseComments
                }
            }

            organization {
                name.set pomOrganizationName
                url.set pomOrganizationUrl
            }

            developers {
                developer {
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
