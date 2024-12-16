plugins {
    id("java")
    id("mockito.test-conventions")
}

description = "End-to-end tests for automatic registration of MockitoExtension with the inline mock maker."

dependencies {
    testImplementation(project(":mockito-extensions:mockito-junit-jupiter"))
    testImplementation(libs.assertj)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks{
    test {
        useJUnitPlatform()
    }
}
