import java.nio.file.Path

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class JUnitCompatibilityTest {
    @TempDir
    lateinit var projectDirectory: Path

    @Test
    fun registers_independent_runtimes_with_late_bound_values() {
        projectDirectory.resolve("settings.gradle.kts").toFile().writeText(
            """
            rootProject.name = "junit-compatibility-test"
            dependencyResolutionManagement {
                versionCatalogs {
                    create("libs") {
                        library("older-bom", "example", "older-bom").version("1.0")
                        library("newer-bom", "example", "newer-bom").version("2.0")
                        library("junit5-bom", "example", "junit5-bom").version("5.0")
                        library("junit6-bom", "example", "junit6-bom").version("6.0")
                        library("junit4", "example", "junit4").version("1.0")
                        library("osgi", "example", "osgi").version("1.0")
                        library("equinox", "example", "equinox").version("1.0")
                    }
                }
            }
            """.trimIndent()
        )
        projectDirectory.resolve("build.gradle.kts").toFile().writeText(
            """
            import org.gradle.api.artifacts.VersionCatalogsExtension
            import org.gradle.api.attributes.java.TargetJvmVersion

            plugins {
                id("mockito.junit-jupiter-compatibility-conventions")
            }

            val newerTestJvmVersion = objects.property<Int>().value(17)
            // Avoid Mockito's generated catalog accessors on the TestKit plugin classpath.
            val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
            junitCompatibility {
                runtimes {
                    named("junit6") {
                        check(testJvmVersion.get() == 17)
                        testJvmVersion.set(newerTestJvmVersion)
                    }
                    register("older") {
                        bom.set(catalog.findLibrary("older-bom").get())
                        testJvmVersion.set(11)
                    }
                    register("newer") {
                        bom.set(catalog.findLibrary("newer-bom").get())
                        testJvmVersion.set(newerTestJvmVersion)
                    }
                }
            }
            newerTestJvmVersion.set(21)

            val baseline = tasks.named<Test>("test")
            val runtimes = listOf(
                "junit5" to (11 to "5.0"),
                "junit6" to (21 to "6.0"),
                "older" to (11 to "1.0"),
                "newer" to (21 to "2.0"),
            )
            tasks.register("verifyRuntimes") {
                doLast {
                    val reports = mutableSetOf(baseline.get().reports.junitXml.outputLocation.get().asFile)
                    runtimes.forEach { (name, expected) ->
                        val runtime = configurations.getByName(name + "RuntimeClasspath")
                        check(runtime.attributes.getAttribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE) == expected.first)
                        val bom = configurations.getByName(name + "RuntimeOnly").dependencies.single()
                        check(bom.group == "example" && bom.name == name + "-bom" && bom.version == expected.second)
                        check(configurations.testRuntimeClasspath.get().allDependencies.isEmpty())
                        val test = tasks.named<Test>(name + "Test").get()
                        check(test.testClassesDirs.files == baseline.get().testClassesDirs.files)
                        check(reports.add(test.reports.junitXml.outputLocation.get().asFile))

                        val osgiBundles = configurations.getByName(name + "OsgiBundles")
                        check(osgiBundles.attributes.getAttribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE) == expected.first)
                        check(bom in osgiBundles.allDependencies)
                        val osgiTest = tasks.named<Test>(name + "OsgiTest").get()
                        check(osgiTest.testClassesDirs.files == sourceSets["osgiTest"].output.classesDirs.files)
                        check(reports.add(osgiTest.reports.junitXml.outputLocation.get().asFile))
                    }
                }
            }
            """.trimIndent()
        )

        val runner = GradleRunner.create()
            .withProjectDir(projectDirectory.toFile())
            .withPluginClasspath()
        val result = runner
            .withArguments("verifyRuntimes", "--stacktrace")
            .build()

        assertThat(result.task(":verifyRuntimes")?.outcome).isEqualTo(TaskOutcome.SUCCESS)

        val bundleResult = runner.withArguments("osgiTestBundle", "--stacktrace").build()
        assertThat(bundleResult.task(":osgiTestBundle")?.outcome).isEqualTo(TaskOutcome.SKIPPED)

        val checkResult = runner.withArguments("check", "--dry-run", "--stacktrace").build()
        assertThat(checkResult.output).contains(
            ":junit5Test SKIPPED",
            ":junit6Test SKIPPED",
            ":junit5OsgiTest SKIPPED",
            ":junit6OsgiTest SKIPPED",
            ":olderTest SKIPPED",
            ":newerTest SKIPPED",
            ":olderOsgiTest SKIPPED",
            ":newerOsgiTest SKIPPED",
        )
    }
}
