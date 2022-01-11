package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class GitLabExtension @Inject constructor(project: Project, objects: ObjectFactory) :
    MavenPublishingExtension(project, objects, "yapp.gitlab.", "YAPP_GITLAB_") {

    var host: Property<String> = property("host")
    var tokenType: Property<String> = property("tokenType")
    var endpointLevel: Property<String> = property("endpointLevel")
    var gitlabGroupId: Property<String> = property("glGroupId")
    var gitlabProjectId: Property<String> = property("glProjectId")
}
