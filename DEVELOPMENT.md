# Development

1. Checkout the project

```
$ git clone GITURL 
```

2. In the folder X, install the plugin to your local repository

```
./gradlew publishToMavenLocal
``` 

3. Setup your test project to look in your local repository for gradle plugins (besides gradlePortal). Note: Gradle
   demands that the plugin management block is the first block in the settings file.

.settings.gradle.kts

```kotlin
pluginManagement {
    repositories {
    mavenLocal()
    gradlePluginPortal()
    // maven(url="https://dl.bintray.com/kotlin/dokka")
    }
}
rootProject.name = "your project name"
```

4. Add the plugin to the plugins block in your build filed

.build.gradle.kts

```kotlin

plugins {
    id("se.svt.oss.gradle-yapp-publisher-plugin") version "1.0.0-SNAPSHOT"
}

```

4. Configure as in the README documentation
