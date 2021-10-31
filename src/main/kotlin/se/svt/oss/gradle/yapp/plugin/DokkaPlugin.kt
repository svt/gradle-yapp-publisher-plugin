package se.svt.oss.gradle.yapp.plugin

import org.gradle.api.Project

class DokkaPlugin(project: Project) : BasePlugin(project) {
    fun configure() {
        if (project.plugins.hasPlugin("org.jetbrains.dokka")) {
            project.plugins.withId("org.jetbrains.kotlin.jvm") {
                project.plugins.apply("org.jetbrains.dokka")
            }
        }
    }
}
