package se.svt.oss.gradle.yapp

import org.gradle.api.Project

fun Project.isSnapShot(): Boolean = project.version.toString().contains("SNAPSHOT")
