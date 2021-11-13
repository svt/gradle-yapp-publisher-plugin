package se.svt.oss.gradle.yapp.config

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import se.svt.oss.gradle.yapp.AbstractIntegrationTest
import se.svt.oss.gradle.yapp.ConfigurationData
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExperimentalPathApi
class ProjectPublishTargetTypeIntegrationTest : AbstractIntegrationTest() {

    val kotlinLibProjectPath: String = "/src/test/resources/projects/kotlinlibrary/"
    val javaLibProjectPath: String = "/src/test/resources/projects/javalibrary/"
    val javaGradlePluginProjectPath: String = "/src/test/resources/projects/javagradleplugin/"
    val kotlinGradlePluginProjectPath: String = "/src/test/resources/projects/kotlingradleplugin/"
    val unknownLibraryProjectPath: String = "/src/test/resources/projects/unknown-library/"

    @Test
    fun `a java library is identified correctly`() {
        copyTemplateBuildFile(javaLibProjectPath)
        settingsFilePath = Paths.get("$testDirPath/${javaLibProjectPath}settings.gradle.kts")
        buildFilePath = Paths.get("$testDirPath/${javaLibProjectPath}build.gradle.kts")
        propertyFilePath = Paths.get("$testDirPath/${javaLibProjectPath}gradle.properties")

        val group = "se.javalib"
        val version = "0.0.2"

        publishToTmp(
            ConfigurationData.buildFileData(
                group,
                version,
                "",
                plugin2 = """`java-library`""",
                buildGradleFile = buildFilePath
            ),
            ConfigurationData.buildFileYappConfData(group, version),
            projectDir = File("$testDirPath/$javaLibProjectPath")
        )
    }

    @Test
    fun `a java library having a snapshot version is identified correctly`() {
        copyTemplateBuildFile(javaLibProjectPath)
        settingsFilePath = Paths.get("$testDirPath/${javaLibProjectPath}settings.gradle.kts")
        buildFilePath = Paths.get("$testDirPath/${javaLibProjectPath}build.gradle.kts")
        propertyFilePath = Paths.get("$testDirPath/${javaLibProjectPath}gradle.properties")

        val group = "se.javalib"
        val version = "0.0.1-SNAPSHOT"

        publishToTmp(
            ConfigurationData.buildFileData(
                group,
                version,
                "",
                plugin2 = """`java-library`""",
                buildGradleFile = buildFilePath
            ),
            ConfigurationData.buildFileYappConfData(group, version),
            projectDir = File("$testDirPath/$javaLibProjectPath")
        )
    }

    @Test
    fun `a kotlin library is identified correctly`() {
        copyTemplateBuildFile(kotlinLibProjectPath)
        settingsFilePath = Paths.get("$testDirPath/${kotlinLibProjectPath}settings.gradle.kts")
        buildFilePath = Paths.get("$testDirPath/${kotlinLibProjectPath}build.gradle.kts")
        propertyFilePath = Paths.get("$testDirPath/${kotlinLibProjectPath}gradle.properties")

        val group = "se.kotlinlib"
        val version = "0.0.1-SNAPSHOT"

        publishToTmp(
            ConfigurationData.buildFileData(
                group, version, "",
                plugin2 = """kotlin("jvm") version "1.5.21" 
                | `java-library`""".trimMargin(),
                buildGradleFile = buildFilePath
            ),

            ConfigurationData.buildFileYappConfData(group, version),
            projectDir = File("$testDirPath/$kotlinLibProjectPath")
        )
    }

    @Test
    fun `a kotlin gradle plugin is identified correctly`() {
        copyTemplateBuildFile(kotlinGradlePluginProjectPath)
        settingsFilePath = Paths.get("$testDirPath/${kotlinGradlePluginProjectPath}settings.gradle.kts")
        buildFilePath = Paths.get("$testDirPath/${kotlinGradlePluginProjectPath}build.gradle.kts")
        propertyFilePath = Paths.get("$testDirPath/${kotlinGradlePluginProjectPath}gradle.properties")

        val group = "se.kotlingradleplugin"
        val version = "0.0.1-SNAPSHOT"

        publishToTmp(
            ConfigurationData.buildFileData(
                group, version, "",
                plugin2 = """id("org.jetbrains.kotlin.jvm") version "1.5.31"  
                | `java-gradle-plugin`""".trimMargin(),
                buildGradleFile = buildFilePath
            ),

            ConfigurationData.buildFileYappConfData(group, version),
            projectDir = File("$testDirPath/$kotlinGradlePluginProjectPath"),
            propertiesData = """yapp.gradleplugin.id=$group"""
        )
    }

    @Test
    fun `a java gradle plugin is identified correctly`() {
        copyTemplateBuildFile(javaGradlePluginProjectPath)
        settingsFilePath = Paths.get("$testDirPath/${javaGradlePluginProjectPath}settings.gradle.kts")
        buildFilePath = Paths.get("$testDirPath/${javaGradlePluginProjectPath}build.gradle.kts")
        propertyFilePath = Paths.get("$testDirPath/${javaGradlePluginProjectPath}gradle.properties")

        val group = "se.javagradleplugin"
        val version = "0.0.1-SNAPSHOT"

        publishToTmp(
            ConfigurationData.buildFileData(
                group,
                version,
                "",
                plugin1 = "",
                plugin2 = """`java-gradle-plugin`""".trimMargin(),
                buildGradleFile = buildFilePath
            ),

            ConfigurationData.buildFileYappConfData(group, version),
            projectDir = File("$testDirPath/$javaGradlePluginProjectPath"),
            propertiesData = """yapp.gradleplugin.id=$group"""
        )
    }

    @Disabled
    fun `could not identify the project type`() {
        copyTemplateBuildFile(unknownLibraryProjectPath)
        settingsFilePath = Paths.get("$testDirPath/${unknownLibraryProjectPath}settings.gradle.kts")
        buildFilePath = Paths.get("$testDirPath/${unknownLibraryProjectPath}build.gradle.kts")
        propertyFilePath = Paths.get("$testDirPath/${unknownLibraryProjectPath}gradle.properties")

        val group = "se.unknownplugin"
        val version = "0.0.1-SNAPSHOT"

        assertThrows<IllegalStateException> {
            publishToTmp(
                ConfigurationData.buildFileData(
                    group,
                    version,
                    "",
                    plugin1 = "",
                    plugin2 = "".trimMargin(),
                    buildGradleFile = buildFilePath
                ),

                ConfigurationData.buildFileYappConfData(group, version),
                projectDir = File("$testDirPath/$unknownLibraryProjectPath"),
            )
        }
    }

    private fun gradlePluginBlock() = """
        
        


gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = "org.gradle.sample.simple-plugin"
            implementationClass = "org.gradle.sample.SimplePlugin"
        }
    }
}

    """
}
