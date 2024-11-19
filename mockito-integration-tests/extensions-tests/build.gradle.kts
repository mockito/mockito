plugins {
    id("java")
    id("mockito.test-conventions")
}

description = "End-to-end tests for Mockito and its extensions."

dependencies {
    testImplementation(project(":mockito-core"))
    testImplementation(project(":mockito-extensions:mockito-junit-jupiter"))
    testImplementation(testFixtures(project(":mockito-core")))
    testImplementation(libs.junit4)
    testImplementation(libs.assertj)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.vintage.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}

configurations.configureEach {
    //TODO SF enable when #154 is implemented
    //let's make those tests not use hamcrest
    //exclude group: 'org.hamcrest', module: 'hamcrest-core'
}
