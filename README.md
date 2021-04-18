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

yappPublisher {

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

## Configuration flags

Pom

| Build File             | Gradle Property          | System Environment        | Description |
| --------------------   | -----------------------  | ------------------------- | ------------|
|artifactId              | pom.artifactId           | POM_ARTIFACTID            |             |
|groupId                 | pom.groupId              | POM_GROUPID               |             | 
|version                 | pom.version              | POM_VERSION               |             | 
|name                    | pom.name                 | POM_NAME                  |             | 
|description             | pom.description          | POM_DESCRIPTION           |             | 
|url                     | pom.url                  | POM_URL                   |             | 
|inceptionYear           | pom.inceptionYear        | POM_INCEPTIONYEAR         |             | 
|licenseName             | pom.licenseName          | POM_LICENSENAME           |             | 
|licenseUrl              | pom.licenseUrl           | POM_LICENSEURL            |             | 
|licenseDistribution     | pom.licenseDistribution  | POM_LICENSDISTRIBUTION    |             | 
|licenseComments         | pom.licenseComments      | POM_LICENSECOMMENTS       |             | 
|developerId             | pom.developerId          | POM_DEVELOPERID           |             | 
|developerName           | pom.developerName        | POM_DEVELOPERNAME         |             | 
|developerEmail          | pom.developerEmail       | POM_DEVELOPEREMAIL        |             | 
|organization            | pom.organization         | POM_ORGANIZATION          |             | 
|organizationUrl         | pom.organizationUrl      | POM_ORGANIZATIONURL       |             | 
|scmUrl                  | pom.scmUrl               | POM_SCMURL                |             | 
|scmConnection           | pom.scmConnection        | POM_SCMCONNECTION         |             | 
|scmDeveloperConnection  | pom.scmDeveloperConnection | POM_SCMDEVELOPERCONNECTION  |         | 

Maven Central

| Build File         | Gradle Property          | System Environment        | Description |
| --------------     | -----------------------  | ------------------------- | ------------|
|ossrhUser           | ossrhUser            | OSSRHUSER             |             |
|ossrhPassword       | ossrhPassword        | OSSRPASSWORD          |             |

Signing

| Build File         | Gradle Property          | System Environment        | Description |
| --------------     | -----------------------  | ------------------------- | ------------|
|enabled             | signing.enabled          | SIGNING_ENABLED           |             |
|signSnapshot        | signing.snapshot         | SIGNING_SNAPSHOT          |             |
|password            | signing.password         | SIGNING_PASSWORD          |             |
|keyId               | signing.keyId            | SIGNING_KEYID             |             |
|key                 | signing.key              | SIGNING_KEY               |             |

Gradle Plugin and publishing

| Build File         | Gradle Property          | System Environment        | Description |
| --------------     | -----------------------  | ------------------------- | ------------|
|webSite             | gradleplugin.web     | GRADLEPLUGIN_WEBSITE          |             |
|vcsUrl              | gradleplugin.vcs      | GRADLEPLUGIN_SNAPSHOT        |             |
|tags                | gradleplugin.tags        | GRADLEPLUGIN_TAGS         |             |
|id                  | gradleplugin.id          | GRADLEPLUGIN_ID           |             |
|implementationClass | gradleplugin.class       | GRADLEPLUGIN_CLASS        |             |
|description         | gradleplugin.description | GRADLEPLUGIN_description  |             |
|displayName         | gradleplugin.displayname | GRADLEPLUGIN_displayname  |             |
|key                 | gradleplugin.key         | GRADLEPLUGIN_KEY          |             |
|keySecret           | gradleplugin.keySecret   | GRADLEPLUGIN_KEYSECRET    |             |

## Future

In a very early stage, the following features would be nice to have:

* Android Library support
* Better Java usage support
* Dokka-support
* More tests
* Nested object model
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