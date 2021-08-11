// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp

import com.gradle.publish.PluginBundleExtension
import com.gradle.publish.PublishPlugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.plugin.devel.plugins.JavaGradlePluginPlugin
import org.gradle.plugin.use.resolve.internal.ArtifactRepositoriesPluginResolver
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin
import se.svt.oss.gradle.yapp.artifact.JavaDoc
import se.svt.oss.gradle.yapp.artifact.Sources
import java.io.File
import java.net.URI

fun Project.configureGradlePublishingPlugin(ext: YappPublisherExtension) {
    plugins.apply(MavenPublishPlugin::class.java)
    plugins.apply(JavaGradlePluginPlugin::class.java)
    plugins.apply(PublishPlugin::class.java)

    afterEvaluate {
        extensions.configure(GradlePluginDevelopmentExtension::class.java) {
            it.plugins.create("${project.name}-plugin") { pd ->
                with(pd) {
                    description = ext.gradlepluginE.description.get() ?: project.description
                    displayName = ext.gradlepluginE.displayName.get()
                    id = ext.gradlepluginE.id.get() ?: "${project.group}.${project.name}"
                    implementationClass = ext.gradlepluginE.implementationClass.get()
                }
            }

            project.extensions.extraProperties.set(
                "gradle.publish.key",
                ext.gradlepluginE.key.get()
            )
            project.extensions.extraProperties.set(
                "gradle.publish.secret",
                ext.gradlepluginE.keySecret.get()
            )
        }

        project.extensions.configure(PublishingExtension::class.java) { p ->
            for (declaration in project.extensions.getByType(GradlePluginDevelopmentExtension::class.java).plugins) {
                val coordinates: MavenPublication = p.publications.getByName("pluginMaven") as MavenPublication
                val pluginId = declaration.id
                val pluginGroupId = coordinates.groupId
                val pluginArtifactId = coordinates.artifactId
                val pluginVersion = coordinates.version
                val publication = p.publications.create(
                    declaration.name + "PluginMarkerMaven",
                    MavenPublication::class.java
                ) as MavenPublicationInternal
                publication.isAlias = true
                publication.artifactId = pluginId + ArtifactRepositoriesPluginResolver.PLUGIN_MARKER_SUFFIX
                publication.groupId = pluginId
                publication.pom.withXml { xmlProvider ->
                    val root = xmlProvider.asElement()
                    val document = root.ownerDocument
                    val dependencies = root.appendChild(document.createElement("dependencies"))
                    val dependency = dependencies.appendChild(document.createElement("dependency"))
                    val groupId = dependency.appendChild(document.createElement("groupId"))
                    groupId.textContent = pluginGroupId
                    val artifactId = dependency.appendChild(document.createElement("artifactId"))
                    artifactId.textContent = pluginArtifactId
                    val version = dependency.appendChild(document.createElement("version"))
                    version.textContent = pluginVersion
                }
                publication.pom.name.set(declaration.displayName)
                publication.pom.description.set(declaration.description)
            }
        }
    }
    project.extensions.configure(PluginBundleExtension::class.java) {
        with(it) {
            website = ext.gradlepluginE.webSite.get()
            vcsUrl = ext.gradlepluginE.vcsUrl.get()
            tags = ext.gradlepluginE.tags.get()
        }
    }
}

fun Project.configureMavenPublishingPlugin(
    ext: YappPublisherExtension
) {

    project.plugins.apply(MavenPublishPlugin::class.java)
    project.extensions.configure(PublishingExtension::class.java) { p ->

        p.publications { publications ->
            publications.register("pluginMaven", MavenPublication::class.java) { publication ->
                project.afterEvaluate { // These values does not seem to use the newer api, i.e Lazy properties,
                    // so we cant get rid of the afterEvalute

                    publication.groupId = ext.pomE.groupId.get().ifEmpty {
                        project.group.toString()
                    }
                    publication.artifactId = ext.pomE.artifactId.get().ifEmpty {
                        project.name.toString()
                    }
                    publication.version = ext.pomE.version.get().ifEmpty {
                        project.version.toString()
                    }

                    with(publication) {

                        pom.apply {
                            name.set(ext.pomE.name)
                            description.set(ext.pomE.description)
                            inceptionYear.set(ext.pomE.inceptionYear)
                            url.set(ext.pomE.url)

                            licenses { l ->
                                l.license { license ->
                                    license.name.set(ext.pomE.licenseName)
                                    license.url.set(ext.pomE.licenseUrl)
                                    license.comments.set(ext.pomE.licenseComments)
                                    license.distribution.set(ext.pomE.licenseDistribution)
                                }
                            }

                            developers { d ->
                                d.developer { dev ->
                                    dev.apply {
                                        id.set(ext.pomE.developerId)
                                        name.set(ext.pomE.developerName)
                                        organization.set(ext.pomE.organization)
                                        organizationUrl.set(ext.pomE.organizationUrl)
                                        email.set(ext.pomE.developerEmail)
                                    }
                                }
                            }

                            scm { scm ->
                                scm.url.set(ext.pomE.scmUrl)
                                scm.connection.set(ext.pomE.scmConnection)
                                scm.developerConnection.set(ext.pomE.scmDeveloperConnection)
                            }
                        }
                    }
                }
            }

            p.repositories { repository ->
                repository.maven { r ->
                    r.apply {
                        name = "MavenCentral"
                        url =
                            if (ext.pomE.version.get().endsWith("SNAPSHOT")) {
                                URI("https://oss.sonatype.org/content/repositories/snapshots/")
                            } else {
                                URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                            }

                        credentials { credentials ->
                            credentials.username = ext.pomE.ossrhUser.get()
                            credentials.password = ext.pomE.ossrhPassword.get()
                        }
                    }
                }
            }
        }
    }
}

fun Project.configureSigningPlugin(extension: YappPublisherExtension) {

    project.plugins.apply(SigningPlugin::class.java)

    project.afterEvaluate { // (the signing plugin old api with conventions, non lazy properties etc so have to do this)

        val isReleaseVersion = !project.isSnapShot()

        project.extensions.configure(SigningExtension::class.java) {

            if (extension.sign.enabled.get() && (isReleaseVersion || extension.sign.signSnapshot.get())) {

                if (isValidFilePath(extension.sign.key.get())) {

                    project.extensions.extraProperties.set("signing.keyId", extension.sign.keyId.get())
                    project.extensions.extraProperties.set(
                        "signing.password",
                        extension.sign.keySecret.get()
                    )
                    project.extensions.extraProperties.set(
                        "signing.secretKeyRingFile",
                        extension.sign.key.get()
                    )
                    it.sign(project.extensions.findByType(PublishingExtension::class.java)?.publications)
                } else {
                    it.useInMemoryPgpKeys(
                        extension.sign.keyId.get(),
                        extension.sign.key.get(),
                        extension.sign.keySecret.get()
                    )
                    it.sign(project.extensions.findByType(PublishingExtension::class.java)?.publications)
                }
            }
        }
    }
}

fun Project.isValidFilePath(path: String): Boolean = try {
    File(path).canonicalPath
    true
} catch (e: Exception) {
    false
}

fun Project.isSnapShot(): Boolean = project.version.toString().contains("SNAPSHOT")

fun Project.configureDokka() {
    if (project.plugins.hasPlugin("org.jetbrains.dokka")) {
        project.plugins.withId("org.jetbrains.kotlin.jvm") {
            project.plugins.apply("org.jetbrains.dokka")
        }
    }
}

fun Project.configureJavaLibraryArtifact() {

    val javaDoc = "javadocJar"
    val sources = "sourcesJar"

    project.extensions.configure(PublishingExtension::class.java) { pe ->
        pe.publications { publications ->
            val p = publications.getByName("pluginMaven") as MavenPublication
            p.from(project.components.getByName("java"))

            val sourcesJar = project.tasks.create(sources, Sources::class.java)
            p.artifact(sourcesJar)

            val javadocsJar = project.tasks.create(javaDoc, JavaDoc::class.java)
            p.artifact(javadocsJar)

            /*project.afterEvaluate {
                project.extensions.configure(JavaPluginExtension::class.java) { java ->
                    java.withJavadocJar()
                    java.withSourcesJar()

                }
            }*/
        }
    }
}
