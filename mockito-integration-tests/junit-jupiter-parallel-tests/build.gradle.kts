plugins {
    id("java")
    id("mockito.test-conventions")
}

description = "Tests that require fine tuned parallel settings for JUnit Jupiter (bug #1630)"

dependencies {
    testImplementation(libs.junit.jupiter.api)
    testImplementation(project(":mockito-extensions:mockito-junit-jupiter"))
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
