package se.svt.oss.gradle.yapp.publishtarget

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.artifact.ArtifactConfigure
import se.svt.oss.gradle.yapp.isSnapShot
import se.svt.oss.gradle.yapp.plugin.MavenPublishingPlugin
import se.svt.oss.gradle.yapp.plugin.SigningPlugin
import java.net.URI

internal open class MavenCentral(
    override val project: Project,
    publishTarget: PublishingTargetType
) : BasePublishTarget(project, publishTarget) {

    override fun configure() {

        MavenPublishingPlugin(project).configure(ossrhCredential(), publishTarget)
        ArtifactConfigure(project).javaKotlinConfigure()
        SigningPlugin(project).configure()
    }

    private fun ossrhCredential(): RepositoryConfiguration {
        val uri = ossrhUrl()
        val credential = RepositoryCredential(
            yappExtension().mavenPublishing.ossrhUser.get(), yappExtension().mavenPublishing.ossrhPassword.get()
        )
        return RepositoryConfiguration(uri, "MavenCentral", credential)
    }

    private fun ossrhUrl(): URI {
        return if (project.isSnapShot()) {
            URI("https://${getUrlPrefix()}oss.sonatype.org/content/repositories/snapshots/")
        } else {
            URI("https://${getUrlPrefix()}oss.sonatype.org/service/local/staging/deploy/maven2/")
        }
    }

    private fun getUrlPrefix(): String = if (yappExtension().mavenPublishing.mavenCentralLegacyUrl.get()) "" else "s01."
}
