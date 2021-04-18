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
       
       |pom.groupId=$group
       |pom.version=$version
       |pom.artifactId=$pluginName
       
       |pom.name=property name 
       |pom.description=property description
       |pom.url=http://propertyurl.se
       |pom.inceptionYear=1901

       |pom.licenseName=property licensename
       |pom.licenseUrl=property licenseurl
       |pom.licenseDistribution=property distribution
       |pom.licenseComment=property comments

       |pom.developerId=property developerid
       |pom.developerName=property developername
       |pom.developerEmail=property developeremail
       |pom.organization=property developerorganization
       |pom.organizationUrl=property developerorganizationurl

       |pom.scmUrl=property scmurl
       |pom.scmConnection=property scmconnection
       |pom.scmDeveloperConnection=property scmdeveloperconnection

       |signing.enabled=false
       |signing.signSnapshot=false
       |signing.keyId=propsk
       |signing.password=propsp
       |signing.key=propkf

       |pom.ossrhUser=propu
       |pom.ossrhPassword=proppw
    """.trimMargin()

    fun yappBuildGradleConf(
        group: String,
        version: String,
        signingKey: String = "",
        signingEnabled: Boolean = false,
        signSnapshot: Boolean = false
    ) = """
       
yappPublisher{

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
        licenseComment.set("plc")

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
        password.set("signing")
        key.set("$signingKey")
    }
}
    """.trimIndent()

    fun systemEnv(): Map<String, String> {
        return mapOf(
            Pair("POM_NAME", "pom.name"),
            Pair("POM_DESCRIPTION", "pom.description"),
            Pair("POM_URL", "pom.url"),
            Pair("POM_INCEPTIONYEAR", "pom.inceptionYear"),
            Pair("POM_LICENCENAME", "pom.licenseName"),
            Pair("POM_LICENCEURL", "pom.licenseUrl"),
            Pair("POM_LICENCEDISTRIBUTION", "pom.licenseDistribution"),
            Pair("POM_LICENCECOMMENTS", "pom.licenseComments"),
            Pair("POM_DEVELOPERID", "pom.developerId"),
            Pair("POM_DEVELOPERNAME", "pom.developerName"),
            Pair("POM_DEVELOPEREMAIL", "pom.developerEmail"),
            Pair("POM_ORGANIZATION", "pom.organization"),
            Pair("POM_ORGANIZATIONURL", "pom.organizationUrl"),
            Pair("POM_SCMURL", "pom.scmUrl"),
            Pair("POM_SCMCONNECTION", "pom.scmConnection"),
            Pair("POM_SCMDEVELOPERCONNECTION", "pom.scmDeveloperConnection"),
            Pair("POM_GROUPID", "se.env"),
            Pair("POM_VERSION", "0.0.4-SNAPSHOT"),
            Pair("POM_ARTIFACTID", "gradle-yapp-publisher-plugin"),
            Pair("SIGNING_ENABLED", "false"),
            Pair("SIGNING_SNAPSHOT", "false"),
            Pair("SIGNING_KEYID", "signing.keyId"),
            Pair("SIGNING_PASSWORD", "signing.password"),
            Pair("SIGNING_KEY", "signing.keyFile"),
            Pair("OSSRH_USER", "ossrhUser"),
            Pair("OSSRH_PASSWORD", "ossrhPassword")
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
