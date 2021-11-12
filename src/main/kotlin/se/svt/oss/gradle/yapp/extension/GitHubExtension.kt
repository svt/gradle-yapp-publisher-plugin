package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class GitHubExtension(project: Project) : MavenPublishingExtension(project) {

    override val envPrefix: String = "GITHUB_"
    override var propPrefix: String = "yapp.github."

    var namespace: Property<String> = property("namespace")
    var repoName: Property<String> = property("reponame")
}
