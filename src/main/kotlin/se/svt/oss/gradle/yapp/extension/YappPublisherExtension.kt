// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0
package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

open class YappPublisherExtension(
    project: Project,
    var signing: SigningExtension,
    var mavenPublishing: MavenPublishingExtension,
    var gitLab: GitLabExtension,
    var gitHub: GitHubExtension,
    var gradlePortalPublishing: GradlePluginPublishingExtension
) {

    // consumes `action` that for more flexible conf, see https://dzone.com/articles/the-complete-custom-gradle-plugin-building-tutoria

    fun mavenPublishing(action: Action<in MavenPublishingExtension>) = action.execute(mavenPublishing)
    fun gitLab(action: Action<in GitLabExtension>) = action.execute(gitLab)
    fun gitHub(action: Action<in GitHubExtension>) = action.execute(gitHub)
    fun signing(action: Action<in SigningExtension>) = action.execute(signing)
    fun gradlePortalPublishing(action: Action<in GradlePluginPublishingExtension>) =
        action.execute(gradlePortalPublishing)

    val envPrefix: String = "YAPP_"
    var propPrefix: String = "yapp."

    var withoutSource: Property<Boolean> = project.propBool("withoutSource", propPrefix, envPrefix)
    var withoutDoc: Property<Boolean> = project.propBool("withoutDoc", propPrefix, envPrefix)
    var dokkaPublishings: ListProperty<String> = project.propList("dokkaPublishings", propPrefix, envPrefix)
    var targets: ListProperty<String> = project.propList("targets", propPrefix, envPrefix)
}
