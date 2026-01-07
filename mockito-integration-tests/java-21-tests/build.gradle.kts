import org.gradle.api.JavaVersion
import org.gradle.api.GradleException
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestOutputEvent
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.process.CommandLineArgumentProvider

plugins {
    id("java")
    id("mockito.test-conventions")
}

description = "Test suite for Java 21 Mockito"

val bytebuddyAgent: Configuration by configurations.creating

dependencies {
    implementation(project(":mockito-core"))
    testImplementation(libs.junit4)
    testImplementation(libs.assertj)
    bytebuddyAgent(libs.bytebuddy.agent) { isTransitive = false }
}

val requiredJavaVersion = 21

// These tests are only meaningful when the build is explicitly running tests on Java 21.
val requestedJava = providers.gradleProperty("mockito.test.java").orElse("auto")
val shouldRun = requestedJava.map { it == requiredJavaVersion.toString() }

val logJavaSkip by tasks.registering {
    doLast {
        val v = requestedJava.get()
        if (v != requiredJavaVersion.toString()) {
            logger.lifecycle(
                "java21-tests run only with -Pmockito.test.java=$requiredJavaVersion. " +
                        "mockito.test.java was set to $v, skipping these tests."
            )
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile>().configureEach {
    dependsOn(logJavaSkip)
    onlyIf { shouldRun.get() }
    options.release = 21
}

tasks.withType<Test>().configureEach {
    dependsOn(logJavaSkip)
    onlyIf { shouldRun.get() }

    javaLauncher = javaToolchains.launcherFor {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks {
    val testWithBytebuddyAgent by registering(Test::class) {
        jvmArgs(
            "-javaagent:${bytebuddyAgent.asPath}",
            // "-Djdk.instrument.traceUsage",
        )
        failIfStdErrWarningOnDynamicallyLoadedAgent(this)
    }

    val testWithMockitoAgent by registering(Test::class) {
        dependsOn(":mockito-core:jar")
        jvmArgumentProviders.add(
            CommandLineArgumentProvider {
                listOf(
                    "-javaagent:${project(":mockito-core").tasks.jar.get().outputs.files.singleFile}"
                    // "-Djdk.instrument.traceUsage",
                )
            }
        )
        failIfStdErrWarningOnDynamicallyLoadedAgent(this)
    }

    val testWithEnableDynamicAgentLoading by registering(Test::class) {
        jvmArgs(
            "-XX:+EnableDynamicAgentLoading",
            // "-Djdk.instrument.traceUsage",
        )
        failIfStdErrWarningOnDynamicallyLoadedAgent(this)
    }

    named<Test>("test") {
        dependsOn(
            testWithMockitoAgent,
            testWithBytebuddyAgent,
            testWithEnableDynamicAgentLoading,
        )

        // Aggregator task should also be skipped unless mockito.test.java=21.
        onlyIf { shouldRun.get() }

        expectStdErrWhenDynamicallyLoadedAgent(this)
    }
}

private fun failIfStdErrWarningOnDynamicallyLoadedAgent(testTask: Test) {
    val stderrMessages = mutableListOf<String>()
    testTask.addTestOutputListener { _, event ->
        if (event.destination == TestOutputEvent.Destination.StdErr) {
            stderrMessages.add(event.message)
        }
    }
    testTask.doLast {
        val stderrText = stderrMessages.joinToString(separator = "\n")
        if (stderrText.contains("A Java agent has been loaded dynamically")
            && stderrText.contains("net.bytebuddy/byte-buddy-agent")
            && stderrText.contains("If a serviceability tool is in use, please run with -XX:+EnableDynamicAgentLoading to hide this warning")
        ) {
            throw GradleException("JDK should no emit warning when mockito is set as agent and -XX:+EnableDynamicAgentLoading is not set")
        }
    }
}

private fun expectStdErrWhenDynamicallyLoadedAgent(testTask: Test) {
    val stderrMessages = mutableListOf<String>()
    testTask.addTestOutputListener { _, event ->
        if (event.destination == TestOutputEvent.Destination.StdErr) {
            stderrMessages.add(event.message)
        }
    }
    testTask.doLast {
        val stdoutText = stderrMessages.joinToString("\n")
        if (!stdoutText.contains("Mockito is currently self-attaching to enable the inline-mock-maker.")) {
            throw GradleException("Mockito should emit a message when self-attaching")
        }
    }
}
