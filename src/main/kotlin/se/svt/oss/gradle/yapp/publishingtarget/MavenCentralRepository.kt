package se.svt.oss.gradle.yapp.publishingtarget

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.artifact.ArtifactConfigure
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.plugin.MavenPublishingPlugin
import se.svt.oss.gradle.yapp.plugin.SigningPlugin
import java.net.URI

internal open class MavenCentralRepository(
    override val project: Project,
    override val publishingTargetType: PublishingTargetType,
    override val projectType: ProjectType
) :
    BasePublishTarget(project, publishingTargetType, projectType) {

    override fun configure() {

        MavenPublishingPlugin(project).configure(ossrhCredential(), publishingTargetType)
        ArtifactConfigure(project, publishingTargetType, projectType).configure()
        SigningPlugin(project).configure()
    }

    private fun ossrhCredential(): RepositoryConfiguration {
        val uri = URI("https://${getUrlPrefix()}oss.sonatype.org/content/repositories/snapshots/")
        val snapShotUri = URI("https://${getUrlPrefix()}oss.sonatype.org/service/local/")
        val credential = RepositoryCredential(
            yappExtension().mavenPublishing.user.get(), yappExtension().mavenPublishing.password.get()
        )
        return RepositoryConfiguration(uri, snapShotUri, "MavenCentral", credential)
    }

    private fun getUrlPrefix(): String = if (yappExtension().mavenPublishing.mavenCentralLegacyUrl.get()) "" else "s01."
}
