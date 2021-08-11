// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp

import org.gradle.api.Project
import org.slf4j.LoggerFactory

internal class PublishTargetConfiguration(val project: Project, val extension: YappPublisherExtension) {

    init {
        configure()
    }

    private fun configure() {
        project.afterEvaluate {
            val publishTarget: PublishTarget = when {
                project.hasPlugin("java-gradle-plugin") -> GradlePortal(project, extension)
                project.hasPlugin("maven-publish") -> MavenCentral(project, extension)
                else -> UnknownPublishTarget(project, extension)
            }
            publishTarget.configure()
        }
    }
}

fun Project.hasPlugin(value: String): Boolean {
    val hasPlugin = project.plugins.hasPlugin(value)
    if (hasPlugin) logger.warn("Yapp-Publisher-Plugin identified plugin $value")
    return hasPlugin
}

interface PublishTarget {
    fun configure()
}

internal class GradlePortal(private val project: Project, private val extension: YappPublisherExtension) :
    PublishTarget {
    override fun configure() {

        project.logger.warn("Yapp-Publisher-Plugin publishTarget conf: GradlePortal")
        project.configureGradlePublishingPlugin(extension)
    }
}

internal class MavenCentral(private val project: Project, private val extension: YappPublisherExtension) :
    PublishTarget {

    override fun configure() {

        val platform: LibraryType = when {
            project.hasPlugin("org.jetbrains.kotlin.jvm") -> KotlinLibrary(project, extension)
            project.hasPlugin("java-library") -> JavaLibrary(project, extension)
            project.hasPlugin("java") -> JavaLibrary(project, extension)
            else -> UnknownLibrary(project, extension)
        }
        project.configureMavenPublishingPlugin(extension)
        project.configureSigningPlugin(extension)
        project.configureJavaLibraryArtifact()

        project.afterEvaluate {

            val snapShotPostfix: String =
                if (project.version.toString().endsWith("SNAPSHOT")) "Snapshot Repo" else ""
            project.logger.warn("Yapp-Publisher-Plugin publishTarget conf: MavenCentral $snapShotPostfix")
        }
    }
}

internal class UnknownPublishTarget(val project: Project, val extension: YappPublisherExtension) : PublishTarget {
    override fun configure() {
        project.logger.warn(
            "Yapp-Publisher-Plugin could not identify a PublishTarget. As you added this plugin," +
                "it is assumed you want to publish your project, see the docs"
        )
    }
}

interface LibraryType {
    fun configure()
}

internal class JavaLibrary(val project: Project, private val extension: YappPublisherExtension) : LibraryType {
    override fun configure() {
    }
}

internal class KotlinLibrary(val project: Project, private val extension: YappPublisherExtension) : LibraryType {
    override fun configure() {
    }
}

internal class UnknownLibrary(val project: Project, val extension: YappPublisherExtension) : LibraryType {
    val logger = LoggerFactory.getLogger("GradleYappPublisherPlugin")
    override fun configure() {
        logger.info("no supported library platform found for ${project.name}")
    }
}
