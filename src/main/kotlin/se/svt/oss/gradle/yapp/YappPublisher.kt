// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0
package se.svt.oss.gradle.yapp

import GradlePluginPortal
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.GradleVersion
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.config.ProjectType.Companion.projectType
import se.svt.oss.gradle.yapp.extension.YappPublisherExtension
import se.svt.oss.gradle.yapp.projecttype.GradleJavaPlugin
import se.svt.oss.gradle.yapp.projecttype.GradleKotlinPlugin
import se.svt.oss.gradle.yapp.projecttype.JavaLibrary
import se.svt.oss.gradle.yapp.projecttype.JavaProject
import se.svt.oss.gradle.yapp.projecttype.KotlinLibrary
import se.svt.oss.gradle.yapp.publishtarget.BasePublishTarget
import se.svt.oss.gradle.yapp.publishtarget.MavenCentral
import se.svt.oss.gradle.yapp.publishtarget.PublishTargetType
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

        project.tasks.register("yappConfiguration", ConfigurationList::class.java)
        project.extensions.create("yapp", YappPublisherExtension::class.java, project)

        val projectType = projectType(project)

        project.afterEvaluate {

            val publishTarget = publishTarget(projectType, project)
            publishTarget.configure()
            registerYappPublisherTask(project, projectType, publishTarget)
            project.logger.info(
                "Yapp Publisher Plugin: Name: {}, Type: {}, Target: {}",
                project.name, projectType.javaClass.simpleName, publishTarget.name()
            )
        }
    }

    private fun registerYappPublisherTask(
        project: Project,
        projectType: ProjectType,
        publishTarget: BasePublishTarget
    ) {
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

/*    private fun log(message: String, filterOptions: GradleRepositoryPublisherExtension) {
        if (filterOptions.log.get()) {
            log.quiet(message)
        }
    }
*/
fun Project.hasPlugin(value: String): Boolean = project.plugins.hasPlugin(value)

fun publishTarget(
    projectType: ProjectType,
    project: Project
): BasePublishTarget =
    userSpecifiedPublishTargetType(project) ?: defaultPublishTargetType(project, projectType)

private fun userSpecifiedPublishTargetType(project: Project): BasePublishTarget? {
    val target = project.extensions.getByType(YappPublisherExtension::class.java).publishTarget.target.get()
    return if (target.isNullOrEmpty()) {
        PublishTargetType.NA.publishTarget(project)
    } else {
        PublishTargetType.valueOf(target.uppercase()).publishTarget(project)
    }
}

private fun defaultPublishTargetType(
    project: Project,
    projectType: ProjectType
): BasePublishTarget {

    return when (projectType) {
        is GradleKotlinPlugin -> GradlePluginPortal(project, PublishTargetType.GRADLE_PORTAL)
        is GradleJavaPlugin -> GradlePluginPortal(project, PublishTargetType.GRADLE_PORTAL)
        is JavaLibrary -> MavenCentral(project, PublishTargetType.MAVEN_CENTRAL)
        is JavaProject -> MavenCentral(project, PublishTargetType.MAVEN_CENTRAL)
        is KotlinLibrary -> MavenCentral(project, PublishTargetType.MAVEN_CENTRAL)
        else -> UnknownPublishTarget(project, PublishTargetType.NA)
    }
}
