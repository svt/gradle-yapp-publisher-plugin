package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class PublishTargetExtension(
    project: Project
) {

    val envPrefix: String = "YAPP_"
    var propPrefix: String = "yapp."

    var url: Property<String> = project.prop("targetUrl", propPrefix, envPrefix)
    var target: Property<String> = project.prop("target", propPrefix, envPrefix)
}
