package se.svt.oss.gradle.yapp.publishtarget

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.artifact.ArtifactConfigure
import se.svt.oss.gradle.yapp.plugin.MavenPublishingPlugin
import java.net.URI

internal class GitHubPackagesRepository(
    override val project: Project,
    override val publishingTargetType: PublishingTargetType
) :
    BasePublishTarget(project, publishingTargetType) {

    override fun configure() {
        MavenPublishingPlugin(project).configure(credentials(), publishingTargetType)
        ArtifactConfigure(project, publishingTargetType).javaKotlinConfigure()
    }

    private fun gitHubUri(): URI =
        URI("https://maven.pkg.github.com/${yappExtension().gitHub.namespace.get()}/${yappExtension().gitHub.repoName.get()}")

    private fun credentials(): RepositoryConfiguration {
        val credential = RepositoryCredential(
            yappExtension().gitHub.user.get(), yappExtension().gitHub.token.get()
        )
        return RepositoryConfiguration(gitHubUri(), "GitHubPackages", credential)
    }
}
