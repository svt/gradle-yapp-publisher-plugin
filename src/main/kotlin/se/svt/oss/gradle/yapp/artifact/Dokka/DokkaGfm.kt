package se.svt.oss.gradle.yapp.artifact.Dokka

import org.gradle.jvm.tasks.Jar
import se.svt.oss.gradle.yapp.artifact.Dokka.DokkaDoc
import se.svt.oss.gradle.yapp.publishingtarget.PublishingTargetType

open class DokkaGfm:
    DokkaDoc() {

    init {
        archiveClassifier.set("gfm-doc")
        val dokkaGfm = project.tasks.getByName("dokkaGfm")
        dependsOn(dokkaGfm)
        from(dokkaGfm)
    }
}