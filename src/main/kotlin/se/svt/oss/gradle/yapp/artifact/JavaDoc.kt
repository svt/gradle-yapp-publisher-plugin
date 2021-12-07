// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp.artifact

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import se.svt.oss.gradle.yapp.extension.YappPublisherExtension

class JavaDoc(val project: Project) {

    fun yappExtension(): YappPublisherExtension = project.extensions.getByType(YappPublisherExtension::class.java)

    init {
        project.extensions.configure(JavaPluginExtension::class.java) { java ->

            if (!(yappExtension().withoutDoc.get())) java.withJavadocJar()
        }
    }
    /*  init {
          archiveClassifier.set("javadoc")

          val javadocTask = project.tasks.getByName("javadoc") as Javadoc
          dependsOn(javadocTask)
          from(javadocTask)
      }*/
}
