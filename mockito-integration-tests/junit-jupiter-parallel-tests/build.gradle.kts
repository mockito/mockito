import org.gradle.api.tasks.testing.Test

plugins {
    id("java")
    id("mockito.test-conventions")
    id("mockito.test-jvm-conventions")
}

testJvm {
    minJvm.set(17)
}

description = "Tests that require fine tuned parallel settings for JUnit Jupiter (bug #1630)"

dependencies {
    testImplementation(libs.junit6.jupiter.api)
    testImplementation(project(":mockito-extensions:mockito-junit-jupiter"))
    testRuntimeOnly(libs.junit6.jupiter.engine)
    testRuntimeOnly(libs.junit6.platform.launcher)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
