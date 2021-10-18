package se.svt.oss.gradle.yapp.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.projecttype.GradleJavaPlugin
import se.svt.oss.gradle.yapp.projecttype.GradleKotlinPlugin
import se.svt.oss.gradle.yapp.publishtarget.BasePublishTarget
import javax.inject.Inject

abstract class Publish @Inject constructor(
    private val projectType: ProjectType,
    private val publishTarget: BasePublishTarget
) : DefaultTask() {
    init {
        group = "yapp publisher"
        description = "Publish ${project.name} as a ${projectType.javaClass.simpleName} to ${publishTarget.name()}"
        when (projectType) {
            is GradleJavaPlugin ->
                dependsOn(project.tasks.getByName("publishPlugins"))
            is GradleKotlinPlugin -> dependsOn(project.tasks.getByName("publishPlugins"))
            else -> dependsOn(project.tasks.getByName("publish"))
        }
        doLast {

            println("Published ${project.name} as a ${projectType.javaClass.simpleName} to ${publishTarget.name()}")
        }
    }

    @TaskAction
    fun publishToPublishTarget() {
    }
}
