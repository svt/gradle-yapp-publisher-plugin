package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class GitLabExtension(project: Project) : MavenPublishingExtension(project) {

    override val envPrefix: String = "YAPP_GITLAB_"
    override var propPrefix: String = "yapp.gitlab."

    override var artifactId: Property<String> = project.withDefault(project.prop("artifactId", propPrefix, envPrefix))
    override var groupId: Property<String> = project.withDefault(project.prop("groupId", propPrefix, envPrefix))
    override var version: Property<String> = project.withDefault(project.prop("version", propPrefix, envPrefix))

    var host: Property<String> = project.withDefault(project.prop("host", propPrefix, envPrefix))
    var token: Property<String> = project.withDefault(project.prop("token", propPrefix, envPrefix))
    var tokenType: Property<String> = project.withDefault(project.prop("tokenType", propPrefix, envPrefix))
    var endpointLevel: Property<String> = project.withDefault(project.prop("endpointLevel", propPrefix, envPrefix))
    var gitlabGroupId: Property<String> = project.withDefault(project.prop("glGroupId", propPrefix, envPrefix))
    var gitlabProjectId: Property<String> = project.withDefault(project.prop("glProjectId", propPrefix, envPrefix))
}
