package se.svt.oss.gradle.yapp.publishingtarget

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.artifact.ArtifactConfigure
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.plugin.MavenPublishingPlugin
import java.net.URI

internal class GitHubPackagesRepository(
    override val project: Project,
    override val publishingTargetType: PublishingTargetType,
    override val projectType: ProjectType
) :
    BasePublishTarget(project, publishingTargetType, projectType) {

    override fun configure() {
        MavenPublishingPlugin(project).configure(credentials(), publishingTargetType)
        ArtifactConfigure(project, publishingTargetType, projectType).configure()
    }

    private fun credentials(): RepositoryConfiguration {
        val uri =
            URI("https://maven.pkg.github.com/${yappExtension().gitHub.namespace.get()}/${yappExtension().gitHub.repoName.get()}")
        val credential = RepositoryCredential(
            yappExtension().gitHub.user.get(), yappExtension().gitHub.token.get()
        )
        return RepositoryConfiguration(uri, uri, "GitHubPackages", credential)
    }
}
