// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp.publishingtarget

import GradlePluginPortal
import org.gradle.api.Project
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.extension.YappPublisherExtension
import se.svt.oss.gradle.yapp.isSnapShot
import se.svt.oss.gradle.yapp.projecttype.GradleJavaPlugin
import se.svt.oss.gradle.yapp.projecttype.GradleKotlinPlugin
import se.svt.oss.gradle.yapp.projecttype.JavaLibrary
import se.svt.oss.gradle.yapp.projecttype.JavaProject
import se.svt.oss.gradle.yapp.projecttype.KotlinLibrary

abstract class BasePublishTarget(
    open val project: Project,
    open val publishingTargetType: PublishingTargetType,
    open val projectType: ProjectType
) {

    abstract fun configure()

    open fun yappExtension(): YappPublisherExtension = project.extensions.getByType(YappPublisherExtension::class.java)

    fun name(): String {
        return if (project.isSnapShot()) {
            "${this.javaClass.simpleName}-SNAPSHOT"
        } else {
            this.javaClass.simpleName
        }
    }
}

fun identifyPublishTarget(
    projectType: ProjectType,
    project: Project
): List<BasePublishTarget> {
    val targets = specifiedPublishTargetType(project, projectType)

    return when (targets.isEmpty()) {
        true -> {
            listOf(guessPublishTargetType(project, projectType))
        }
        else -> targets
    }
}

private fun specifiedPublishTargetType(project: Project, projectType: ProjectType): List<BasePublishTarget> =
    project.extensions.getByType(YappPublisherExtension::class.java).targets.getOrElse(emptyList()).map {
        PublishingTargetType.valueOf(it.uppercase().trim()).publishTarget(project, projectType)
    }

private fun guessPublishTargetType(
    project: Project,
    projectType: ProjectType
): BasePublishTarget {

    return when (projectType) {
        is GradleKotlinPlugin -> GradlePluginPortal(project, PublishingTargetType.GRADLE_PORTAL, projectType)
        is GradleJavaPlugin -> GradlePluginPortal(project, PublishingTargetType.GRADLE_PORTAL, projectType)
        is JavaLibrary -> MavenCentralRepository(project, PublishingTargetType.MAVEN_CENTRAL, projectType)
        is JavaProject -> MavenCentralRepository(project, PublishingTargetType.MAVEN_CENTRAL, projectType)
        is KotlinLibrary -> MavenCentralRepository(project, PublishingTargetType.MAVEN_CENTRAL, projectType)
        else -> UnknownPublishTarget(project, projectType)
    }
}
