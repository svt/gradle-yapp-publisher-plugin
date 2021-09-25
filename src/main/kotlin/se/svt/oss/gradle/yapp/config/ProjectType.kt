package se.svt.oss.gradle.yapp.config

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.hasPlugin

enum class PROJECTTYPE {
    GRADLE_KOTLIN_PLUGIN,
    JAVA_KOTLIN_PLUGIN,
    KOTLIN_LIBRARY,
    JAVA_LIBRARY,
    UNKNOWN_LIBRARY
}

open class ProjectType(open val project: Project) {

    companion object {
        fun libraryType(project: Project): ProjectType = when {
            project.hasPlugin("org.jetbrains.kotlin.jvm") &&
                project.hasPlugin("java-gradle-plugin") &&
                project.hasPlugin("java-library") -> GradleKotlinPlugin(project)
            project.hasPlugin("org.jetbrains.kotlin.jvm") &&
                project.hasPlugin("java-library") -> KotlinLibrary(project)
            project.hasPlugin("java-gradle-plugin") -> GradleJavaPlugin(project)
            project.hasPlugin("java-library") -> JavaLibrary(project)
            project.hasPlugin("java") -> JavaProject(project)

            else -> UnknownProject(project)
        }
    }
}

class JavaLibrary(override val project: Project) :
    ProjectType(project)

internal class JavaProject(override val project: Project) :
    ProjectType(project)

internal class KotlinLibrary(override val project: Project) :
    ProjectType(project)

class GradleJavaPlugin(override val project: Project) :
    ProjectType(project)

class GradleKotlinPlugin(override val project: Project) :
    ProjectType(project)

internal class UnknownProject(override val project: Project) :
    ProjectType(project)
