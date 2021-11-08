package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class GitHubExtension(project: Project) : MavenPublishingExtension(project) {

    override val envPrefix: String = "GITHUB_"
    override var propPrefix: String = "yapp.github."

    override var artifactId: Property<String> = project.prop("artifactId", propPrefix, envPrefix)
    override var groupId: Property<String> = project.prop("groupId", propPrefix, envPrefix)
    override var version: Property<String> = project.prop("version", propPrefix, envPrefix)

    var actor: Property<String> = project.prop("actor", propPrefix, envPrefix)
    var token: Property<String> = project.prop("token", propPrefix, envPrefix)
    var namespace: Property<String> = project.prop("namespace", propPrefix, envPrefix)
    var repoName: Property<String> = project.prop("reponame", propPrefix, envPrefix)
}
