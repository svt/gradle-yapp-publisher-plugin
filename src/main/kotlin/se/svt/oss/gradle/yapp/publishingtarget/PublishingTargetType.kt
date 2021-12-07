package se.svt.oss.gradle.yapp.publishingtarget

import GradlePluginPortal
import org.gradle.api.Project
import se.svt.oss.gradle.yapp.config.ProjectType

enum class PublishingTargetType {

    GRADLE_PORTAL {
        override fun publishTarget(project: Project, projectType: ProjectType): BasePublishTarget =
            GradlePluginPortal(project, this, projectType)
    },
    MAVEN_CENTRAL {
        override fun publishTarget(project: Project, projectType: ProjectType): BasePublishTarget =
            MavenCentralRepository(project, this, projectType)
    },
    GITLAB {
        override fun publishTarget(project: Project, projectType: ProjectType): BasePublishTarget =
            GitLabRepository(project, this, projectType)
    },
    GITHUB {
        override fun publishTarget(project: Project, projectType: ProjectType): BasePublishTarget =
            GitHubPackagesRepository(project, this, projectType)
    },
    UNKNOWN {
        override fun publishTarget(project: Project, projectType: ProjectType): BasePublishTarget =
            GitHubPackagesRepository(project, this, projectType)
    };

    abstract fun publishTarget(project: Project, projectType: ProjectType): BasePublishTarget
}
