// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.gradle.yapp

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.io.TempDir
import org.xmlunit.builder.DiffBuilder
import org.xmlunit.builder.Input
import org.xmlunit.diff.Diff
import org.xmlunit.xpath.JAXPXPathEngine
import org.xmlunit.xpath.XPathEngine
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.appendText
import kotlin.io.path.writeText

@ExperimentalPathApi
abstract class AbstractIntegrationTest {

    open val buildFileTemplatePath: String = "/src/test/resources/projects/build.gradle.kts"

    open lateinit var settingsFilePath: Path
    open lateinit var buildFilePath: Path
    open lateinit var propertyFilePath: Path
    lateinit var signingKey: String

    private val pluginName = "gradle-yapp-publisher-plugin"
    private val tmpdir: String = System.getProperty("java.io.tmpdir")

    @BeforeAll
    fun setup() {
        publishYappPluginToTmp()
    }

    private fun publishYappPluginToTmp() {
        settingsFilePath = testDirPath.resolve("settings.gradle.kts")
        buildFilePath = testDirPath.resolve("build.gradle.kts")
        propertyFilePath = testDirPath.resolve("gradle.properties")

        signingKey = resource("gpg/sec_signingkey_ascii_newlineliteral.asc").readText()

        FileUtils.copyDirectory(File("./"), testDirPath.toAbsolutePath().toFile())

        publishToTmp(ConfigurationData.yappBasePlugin())
    }

    fun publishToTmp(
        buildFileData: String,
        buildFileAppendData: String = "",
        propertiesData: String = "",
        gradleTask: String = "publishToMavenLocal",
        projectDir: File = testDirPath.toFile()
    ) {

        buildFilePath.writeText(buildFileData)
        buildFilePath.appendText(buildFileAppendData)
        propertyFilePath.toFile().writeText(propertiesData)

        val buildResult = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments("-Dmaven.repo.local=$tmpdir", gradleTask)
            .withPluginClasspath()
            .forwardOutput()
            .build()

        assertEquals(TaskOutcome.SUCCESS, buildResult.task(":$gradleTask")!!.outcome)
    }

    fun resource(resource: String): File = File(javaClass.classLoader.getResource(resource)!!.file)

    fun diff(resourcePom: File, generatedPom: File): Diff {

        val diff = DiffBuilder.compare(Input.fromFile(resourcePom)).withTest(Input.fromFile(generatedPom))
            .checkForSimilar()
            .ignoreWhitespace().build()
        println(diff.differences)
        return diff
    }

    fun generatedPom(name: String, subdir: String, version: String, extension: String = "pom"): File = Paths.get(
        tmpdir, "se", subdir, name, version,
        "$name-$version.$extension"
    ).toFile()

    fun generatedSignatures(name: String = pluginName, subdir: String, version: String) = Paths.get(
        tmpdir, "se", subdir, name, version
    ).toFile().walk().filter { it.extension == "asc" }.map { it.name }.toList().sorted()

    fun xpathFieldDiff(
        query: String,
        expectedValue: String,
        subdir: String,
        version: String,
        name: String = testLibraryDir()
    ) {
        val xpath: XPathEngine = JAXPXPathEngine()
        xpath.setNamespaceContext(mapOf(Pair("m", "http://maven.apache.org/POM/4.0.0")))
        val nodes = xpath.selectNodes(query, Input.fromFile(generatedPom(name, subdir, version)).build())

        assertTrue(nodes.count() > 0)

        nodes.forEach {
            assertEquals(expectedValue, it.textContent)
        }
    }

    fun copyTemplateBuildFile(projectPath: String = testLibraryPath()) {
        Files.copy(
            Paths.get("$testDirPath", buildFileTemplatePath),
            Paths.get("$testDirPath", projectPath, "build.gradle.kts"), StandardCopyOption.REPLACE_EXISTING
        )
    }

    companion object {
        @JvmStatic
        @TempDir
        lateinit var testDirPath: Path
    }

    open fun projectDir() = File("$testDirPath/${testLibraryPath()}")

    open fun testLibraryPath() = "/src/test/resources/projects/kotlinlibrary"

    open fun testLibraryDir() = testLibraryPath().substringAfterLast("/")
}
