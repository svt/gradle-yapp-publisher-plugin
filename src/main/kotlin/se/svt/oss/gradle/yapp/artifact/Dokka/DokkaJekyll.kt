package se.svt.oss.gradle.yapp.artifact.Dokka

import se.svt.oss.gradle.yapp.artifact.Dokka.DokkaDoc
import se.svt.oss.gradle.yapp.publishingtarget.PublishingTargetType

open class DokkaJekyll:
    DokkaDoc() {

    init {
        archiveClassifier.set("jekyll-doc")
        val dokkaJekyll = project.tasks.getByName("dokkaJekyll")
        dependsOn(dokkaJekyll)
        from(dokkaJekyll)
    }
}
