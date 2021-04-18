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
    internal val gradlePluginE: GradlePluginExtension = GradlePluginExtension(project)

    fun pom(action: Action<in PomExtension>) = action.execute(pomE)
    fun signing(action: Action<in SignExtension>) = action.execute(sign)
    fun gradlePlugin(action: Action<in GradlePluginExtension>) = action.execute(gradlePluginE)
}

open class PomExtension(project: Project) {

    var artifactId: Property<String> = project.withDefault(project.prop("POM_ARTIFACTID", "pom.artifactId"))
    var groupId: Property<String> = project.withDefault(project.prop("POM_GROUPID", "pom.groupId"))
    var version: Property<String> = project.withDefault(project.prop("POM_VERSION", "pom.version"))
    var name: Property<String> = project.withDefault(project.prop("POM_NAME", "pom.name"))

    var description: Property<String> = project.withDefault(project.prop("POM_DESCRIPTION", "pom.description"))
    var url: Property<String> = project.withDefault(project.prop("POM_URL", "pom.url"))
    var inceptionYear: Property<String> = project.withDefault(project.prop("POM_INCEPTIONYEAR", "pom.inceptionYear"))

    var licenseName: Property<String> = project.withDefault(project.prop("POM_LICENCENAME", "pom.licenseName"))
    var licenseUrl: Property<String> = project.withDefault(project.prop("POM_LICENCEURL", "pom.licenseUrl"))
    var licenseDistribution: Property<String> =
        project.withDefault(project.prop("POM_LICENCEDISTRIBUTION", "pom.licenseDistribution"))
    var licenseComment: Property<String> =
        project.withDefault(project.prop("POM_LICENCECOMMENTS", "pom.licenseComment"))

    var developerId: Property<String> = project.withDefault(project.prop("POM_DEVELOPERID", "pom.developerId"))
    var developerName: Property<String> = project.withDefault(project.prop("POM_DEVELOPERNAME", "pom.developerName"))
    var developerEmail: Property<String> =
        project.withDefault(project.prop("POM_DEVELOPEREMAIL", "pom.developerEmail"))

    var organization: Property<String> = project.withDefault(project.prop("POM_ORGANIZATION", "pom.organization"))
    var organizationUrl: Property<String> =
        project.withDefault(project.prop("POM_ORGANIZATIONURL", "pom.organizationUrl"))

    var scmUrl: Property<String> = project.withDefault(project.prop("POM_SCMURL", "pom.scmUrl"))
    var scmConnection: Property<String> = project.withDefault(project.prop("POM_SCMCONNECTION", "pom.scmConnection"))
    var scmDeveloperConnection: Property<String> =
        project.withDefault(project.prop("POM_SCMDEVELOPERCONNECTION", "pom.scmDeveloperConnection"))

    var ossrhUser: Property<String> = project.withDefault(project.prop("OSSRH_USER", "ossrhUser"))
    var ossrhPassword: Property<String> = project.withDefault(project.prop("OSSRH_PASSWORD", "ossrhPassword"))
}

open class SignExtension(
    project: Project
) {

    var enabled: Property<Boolean> = project.withDefault(project.propBool("SIGNING_ENABLED", "signing.enabled"))
    var signSnapshot: Property<Boolean> =
        project.withDefault(project.propBool("SIGNING_SNAPSHOT", "signing.snapshot"))
    var keyId: Property<String> = project.withDefault(project.prop("SIGNING_KEYID", "signing.keyId"))
    var password: Property<String> = project.withDefault(project.prop("SIGNING_PASSWORD", "signing.password"))
    var key: Property<String> = project.withDefault(project.prop("SIGNING_KEY", "signing.key"))
}

open class GradlePluginExtension(
    project: Project
) {

    var id: Property<String> = project.withDefault(project.prop("GRADLEPLUGIN_ID", "gradlePlugin.id"))
    var webSite: Property<String> = project.withDefault(project.prop("GRADLEPLUGIN_WEBSITE", "gradlePlugin.web"))
    var vcsUrl: Property<String> = project.withDefault(project.prop("GRADLEPLUGIN_VCS", "gradlePlugin.vcs"))
    var tags: ListProperty<String> = project.withDefaultList(project.prop("GRADLEPLUGIN_TAGS", "gradlePlugin.tags"))
    var implementationClass: Property<String> =
        project.withDefault(project.prop("GRADLEPLUGIN_CLASS", "gradlePlugin.class"))
    var description: Property<String> =
        project.withDefault(project.prop("GRADLEPLUGIN_DESCRIPTION", "gradlePlugin.description"))
    var displayName: Property<String> =
        project.withDefault(project.prop("GRADLEPLUGIN_DISPLAYNAME", "gradlePlugin.displayname"))

    var key: Property<String> =
        project.withDefault(project.prop("GRADLEPLUGIN_KEY", "gradlePlugin.key"))
    var keySecret: Property<String> =
        project.withDefault(project.prop("GRADLEPLUGIN_KEYSECRET", "gradlePlugin.keysecret"))
}

private fun Project.prop(envName: String, propName: String, notFound: String = ""): String {
    return (this.findProperty(propName) ?: System.getenv(envName) ?: notFound).toString()
}

private fun Project.propBool(envName: String, propName: String): Boolean {
    val prop = this.findProperty(propName)
    return prop?.toString()?.toBoolean() ?: System.getenv(envName).toBoolean()
}

inline fun <reified T> Project.withDefault(value: T): Property<T> =
    objects.property(T::class.java).apply { convention(value) }

inline fun <reified T> Project.withDefaultList(value: T): ListProperty<T> =
    objects.listProperty(T::class.java).apply { listOf(value) }
