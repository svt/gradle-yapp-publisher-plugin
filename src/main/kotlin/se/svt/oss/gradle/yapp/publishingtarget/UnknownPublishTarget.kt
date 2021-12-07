package se.svt.oss.gradle.yapp.publishingtarget

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.config.ProjectType

internal class UnknownPublishTarget(override val project: Project, projectType: ProjectType) :
    BasePublishTarget(project, PublishingTargetType.UNKNOWN, projectType) {

    override fun configure() {

        project.logger.error(
            """
                |Yapp-Publisher-Plugin could not identify a PublishTarget.
                |As you added this plugin, it is assumed that you wanted to publish, i.e use a Publish Target.
                |Please see the docs (README or https://github.com/svt/gradle-yapp-publisher-plugin )
                |on how you can add a PublishTarget
            """.trimIndent()
        )
    }
}
