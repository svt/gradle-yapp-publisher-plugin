import org.gradle.api.Project
import se.svt.oss.gradle.yapp.artifact.ArtifactConfigure
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.plugin.GradlePortalPublishingPlugin
import se.svt.oss.gradle.yapp.publishingtarget.BasePublishTarget
import se.svt.oss.gradle.yapp.publishingtarget.PublishingTargetType

class GradlePluginPortal(
    project: Project,
    override val publishingTargetType: PublishingTargetType,
    override val projectType: ProjectType
) :
    BasePublishTarget(project, publishingTargetType, projectType) {

    override fun configure() {
        GradlePortalPublishingPlugin(project).configure()
        ArtifactConfigure(project, publishingTargetType, projectType).configure()
    }
}
