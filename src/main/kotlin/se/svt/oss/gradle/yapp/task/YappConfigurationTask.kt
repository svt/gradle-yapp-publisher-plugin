package se.svt.oss.gradle.yapp.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import se.svt.oss.gradle.yapp.config.projectType
import se.svt.oss.gradle.yapp.publishingtarget.publishingTargets

abstract class YappConfigurationTask : DefaultTask() {
    init {
        group = "yapp publisher"
        description = "Prints out the current Yapp Publisher Plugin configuration"
    }

    @TaskAction
    fun printConfiguration() {

        project.publishingTargets().forEach { println(it.name()) }

        println(
            """
       
                | Yapp Publisher Plugin: Name: ${project.name}, Type: ${project.projectType().javaClass.simpleName}, Target: ${project.publishingTargets().map { it.name() }}
                
            """.trimIndent()
        )
    }
}
