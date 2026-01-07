import org.gradle.api.JavaVersion
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("java")
    id("mockito.test-conventions")
}

description = "End-to-end tests for automatic registration of MockitoExtension with the inline mock maker."

// These integration tests depend on :mockito-extensions:mockito-junit-jupiter, which uses JUnit 6
// and therefore requires Java 17+. Even if the overall build runs with -Pmockito.test.java=11,
// we must override to 17 for this project.
val minJavaVersion = 17
val requestedJavaVersion = providers
    .gradleProperty("mockito.test.java")
    .orElse("auto")
    .map { v ->
        if (v == "auto") {
            JavaVersion.current().majorVersion.toInt()
        } else {
            v.toInt()
        }
    }
val effectiveJavaVersion = requestedJavaVersion.map { requested ->
    maxOf(requested, minJavaVersion)
}

val logJavaOverride by tasks.registering {
    doLast {
        val requested = requestedJavaVersion.get()
        if (requested < minJavaVersion) {
            logger.lifecycle(
                "junit-jupiter-inline-mock-maker-extension-tests require Java $minJavaVersion+. " +
                        "mockito.test.java=$requested, overriding to $minJavaVersion."
            )
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    dependsOn(logJavaOverride)
    options.release = effectiveJavaVersion
}

java {
    toolchain {
        languageVersion = effectiveJavaVersion.map(JavaLanguageVersion::of)
    }
}

dependencies {
    testImplementation(project(":mockito-extensions:mockito-junit-jupiter"))
    testImplementation(libs.assertj)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test>().configureEach {
    dependsOn(logJavaOverride)

    javaLauncher = javaToolchains.launcherFor {
        languageVersion = effectiveJavaVersion.map(JavaLanguageVersion::of)
    }
    useJUnitPlatform()
}
