package se.svt.oss.gradle.yapp.plugin

import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.credentials.HttpHeaderCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.authentication.http.HttpHeaderAuthentication
import se.svt.oss.gradle.yapp.extension.YappPublisherExtension
import se.svt.oss.gradle.yapp.publishtarget.PublishTargetType
import se.svt.oss.gradle.yapp.publishtarget.RepositoryConfiguration

class MavenPublishing() {
    fun configure(
        repositoryConf: RepositoryConfiguration,
        publishTarget: PublishTargetType,
        project: Project
    ) {

        val extensions = project.extensions
        val ext = extensions.getByType(YappPublisherExtension::class.java)
        println(ext.mavenPublishing.name)
        project.plugins.apply(MavenPublishPlugin::class.java)
        project.extensions.configure(PublishingExtension::class.java) { p ->

            p.publications { publications ->
                publications.register("pluginMaven", MavenPublication::class.java) { publication ->
                    project.afterEvaluate { // These values does not seem to use the newer api, i.e Lazy properties,
                        // so we cant get rid of the afterEvalute

                        publication.groupId = ext.mavenPublishing.groupId.get().ifEmpty {
                            project.group.toString()
                        }
                        publication.artifactId = ext.mavenPublishing.artifactId.get().ifEmpty {
                            project.name.toString()
                        }
                        publication.version = ext.mavenPublishing.version.get().ifEmpty {
                            project.version.toString()
                        }

                        with(publication) {

                            pom.apply {
                                name.set(ext.mavenPublishing.name)
                                description.set(ext.mavenPublishing.description)
                                inceptionYear.set(ext.mavenPublishing.inceptionYear)
                                url.set(ext.mavenPublishing.url)
                                packaging

                                licenses { l ->
                                    l.license { license ->
                                        license.name.set(ext.mavenPublishing.licenseName)
                                        license.url.set(ext.mavenPublishing.licenseUrl)
                                        license.comments.set(ext.mavenPublishing.licenseComments)
                                        license.distribution.set(ext.mavenPublishing.licenseDistribution)
                                    }
                                }

                                println(ext.mavenPublishing.licenseComments)
                                developers { d ->
                                    d.developer { dev ->
                                        dev.apply {
                                            id.set(ext.mavenPublishing.developerId)
                                            name.set(ext.mavenPublishing.developerName)
                                            organization.set(ext.mavenPublishing.organization)
                                            organizationUrl.set(ext.mavenPublishing.organizationUrl)
                                            email.set(ext.mavenPublishing.developerEmail)
                                        }
                                    }
                                }

                                scm { scm ->
                                    scm.url.set(ext.mavenPublishing.scmUrl)
                                    scm.connection.set(ext.mavenPublishing.scmConnection)
                                    scm.developerConnection.set(ext.mavenPublishing.scmDeveloperConnection)
                                }
                            }
                        }
                    }
                }

                p.repositories { repository ->

                    repository.maven { r ->
                        r.apply {
                            name = repositoryConf.name
                            url = repositoryConf.uri

                            if (publishTarget == PublishTargetType.GITLAB) {
                                credentials(HttpHeaderCredentials::class.java) {
                                    it.name = repositoryConf.credential.name
                                    it.value = repositoryConf.credential.value
                                }
                                authentication {

                                    it.create("header", HttpHeaderAuthentication::class.java)
                                }
                            } else {
                                credentials(PasswordCredentials::class.java) {
                                    credentials.username = repositoryConf.credential.name
                                    credentials.password = repositoryConf.credential.value
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
