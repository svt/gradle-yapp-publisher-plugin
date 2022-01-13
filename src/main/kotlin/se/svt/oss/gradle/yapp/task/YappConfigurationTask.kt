package se.svt.oss.gradle.yapp.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import se.svt.oss.gradle.yapp.config.projectType
import se.svt.oss.gradle.yapp.publishingtarget.fetchPluginExtensionsPropertiesForTarget
import se.svt.oss.gradle.yapp.publishingtarget.publishingTargets

abstract class YappConfigurationTask : DefaultTask() {
    init {
        group = "yapp publisher"
        description = "Prints out the current Yapp Publisher Plugin configuration"
    }

    @TaskAction
    fun printConfiguration() {

        println("Yapp Publisher Plugin")
        println("Name: ${project.name}, Type: ${project.projectType().javaClass.simpleName}")

        println("\nConfigurations\n")

        project.publishingTargets().forEach { target ->
            val properties = fetchPluginExtensionsPropertiesForTarget(target)
            properties.prettyPrint()
            println("\n")
        }
    }
}
