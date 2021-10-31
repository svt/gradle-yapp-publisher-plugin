// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0
package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Action
import org.gradle.api.Project

open class YappPublisherExtension(
    project: Project,
    var signing: SigningExtension,
    var mavenPublishing: MavenPublishingExtension,
    var gitLab: GitLabExtension,
    var gradlePortalPublishing: GradlePluginPublishingExtension,
    var publishTarget: PublishTargetExtension
) {

    // consumes `action` that for more flexible conf, see https://dzone.com/articles/the-complete-custom-gradle-plugin-building-tutoria

    fun mavenPublishing(action: Action<in MavenPublishingExtension>) = action.execute(mavenPublishing)
    fun gitLab(action: Action<in GitLabExtension>) = action.execute(gitLab)
    fun signing(action: Action<in SigningExtension>) = action.execute(signing)
    fun gradlePortalPublishing(action: Action<in GradlePluginPublishingExtension>) =
        action.execute(gradlePortalPublishing)

    fun publishTarget(action: Action<in PublishTargetExtension>) = action.execute(publishTarget)
}
