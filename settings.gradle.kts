pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "gradle-yapp-publisher-plugin"
include("gradle-yapp-publisher")

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}
