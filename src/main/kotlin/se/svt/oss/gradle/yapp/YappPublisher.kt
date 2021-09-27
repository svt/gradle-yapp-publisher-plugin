// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0
package se.svt.oss.gradle.yapp

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.GradleVersion
import se.svt.oss.gradle.yapp.config.ProjectType.Companion.libraryType
import se.svt.oss.gradle.yapp.config.publishTarget

class YappPublisher : Plugin<Project> {
    companion object {
        // Why not Kotlinglogging etc - see https://discuss.gradle.org/t/logging-in-gradle-plugin/31685/2
        val MIN_GRADLE = GradleVersion.version("7.1")
    }

    override fun apply(project: Project) {
        isMinSupportedGradleVersion(project)

        project.tasks.register("yappConfiguration", ConfigurationListTask::class.java)

        val extension = project.extensions.create("yapp", YappPublisherExtension::class.java, project)

        project.afterEvaluate {
            val projectType = libraryType(project)
            val publishTarget = publishTarget(projectType, project, extension)
            publishTarget.configure()

            project.logger.info(
                "Yapp Publisher Plugin: Name: {}, Type: {}, Target: {}",
                project.name, projectType.javaClass.simpleName, publishTarget.name()
            )
        }
    }

    private fun isMinSupportedGradleVersion(project: Project) {
        if (GradleVersion.version(project.gradle.gradleVersion) < MIN_GRADLE) {
            error("This plugin is tested with $MIN_GRADLE and higher")
        }
    }
}

/*    private fun log(message: String, filterOptions: GradleRepositoryPublisherExtension) {
        if (filterOptions.log.get()) {
            log.quiet(message)
        }
    }
*/
fun Project.hasPlugin(value: String): Boolean = project.plugins.hasPlugin(value)
