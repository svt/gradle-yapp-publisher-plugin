// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.io.TempDir
import org.xmlunit.builder.DiffBuilder
import org.xmlunit.builder.Input
import org.xmlunit.diff.Diff
import org.xmlunit.xpath.JAXPXPathEngine
import org.xmlunit.xpath.XPathEngine
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.appendText
import kotlin.io.path.writeText

@ExperimentalPathApi
abstract class AbstractIntegrationTest {

    open val mcProjectPath: String = "/src/test/resources/projects/mcproject/"

    open lateinit var settingsFile: Path
    open lateinit var buildFile: Path
    open lateinit var propertyFile: Path
    lateinit var signingKey: String

    private val pluginName = "gradle-yapp-publisher-plugin"
    private val tmpdir: String = System.getProperty("java.io.tmpdir")

    @BeforeAll
    open fun setup() {

        settingsFile = testDirPath.resolve("settings.gradle.kts")
        buildFile = testDirPath.resolve("build.gradle.kts")
        propertyFile = testDirPath.resolve("gradle.properties")

        signingKey = resource("gpg/sec_signingkey_ascii_newlineliteral.asc").readText()

        FileUtils.copyDirectory(File("./"), File(testDirPath.toAbsolutePath().toString()))

        publish(TestConfiguration.yappBasePlugin())
    }

    fun publish(
        buildGradleBase: String = "",
        buildGradleAppend: String = "",
        properties: String = "",
        gradleTask: String = "publishToMavenLocal",
        projectdir: File = testDirPath.toFile()
    ) {

        buildFile.writeText(buildGradleBase)
        buildFile.appendText(buildGradleAppend)
        propertyFile.toFile().writeText(properties)

        val buildResult = GradleRunner.create()
            .withProjectDir(projectdir)
            .withArguments("-Dmaven.repo.local=$tmpdir", "clean", gradleTask)
            .withPluginClasspath()
            .forwardOutput()
            .build()
    }

    fun resource(resource: String): File = File(javaClass.classLoader.getResource(resource).file)

    fun diff(resourcePom: File, generatedPom: File): Diff {

        val diff = DiffBuilder.compare(Input.fromFile(resourcePom)).withTest(Input.fromFile(generatedPom))
            .checkForSimilar()
            .ignoreWhitespace().build()
        println(diff.differences)
        return diff
    }

    fun generatedPom(name: String, subdir: String, version: String, extension: String = "pom") = Paths.get(
        tmpdir, "se", subdir, name, version,
        "$name-$version.$extension"
    ).toFile()

    fun generatedSignatures(name: String = pluginName, subdir: String, version: String) = Paths.get(
        tmpdir, "se", subdir, name, version
    ).toFile().walk().filter { it.extension == "asc" }.map { it.name }.toList().sorted()

    fun xpathFieldDiff(query: String, expectedValue: String, subdir: String, version: String, name: String = "mc") {
        val xpath: XPathEngine = JAXPXPathEngine()
        xpath.setNamespaceContext(mapOf(Pair("m", "http://maven.apache.org/POM/4.0.0")))
        val nodes = xpath.selectNodes(query, Input.fromFile(generatedPom(name, subdir, version)).build())

        Assertions.assertTrue(nodes.count() > 0)

        nodes.forEach {
            Assertions.assertEquals(expectedValue, it.textContent)
        }
    }

    companion object {
        @JvmStatic
        @TempDir
        open lateinit var testDirPath: Path
    }
}
