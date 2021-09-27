package se.svt.oss.gradle.yapp

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.config.publishTarget

class YappTask

abstract class ConfigurationListTask : DefaultTask() {
    init {
        group = "yapp publisher"
        description = "Print out the YAPP Publisher configuration"
    }

    @TaskAction
    fun printConfiguration() {

        val extension = project.extensions.getByType(YappPublisherExtension::class.java)
        val projectType = ProjectType.libraryType(project)

        val publishTarget = publishTarget(projectType, project, extension)

        println(
            """
                
                |Yapp Publisher Plugin: Name: ${project.name}, Type: ${projectType.javaClass.simpleName}, Target: ${publishTarget.name()}
                
            """.trimIndent()
        )
    }
}
