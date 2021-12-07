package se.svt.oss.gradle.yapp.artifact

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import se.svt.oss.gradle.yapp.artifact.Dokka.DokkaDoc
import se.svt.oss.gradle.yapp.artifact.Dokka.DokkaGfm
import se.svt.oss.gradle.yapp.artifact.Dokka.DokkaHtml
import se.svt.oss.gradle.yapp.artifact.Dokka.DokkaJavaDoc
import se.svt.oss.gradle.yapp.artifact.Dokka.DokkaJekyll
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.extension.YappPublisherExtension
import se.svt.oss.gradle.yapp.publishingtarget.PublishingTargetType

data class ArtifactConfigure(
    val project: Project,
    val publishTarget: PublishingTargetType,
    val projectType: ProjectType
) {

    fun yappExtension(): YappPublisherExtension = project.extensions.getByType(YappPublisherExtension::class.java)

    fun configure() {
        doc()
        Sources(project)
    }

    private fun doc() {
        if (project.plugins.hasPlugin("org.jetbrains.dokka")) {
            if (yappExtension().dokkaPublishings.get().contains("javadoc"))
                dokkaDoc("dokkaJavadoc", DokkaJavaDoc::class.java)
            if (yappExtension().dokkaPublishings.get().contains("html"))
                dokkaDoc("dokkaHtml", DokkaHtml::class.java)
            if (yappExtension().dokkaPublishings.get().contains("gfm"))
                dokkaDoc("dokkaGfm", DokkaGfm::class.java)
            if (yappExtension().dokkaPublishings.get().contains("jekyll"))
                dokkaDoc("dokkaJekyll", DokkaJekyll::class.java)
        } else {
            JavaDoc(project)
        }
    }

    private inline fun <reified T : DokkaDoc> dokkaDoc(task: String, clazz: Class<T>) {

        project.extensions.configure(PublishingExtension::class.java) { pe ->

            pe.publications { publications ->

                val p = publications.getByName(publishTarget.name) as MavenPublication
                // p.from(project.components.getByName("java"))

                val source = project.tasks.findByPath(task)
                val d = project.tasks.register("${task}Gen", clazz)
                p.artifact(d)

                println("${task}Gen")
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