package se.svt.oss.gradle.yapp.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.publishingtarget.BasePublishTarget
import javax.inject.Inject

abstract class CreateConfigurationTemplateTask @Inject constructor(
    private val projectType: ProjectType,
    private val publishTarget: List<BasePublishTarget>
) : DefaultTask() {
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