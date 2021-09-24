// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp.config.artifact

import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.jvm.tasks.Jar

open class Sources : Jar() {
    init {
        archiveClassifier.set("sources")

        val javaPlugin = project.convention.getPlugin(JavaPluginConvention::class.java)
        from(javaPlugin.sourceSets.getByName("main").allSource)
    }
}
