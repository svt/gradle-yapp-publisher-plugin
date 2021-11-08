package se.svt.oss.gradle.yapp.publishtarget

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.artifact.ArtifactConfigure
import se.svt.oss.gradle.yapp.plugin.MavenPublishingPlugin
import java.net.URI

internal class GitHubPackageRegistry(
    override val project: Project,
    publishTarget: PublishingTargetType
) :
    BasePublishTarget(project, publishTarget) {

    override fun configure() {
        MavenPublishingPlugin(project).configure(credentials(), publishTarget)
        ArtifactConfigure(project).javaKotlinConfigure()
    }

    private fun gitHubUri(): URI =
        URI("https://maven.pkg.github.com/${yappExtension().gitHub.namespace.get()}/${yappExtension().gitHub.repoName.get()}")

    private fun credentials(): RepositoryConfiguration {
        val credential = RepositoryCredential(
            yappExtension().gitHub.actor.get(), yappExtension().gitHub.token.get()
        )
        return RepositoryConfiguration(gitHubUri(), "GitHubPackages", credential)
    }
}
