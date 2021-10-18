import org.gradle.api.Project
import se.svt.oss.gradle.yapp.plugin.GradlePluginPublishing
import se.svt.oss.gradle.yapp.publishtarget.BasePublishTarget
import se.svt.oss.gradle.yapp.publishtarget.PublishTargetType

class GradlePluginPortal(
    project: Project,
    publishTarget: PublishTargetType
) :
    BasePublishTarget(project, publishTarget) {

    override fun configure() {
        GradlePluginPublishing().configure(project)
    }
}
