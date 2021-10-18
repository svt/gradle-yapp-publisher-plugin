// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0
package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Action
import org.gradle.api.Project

open class YappPublisherExtension(project: Project) {

    val signing: SigningExtension = SigningExtension(project)
    val mavenPublishing: MavenPublishingExtension = MavenPublishingExtension(project)
    val gitLab: GitLabExtension = GitLabExtension(project)
    val gradlePortalPublishing: GradlePluginPublishingExtension = GradlePluginPublishingExtension(project)
    val publishTarget: PublishTargetExtension = PublishTargetExtension(project)

    fun mavenPublishing(action: Action<in MavenPublishingExtension>) = action.execute(mavenPublishing)
    fun gitLab(action: Action<in GitLabExtension>) = action.execute(gitLab)
    fun signing(action: Action<in SigningExtension>) = action.execute(signing)
    fun gradlePortalPublishing(action: Action<in GradlePluginPublishingExtension>) = action.execute(gradlePortalPublishing)
    fun publishTarget(action: Action<in PublishTargetExtension>) = action.execute(publishTarget)
}
