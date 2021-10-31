package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class MavenPublishingExtension(project: Project) {
    open val envPrefix: String = "YAPP_MAVENPUBLISHING_"
    open var propPrefix: String = "yapp.mavenPublishing."

    var mavenCentralLegacyUrl: Property<Boolean> = project.propBool("mavenCentralLegacyUrl", propPrefix, envPrefix)

    open var artifactId: Property<String> = project.prop("artifactId", propPrefix, envPrefix)
    open var groupId: Property<String> = project.prop("groupId", propPrefix, envPrefix)
    open var version: Property<String> = project.prop("version", propPrefix, envPrefix)
    var name: Property<String> = project.prop("name", propPrefix, envPrefix)

    var description: Property<String> = project.prop("description", propPrefix, envPrefix)
    var url: Property<String> = project.prop("url", propPrefix, envPrefix)
    var inceptionYear: Property<String> =
        project.prop("inceptionYear", propPrefix, envPrefix)

    var licenseName: Property<String> = project.prop("licenseName", propPrefix, envPrefix)
    var licenseUrl: Property<String> = project.prop("licenseUrl", propPrefix, envPrefix)
    var licenseDistribution: Property<String> =
        project.prop("licenseDistribution", propPrefix, envPrefix)
    var licenseComments: Property<String> =
        project.prop("licenseComments", propPrefix, envPrefix)

    var developerId: Property<String> = project.prop("developerId", propPrefix, envPrefix)
    var developerName: Property<String> =
        project.prop("developerName", propPrefix, envPrefix)
    var developerEmail: Property<String> =
        project.prop("developerEmail", propPrefix, envPrefix)

    var organization: Property<String> = project.prop("organization", propPrefix, envPrefix)
    var organizationUrl: Property<String> =
        project.prop("organizationUrl", propPrefix, envPrefix)

    var scmUrl: Property<String> = project.prop("scmUrl", propPrefix, envPrefix)
    var scmConnection: Property<String> =
        project.prop("scmConnection", propPrefix, envPrefix)
    var scmDeveloperConnection: Property<String> =
        project.prop("scmDeveloperConnection", propPrefix, envPrefix)

    var ossrhUser: Property<String> = project.prop("ossrhUser", "yapp.", "YAPP_")
    var ossrhPassword: Property<String> = project.prop("ossrhPassword", "yapp.", "YAPP_")
}
