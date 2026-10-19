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
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.bundles.junit5.runtime)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
