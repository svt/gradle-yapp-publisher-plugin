package se.svt.oss.gradle.yapp.publishtarget

import GradlePluginPortal
import org.gradle.api.Project

enum class PublishingTargetType {

    GRADLE_PORTAL {
        override fun publishTarget(project: Project): BasePublishTarget =
            GradlePluginPortal(project, this)
    },
    MAVEN_CENTRAL {
        override fun publishTarget(project: Project): BasePublishTarget =
            MavenCentralRepository(project, this)
    },
    MAVEN_CENTRAL_SNAPSHOT {
        override fun publishTarget(project: Project): BasePublishTarget =
            MavenCentralRepository(project, this)
    },
    GITLAB {
        override fun publishTarget(project: Project): BasePublishTarget =
            GitLabRepository(project, this)
    },
    GITHUB {
        override fun publishTarget(project: Project): BasePublishTarget =
            GitHubPackagesRepository(project, this)
    },
    UNKNOWN {
        override fun publishTarget(project: Project): BasePublishTarget =
            GitHubPackagesRepository(project, this)
    };

    abstract fun publishTarget(project: Project): BasePublishTarget
}
