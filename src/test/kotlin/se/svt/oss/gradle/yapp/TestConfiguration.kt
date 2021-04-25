// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0
package se.svt.oss.gradle.yapp

object TestConfiguration {

    private const val pluginName = "gradle-yapp-publisher-plugin"

    fun buildGradle(group: String, version: String) =
        """
    plugins {
        id("java-gradle-plugin")
        kotlin("jvm") version "1.4.32"
        id("se.svt.oss.gradle-yapp-publisher-plugin") version "1.0.0-SNAPSHOT"
        id("com.gradle.plugin-publish") version "0.14.0"
    }

    group = "$group"
    version = "$version"

    repositories {
        mavenCentral()
    gradlePluginPortal()
    
    }

    gradlePlugin {
        plugins {
            create("yappPlugin") {
                id = "se.svt.oss.gradle-yapp-publisher-plugin"
                implementationClass = "se.svt.oss.gradle.yapp.YappPublisher"
            }
        }
    }
    
    
dependencies {
    api("com.gradle.publish:plugin-publish-plugin:0.14.0")
    
    }
    """

    fun yappPropertiesConf(group: String, version: String) = """
       
       |yapp.pom.groupId=$group
       |yapp.pom.version=$version
       |yapp.pom.artifactId=$pluginName
       
       |yapp.pom.name=property name 
       |yapp.pom.description=property description
       |yapp.pom.url=http://propertyurl.se
       |yapp.pom.inceptionYear=1901

       |yapp.pom.licenseName=property licensename
       |yapp.pom.licenseUrl=property licenseurl
       |yapp.pom.licenseDistribution=property distribution
       |yapp.pom.licenseComments=property comments

       |yapp.pom.developerId=property developerid
       |yapp.pom.developerName=property developername
       |yapp.pom.developerEmail=property developeremail
       |yapp.pom.organization=property developerorganization
       |yapp.pom.organizationUrl=property developerorganizationurl

       |yapp.pom.scmUrl=property scmurl
       |yapp.pom.scmConnection=property scmconnection
       |yapp.pom.scmDeveloperConnection=property scmdeveloperconnection

       |yapp.signing.enabled=false
       |yapp.signing.signSnapshot=false
       |yapp.signing.keyId=propsk
       |yapp.signing.password=propsp
       |yapp.signing.key=propkf
       
       |yapp.ossrhUser=propu
       |yapp.ossrhPassword=proppw
    """.trimMargin()

    fun yappBuildGradleConf(
        group: String,
        version: String,
        signingKey: String = "",
        signingEnabled: Boolean = false,
        signSnapshot: Boolean = false
    ) = """
       
yapp {

    pom {
        groupId.set("$group")
        version.set("$version")

        name.set("pn")
        description.set("pd")
        url.set("http://p.se")
        inceptionYear.set("1999")

        licenseName.set("pln")
        licenseUrl.set("plu")
        licenseDistribution.set("pld")
        licenseComments.set("plc")

        developerId.set("pdi")
        developerName.set("pdn")
        developerEmail.set("pde")

        organization.set("pdo")
        organizationUrl.set("pou")

        scmUrl.set("psu")
        scmConnection.set("psc")
        scmDeveloperConnection.set("psd")
    }
    
    signing { 
        enabled.set($signingEnabled)
        signSnapshot.set($signSnapshot)
        keyId.set("3DC10F04")
        keySecret.set("signing")
        key.set("$signingKey")
    }
}
    """.trimIndent()

    fun systemEnv(): Map<String, String> {
        val envPrefix = "YAPP_POM_"
        return mapOf(
            Pair("${envPrefix}NAME", "yapp.pom.name"),
            Pair("${envPrefix}DESCRIPTION", "yapp.pom.description"),
            Pair("${envPrefix}URL", "yapp.pom.url"),
            Pair("${envPrefix}INCEPTIONYEAR", "yapp.pom.inceptionYear"),
            Pair("${envPrefix}LICENSENAME", "yapp.pom.licenseName"),
            Pair("${envPrefix}LICENSEURL", "yapp.pom.licenseUrl"),
            Pair("${envPrefix}LICENSEDISTRIBUTION", "yapp.pom.licenseDistribution"),
            Pair("${envPrefix}LICENSECOMMENTS", "yapp.pom.licenseComments"),
            Pair("${envPrefix}DEVELOPERID", "yapp.pom.developerId"),
            Pair("${envPrefix}DEVELOPERNAME", "yapp.pom.developerName"),
            Pair("${envPrefix}DEVELOPEREMAIL", "yapp.pom.developerEmail"),
            Pair("${envPrefix}ORGANIZATION", "yapp.pom.organization"),
            Pair("${envPrefix}ORGANIZATIONURL", "yapp.pom.organizationUrl"),
            Pair("${envPrefix}SCMURL", "yapp.pom.scmUrl"),
            Pair("${envPrefix}SCMCONNECTION", "yapp.pom.scmConnection"),
            Pair("${envPrefix}SCMDEVELOPERCONNECTION", "yapp.pom.scmDeveloperConnection"),
            Pair("${envPrefix}GROUPID", "se.env"),
            Pair("${envPrefix}VERSION", "0.0.4-SNAPSHOT"),
            Pair("${envPrefix}ARTIFACTID", "gradle-yapp-publisher-plugin"),
            Pair("YAPP_SIGNING_ENABLED", "false"),
            Pair("YAPP_SIGNING_SIGNSNAPSHOT", "false"),
            Pair("YAPP_SIGNING_KEYID", "yapp.signing.keyId"),
            Pair("YAPP_SIGNING_KEYSECRET", "yapp.signing.keySecret"),
            Pair("YAPP_SIGNING_KEY", "yapp.signing.keyFile"),
            Pair("YAPP_OSSRHUSER", "yapp.ossrhUser"),
            Pair("YAPP_OSSRHPASSWORD", "yapp.ossrhPassword")
        )
    }

    fun yappBasePlugin(group: String = "se.svt.oss.gradle", version: String = "1.0.0-SNAPSHOT") = """
    
plugins {
    `java-gradle-plugin`
    `maven-publish`
    signing
    kotlin("jvm") version "1.4.32"
    id("com.gradle.plugin-publish") version "0.14.0"
    id("org.jetbrains.dokka") version "1.4.30"
}

group = "$group"
version = "$version"

repositories {
    mavenCentral()
    maven(url="https://dl.bintray.com/kotlin/dokka")
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    api("com.gradle.publish:plugin-publish-plugin:0.14.0")

    testImplementation("commons-io:commons-io:2.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.1")
}


tasks {
    "test"(Test::class) {
        useJUnitPlatform()
    }
}

pluginBundle {
    website = "websiteUrl"
    vcsUrl = "websiteUrli"
    tags = listOf("maven central","gradle portal")
}

gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = "se.svt.oss.gradle-yapp-publisher-plugin"
            displayName = "fullName"
            description = "projectDetails"
            implementationClass = "se.svt.oss.gradle.yapp.YappPublisher"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            artifactId = "gradle-yapp-publisher-plugin"

            pom {
                name.set("Gradle Yap Publisher Plugin")
                description.set("default description")
                url.set("http://www.example.com/library")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("devid")
                        name.set("devname")
                        email.set("dev@dev.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://dev.com/my-library.git")
                    developerConnection.set("scm:git:ssh://dev.com/my-library.git")
                    url.set("http://dev.com/my-library/")
                }
            }
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "6.8.3"
}
    """.trimIndent()
}
