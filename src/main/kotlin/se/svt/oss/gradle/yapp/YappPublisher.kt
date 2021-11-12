// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0
package se.svt.oss.gradle.yapp

import GradlePluginPortal
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.GradleVersion
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.extension.GitHubExtension
import se.svt.oss.gradle.yapp.extension.GitLabExtension
import se.svt.oss.gradle.yapp.extension.GradlePluginPublishingExtension
import se.svt.oss.gradle.yapp.extension.MavenPublishingExtension
import se.svt.oss.gradle.yapp.extension.SigningExtension
import se.svt.oss.gradle.yapp.extension.YappPublisherExtension
import se.svt.oss.gradle.yapp.projecttype.GradleJavaPlugin
import se.svt.oss.gradle.yapp.projecttype.GradleKotlinPlugin
import se.svt.oss.gradle.yapp.projecttype.JavaLibrary
import se.svt.oss.gradle.yapp.projecttype.JavaProject
import se.svt.oss.gradle.yapp.projecttype.KotlinLibrary
import se.svt.oss.gradle.yapp.publishtarget.BasePublishTarget
import se.svt.oss.gradle.yapp.publishtarget.MavenCentralRepository
import se.svt.oss.gradle.yapp.publishtarget.PublishingTargetType
import se.svt.oss.gradle.yapp.publishtarget.UnknownPublishTarget
import se.svt.oss.gradle.yapp.task.ConfigurationList
import se.svt.oss.gradle.yapp.task.CreateConfigurationTemplate
import se.svt.oss.gradle.yapp.task.Publish
import se.svt.oss.gradle.yapp.task.PublishToLocal

class YappPublisher : Plugin<Project> {

    companion object {
        val MIN_GRADLE = GradleVersion.version("7.1")
    }

    override fun apply(project: Project) {

        isMinSupportedGradleVersion(project)

        project.extensions.create(
            "yapp", YappPublisherExtension::class.java, project,
            SigningExtension(project),
            MavenPublishingExtension(project),
            GitLabExtension(project),
            GitHubExtension(project),
            GradlePluginPublishingExtension(project)
        )

        project.afterEvaluate {
            val projectType = ProjectType.projectType(project)
            val publishTarget = publishTarget(projectType, project)
            println("HERE we go ${projectType.javaClass.simpleName}")

            publishTarget.forEach { it.configure() }

            registerTasks(project, projectType, publishTarget)
            project.logger.info(
                "Yapp Publisher Plugin: Name: {}, Type: {}, Target: {}",
                project.name, projectType.javaClass.simpleName, publishTarget.forEach { it.name() }
            )
        }
    }

    private fun registerTasks(
        project: Project,
        projectType: ProjectType,
        publishTarget: List<BasePublishTarget>
    ) {
        project.tasks.register("yappConfiguration", ConfigurationList::class.java)

        project.tasks.register<PublishToLocal>(
            "publishArtifactToLocalRepo",
            PublishToLocal::class.java,
            projectType,
            publishTarget
        )
        project.tasks.register<Publish>(
            "publishArtifact",
            Publish::class.java,
            projectType,
            publishTarget

        )
        project.tasks.register<CreateConfigurationTemplate>(
            "createConfigurationTemplate",
            CreateConfigurationTemplate::class.java,
            projectType,
            publishTarget
        )
    }

    private fun isMinSupportedGradleVersion(project: Project) {
        if (GradleVersion.version(project.gradle.gradleVersion) < MIN_GRADLE) {
            error("This plugin is tested with $MIN_GRADLE and higher")
        }
    }
}

fun Project.hasPlugin(value: String): Boolean = project.plugins.hasPlugin(value)

fun publishTarget(
    projectType: ProjectType,
    project: Project
): List<BasePublishTarget> {
    val targets = userSpecifiedPublishTargetType(project)

    return when (targets.isEmpty()) {
        true -> {
            listOf(defaultPublishTargetType(project, projectType))
        }
        else -> targets
    }
}

private fun userSpecifiedPublishTargetType(project: Project): List<BasePublishTarget> =
    project.extensions.getByType(YappPublisherExtension::class.java).targets.getOrElse(emptyList()).map {
        PublishingTargetType.valueOf(it.uppercase().trim()).publishTarget(project)
    }

private fun defaultPublishTargetType(
    project: Project,
    projectType: ProjectType
): BasePublishTarget {

    return when (projectType) {
        is GradleKotlinPlugin -> GradlePluginPortal(project, PublishingTargetType.GRADLE_PORTAL)
        is GradleJavaPlugin -> GradlePluginPortal(project, PublishingTargetType.GRADLE_PORTAL)
        is JavaLibrary -> MavenCentralRepository(project, PublishingTargetType.MAVEN_CENTRAL)
        is JavaProject -> MavenCentralRepository(project, PublishingTargetType.MAVEN_CENTRAL)
        is KotlinLibrary -> MavenCentralRepository(project, PublishingTargetType.MAVEN_CENTRAL)
        else -> UnknownPublishTarget(project)
    }
}
