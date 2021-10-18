package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class SigningExtension(
    project: Project
) {

    val envPrefix: String = "YAPP_SIGNING_"
    val propPrefix: String = "yapp.signing."

    var enabled: Property<Boolean> = project.withDefault(project.propBool("enabled", propPrefix, envPrefix))
    var signSnapshot: Property<Boolean> =
        project.withDefault(project.propBool("signSnapshot", propPrefix, envPrefix))
    var keyId: Property<String> = project.withDefault(project.prop("keyId", propPrefix, envPrefix))
    var keySecret: Property<String> = project.withDefault(project.prop("keySecret", propPrefix, envPrefix))
    var key: Property<String> = project.withDefault(project.prop("key", propPrefix, envPrefix))
}
