// SPDX-FileCopyrightText: 2021 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0
package se.svt.oss.gradle.yapp

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.xmlunit.diff.Diff
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import se.svt.oss.gradle.yapp.ConfigurationData as conf

@ExperimentalPathApi
@ExtendWith(
    SystemStubsExtension::class
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PropertiesIntegrationTest : AbstractIntegrationTest() {

    @BeforeEach
    fun before() {
        copyTemplateBuildFile()
        settingsFile = Paths.get("$testDirPath/${mcProjectPath}settings.gradle.kts")
        buildFile = Paths.get("$testDirPath/${mcProjectPath}build.gradle.kts")
        propertyFile = Paths.get("$testDirPath/${mcProjectPath}gradle.properties")
    }

    @Test
    fun `properties are read from build file plugin`() {

        val group = "se.build"
        val version = "0.0.2-SNAPSHOT"

        publishToTmp(
            conf.buildGradle(group, version, buildGradleFile = buildFile),
            conf.yappBuildGradleConf(group, version),
            projectdir = File("$testDirPath/$mcProjectPath")
        )

        val pomDiff: Diff = diff(resource("pom/buildGradleConf.pom"), generatedPom("mc", "build", version))

        assertFalse(pomDiff.hasDifferences())
    }

    @Test
    fun `properties are read from properties file`() {
        val group = "se.property"
        val version = "0.0.3-SNAPSHOT"

        publishToTmp(
            conf.buildGradle(group, version, buildGradleFile = buildFile),
            "",
            conf.yappPropertiesConf("mc", group, version),
            projectdir = File("$testDirPath/$mcProjectPath")
        )

        val pomDiff: Diff = diff(resource("pom/propertyConf.pom"), generatedPom("mc", "property", version))
        println(pomDiff.toString())

        assertFalse(pomDiff.hasDifferences())
    }

    @Test
    fun `properties are read from environment`(environmentVariables: EnvironmentVariables) {

        val group = "se.env"
        val version = "0.0.4-SNAPSHOT"

        conf.systemEnv().entries.forEach {
            environmentVariables.set(it.key, it.value)
        }

        publishToTmp(
            conf.buildGradle(group, version, buildGradleFile = buildFile),
            projectdir = File("$testDirPath/$mcProjectPath")
        )

        val pomDiff: Diff = diff(resource("pom/envConf.pom"), generatedPom("mc", "env", version))

        assertFalse(pomDiff.hasDifferences())
    }

    @Test
    fun `properties are read in order build file, properties file, system env`(environmentVariables: EnvironmentVariables) {

        val group = "se.order"
        val version = "0.0.4-SNAPSHOT"

        environmentVariables.set("YAPP_MAVENPUBLISHING_NAME", "envname")
        environmentVariables.set("YAPP_TARGETS", "maven_central")

        publishToTmp(
            conf.buildGradle(group, version, buildGradleFile = buildFile),

            conf.yappBuildGradleConf(group, version, name = "confname"),
            """yapp.mavenPublishing.name=propertyname
                |yapp.targets=maven_central
            """.trimMargin(),
            projectdir = File("$testDirPath/$mcProjectPath")
        )

        xpathFieldDiff("m:project/m:name", "confname", "order", version)

        copyTemplateBuildFile()
        publishToTmp(
            conf.buildGradle(group, version, buildGradleFile = buildFile), "",
            """yapp.mavenPublishing.name=propertyname
                |yapp.targets=maven_central
            """.trimMargin(),
            projectdir = File("$testDirPath/$mcProjectPath")
        )

        xpathFieldDiff("m:project/m:name", "propertyname", "order", version)

        copyTemplateBuildFile()
        publishToTmp(
            conf.buildGradle(group, version, buildGradleFile = buildFile), "", "",
            projectdir = File("$testDirPath/$mcProjectPath")
        )

        xpathFieldDiff("m:project/m:name", "envname", "order", version)
    }

    @Test
    fun `properties are read in order build file, properties file`() {

        val group = "se.order"
        val version = "0.0.5-SNAPSHOT"

        publishToTmp(
            conf.buildGradle(group, version, buildGradleFile = buildFile),
            """
                
            yapp {
                targets.add("maven_central")
                mavenPublishing {
                    name.set("confname")
                }
            }
            """.trimIndent(),
            """yapp.mavenPublishing.name=propertyname
                |yapp.targets=maven_central
            """.trimMargin(),
            projectdir = File("$testDirPath/$mcProjectPath")
        )

        xpathFieldDiff("m:project/m:name", "confname", "order", version)

        copyTemplateBuildFile()
        publishToTmp(
            conf.buildGradle(group, version, buildGradleFile = buildFile), "",
            """yapp.mavenPublishing.name=propertyname
                |
                |yapp.targets=maven_central
            """.trimMargin(),
            projectdir = File("$testDirPath/$mcProjectPath")
        )

        xpathFieldDiff("m:project/m:name", "propertyname", "order", version)
    }

    fun yappConf() = """yapp {
           mavenPublishing {
               name.set("asdf")
           }
       }
       """
}
