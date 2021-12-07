package se.svt.oss.gradle.yapp.artifact.Dokka

import org.gradle.jvm.tasks.Jar
import se.svt.oss.gradle.yapp.artifact.Dokka.DokkaDoc
import se.svt.oss.gradle.yapp.publishingtarget.PublishingTargetType

open class DokkaHtml:

    DokkaDoc() {

    init {
        archiveClassifier.set("html-doc")
        val dokkaHtml = project.tasks.getByName("dokkaHtml")
        dependsOn(dokkaHtml)
        from(dokkaHtml)
    }
}
