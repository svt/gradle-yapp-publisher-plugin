package se.svt.oss.gradle.yapp.publishtarget

import GradlePluginPortal
import org.gradle.api.Project

enum class PublishTargetType {

    GRADLE_PORTAL {
        override fun publishTarget(project: Project): BasePublishTarget? =
            GradlePluginPortal(project, this)
    },
    MAVEN_CENTRAL {
        override fun publishTarget(project: Project): BasePublishTarget? =
            MavenCentral(project, this)
    },
    MAVEN_CENTRAL_SNAPSHOT {
        override fun publishTarget(project: Project): BasePublishTarget? =
            MavenCentral(project, this)
    },
    GITLAB {
        override fun publishTarget(project: Project): BasePublishTarget? =
            GitLabPackageRegistry(project, this)
    },
    NA {
        override fun publishTarget(project: Project): BasePublishTarget? =
            null
    };

    abstract fun publishTarget(project: Project): BasePublishTarget?
}
