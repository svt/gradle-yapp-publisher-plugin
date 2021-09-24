package se.svt.oss.gradle.yapp.config

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.YappPublisherExtension
import se.svt.oss.gradle.yapp.hasPlugin

enum class PROJECTTYPE {
    GRADLE_KOTLIN_PLUGIN,
    JAVA_KOTLIN_PLUGIN,
    KOTLIN_LIBRARY,
    JAVA_LIBRARY,
    UNKNOWN_LIBRARY
}

open class ProjectType(open val project: Project) {
    fun configure() {
        project.logger.warn("Project: {} is a {}", project.name, this.javaClass.simpleName)
    }

    companion object {
        fun libraryType(project: Project, extension: YappPublisherExtension): ProjectType = when {
            project.hasPlugin("org.jetbrains.kotlin.jvm") &&
                project.hasPlugin("java-gradle-plugin") &&
                project.hasPlugin("java-library") -> GradleKotlinPlugin(
                project,
                extension
            )
            project.hasPlugin("org.jetbrains.kotlin.jvm") &&
                project.hasPlugin("java-library") -> KotlinProject(
                project,
                extension
            )
            project.hasPlugin("java-gradle-plugin") -> GradleJavaPlugin(project, extension)
            project.hasPlugin("java-library") -> JavaProject(project, extension)
            project.hasPlugin("java") -> JavaProject(project, extension)

            else -> UnknownProject(project, extension)
        }
    }
}

internal class JavaProject(override val project: Project, private val extension: YappPublisherExtension) :
    ProjectType(project)

internal class KotlinProject(override val project: Project, private val extension: YappPublisherExtension) :
    ProjectType(project)

internal class GradleJavaPlugin(override val project: Project, private val extension: YappPublisherExtension) :
    ProjectType(project)

internal class GradleKotlinPlugin(override val project: Project, private val extension: YappPublisherExtension) :
    ProjectType(project)

internal class UnknownProject(override val project: Project, private val extension: YappPublisherExtension) :
    ProjectType(project)
