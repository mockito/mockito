import org.gradle.api.tasks.testing.Test

plugins {
    id("java")
    id("mockito.test-conventions")
    id("mockito.test-jvm-conventions")
}

testJvm {
    minJvm.set(17)
}

description = "End-to-end tests for automatic registration of MockitoExtension."

dependencies {
    testImplementation(project(":mockito-extensions:mockito-junit-jupiter"))
    testImplementation(libs.assertj)
    testImplementation(libs.junit6.jupiter.api)
    testRuntimeOnly(libs.junit6.jupiter.engine)
    testRuntimeOnly(libs.junit6.platform.launcher)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
