import org.gradle.api.Project
import se.svt.oss.gradle.yapp.plugin.GradlePortalPublishingPlugin
import se.svt.oss.gradle.yapp.publishtarget.BasePublishTarget
import se.svt.oss.gradle.yapp.publishtarget.PublishingTargetType

class GradlePluginPortal(
    project: Project,
    publishTarget: PublishingTargetType
) :
    BasePublishTarget(project, publishTarget) {

    override fun configure() {
        GradlePortalPublishingPlugin(project).configure()
    }
}
