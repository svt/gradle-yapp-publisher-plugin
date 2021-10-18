package se.svt.oss.gradle.yapp.config

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.hasPlugin
import se.svt.oss.gradle.yapp.projecttype.GradleJavaPlugin
import se.svt.oss.gradle.yapp.projecttype.GradleKotlinPlugin
import se.svt.oss.gradle.yapp.projecttype.JavaLibrary
import se.svt.oss.gradle.yapp.projecttype.JavaProject
import se.svt.oss.gradle.yapp.projecttype.KotlinLibrary
import se.svt.oss.gradle.yapp.projecttype.UnknownProject

open class ProjectType(open val project: Project) {

    companion object {
        fun projectType(project: Project): ProjectType = when {
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
