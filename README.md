# Gradle Yapp Publisher Plugin - Yet Another Publisher Plugin

A Gradle plugin for making publishing releases and snapshots more convenient.

## Features

* Maven Central Publishing
* Signing
* Gradle Portal Publishing
* Build file, properties or System Environment Configuration

## Usage

Add the plugin to your plugins block and configure it:

```kotlin
plugins {
    id("se.svt.oss.gradle-yapp-publisher-plugin") version "0.0.0"
}

//Configure the plugin. 

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

Note: If signing is enabled, the signing key can be either a path to an gpg-key or in textformat with literal newlines
as \n.

## Configuration

You can put your configuration in a

- Build File (build.gradle.kts etc)
- Property File (gradle.properties etc)
- System Environment Variable     

Note: System Environments are always in CAPITAL, i.e YAPP_POM_ARTIFACTID, and so on.

Note: Configurations are picked up in order: Build File, Property File, System Env.

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

In a very early stage, the following features would be nice to have:

* Android Library support
* Better Java usage support
* Dokka-support
* More tests
* Nested object model
* Plugin selection applied only on configuration in project
* Ability to do final release stage on maven central so one does not have to do it manually 
* and more

## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.

## Getting involved

General instructions for contributing [CONTRIBUTING](docs/CONTRIBUTIONS.adoc).

----

## License

The Gradle Yap Publisher Plugin is released under:

[Apache License 2.0](LICENSE)

----

## Primary Maintainer

Josef Andersson https://github.com/jandersson-svt