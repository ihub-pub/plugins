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
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware
import pub.ihub.plugin.IHubProperty

import static java.time.Year.now
import static pub.ihub.plugin.IHubProperty.Type.ENV
import static pub.ihub.plugin.IHubProperty.Type.PROJECT
import static pub.ihub.plugin.IHubProperty.Type.SYSTEM



/**
 * 组件发布属性扩展
 * @author henry
 */
@IHubExtension('iHubPublish')
@TupleConstructor(allProperties = true, includes = 'project')
class IHubPublishExtension implements IHubProjectExtensionAware {

    /**
     * 组件发布是否需要签名
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean publishNeedSign = false

    /**
     * 签名key
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    String signingKeyId

    /**
     * 签名密钥
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    String signingSecretKey

    /**
     * 签名密码
     */
    @IHubProperty(type = [PROJECT, SYSTEM, ENV])
    String signingPassword

    /**
     * 是否发布文档
     */
    @IHubProperty(type = [PROJECT, SYSTEM])
    boolean publishDocs = false

    //<editor-fold desc="POM属性">

    String pomName
    String pomPackaging
    String pomDescription
    String pomUrl
    String pomInceptionYear = now().value

    String pomScmUrl
    String pomScmConnection
    String pomScmDeveloperConnection
    String pomScmTag

    String pomLicenseName
    String pomLicenseUrl
    String pomLicenseDistribution
    String pomLicenseComments

    String pomOrganizationName
    String pomOrganizationUrl

    String pomDeveloperId
    String pomDeveloperName
    String pomDeveloperEmail
    String pomDeveloperUrl
    String pomDeveloperOrganization
    String pomDeveloperOrganizationUrl
    Set<String> pomDeveloperRoles = []
    String pomDeveloperTimezone

    //</editor-fold>

    void configPom(MavenPublication publication) {
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
                tag.set pomScmTag
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
                    roles.set pomDeveloperRoles
                    timezone.set pomDeveloperTimezone
                }
            }
        }
    }

}
