package se.svt.oss.gradle.yapp.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.publishingtarget.BasePublishTarget
import javax.inject.Inject

abstract class PublishArtifactToLocalRepoTask @Inject constructor(
    private val projectType: ProjectType,
    private val publishTarget: List<BasePublishTarget>
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
