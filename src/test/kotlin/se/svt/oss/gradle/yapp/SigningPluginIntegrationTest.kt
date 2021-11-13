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
        settingsFilePath = Paths.get("$testDirPath", testLibraryPath(), "settings.gradle.kts")
        buildFilePath = Paths.get("$testDirPath", testLibraryPath(), "build.gradle.kts")
        propertyFilePath = Paths.get("$testDirPath", testLibraryPath(), "gradle.properties")
    }

    @Test
    fun `signing when signing is enabled and is a release version or overrides snapshot`() {
        val group = "se.signing"
        var version = "0.0.7"
        // val signingKey = resource("gpg/sec_signingkey.gpg").canonicalPath

        var signatures = signatures(version)

        publishToTmp(
            ConfigurationData.buildFileData(group, version, buildGradleFile = buildFilePath),
            ConfigurationData.yappBuildGradleConfSigning(signingKey, true),
            projectDir = projectDir()
        )
        assertIterableEquals(generatedSignatures(testLibraryDir(), "signing", version), signatures)

        version = "0.0.7-SNAPSHOT"

        copyTemplateBuildFile()

        signatures = signatures(version)
        publishToTmp(
            ConfigurationData.buildFileData(group, version, buildGradleFile = buildFilePath),
            ConfigurationData.yappBuildGradleConfSigning(signingKey, true, true),
            projectDir = projectDir()
        )

        assertIterableEquals(generatedSignatures(testLibraryDir(), "signing", version), signatures)

        copyTemplateBuildFile()

        version = "0.0.8-SNAPSHOT"
        publishToTmp(
            ConfigurationData.buildFileData(group, version, buildGradleFile = buildFilePath),
            ConfigurationData.buildFileYappConfData(group, version, signingKey, true, false),
            projectDir = projectDir()
        )

        assertTrue(generatedSignatures(testLibraryDir(), "signing", version).isEmpty())
    }

    @Test
    fun `not signing when signing is disabled or a snapshot version`() {

        copyTemplateBuildFile()
        val group = "se.signing"
        var version = "0.0.9"

        publishToTmp(
            ConfigurationData.buildFileData(group, version, buildGradleFile = buildFilePath),
            ConfigurationData.buildFileYappConfData(group, version, signingKey, false),
            projectDir = projectDir()

        )
        assertTrue(generatedSignatures(testLibraryDir(), "signing", version).isEmpty())

        copyTemplateBuildFile()

        version = "0.0.10-SNAPSHOT"
        publishToTmp(
            ConfigurationData.buildFileData(group, version, buildGradleFile = buildFilePath),
            ConfigurationData.buildFileYappConfData(group, version, signingKey, true),
            projectDir = projectDir()
        )
    }

    @Test
    fun `same signing artifacts regardless if key is binary format or text format`() {
        var group = "se.signing"
        val version = "0.0.7"

        val signatures = signatures(version)

        publishToTmp(
            ConfigurationData.buildFileData(group, version, buildGradleFile = buildFilePath),
            ConfigurationData.buildFileYappConfData(group, version, signingKey, true),
            projectDir = projectDir()
        )

        assertIterableEquals(generatedSignatures(testLibraryDir(), "signing", version), signatures)
        val generatedSignatures = generatedSignatures(testLibraryDir(), "signing", version)

        signingKey = resource("gpg/sec_signingkey.gpg").canonicalPath

        group = "se.signing2"

        copyTemplateBuildFile()
        publishToTmp(
            ConfigurationData.buildFileData(group, version, buildGradleFile = buildFilePath),
            ConfigurationData.buildFileYappConfData(group, version, signingKey, true),
            projectDir = projectDir()

        )
        assertIterableEquals(generatedSignatures(testLibraryDir(), "signing2", version), generatedSignatures)
    }

    private fun signatures(version: String): List<String> {
        val signatures = listOf(
            "${testLibraryDir()}-$version.pom.asc",
            "${testLibraryDir()}-$version-javadoc.jar.asc",
            "${testLibraryDir()}-$version.jar.asc",
            "${testLibraryDir()}-$version.module.asc",
            "${testLibraryDir()}-$version-sources.jar.asc"
        )
        return signatures.sorted()
    }
}
