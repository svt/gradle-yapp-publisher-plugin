// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0
package se.svt.oss.gradle.yapp

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.GradleVersion
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.config.identifyProjectType
import se.svt.oss.gradle.yapp.extension.GitHubExtension
import se.svt.oss.gradle.yapp.extension.GitLabExtension
import se.svt.oss.gradle.yapp.extension.GradlePluginPublishingExtension
import se.svt.oss.gradle.yapp.extension.MavenPublishingExtension
import se.svt.oss.gradle.yapp.extension.SigningExtension
import se.svt.oss.gradle.yapp.extension.YappPublisherExtension
import se.svt.oss.gradle.yapp.publishingtarget.BasePublishTarget
import se.svt.oss.gradle.yapp.publishingtarget.identifyPublishTarget
import se.svt.oss.gradle.yapp.task.CreateConfigurationTemplateTask
import se.svt.oss.gradle.yapp.task.PublishArtifactTask
import se.svt.oss.gradle.yapp.task.PublishArtifactToLocalRepoTask
import se.svt.oss.gradle.yapp.task.YappConfigurationTask

class YappPublisher : Plugin<Project> {

    companion object {
        val MIN_GRADLE: GradleVersion = GradleVersion.version("7.1")
    }

    override fun apply(project: Project) {
        isMinSupportedGradleVersion(project)
        buildExtensions(project)

        project.afterEvaluate {
            val projectType = identifyProjectType(project)
            val publishTarget = identifyPublishTarget(projectType, project)
            project.logger.info("HERE we go ${projectType.javaClass.simpleName}") // TODO remove

            configurePublishingTargets(publishTarget)
            registerTasks(project, projectType, publishTarget)

            project.logger.info( // TODO remove
                "Yapp Publisher Plugin: Name: {}, Type: {}, Target: {}",
                project.name, projectType.javaClass.simpleName, publishTarget.forEach { it.name() }
            )
        }
    }

    private fun isMinSupportedGradleVersion(project: Project) {
        if (GradleVersion.version(project.gradle.gradleVersion) < MIN_GRADLE) {
            error("This plugin is tested with $MIN_GRADLE and higher")
        }
    }

    private fun buildExtensions(project: Project) {
        project.extensions.create(
            "yapp", YappPublisherExtension::class.java, project,
            SigningExtension(project),
            MavenPublishingExtension(project),
            GitLabExtension(project),
            GitHubExtension(project),
            GradlePluginPublishingExtension(project)
        )
    }

    private fun configurePublishingTargets(publishingTargets: List<BasePublishTarget>) =
        publishingTargets.forEach { it.configure() }

    private fun registerTasks(
        project: Project,
        projectType: ProjectType,
        publishingTargets: List<BasePublishTarget>
    ) {
        project.tasks.register("yappConfiguration", YappConfigurationTask::class.java)

        project.tasks.register(
            "publishArtifactToLocalRepo",
            PublishArtifactToLocalRepoTask::class.java,
            projectType,
            publishingTargets
        )
        project.tasks.register(
            "publishArtifact",
            PublishArtifactTask::class.java,
            projectType,
            publishingTargets

        )
        project.tasks.register(
            "createConfigurationTemplate",
            CreateConfigurationTemplateTask::class.java,
            projectType,
            publishingTargets
        )
    }
}
