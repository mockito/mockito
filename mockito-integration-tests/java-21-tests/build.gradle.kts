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

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks {
    val testWithBytebuddyAgent by registering(Test::class) {
        if (!JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_21)) {
            enabled = false
        } else {
            jvmArgs(
                "-javaagent:${bytebuddyAgent.asPath}",
                // "-Djdk.instrument.traceUsage",
            )
        }
        failIfStdErrWarningOnDynamicallyLoadedAgent(this)
    }

    val testWithMockitoAgent by registering(Test::class) {
        dependsOn(":mockito-core:jar")
        if (!JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_21)) {
            enabled = false
        } else {
            jvmArgumentProviders.add(
                CommandLineArgumentProvider {
                    listOf(
                        "-javaagent:${project(":mockito-core").tasks.jar.get().archiveFile.get()}"
                        // "-Djdk.instrument.traceUsage",
                    )
                }
            )
        }
        failIfStdErrWarningOnDynamicallyLoadedAgent(this)
    }

    val testWithEnableDynamicAgentLoading by registering(Test::class) {
        if (!JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_21)) {
            enabled = false
        } else {
            jvmArgs(
                "-XX:+EnableDynamicAgentLoading",
                // "-Djdk.instrument.traceUsage",
            )
        }
        failIfStdErrWarningOnDynamicallyLoadedAgent(this)
    }

    named<Test>("test") {
        dependsOn(
            testWithMockitoAgent,
            testWithBytebuddyAgent,
            testWithEnableDynamicAgentLoading,
        )
        isEnabled = JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_21)

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
