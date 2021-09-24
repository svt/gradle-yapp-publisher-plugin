[![REUSE status](https://api.reuse.software/badge/github.com/fsfe/reuse-tool)](https://api.reuse.software/info/github.com/fsfe/reuse-tool) ![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/svt/gradle-yapp-publisher-plugin)

# Gradle Yapp Publisher Plugin - Yet Another Publisher Plugin

## What is it?

A Gradle plugin for publishing releases and snapshots to Maven Central, Gradle Portal.

## Why does it exist?

To make life easier when configuring the plugins needed for these tasks.
To offer a simpler interface for these tasks.

## Features

* Maven Central Publishing
* Gradle Portal Publishing
* Signing
* Choose between Build file, properties or System Environment Configuration
* Semi smart configuration

## How does it work?

It abstracts the following plugins and creates a simplified, coherent configuration:

* [Maven Publish](https://docs.gradle.org/current/userguide/publishing_maven.html)
* [Java Gradle Plugin Development Plugin](https://docs.gradle.org/current/userguide/java_gradle_plugin.html#java_gradle_plugin)
* [Gradle Portal Publishing Plugin](https://plugins.gradle.org/docs/publish-plugin)
* [Gradle Signing Plugin](https://docs.gradle.org/current/userguide/publishing_signing.html)

and more.

## Usage

1. Add the plugin to your plugins block:

```kotlin
plugins {
    id("se.svt.oss.gradle-yapp-publisher-plugin") version "0.0.0"
        ...
}

2. If you are publishing to Maven Central (Java/Kotlin Library)
    
plugins {
    `maven-publish`
        ...
}            
If you are publishing to Gradle Portal (Gradle Plugin)

plugins {
    `java-gradle-plugin`
        ...
}

(A semi smart identification  will add the necessary needed plugins)
    
3. Configure the plugin. Example - Pom with signing

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
yapp.pom.name = exampleproject
yapp.pom.description = an pom example description
yapp.pom.url = https://github.com/myexamplaproject
yapp.pom.inceptionYear = 2021

yapp.pom.scmConnection = scm:git:github.com/example.git
yapp.pom.scmDeveloperConnection = scm:git:ssh://github.com/example.git
yapp.pom.scmUrl = https://github.com/example

yapp.pom.licenseName = The Apache Software License, Version 2.0
yapp.pom.licenseUrl = http://www.apache.org/licenses/LICENSE-2.0.txt

yapp.pom.developerId = an dev id
yapp.pom.developerName = an dev name
yapp.pom.developerEmail = my@email.com
yapp.pom.organization =  my org name
yapp.pom.organizationUrl = my org url

yapp.signing.enabled = false
```

## Configuration

You can put your configuration in a

- Build File (build.gradle.kts etc)
- Property File (gradle.properties etc)
- System Environment Variable

Note: System Environments are always in CAPITAL, i.e YAPP_POM_ARTIFACTID, and so on.

Note: Configurations are picked up in this order: Build File, Property File, System Env.
All locations are checked so theoretically, you can spread out your properties.

**POM Configuration**

| yapp { pom { property } } }  | yapp.pom.property       | YAPP_POM_PROPERTY         |             |
| ---------------------------  | ----------------------- | ------------------------- | ----------- |
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

* Better semi smart Plugin identification
* More settings can be simplified and auto set
* Android Library support
* Better Java support
* Dokka-support
* More tests are 
* Nested object model, for the few 10% that might need them
* Ability to do final release stage on maven central so one does not have to do it manually - MAYBE, on the other hand, it is a dangerous process.
* and more,

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