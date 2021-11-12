// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp

import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
@ExtendWith(
    SystemStubsExtension::class
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SigningPluginIntegrationTest : AbstractIntegrationTest() {

    @BeforeEach
    fun before() {
        copyTemplateBuildFile()
        settingsFile = Paths.get("$testDirPath/${kotlinLibraryPath}settings.gradle.kts")
        buildFile = Paths.get("$testDirPath/${kotlinLibraryPath}build.gradle.kts")
        propertyFile = Paths.get("$testDirPath/${kotlinLibraryPath}gradle.properties")
    }

    @Test
    fun `signing when signing is enabled and is a release version or overrides snapshot`() {
        val group = "se.signing"
        var version = "0.0.7"
        // val signingKey = resource("gpg/sec_signingkey.gpg").canonicalPath

        var signatures = signatures(version)

        publishToTmp(
            ConfigurationData.buildGradle(group, version, buildGradleFile = buildFile),
            ConfigurationData.yappBuildGradleConfSigning(signingKey, true),
            projectdir = projectDir()
        )
        assertIterableEquals(generatedSignatures("kotlinlibrary", "signing", version), signatures)

        version = "0.0.7-SNAPSHOT"

        copyTemplateBuildFile()

        signatures = signatures(version)
        publishToTmp(
            ConfigurationData.buildGradle(group, version, buildGradleFile = buildFile),
            ConfigurationData.yappBuildGradleConfSigning(signingKey, true, true),
            projectdir = projectDir()
        )

        assertIterableEquals(generatedSignatures("kotlinlibrary", "signing", version), signatures)

        copyTemplateBuildFile()

        version = "0.0.8-SNAPSHOT"
        publishToTmp(
            ConfigurationData.buildGradle(group, version, buildGradleFile = buildFile),
            ConfigurationData.yappBuildGradleConf(group, version, signingKey, true, false),
            projectdir = projectDir()
        )

        assertTrue(generatedSignatures("kotlinlibrary", "signing", version).isEmpty())
    }

    @Test
    fun `not signing when signing is disabled or a snapshot version`() {

        copyTemplateBuildFile()
        val group = "se.signing"
        var version = "0.0.9"

        publishToTmp(
            ConfigurationData.buildGradle(group, version, buildGradleFile = buildFile),
            ConfigurationData.yappBuildGradleConf(group, version, signingKey, false),
            projectdir = projectDir()

        )
        assertTrue(generatedSignatures("kotlinlibrary", "signing", version).isEmpty())

        copyTemplateBuildFile()

        version = "0.0.10-SNAPSHOT"
        publishToTmp(
            ConfigurationData.buildGradle(group, version, buildGradleFile = buildFile),
            ConfigurationData.yappBuildGradleConf(group, version, signingKey, true),
            projectdir = projectDir()
        )
    }

    @Test
    fun `same signing artifacts regardless if key is binary format or text format`() {
        var group = "se.signing"
        val version = "0.0.7"

        val signatures = signatures(version)

        publishToTmp(
            ConfigurationData.buildGradle(group, version, buildGradleFile = buildFile),
            ConfigurationData.yappBuildGradleConf(group, version, signingKey, true),
            projectdir = projectDir()
        )

        assertIterableEquals(generatedSignatures("kotlinlibrary", "signing", version), signatures)
        val generatedSignatures = generatedSignatures("kotlinlibrary", "signing", version)

        signingKey = resource("gpg/sec_signingkey.gpg").canonicalPath

        group = "se.signing2"

        copyTemplateBuildFile()
        publishToTmp(
            ConfigurationData.buildGradle(group, version, buildGradleFile = buildFile),
            ConfigurationData.yappBuildGradleConf(group, version, signingKey, true),
            projectdir = projectDir()

        )
        assertIterableEquals(generatedSignatures("kotlinlibrary", "signing2", version), generatedSignatures)
    }

    private fun signatures(version: String): List<String> {
        val signatures = listOf(
            "kotlinlibrary-$version.pom.asc",
            "kotlinlibrary-$version-javadoc.jar.asc",
            "kotlinlibrary-$version.jar.asc",
            "kotlinlibrary-$version.module.asc",
            "kotlinlibrary-$version-sources.jar.asc"
        )
        return signatures.sorted()
    }
}
