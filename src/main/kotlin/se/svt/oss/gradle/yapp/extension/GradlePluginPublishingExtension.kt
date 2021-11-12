package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

open class GradlePluginPublishingExtension(
    val project: Project
) {

    val envPrefix: String = "YAPP_GRADLEPLUGIN_"
    var propPrefix: String = "yapp.gradleplugin."

    var id: Property<String> = property("id")
    var webSite: Property<String> = property("web")
    var vcsUrl: Property<String> = property("vcs")
    var tags: ListProperty<String> =
        project.propList("tags", propPrefix, envPrefix)
    var implementationClass: Property<String> =
        property("class")
    var description: Property<String> =
        property("description")
    var displayName: Property<String> =
        property("displayname")

    var key: Property<String> =
        property("key")
    var keySecret: Property<String> =
        property("keySecret")

    fun property(property: String) = project.prop(property, propPrefix, envPrefix)
}
