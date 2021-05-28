// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp.artifact

import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar

open class JavaDoc : Jar() {
    init {
        archiveClassifier.set("javadoc")

        val javadocTask = project.tasks.getByName("javadoc") as Javadoc
        dependsOn(javadocTask)
        from(javadocTask)
    }
}
