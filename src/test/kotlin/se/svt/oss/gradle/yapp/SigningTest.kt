package se.svt.oss.gradle.yapp

import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension
import kotlin.io.path.ExperimentalPathApi

@ExperimentalPathApi
@ExtendWith(
    SystemStubsExtension::class
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SigningTest : AbstractIntegrationTest() {

    @Test
    fun `signing when signing is enabled and is a release version or overrides snapshot`() {
        val group = "se.signing"
        var version = "0.0.7"
        // val signingKey = resource("gpg/sec_signingkey.gpg").canonicalPath

        var signatures = signatures(version)

        publish(
            TestConfiguration.buildGradle(group, version),
            TestConfiguration.yappBuildGradleConf(group, version, signingKey, true)
        )
        assertIterableEquals(generatedSignatures("signing", version), signatures)

        version = "0.0.7-SNAPSHOT"

        signatures = signatures(version)
        publish(
            TestConfiguration.buildGradle(group, version),
            TestConfiguration.yappBuildGradleConf(group, version, signingKey, true, true)
        )

        assertIterableEquals(generatedSignatures("signing", version), signatures)

        version = "0.0.8-SNAPSHOT"
        publish(
            TestConfiguration.buildGradle(group, version),
            TestConfiguration.yappBuildGradleConf(group, version, signingKey, true, false)
        )

        assertTrue(generatedSignatures("signing", version).isEmpty())
    }

    @Test
    fun `not signing when signing is disabled or a snapshot version`() {

        val group = "se.signing"
        var version = "0.0.9"

        publish(
            TestConfiguration.buildGradle(group, version),
            TestConfiguration.yappBuildGradleConf(group, version, signingKey, false)
        )
        assertTrue(generatedSignatures("signing", version).isEmpty())

        version = "0.0.10-SNAPSHOT"
        publish(
            TestConfiguration.buildGradle(group, version),
            TestConfiguration.yappBuildGradleConf(group, version, signingKey, true)
        )
    }

    @Test
    fun `same signing artifacts regardless if key is binary format or text format`() {
        var group = "se.signing"
        val version = "0.0.7"

        val signatures = signatures(version)

        publish(
            TestConfiguration.buildGradle(group, version),
            TestConfiguration.yappBuildGradleConf(group, version, signingKey, true)
        )

        assertIterableEquals(generatedSignatures("signing", version), signatures)
        val generatedSignatures = generatedSignatures("signing", version)

        signingKey = resource("gpg/sec_signingkey.gpg").canonicalPath

        group = "se.signing2"
        publish(
            TestConfiguration.buildGradle(group, version),
            TestConfiguration.yappBuildGradleConf(group, version, signingKey, true)
        )
        assertIterableEquals(generatedSignatures("signing2", version), generatedSignatures)
    }

    private fun signatures(version: String): List<String> {
        val signatures = listOf(
            "gradle-yapp-publisher-plugin-$version.pom.asc",
            "gradle-yapp-publisher-plugin-$version-javadoc.jar.asc",
            "gradle-yapp-publisher-plugin-$version.jar.asc",
            "gradle-yapp-publisher-plugin-$version.module.asc",
            "gradle-yapp-publisher-plugin-$version-sources.jar.asc"
        )
        return signatures.sorted()
    }
}
