package se.svt.oss.gradle.yapp.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.publishtarget.BasePublishTarget
import javax.inject.Inject

abstract class PublishToLocal @Inject constructor(
    private val projectType: ProjectType,
    private val publishTarget: BasePublishTarget
) : DefaultTask() {
    init {

        group = "yapp publisher"
        description = "Publish ${project.name} as a ${projectType.javaClass.simpleName} to the local repository"
        dependsOn(project.tasks.getByName("publishToMavenLocal"))
        doLast {
            println("Publish ${project.name} as a ${projectType.javaClass.simpleName} to the local repository")
        }
    }

    @TaskAction
    fun publishToLocal() {
    }
}
