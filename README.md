![REUSE Compliance](https://img.shields.io/reuse/compliance/github.com/svt/gradle-yapp-publisher-plugin)
![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/jandersson-svt/gradle-yapp-publisher-plugin)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-1.0.0-yellow.svg)](https://conventionalcommits.org)

# Gradle Yapp Publisher Plugin - or Yet Another Publisher Plugin 

## What is it?

A Gradle plugin for publishing and optionally signing Java and Kotlin releases/snapshots to Maven Central, Gradle Portal, GitLab.

## Why does it exist?

To make life (arguably) easier when configuring the plugins needed for these tasks.

To offer a simple, flexible union interface for these tasks, regardless of publishing target.

## Features

* Maven Central Publishing
* Gradle Portal Publishing
* GitLab Publishing
* Signing
* Chose Build file, Properties or System Environment Configuration 
* Semi smart configuration :)

### Quickstart steps

1. Add this [plugin](#plugin-addition) plugin itself to your plugins block

2. Add a [Basic publish plugin identificator](#plugin-addition)
    
3. Configure the plugin, see the [examples](#examples).
The examples show the most basic settings - there are lots more to [configure or override if you need/want to](#configurations).

4. Run [Gradle Task](#tasks) *yappConfiguration* to view the plugins current project type and publish target configuration.

5. Run [Gradle Task](#tasks) *publishArtifactToLocalRepo* to publish your task to the local repo if you want to verify

6. Run [Gradle Task](#tasks) *publish* to publish your plugin, if all is good.

## Plugin addition 

Beside adding this plugin

```kotlin
plugins {
    id("se.svt.oss.gradle-yapp-publisher-plugin") version "x.y.z"
    //...
}
```

You also need to add one of the following  basic publishing plugins as a placeholder

*Publishing to Maven Central/GitLab/Custom (Java/Kotlin Library)*

```kotlin
plugins {
    `maven-publish`
        //...
}            
```

* Gradle Portal (Gradle Plugin)*

```kotlin
plugins {
    `java-gradle-plugin`
        //...
}
```

## Tasks

The plugin offers a few tasks found under "yapp publisher".

* createConfigurationTemplate TO-DO
* publishArtifact - publish the artifact to the [publish target](#publish-target)
* publishArtifactToLocalRepo - publish to local repo
* yappConfiguration - show the plugins current configuration - i.e type of [publish target](#publish-target), project type and more

## Publish target

A publish target defines where to publish the project.
If you leave this empty, the plugin will make a guess based on your plugins and version.

Allowed target values are:

- maven_central
- maven_central_snapshot
- gitlab
- gradle_portal
- custom TO-DO

### So, The plugin needs to know about:

* A project type (for example, a java-library)
* A [Publish Target](#publish-target)
* A few properties, depending on your configuration

If project type and publish target are not configured explicitly, _they are chosen on a best-effort guess_, depending on you chosen plugin and version setup.

## Configurations

You can put your configuration in a

- Build File (build.gradle.kts etc)
- Property File (gradle.properties etc)
- System Environment Variable

This is also the order in which they will be read.

The plugin abstracts:

* [Maven Publish](https://docs.gradle.org/current/userguide/publishing_maven.html)
* [Java Gradle Plugin Development Plugin](https://docs.gradle.org/current/userguide/java_gradle_plugin.html#java_gradle_plugin)
* [Gradle Portal Publishing Plugin](https://plugins.gradle.org/docs/publish-plugin)
* [Gradle Signing Plugin](https://docs.gradle.org/current/userguide/publishing_signing.html)


The following configuration tables are showing the values as 
| Build file         | Property file     | System environment     |  Comment              |

* NOTE: System Environments are always in CAPITAL, i.e YAPP_POM_ARTIFACTID, and so on.

* **Yapp Publisher General Configuration **

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

* More tests
* More targets are planned. JFrog, GitHub and Custom
* Better semi smart Plugin identification
* More settings can be simplified and auto set
* Ability to do final release stage on maven central so one does not have to do it manually
* Create sample configuration
* Android Library support
* Better Java support
* Dokka-support
* Docs/source inclusion/exclusion option
* Nested object model, for the few 10% that might need them
* Support other languages than Java/Kotlin
* and more,

# F.A.Q

* Why are there tasks called generateMetaDataFileForPluginMavenPublication and more, all relating to "pluginMaven"

It is a design choice (or a bug), these tasks are autogenerated for the Maven Publish plugin and can't be disabled.
Look att [https://github.com/gradle/gradle/issues/12394](https://github.com/gradle/gradle/issues/12394)

* Why another publisher plugin ?

At the time of starting this plugin there was none found that had all the features this one has. 


## Examples

*Maven Central*

Build file(build.gradle.kts) configuration, with signing

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

Properties file(gradle.properties) configuration 

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