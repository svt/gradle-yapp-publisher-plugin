// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0
package se.svt.oss.gradle.yapp

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

open class YappPublisherExtension(project: Project) {

    internal val sign: SignExtension = SignExtension(project)
    internal val pomE: PomExtension = PomExtension(project)
    internal val gradlepluginE: GradlePluginExtension = GradlePluginExtension(project)

    fun pom(action: Action<in PomExtension>) = action.execute(pomE)
    fun signing(action: Action<in SignExtension>) = action.execute(sign)
    fun gradleplugin(action: Action<in GradlePluginExtension>) = action.execute(gradlepluginE)
}

open class PomExtension(project: Project) {

    val envPrefix: String = "YAPP_POM_"
    var propPrefix: String = "yapp.pom."

    var artifactId: Property<String> = project.withDefault(project.prop("artifactId", propPrefix, envPrefix))
    var groupId: Property<String> = project.withDefault(project.prop("groupId", propPrefix, envPrefix))
    var version: Property<String> = project.withDefault(project.prop("version", propPrefix, envPrefix))
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

open class SignExtension(
    project: Project
) {

    val envPrefix: String = "YAPP_SIGNING_"
    var propPrefix: String = "yapp.signing."

    var enabled: Property<Boolean> = project.withDefault(project.propBool("enabled", propPrefix, envPrefix))
    var signSnapshot: Property<Boolean> =
        project.withDefault(project.propBool("signSnapshot", propPrefix, envPrefix))
    var keyId: Property<String> = project.withDefault(project.prop("keyId", propPrefix, envPrefix))
    var keySecret: Property<String> = project.withDefault(project.prop("keySecret", propPrefix, envPrefix))
    var key: Property<String> = project.withDefault(project.prop("key", propPrefix, envPrefix))
}

open class GradlePluginExtension(
    project: Project
) {

    val envPrefix: String = "YAPP_GRADLEPLUGIN_"
    var propPrefix: String = "yapp.gradleplugin."

    var id: Property<String> = project.withDefault(project.prop("id", propPrefix, envPrefix))
    var webSite: Property<String> = project.withDefault(project.prop("web", propPrefix, envPrefix))
    var vcsUrl: Property<String> = project.withDefault(project.prop("vcs", propPrefix, envPrefix))
    var tags: ListProperty<String> =
        project.withDefaultList(project.propList("tags", propPrefix, envPrefix))
    var implementationClass: Property<String> =
        project.withDefault(project.prop("class", propPrefix, envPrefix))
    var description: Property<String> =
        project.withDefault(project.prop("description", propPrefix, envPrefix))
    var displayName: Property<String> =
        project.withDefault(project.prop("displayname", propPrefix, envPrefix))

    var key: Property<String> =
        project.withDefault(project.prop("key", propPrefix, envPrefix))
    var keySecret: Property<String> =
        project.withDefault(project.prop("keySecret", propPrefix, envPrefix))
}

private fun Project.prop(property: String, propPrefix: String, envPrefix: String, notFound: String = ""): String {
    return (
        this.findProperty("$propPrefix$property") ?: System.getenv("$envPrefix${property.toUpperCase()}")
            ?: notFound
        ).toString()
}

private fun Project.propBool(property: String, propPrefix: String, envPrefix: String): Boolean {
    val prop = this.findProperty("$propPrefix$property")
    return prop?.toString()?.toBoolean() ?: System.getenv("$envPrefix$property").toBoolean()
}

private fun Project.propList(property: String, propPrefix: String, envPrefix: String): List<String> {
    return (this.findProperty("$propPrefix$property") ?: System.getenv("$envPrefix$property") ?: "").toString()
        .split(",")
}

inline fun <reified T> Project.withDefault(value: T): Property<T> =
    objects.property(T::class.java).apply { convention(value) }

inline fun <reified T> Project.withDefaultList(value: List<T>): ListProperty<T> =
    objects.listProperty(T::class.java).apply { convention(value) }
