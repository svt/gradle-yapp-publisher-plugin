package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

open class GradlePluginPublishingExtension(
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
