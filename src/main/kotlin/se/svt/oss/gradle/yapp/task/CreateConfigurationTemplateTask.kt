package se.svt.oss.gradle.yapp.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class CreateConfigurationTemplateTask : DefaultTask() {
    init {
        group = "yapp publisher"
        description = "Create template gradle.properties.template based on the guess setup"
    }

    @TaskAction
    fun createTemplate() {

        println(
            """
                
                |TODO - nt yet implemented
                
            """.trimIndent()
        )
    }
}
