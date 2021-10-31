import org.gradle.api.Project
import se.svt.oss.gradle.yapp.plugin.GradlePortalPublishingPlugin
import se.svt.oss.gradle.yapp.publishtarget.BasePublishTarget
import se.svt.oss.gradle.yapp.publishtarget.PublishTargetType

class GradlePluginPortal(
    project: Project,
    publishTarget: PublishTargetType
) :
    BasePublishTarget(project, publishTarget) {

    override fun configure() {
        GradlePortalPublishingPlugin(project).configure()
    }
}
