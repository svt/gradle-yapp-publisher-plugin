package se.svt.oss.gradle.yapp.artifact.Dokka

import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import se.svt.oss.gradle.yapp.publishingtarget.PublishingTargetType
import kotlin.reflect.KClass

abstract class DokkaDoc: Jar()
