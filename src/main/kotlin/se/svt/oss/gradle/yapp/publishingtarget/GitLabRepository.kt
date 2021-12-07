package se.svt.oss.gradle.yapp.publishingtarget

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.artifact.ArtifactConfigure
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.plugin.MavenPublishingPlugin
import java.net.URI

internal class GitLabRepository(
    override val project: Project,
    override val publishingTargetType: PublishingTargetType,
    override val projectType: ProjectType
) :
    BasePublishTarget(project, publishingTargetType, projectType) {

    override fun configure() {
        // val tokenTypes = listOf("Private-Token", "Deploy-Token", "Job-Token")

        MavenPublishingPlugin(project).configure(credentials(), publishingTargetType)
        ArtifactConfigure(project, publishingTargetType, projectType).configure()
    }

    private fun gitLabUri(): URI {
        return when (yappExtension().gitLab.endpointLevel.get()) {
            "project" -> URI("${yappExtension().gitLab.host.get()}/api/v4/projects/${yappExtension().gitLab.gitlabProjectId.get()}/packages/maven")
            "group" -> URI("${yappExtension().gitLab.host.get()}/api/v4/groups/${yappExtension().gitLab.gitlabGroupId.get()}/-/packages/maven")
            else -> {
                throw IllegalArgumentException("Submit at valid deployType - project, group")
            }
        }
    }

    private fun credentials(): RepositoryConfiguration {
        val credential = RepositoryCredential(
            yappExtension().gitLab.tokenType.get(), yappExtension().gitLab.token.get()
        )
        return RepositoryConfiguration(gitLabUri(), gitLabUri(), "GitLab", credential)
    }
}
