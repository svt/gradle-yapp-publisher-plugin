package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class PublishTargetExtension(
    project: Project
) {

    val envPrefix: String = "YAPP_"
    var propPrefix: String = "yapp."

    var mavenCentralLegacyUrl: Property<Boolean> = project.withDefault(project.propBool("mavenCentralLegacyUrl", propPrefix, envPrefix))
    var url: Property<String> = project.withDefault(project.prop("targetUrl", propPrefix, envPrefix))
    var target: Property<String> = project.withDefault(project.prop("target", propPrefix, envPrefix))
}
