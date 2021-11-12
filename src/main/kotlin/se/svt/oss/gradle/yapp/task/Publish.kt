package se.svt.oss.gradle.yapp.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import se.svt.oss.gradle.yapp.config.ProjectType
import se.svt.oss.gradle.yapp.publishtarget.BasePublishTarget
import se.svt.oss.gradle.yapp.publishtarget.PublishingTargetType.GITHUB
import se.svt.oss.gradle.yapp.publishtarget.PublishingTargetType.GITLAB
import se.svt.oss.gradle.yapp.publishtarget.PublishingTargetType.GRADLE_PORTAL
import se.svt.oss.gradle.yapp.publishtarget.PublishingTargetType.MAVEN_CENTRAL
import se.svt.oss.gradle.yapp.publishtarget.PublishingTargetType.MAVEN_CENTRAL_SNAPSHOT
import se.svt.oss.gradle.yapp.publishtarget.PublishingTargetType.UNKNOWN
import javax.inject.Inject

abstract class Publish @Inject constructor(
    private val projectType: ProjectType,
    private val publishTarget: List<BasePublishTarget>
) : DefaultTask() {
    init {
        group = "yapp publisher"
        description =
            "Publish ${project.name} as a ${projectType.javaClass.simpleName} to ${publishTarget.forEach { it.name() }}"

        publishTarget.forEach {
            when (it.publishingTargetType) {
                GRADLE_PORTAL -> {
                    dependsOn(project.tasks.getByName("publishPlugins"))
                }
                MAVEN_CENTRAL -> {
                    if (it.yappExtension().mavenPublishing.directReleaseToMavenCentral.get()) {
                        dependsOn(project.tasks.getByName("publishToSonatype"))
                        dependsOn(project.tasks.getByName("closeAndReleaseSonatypeStagingRepository"))
                    } else {
                        dependsOn(project.tasks.getByName("publish${MAVEN_CENTRAL}PublicationTo${it.name()}"))
                    }
                }
                MAVEN_CENTRAL_SNAPSHOT -> {
                    dependsOn(project.tasks.getByName("publish${MAVEN_CENTRAL_SNAPSHOT}PublicationTo${it.name()}"))
                }
                GITHUB -> {
                    dependsOn(project.tasks.getByName("publish${GITHUB}PublicationTo${it.name()}"))
                }
                GITLAB -> {
                    dependsOn(project.tasks.getByName("publish${GITLAB}PublicationTo${it.name()}"))
                }
                UNKNOWN -> {}
            }
        }
        doLast {

            println("Published ${project.name} as a ${projectType.javaClass.simpleName} to ${publishTarget.forEach { it.name() }}")
        }
    }

    @TaskAction
    fun publishToPublishTarget() {
    }
}
