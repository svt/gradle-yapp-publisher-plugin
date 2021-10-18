package se.svt.oss.gradle.yapp.plugin

import org.gradle.api.Project

fun Project.configureDokka() {
    if (project.plugins.hasPlugin("org.jetbrains.dokka")) {
        project.plugins.withId("org.jetbrains.kotlin.jvm") {
            project.plugins.apply("org.jetbrains.dokka")
        }
    }
}
