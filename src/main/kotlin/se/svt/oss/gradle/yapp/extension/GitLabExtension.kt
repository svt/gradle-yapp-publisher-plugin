package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class GitLabExtension(project: Project) : MavenPublishingExtension(project) {

    override val envPrefix: String = "YAPP_GITLAB_"
    override var propPrefix: String = "yapp.gitlab."

    var host: Property<String> = property("host")
    var tokenType: Property<String> = property("tokenType")
    var endpointLevel: Property<String> = property("endpointLevel")
    var gitlabGroupId: Property<String> = property("glGroupId")
    var gitlabProjectId: Property<String> = property("glProjectId")
}
