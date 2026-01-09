import org.gradle.api.tasks.testing.Test

plugins {
    id("java")
    id("mockito.test-conventions")
    id("mockito.test-conventions-java-17")
}

description = "End-to-end tests for automatic registration of MockitoExtension with the inline mock maker."

dependencies {
    testImplementation(project(":mockito-extensions:mockito-junit-jupiter"))
    testImplementation(libs.assertj)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
