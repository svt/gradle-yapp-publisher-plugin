import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    `java-gradle-plugin`
    signing
    idea
    kotlin("jvm") version "1.4.31"
    id("org.jetbrains.dokka") version "1.5.0"
    id("se.ascp.gradle.gradle-versions-filter") version "0.1.10"
    id("org.jmailen.kotlinter") version "3.4.5"
    id("org.owasp.dependencycheck") version "6.2.2"
    id("com.gradle.plugin-publish") version "0.15.0"
    id("pl.allegro.tech.build.axion-release") version "1.13.3"
    id("se.svt.oss.gradle-yapp-publisher-plugin") version "0.1.13"
}

group = "se.svt.oss"
version = scmVersion.version
description = "Yet another plugin that manages publishing for Gradle projects"

repositories {
    //mavenLocal()
    gradlePluginPortal()
    mavenCentral()
//    maven(url="https://dl.bintray.com/kotlin/dokka")
}

dependencies {
    api("com.gradle.publish:plugin-publish-plugin:0.15.0")

    testImplementation("commons-io:commons-io:2.8.0")
    testImplementation("org.xmlunit:xmlunit-core:2.8.2")
    testImplementation("org.xmlunit:xmlunit-matchers:2.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("uk.org.webcompere:system-stubs-jupiter:1.2.0")

}

tasks {
    test {
        useJUnitPlatform()
    }
}


/*pluginBundle {
    website = "https://github.com/svt/gradle-yapp-publisher-plugin"
    vcsUrl = "https://github.com/svt/gradle-yapp-publisher-plugin.git"
    tags = listOf("maven central", "gradle portal", "publish")
}
gradlePlugin {
    plugins {
        create("yappPlugin") {
            id = "${project.group}.${project.name}"
            displayName = "Gradle Yapp Publisher Plugin"
            implementationClass = "se.svt.oss.gradle.yapp.YappPublisher"
            description = project.description
        }
    }
}
*/
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_1_8

    withSourcesJar()
    withJavadocJar()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.1.1"
}
