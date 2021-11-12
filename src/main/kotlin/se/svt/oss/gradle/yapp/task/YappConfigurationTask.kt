package se.svt.oss.gradle.yapp.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.publishingtarget.identifyPublishTarget

abstract class YappConfigurationTask : DefaultTask() {
    init {
        group = "yapp publisher"
        description = "Prints out the current Yapp Publisher Plugin configuration"
    }

    @TaskAction
    fun printConfiguration() {

        val projectType = ProjectType.projectType(project)

        val publishTarget = identifyPublishTarget(projectType, project)

        publishTarget.forEach { println(it.name()) }

        println(
            """
       
                | Yapp Publisher Plugin: Name: ${project.name}, Type: ${projectType.javaClass.simpleName}, Target: ${publishTarget.map { it.name() }}
                
            """.trimIndent()
        )
    }
}
