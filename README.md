![REUSE Compliance](https://img.shields.io/reuse/compliance/github.com/svt/gradle-yapp-publisher-plugin)

# Gradle Yapp Publisher Plugin - or Yet Another Publisher Plugin 

## What is it?

A Gradle plugin for publishing Java and Kotlin releases/snapshots to Maven Central, Gradle Portal, GitLab Repositorys.

## Why does it exist?

To make life easier when configuring the plugins needed for these tasks.
To offer a simple clean union interface for these tasks.

## Features

* Maven Central Publishing
* Gradle Portal Publishing
* GitLab Publishing
* Signing
* Build file, Properties or System Environment Configuration 
* Semi smart configuration (called semi smart because it is not really that eally :)

## How does it work?

It abstracts the following plugins:

* [Maven Publish](https://docs.gradle.org/current/userguide/publishing_maven.html)
* [Java Gradle Plugin Development Plugin](https://docs.gradle.org/current/userguide/java_gradle_plugin.html#java_gradle_plugin)
* [Gradle Portal Publishing Plugin](https://plugins.gradle.org/docs/publish-plugin)
* [Gradle Signing Plugin](https://docs.gradle.org/current/userguide/publishing_signing.html)

## How do I use it?

TODO - step by step guide. with pics

### The plugin needs:

* A project type (for example, a java-library)
* A publish target (for example, maven-central)


If you not give it a publish target one will be choosen from your set of plugins.
It tries to identify the project target depending on your configuration.

You can also configure the type and target (only the target yet)

### Quickstart

1. Add the plugin to your plugins block:

```kotlin
plugins {
    id("se.svt.oss.gradle-yapp-publisher-plugin") version "0.1.17"
        ...
}
```

2. Add basic plugins

If you are publishing to Maven Central (Java/Kotlin Library)

```kotlin
plugins {
    `maven-publish`
        ...
}            
```

If you are publishing to Gradle Portal (Gradle Plugin)

```kotlin
plugins {
    `java-gradle-plugin`
        ...
}
```
    
3. Configure the plugin. Example - Pom with signing

```kotlin
yapp {

    mavenPublishing {
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
        enabled.set(true)
        signSnapshot.set(true)
        keyId.set("11111111")
        password.set("signing")
        key.set("signingKey")
    }
}
```

Or even simpler, configure it in a properties location like gradle.properties

```
yapp.mavenPublishing.name = exampleproject
yapp.mavenPublishing.description = an pom example description
yapp.mavenPublishing.url = https://github.com/myexamplaproject
yapp.mavenPublishing.inceptionYear = 2021

yapp.mavenPublishing.scmConnection = scm:git:github.com/example.git
yapp.mavenPublishing.scmDeveloperConnection = scm:git:ssh://github.com/example.git
yapp.mavenPublishing.scmUrl = https://github.com/example

yapp.mavenPublishing.licenseName = The Apache Software License, Version 2.0
yapp.mavenPublishing.licenseUrl = http://www.apache.org/licenses/LICENSE-2.0.txt

yapp.mavenPublishing.developerId = an dev id
yapp.mavenPublishing.developerName = an dev name
yapp.mavenPublishing.developerEmail = my@email.com
yapp.mavenPublishing.organization =  my org name
yapp.mavenPublishing.organizationUrl = my org url

yapp.signing.enabled = false
```
## Tasks

The plugin will render a few tasks under "yapp publisher".
They names are 

* createConfigurationTemplate TO-DO
* publishArtifact - publish the artifact to the [publish target](#publish-target)
* publishArtifactToLocalRepo - publish to local repo
* yappConfiguration - show the plugins current configuration - i.e type of [publish target](#publish-target) and project type

## Publish target

A publish target defines where to publish the project.
If you leave this empty, the plugin will make a guess based on your plugins and version.
Run the *yappConfiguration* task to see what your current configuration is.

Allowed target values are:

- maven_central
- maven_central_snapshot
- gitlab
- gradle_portal
- custom TO-DO

## Properties Configuration

You can put your configuration in a

- Build File (build.gradle.kts etc)
- Property File (gradle.properties etc)
- System Environment Variable

This is also the order in which they will be read.

NOTE: System Environments are always in CAPITAL, i.e YAPP_POM_ARTIFACTID, and so on.

The configuration tables are showing the values as 
| Build file         | Property file     | System environment     |  Comment              |

**Yapp Publisher General Configuration**


| yapp { property }  | yapp.property     | YAPP_PROPERTY          | Value                   |
| --------------     | ----------------- | ---------------------- | ----------------------------- |
| target             | *                 | *                      |  A [publish target](#publish-target) or empty|

**MavenPublishing Configuration**

| yapp { mavenPublishing { property } } }  | yapp.mavenPublishing.property       | YAPP_MAVENPUBLISHING_PROPERTY         |             |
| ---------------------------  | ----------------------- | ------------------------- | ----------- |
| mavenCentralLegacyUrl          | *                 | *                      |              |
| artifactId                   | *                       | *                         |             |
| groupId                      | *                       | *                         |             | 
| version                      | *                       | *                         |             | 
| name                         | *                       | *                         |             | 
| description                  | *                       | *                         |             | 
| url                          | *                       | *                         |             | 
| inceptionYear                | *                       | *                         |             | 
| licenseName                  | *                       | *                         |             | 
| licenseUrl                   | *                       | *                         |             | 
| licenseDistribution          | *                       | *                         |             | 
| licenseComments              | *                       | *                         |             | 
| developerId                  | *                       | *                         |             | 
| developerName                | *                       | *                         |             | 
| developerEmail               | *                       | *                         |             | 
| organization                 | *                       | *                         |             | 
| organizationUrl              | *                       | *                         |             | 
| scmUrl                       | *                       | *                         |             | 
| scmConnection                | *                       | *                         |             | 
| scmDeveloperConnection       | *                       | *                         |             | 

**Maven Central**

| yapp { property }  | yapp.property     | YAPP_PROPERTY          |              |
| --------------     | ----------------- | ---------------------- | ------------ |
| ossrhUser          | *                 | *                      |              |
| ossrhPassword      | *                 | *                      |              |

**Signing**

Note: If signing is enabled, the signing key can be either a path to an gpg-key or in text format with literal newlines
as \n.
See the gpg folder under the Project test resources for examples.

| yapp { signing { property } } } | yapp.signing.property | YAPP_SIGNING_PROPERTY     |              |
| ------------------------------- | ----------------------| ------------------------- | ------------ |
| enabled                         | *                     | *                         |              |
| signSnapshot                    | *                     | *                         |              |
| keySecret                       | *                     | *                         |              |
| keyId                           | *                     | *                         |              |
| key                             | *                     | *                         |              |

**Gradle Plugin and publishing**

| yapp { gradleplugin { property } } } | yapp.gradleplugin.property   | YAPP_GRADLEPLUGIN_PROPERTY |             |
| ------------------------------------ | -----------------------      | -------------------------  | ----------- |
| web                                  | *                            | *                          |             |
| vcs                                  | *                            | *                          |             |
| tags                                 | *                            | *                          |             |
| id                                   | *                            | *                          |             |
| class                                | *                            | *                          |             |
| description                          | *                            | *                          |             |
| displayName                          | *                            | *                          |             |
| key                                  | *                            | *                          |             |
| keySecret                            | *                            | *                          |             |

## Future

In a very early stage, the following features are planned, according to priority:

* Create sample configuration
* More targets are planned. JFrog, GitHub and Custom
* Better semi smart Plugin identification
* More settings can be simplified and auto set
* Android Library support
* Better Java support
* Dokka-support
* More tests
* Docs/source inclusion/exclusion option
* Nested object model, for the few 10% that might need them
* Ability to do final release stage on maven central so one does not have to do it manually - MAYBE, on the other hand, it is a dangerous process.
* Support other languages than Java/Kotlin
* and more,

# Q & A

* Why are there tasks called generateMetaDataFileForPluginMavenPublication and more, all relating to "pluginMaven"

It is a design choice (or a bug), these tasks are autogenerated for the Maven Publish plugin and can't be disabled.
Look att [https://github.com/gradle/gradle/issues/12394](https://github.com/gradle/gradle/issues/12394)



## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.

## Getting involved

General instructions for contributing [CONTRIBUTING](docs/CONTRIBUTIONS.adoc).

----

## License

The Gradle Yapp Publisher Plugin is released under the:

[Apache License 2.0](LICENSE)

----

## Primary Maintainer

Josef Andersson https://github.com/jandersson-svt