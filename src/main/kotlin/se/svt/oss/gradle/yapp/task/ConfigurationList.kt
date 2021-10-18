package se.svt.oss.gradle.yapp.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.extension.YappPublisherExtension
import se.svt.oss.gradle.yapp.publishTarget

abstract class ConfigurationList : DefaultTask() {
    init {
        group = "yapp publisher"
        description = "Print out the Yapp Publisher Plugin configuration"
    }

    @TaskAction
    fun printConfiguration() {

        val extension = project.extensions.getByType(YappPublisherExtension::class.java)
        val projectType = ProjectType.projectType(project)

        val publishTarget = publishTarget(projectType, project)

        println(
            """
       
                | Yapp Publisher Plugin: Name: ${project.name}, Type: ${projectType.javaClass.simpleName}, Target: ${publishTarget.name()}
                
            """.trimIndent()
        )
    }
}
