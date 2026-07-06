plugins {
    id("java")
    id("mockito.test-conventions")
}

description = "End-to-end tests for Mockito and its extensions."

dependencies {
    testImplementation(project(":mockito-core"))
    testImplementation(project(":mockito-extensions:mockito-junit-jupiter"))
    testImplementation(testFixtures(project(":mockito-core")))
    testImplementation(libs.assertj)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}

configurations.configureEach {
    // Ensure these tests do not depend on hamcrest (see #154).
    exclude(group = "org.hamcrest", module = "hamcrest-core")
}
