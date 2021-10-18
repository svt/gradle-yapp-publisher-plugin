package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class MavenPublishingExtension(project: Project) {
    open val envPrefix: String = "YAPP_MAVENPUBLISHING_"
    open var propPrefix: String = "yapp.mavenPublishing."

    open var artifactId: Property<String> = project.withDefault(project.prop("artifactId", propPrefix, envPrefix))
    open var groupId: Property<String> = project.withDefault(project.prop("groupId", propPrefix, envPrefix))
    open var version: Property<String> = project.withDefault(project.prop("version", propPrefix, envPrefix))
    var name: Property<String> = project.withDefault(project.prop("name", propPrefix, envPrefix))

    var description: Property<String> = project.withDefault(project.prop("description", propPrefix, envPrefix))
    var url: Property<String> = project.withDefault(project.prop("url", propPrefix, envPrefix))
    var inceptionYear: Property<String> =
        project.withDefault(project.prop("inceptionYear", propPrefix, envPrefix))

    var licenseName: Property<String> = project.withDefault(project.prop("licenseName", propPrefix, envPrefix))
    var licenseUrl: Property<String> = project.withDefault(project.prop("licenseUrl", propPrefix, envPrefix))
    var licenseDistribution: Property<String> =
        project.withDefault(project.prop("licenseDistribution", propPrefix, envPrefix))
    var licenseComments: Property<String> =
        project.withDefault(project.prop("licenseComments", propPrefix, envPrefix))

    var developerId: Property<String> = project.withDefault(project.prop("developerId", propPrefix, envPrefix))
    var developerName: Property<String> =
        project.withDefault(project.prop("developerName", propPrefix, envPrefix))
    var developerEmail: Property<String> =
        project.withDefault(project.prop("developerEmail", propPrefix, envPrefix))

    var organization: Property<String> = project.withDefault(project.prop("organization", propPrefix, envPrefix))
    var organizationUrl: Property<String> =
        project.withDefault(project.prop("organizationUrl", propPrefix, envPrefix))

    var scmUrl: Property<String> = project.withDefault(project.prop("scmUrl", propPrefix, envPrefix))
    var scmConnection: Property<String> =
        project.withDefault(project.prop("scmConnection", propPrefix, envPrefix))
    var scmDeveloperConnection: Property<String> =
        project.withDefault(project.prop("scmDeveloperConnection", propPrefix, envPrefix))

    var ossrhUser: Property<String> = project.withDefault(project.prop("ossrhUser", "yapp.", "YAPP_"))
    var ossrhPassword: Property<String> = project.withDefault(project.prop("ossrhPassword", "yapp.", "YAPP_"))
}
