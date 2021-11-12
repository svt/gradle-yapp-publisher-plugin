import org.gradle.api.Project
import se.svt.oss.gradle.yapp.plugin.GradlePortalPublishingPlugin
import se.svt.oss.gradle.yapp.publishingtarget.BasePublishTarget
import se.svt.oss.gradle.yapp.publishingtarget.PublishingTargetType

class GradlePluginPortal(
    project: Project,
    override val publishingTargetType: PublishingTargetType
) :
    BasePublishTarget(project, publishingTargetType) {

    override fun configure() {
        GradlePortalPublishingPlugin(project).configure()
    }
}
