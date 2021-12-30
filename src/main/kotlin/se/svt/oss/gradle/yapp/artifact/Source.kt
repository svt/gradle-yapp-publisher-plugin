// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp.artifact

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import se.svt.oss.gradle.yapp.extension.YappPublisherExtension

class Source(val project: Project) {
    fun yappExtension(): YappPublisherExtension = project.extensions.getByType(YappPublisherExtension::class.java)

    init {
        project.extensions.configure(JavaPluginExtension::class.java) { java ->

            if (!(yappExtension().withoutSource.get())) java.withSourcesJar()
        }
    }

   /* init {
        archiveClassifier.set("sources")

        val javaPlugin = project.extensions.getByType(JavaPluginExtension::class.java)
        from(javaPlugin.sourceSets.getByName("main").allSource)
    }*/
}
