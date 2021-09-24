// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp.config

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.YappPublisherExtension
import se.svt.oss.gradle.yapp.hasPlugin

enum class PublishTarget {
    GRADLE_PORTAL, MAVEN_CENTRAL, MAVEN_CENTRAL_SNAPSHOT
}

open class PublishTargetType(open val project: Project) {
    open fun configure() {
        project.logger.warn("Project: {} targets {} ", project.name, this.javaClass.simpleName)
    }

    companion object {
        fun publishTarget(project: Project, extension: YappPublisherExtension): PublishTargetType =
            when {
                project.hasPlugin("java-gradle-plugin") -> GradlePortal(project, extension)
                project.hasPlugin("maven-publish") -> MavenCentral(project, extension)
                else -> UnknownPublishTarget(project, extension)
            }
    }
}

internal class GradlePortal(override val project: Project, private val extension: YappPublisherExtension) :
    PublishTargetType(project) {

    override fun configure() {
        super.configure()
        project.configureGradlePublishingPlugin(extension)
    }
}

internal class MavenCentral(override val project: Project, private val extension: YappPublisherExtension) :
    PublishTargetType(project) {

    override fun configure() {
        super.configure()

        project.configureMavenPublishingPlugin(extension)
        project.configureSigningPlugin(extension)
        project.configureJavaLibraryArtifact()

        project.afterEvaluate {

            val snapShotPostfix: String =
                if (project.version.toString().endsWith("SNAPSHOT")) "Snapshot Repo" else ""
            project.logger.info("Yapp-Publisher-Plugin publishTarget conf: MavenCentral $snapShotPostfix")
        }
    }
}

internal class UnknownPublishTarget(override val project: Project, val extension: YappPublisherExtension) :
    PublishTargetType(project) {

    override fun configure() {
        super.configure()
        project.logger.error(
            "Yapp-Publisher-Plugin could not identify a PublishTarget (As you added this plugin," +
                "it is assumed that you wanted to publish your project), see the docs"
        )
    }
}
