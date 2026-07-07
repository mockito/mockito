import org.gradle.api.tasks.testing.Test

plugins {
    id("java")
    id("mockito.test-conventions")
    id("mockito.test-jvm-conventions")
}

testJvm {
    minJvm.set(17)
}

description = "End-to-end tests for Mockito and its extensions."

dependencies {
    testImplementation(project(":mockito-core"))
    testImplementation(project(":mockito-extensions:mockito-junit-jupiter"))
    testImplementation(testFixtures(project(":mockito-core")))
    testImplementation(libs.junit4)
    testImplementation(libs.assertj)
    testImplementation(libs.junit6.jupiter.api)
    testRuntimeOnly(libs.junit6.jupiter.engine)
    testRuntimeOnly(libs.junit5.vintage.engine)
    testRuntimeOnly(libs.junit6.platform.launcher)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

configurations.configureEach {
    //TODO SF enable when #154 is implemented
    //let's make those tests not use hamcrest
    //exclude group: 'org.hamcrest', module: 'hamcrest-core'
}
