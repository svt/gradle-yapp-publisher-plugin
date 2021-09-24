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
class ProjectTypeIntegrationTest : AbstractIntegrationTest() {

    val kotlinLibProjectPath: String = "/src/test/resources/projects/kotlin-library/"
    val javaLibProjectPath: String = "/src/test/resources/projects/java-library/"
    val javaGradlePluginProjectPath: String = "/src/test/resources/projects/java-gradle-plugin/"
    val kotlinGradlePluginProjectPath: String = "/src/test/resources/projects/kotlin-gradle-plugin/"
    val unknownLibraryProjectPath: String = "/src/test/resources/projects/unknown-library/"

    @Test
    fun `a java library is identified correctly`() {
        copyTemplateBuildFile(javaLibProjectPath)
        settingsFile = Paths.get("$testDirPath/${javaLibProjectPath}settings.gradle.kts")
        buildFile = Paths.get("$testDirPath/${javaLibProjectPath}build.gradle.kts")
        propertyFile = Paths.get("$testDirPath/${javaLibProjectPath}gradle.properties")

        val group = "se.javalib"
        val version = "0.0.1-SNAPSHOT"

        publishToTmp(
            ConfigurationData.buildGradle(
                group,
                version,
                "",
                plugin2 = """`java-library`""",
                buildGradleFile = buildFile
            ),
            ConfigurationData.yappBuildGradleConf(group, version),
            projectdir = File("$testDirPath/$javaLibProjectPath")
        )
    }

    @Test
    fun `a kotlin library is identified correctly`() {
        copyTemplateBuildFile(kotlinLibProjectPath)
        settingsFile = Paths.get("$testDirPath/${kotlinLibProjectPath}settings.gradle.kts")
        buildFile = Paths.get("$testDirPath/${kotlinLibProjectPath}build.gradle.kts")
        propertyFile = Paths.get("$testDirPath/${kotlinLibProjectPath}gradle.properties")

        val group = "se.kotlinlib"
        val version = "0.0.1-SNAPSHOT"

        publishToTmp(
            ConfigurationData.buildGradle(
                group, version, "",
                plugin2 = """kotlin("jvm") version "1.5.21" 
                | `java-library`""".trimMargin(),
                buildGradleFile = buildFile
            ),

            ConfigurationData.yappBuildGradleConf(group, version),
            projectdir = File("$testDirPath/$kotlinLibProjectPath")
        )
    }

    @Test
    fun `a kotlin gradle plugin is identified correctly`() {
        copyTemplateBuildFile(kotlinGradlePluginProjectPath)
        settingsFile = Paths.get("$testDirPath/${kotlinGradlePluginProjectPath}settings.gradle.kts")
        buildFile = Paths.get("$testDirPath/${kotlinGradlePluginProjectPath}build.gradle.kts")
        propertyFile = Paths.get("$testDirPath/${kotlinGradlePluginProjectPath}gradle.properties")

        val group = "se.kotlingradleplugin"
        val version = "0.0.1-SNAPSHOT"

        publishToTmp(
            ConfigurationData.buildGradle(
                group, version, "",
                plugin2 = """id("org.jetbrains.kotlin.jvm") version "1.5.31"  
                | `java-gradle-plugin`""".trimMargin(),
                buildGradleFile = buildFile
            ),

            ConfigurationData.yappBuildGradleConf(group, version),
            projectdir = File("$testDirPath/$kotlinGradlePluginProjectPath"),
            properties = """yapp.gradleplugin.id=$group"""
        )
    }

    @Test
    fun `a java gradle plugin is identified correctly`() {
        copyTemplateBuildFile(javaGradlePluginProjectPath)
        settingsFile = Paths.get("$testDirPath/${javaGradlePluginProjectPath}settings.gradle.kts")
        buildFile = Paths.get("$testDirPath/${javaGradlePluginProjectPath}build.gradle.kts")
        propertyFile = Paths.get("$testDirPath/${javaGradlePluginProjectPath}gradle.properties")

        val group = "se.javagradleplugin"
        val version = "0.0.1-SNAPSHOT"

        publishToTmp(
            ConfigurationData.buildGradle(
                group,
                version,
                "",
                plugin1 = "",
                plugin2 = """`java-gradle-plugin`""".trimMargin(),
                buildGradleFile = buildFile
            ),

            ConfigurationData.yappBuildGradleConf(group, version),
            projectdir = File("$testDirPath/$javaGradlePluginProjectPath"),
            properties = """yapp.gradleplugin.id=$group"""
        )
    }

    @Disabled
    fun `could not identify the project type`() {
        copyTemplateBuildFile(unknownLibraryProjectPath)
        settingsFile = Paths.get("$testDirPath/${unknownLibraryProjectPath}settings.gradle.kts")
        buildFile = Paths.get("$testDirPath/${unknownLibraryProjectPath}build.gradle.kts")
        propertyFile = Paths.get("$testDirPath/${unknownLibraryProjectPath}gradle.properties")

        val group = "se.unknownplugin"
        val version = "0.0.1-SNAPSHOT"

        var assertThrows = assertThrows<IllegalStateException> {
            publishToTmp(
                ConfigurationData.buildGradle(
                    group,
                    version,
                    "",
                    plugin1 = "",
                    plugin2 = "".trimMargin(),
                    buildGradleFile = buildFile
                ),

                ConfigurationData.yappBuildGradleConf(group, version),
                projectdir = File("$testDirPath/$unknownLibraryProjectPath"),
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
