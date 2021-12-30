package se.svt.oss.gradle.yapp.artifact

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import java.io.File

open class AndroidDoc : Javadoc() {

    init {
        val androidExtension = project.extensions.getByType(LibraryExtension::class.java)

        val releaseVariantCompileProvider = androidExtension.libraryVariants.toList().last().javaCompileProvider
        dependsOn(androidExtension.libraryVariants.toList().last().javaCompileProvider)
        setSource(androidExtension.sourceSets.getByName("main").java.srcDirs)

        isFailOnError = false
        classpath += project.files(androidExtension.bootClasspath.joinToString(File.pathSeparator))
        classpath += releaseVariantCompileProvider.get().classpath
        classpath += releaseVariantCompileProvider.get().outputs.files

        exclude("**/internal/*")

        val options = options as StandardJavadocDocletOptions
        options.apply {
            encoding("utf-8")
            addStringOption("docencoding", "utf-8")
            addStringOption("charset", "utf-8")
            links("https://docs.oracle.com/javase/8/docs/api/")
            links("https://d.android.com/reference")
            links("https://developer.android.com/reference/androidx/")
        }
    }

    internal companion object {

        internal fun doc(task: String, project: Project): TaskProvider<*> {

            return project.tasks.register("${task}Gen", AndroidDoc::class.java) {
                val androidExtension = project.extensions.getByType(LibraryExtension::class.java)

                it.source(androidExtension.sourceSets.getByName("main").java.srcDirs)
            }
        }
    }
}
