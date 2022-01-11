package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class MavenPublishingExtension @Inject constructor(
    project: Project,
    objects: ObjectFactory,
    propPrefix: String = "yapp.mavenPublishing.",
    envPrefix: String = "YAPP_MAVENPUBLISHING_"
) :
    PropertyHandler(project, objects, propPrefix, envPrefix) {

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

    var developers = propertyList<Developer>("developer") { map ->
        map.values.map {

            Developer(
                it.getOrElse(0, { "a" }),
                it.getOrElse(1, { "b" }),
                it.getOrElse(2, { "c" })
            )
        }
    }

    var organization: Property<String> = property("organization")
    var organizationUrl: Property<String> = property("organizationUrl")

    var scmUrl: Property<String> = property("scmUrl")

    var scmConnection: Property<String> = property("scmConnection")
    var scmDeveloperConnection: Property<String> = property("scmDeveloperConnection")

    open var user: Property<String> = property("user")
    open var password: Property<String> = property("password")
    open var token: Property<String> = property("token")

    open var directReleaseToMavenCentral: Property<Boolean> = propertyBool("directReleaseToMavenCentral")
}

data class Developer(val id: String, val name: String, val email: String) { // : StringConvertable<Developer> {
}
