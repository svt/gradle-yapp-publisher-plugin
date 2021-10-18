package se.svt.oss.gradle.yapp.publishtarget

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.artifact.ArtifactConfigure
import se.svt.oss.gradle.yapp.isSnapShot
import se.svt.oss.gradle.yapp.plugin.MavenPublishing
import se.svt.oss.gradle.yapp.plugin.Signing
import java.net.URI

internal open class MavenCentral(
    override val project: Project,
    publishTarget: PublishTargetType
) :
    BasePublishTarget(project, publishTarget) {

    override fun configure() {

        MavenPublishing().configure(ossrhCredential(), publishTarget, project)
        ArtifactConfigure().javaKotlinConfigure(project)
        Signing().configure(project)
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

    private fun getUrlPrefix(): String = if (yappExtension().publishTarget.mavenCentralLegacyUrl.get()) "" else "s01."
}
