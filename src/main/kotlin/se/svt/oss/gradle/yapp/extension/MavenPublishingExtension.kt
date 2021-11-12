package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class MavenPublishingExtension(val project: Project) {
    open val envPrefix: String = "YAPP_MAVENPUBLISHING_"
    open var propPrefix: String = "yapp.mavenPublishing."

    var mavenCentralLegacyUrl: Property<Boolean> = propertyBool("mavenCentralLegacyUrl")

    open var artifactId: Property<String> = property("artifactId")
    open var groupId: Property<String> = property("groupId")
    open var version: Property<String> = property("version")
    var name: Property<String> = property("name")

    var description: Property<String> = property("description")
    var url: Property<String> = property("url")
    var inceptionYear: Property<String> = property("inceptionYear")

    var licenseName: Property<String> = property("licenseName")
    var licenseUrl: Property<String> = property("licenseUrl")
    var licenseDistribution: Property<String> =
        property("licenseDistribution")
    var licenseComments: Property<String> = property("licenseComments")

    var developerId: Property<String> = property("developerId")
    var developerName: Property<String> = property("developerName")
    var developerEmail: Property<String> = property("developerEmail")

    var organization: Property<String> = property("organization")
    var organizationUrl: Property<String> = property("organizationUrl")

    var scmUrl: Property<String> = property("scmUrl")

    var scmConnection: Property<String> = property("scmConnection")
    var scmDeveloperConnection: Property<String> = property("scmDeveloperConnection",)

    open var user: Property<String> = property("user")
    open var password: Property<String> = property("password")
    open var token: Property<String> = property("token")

    open var directReleaseToMavenCentral: Property<Boolean> = propertyBool("directReleaseToMavenCentral")

    internal fun property(property: String) = project.prop(property, propPrefix, envPrefix)
    internal fun propertyBool(property: String) = project.propBool(property, propPrefix, envPrefix)
}
