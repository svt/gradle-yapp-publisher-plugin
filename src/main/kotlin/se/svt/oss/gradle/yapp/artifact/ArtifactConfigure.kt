package se.svt.oss.gradle.yapp.artifact

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.TaskProvider
import se.svt.oss.gradle.yapp.artifact.Dokka.DokkaHtml
import se.svt.oss.gradle.yapp.artifact.Dokka.DokkaJavaDoc
import se.svt.oss.gradle.yapp.extension.YappPublisherExtension
import se.svt.oss.gradle.yapp.publishingtarget.PublishingTargetType

data class ArtifactConfigure(
    val project: Project,
    val publishTarget: PublishingTargetType,
) {

    fun yappExtension(): YappPublisherExtension = project.extensions.getByType(YappPublisherExtension::class.java)

    fun configure() {
        doc()
        if (project.plugins.hasPlugin("com.android.library")) {

            project.afterEvaluate {
                addArtifact(AndroidSource.source("androidSourcesGen", project))
            }
        } else {
            Source(project)
        }
    }

    private fun doc() {
        /*if (project.plugins.hasPlugin("com.android.library")) {
            project.afterEvaluate {
                addArtifact(AndroidDoc.doc("androidDocGen", project))
            }

        } else */
        if (project.plugins.hasPlugin("org.jetbrains.dokka")) {
            if (yappExtension().dokkaPublishings.get().contains("javadoc"))
                addArtifact(DokkaJavaDoc.doc("dokkaJavadocGen", project))
            if (yappExtension().dokkaPublishings.get().contains("html"))
                addArtifact(DokkaHtml.doc("dokkaHtmlGen", project))
            if (yappExtension().dokkaPublishings.get().contains("gfm"))
                addArtifact(DokkaHtml.doc("dokkaGfmGen", project))
            if (yappExtension().dokkaPublishings.get().contains("jekyll")) println()
            addArtifact(DokkaHtml.doc("dokkaJekyllGen", project))
        } else {
            JavaDoc(project)
        }
    }

    private fun addArtifact(provider: TaskProvider<*>) {

        project.extensions.configure(PublishingExtension::class.java) { pe ->

            pe.publications { publications ->
                publications.forEach { project.logger.warn("PUBLICATIONS " + it.name) }

                try {
                    val p = publications.getByName(publishTarget.name) as MavenPublication
                    // p.from(project.components.getByName("java"))
                    p.artifact(provider)
                } catch (e: Exception) {

                    publications.forEach { project.logger.warn("ERRPUBLICATIONS " + it.name) }
                }
            }
        }
    }
}
/*
project.extensions.configure(PublishingExtension::class.java) { pe ->

    pe.publications { publications ->
        val p = publications.getByName(publishTarget.name) as MavenPublication
        p.from(project.components.getByName("java"))

        val source = project.tasks.findByPath(sources)
        val docs = project.tasks.findByPath(javaDoc)

        when (docs) {
            null -> {
                val d = project.tasks.register(javaDoc, JavaDoc::class.java)
                p.artifact(d)
            }
        }
        when (source) {
            null -> {
                val s = project.tasks.register(sources, Sources::class.java)
                p.artifact(s)
            }
        }
           */
