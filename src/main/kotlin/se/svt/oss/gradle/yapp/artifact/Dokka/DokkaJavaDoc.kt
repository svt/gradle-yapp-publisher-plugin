package se.svt.oss.gradle.yapp.artifact.Dokka

import org.gradle.jvm.tasks.Jar
import se.svt.oss.gradle.yapp.artifact.Dokka.DokkaDoc
import se.svt.oss.gradle.yapp.publishingtarget.PublishingTargetType

open class DokkaJavaDoc:
    DokkaDoc() {

    init {

        archiveClassifier.set("javadoc")

        val dokkaJavaDoc = project.tasks.getByName("dokkaJavadoc")
        dependsOn(dokkaJavaDoc)
        from(dokkaJavaDoc)
    }
}
