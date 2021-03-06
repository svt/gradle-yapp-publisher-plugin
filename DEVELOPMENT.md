# Development

1. Clone the project

```console
git clone GITURL 
```

2. Verify your setup works, run the tests,

```console
./gradlew clean check
```

## Installing this plugin to your local repo (so you can use it in test projects)

1. In the folder X, install the plugin to your local repository. We are using the official plugins for this as long as
   this plugin is not published (in the future, we can use this plugin for this also, but for now)

```console
./gradlew publishArtifactToLocalRepo
```

2. Verify that gradle plugins are searched for in your local repo (besides gradlePortal). Note: Gradle demands that the
   plugin management block is the first block in the settings file.

.settings.gradle.kts

```kotlin
pluginManagement {
    repositories {
    mavenLocal()
    gradlePluginPortal()
    }
}
```

3. Add the plugin to the plugins block in the labproject/testproject build file

.build.gradle.kts

by uncommenting the line

```kotlin

plugins {
    id("se.svt.oss.gradle-yapp-publisher") version "x.y.z"
}



```


4. Verify install

Refresh your gradle and you should see tasks yapp publisher. Verify that you can publish this plugin to local repo

```console
./gradlew clean publishArtifactToLocalRepo
```

## Use the plugin in a test project

Init a gradle project (java/kotlin) with gradle init (or use an existing).  
Add the plugin, and repo mavenLocal() to it's build.gradle.
Configure as in the README documentation, just copy the
basic gradle.properties maven_central or alike.




