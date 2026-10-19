plugins {
    id("java")
    id("mockito.test-conventions")
}

description = "Tests that require fine tuned parallel settings for JUnit Jupiter (bug #1630)"

dependencies {
    testImplementation(libs.junit5.api)
    testImplementation(project(":mockito-extensions:mockito-junit-jupiter"))
    testRuntimeOnly(libs.bundles.junit5.runtime)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
