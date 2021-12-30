// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp.publishingtarget

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.extension.YappPublisherExtension
import se.svt.oss.gradle.yapp.isSnapShot

abstract class BasePublishTarget(
    protected val project: Project,
    val publishingTargetType: PublishingTargetType
) {

    val yappExtension: YappPublisherExtension =
        project.extensions.getByType(YappPublisherExtension::class.java)

    abstract fun configure()

    fun name(): String {
        return if (project.isSnapShot()) {
            "${this.javaClass.simpleName}-SNAPSHOT"
        } else {
            this.javaClass.simpleName
        }
    }
}
