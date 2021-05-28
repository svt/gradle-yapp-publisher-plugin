// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0
package se.svt.oss.gradle.yapp

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.VersionNumber
import org.slf4j.LoggerFactory

class YappPublisher : Plugin<Project> {
    companion object {
        // Why not Kotlinglogging etc - see https://discuss.gradle.org/t/logging-in-gradle-plugin/31685/2
        val logger = LoggerFactory.getLogger("GradleYappPublisherPlugin")
        val MIN_GRADLE = VersionNumber.parse("6.8.0")
    }

    override fun apply(project: Project) {
        isMinSupportedGradleVersion(project)

        val extension = project.extensions.create("yapp", YappPublisherExtension::class.java, project)
        PublishTargetConfiguration(project, extension)
    }

    private fun isMinSupportedGradleVersion(project: Project) {
        if (VersionNumber.parse(project.gradle.gradleVersion) < MIN_GRADLE) {
            error("This plugin is tested with $MIN_GRADLE and higher")
        }
    }
}

/*    private fun log(message: String, filterOptions: GradleRepositoryPublisherExtension) {
        if (filterOptions.log.get()) {
            log.quiet(message)
        }
    }
*/
