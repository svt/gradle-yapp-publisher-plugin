// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp.config

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.YappPublisherExtension
import java.net.URI

enum class PublishTarget {
    GRADLE_PORTAL, MAVEN_CENTRAL, MAVEN_CENTRAL_SNAPSHOT
}

open class PublishTargetType(
    open val project: Project
) {

    open var name: String = this.javaClass.simpleName
}

fun publishTarget(projectType: ProjectType, project: Project, extension: YappPublisherExtension): PublishTargetType =
    when (projectType) {
        is GradleKotlinPlugin -> GradlePortal(project, extension)
        is GradleJavaPlugin -> GradlePortal(project, extension)
        is JavaLibrary -> MavenCentral(project, extension)
        is JavaProject -> MavenCentral(project, extension)
        is KotlinLibrary -> MavenCentral(project, extension)
        else -> UnknownPublishTarget(project)
    }
internal class GradlePortal(override val project: Project, extension: YappPublisherExtension) :
    PublishTargetType(project) {

    init {
        project.configureGradlePublishingPlugin(extension)
    }
}

internal class MavenCentral(override val project: Project, extension: YappPublisherExtension) :
    PublishTargetType(project) {

    init {
        val isSnapShot = project.version.toString().endsWith("SNAPSHOT")

        val uri = if (isSnapShot) {
            name = "${javaClass.simpleName}-SNAPSHOT"
            URI("https://oss.sonatype.org/content/repositories/snapshots/")
        } else {
            URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
        }
        project.configureMavenPublishingPlugin(uri, extension)
        project.configureSigningPlugin(extension)
        project.configureJavaLibraryArtifact()
    }
}

internal class UnknownPublishTarget(override val project: Project) :
    PublishTargetType(project) {

    init {
        project.logger.error(
            "Yapp-Publisher-Plugin could not identify a PublishTarget (As you added this plugin," +
                "it is assumed that you wanted to publish your project), see the docs"
        )
    }
}
