package se.svt.oss.gradle.yapp.artifact

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import se.svt.oss.gradle.yapp.publishtarget.PublishingTargetType

data class ArtifactConfigure(val project: Project, val publishTarget: PublishingTargetType) {

    fun javaKotlinConfigure() {

        val javaDoc = "javadocJar"
        val sources = "sourcesJar"

        /*   project.afterEvaluate {
           project.extensions.configure(JavaPluginExtension::class.java) { java ->
               java.withJavadocJar()
               java.withSourcesJar()

           }
      }*/
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
            }
        }
    }
}
