package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

open class GradlePluginPublishingExtension @Inject constructor(project: Project, objects: ObjectFactory) :
    PropertyHandler(project, objects, "yapp.gradleplugin.", "YAPP_GRADLEPLUGIN_") {

    var id: Property<String> = property("id")
    var webSite: Property<String> = property("web")
    var vcsUrl: Property<String> = property("vcs")
    var tags: ListProperty<String> = propertyList("tags") { map -> map.values.first() }
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
}
